
package test.bwie.com.shejiaoapp.speex;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;
import android.os.RecoverySystem.ProgressListener;


import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class SpeexDecoder {
	protected Speex speexDecoder;

	protected boolean enhanced = false;

	private boolean paused = false;

	protected String srcFile;

	private List<ProgressListener> listenerList = new ArrayList<ProgressListener>();

	private File srcPath;
	private AudioTrack track;
	
	public  boolean isStop = false ;
	public  boolean releaseMusic = true ;

	public Handler handler ;
	private long sendtimer ;
	private long atimer ;


	public SpeexDecoder(File srcPath,Handler handler) throws Exception {
		this.srcPath = srcPath;
		this.handler = handler ;
//		this.sendtimer = sendtimer ;
//		this.atimer = atimer;
	}

	private void initializeAndroidAudio(int sampleRate) throws Exception {
		int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);

		if (minBufferSize < 0) {
			throw new Exception("Failed to get minimum buffer size: " + Integer.toString(minBufferSize));
		}

		track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
				minBufferSize, AudioTrack.MODE_STREAM);

	}

	public void addOnMetadataListener(ProgressListener l) {
		listenerList.add(l);
	}

	public synchronized void setPaused(boolean paused) {
		this.paused = paused;
	}

	public synchronized boolean isPaused() {
		return paused;
	}

	public void decode() throws Exception {
		byte[] header = new byte[2048];
		byte[] payload = new byte[65536];
		final int OGG_HEADERSIZE = 27;
		final int OGG_SEGOFFSET = 26;
		final String OGGID = "OggS";
		int segments = 0;
		int curseg = 0;
		int bodybytes = 0;
		int decsize = 0;
		int packetNo = 0;
		// construct a new decoder
		speexDecoder = new Speex();
		speexDecoder.init();
		// open the input stream
		RandomAccessFile dis = new RandomAccessFile(srcPath, "r");

		int origchksum;
		int chksum;
		try {

			// read until we get to EOF
			while (true) {
				if (Thread.interrupted()) {
					dis.close();
					closeTrack();
					sendMessage();
					return;
				}

				while (this.isPaused()) {
					closeTrack();
					dis.close();
					sendMessage();
					Thread.sleep(100);
				}

				// read the OGG header
				dis.readFully(header, 0, OGG_HEADERSIZE);
				origchksum = readInt(header, 22);
				readLong(header, 6);
				header[22] = 0;
				header[23] = 0;
				header[24] = 0;
				header[25] = 0;
				chksum = OggCrc.checksum(0, header, 0, OGG_HEADERSIZE);

				// make sure its a OGG header
				if (!OGGID.equals(new String(header, 0, 4))) {
					System.err.println("missing ogg id!");
					return;
				}

				/* how many segments are there? */
				segments = header[OGG_SEGOFFSET] & 0xFF;
				dis.readFully(header, OGG_HEADERSIZE, segments);
				chksum = OggCrc.checksum(chksum, header, OGG_HEADERSIZE, segments);

				/* decode each segment, writing output to wav */
				for (curseg = 0; curseg < segments; curseg++) {

					if (Thread.interrupted()) {
						System.err.println("Thread.interrupted()");

						dis.close();
						track.stop();
						return;
					}

					while (this.isPaused()) {
						track.stop();
						System.err.println("this.isPaused()");
						Thread.sleep(100);
					}

					/* get the number of bytes in the segment */
					bodybytes = header[OGG_HEADERSIZE + curseg] & 0xFF;
					if (bodybytes == 255) {
						System.err.println("sorry, don't handle 255 sizes!");
						return;
					}
					dis.readFully(payload, 0, bodybytes);
					chksum = OggCrc.checksum(chksum, payload, 0, bodybytes);

					/* decode the segment */
					/* if first packet, read the Speex header */
					if (packetNo == 0) {
						if (readSpeexHeader(payload, 0, bodybytes, true)) {
							packetNo++;
						} else {
							packetNo = 0;
						}
					} else if (packetNo == 1) { // Ogg Comment packet
						packetNo++;
					} else {
						/* get the amount of decoded data */
						short[] decoded = new short[160];
						if ((decsize = speexDecoder.decode(payload, decoded, 160)) > 0) {
							if (track != null){
								track.write(decoded, 0, decsize);
								track.setStereoVolume(0.8f, 0.8f);// 设置当前音量大小
							}
							if(isStop){
								if(track != null){
//									System.err.println("track stop ");
									closeTrack();
									break;
								}
							}else{
								if (track!= null){
									track.play();
									packetNo++;
								}

							}

						}

					}
				}
				if (chksum != origchksum)
					throw new IOException("Ogg CheckSums do not match"+"chksum"+chksum+"origchksum"+origchksum);
			}
		} catch (EOFException eof) {
			eof.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}finally {
			dis.close();
			closeTrack();
			//
			sendMessage();
		}


	}

	private void closeTrack(){
		try {
			if(track != null){
                track.stop();
                track.release();
                track = null;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendMessage(){

		isStop = false ;
		releaseMusic = true;
	}

	/**
	 * Reads the header packet.
	 * 
	 * <pre>
	 *  0 -  7: speex_string: "Speex   "
	 *  8 - 27: speex_version: "speex-1.0"
	 * 28 - 31: speex_version_id: 1
	 * 32 - 35: header_size: 80
	 * 36 - 39: rate
	 * 40 - 43: mode: 0=narrowband, 1=wb, 2=uwb
	 * 44 - 47: mode_bitstream_version: 4
	 * 48 - 51: nb_channels
	 * 52 - 55: bitrate: -1
	 * 56 - 59: frame_size: 160
	 * 60 - 63: vbr
	 * 64 - 67: frames_per_packet
	 * 68 - 71: extra_headers: 0
	 * 72 - 75: reserved1
	 * 76 - 79: reserved2
	 * </pre>
	 * 
	 * @param packet
	 * @param offset
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	private synchronized boolean readSpeexHeader(final byte[] packet, final int offset, final int bytes, boolean init) throws Exception {
		if (bytes != 80) {
			System.out.println("Oooops");
			return false;
		}
		if (!"Speex   ".equals(new String(packet, offset, 8))) {
			return false;
		}
		int mode = packet[40 + offset] & 0xFF;
		int sampleRate = readInt(packet, offset + 36);
		int channels = readInt(packet, offset + 48);
		int nframes = readInt(packet, offset + 64);
		int frameSize = readInt(packet, offset + 56);
		initializeAndroidAudio(sampleRate);

		if (init) {
			// return speexDecoder.init(mode, sampleRate, channels, enhanced);
			return true;
		} else {
			return true;
		}
	}

	protected static int readInt(final byte[] data, final int offset) {
		/*
		 * no 0xff on the last one to keep the sign
		 */
		return (data[offset] & 0xff) | ((data[offset + 1] & 0xff) << 8) | ((data[offset + 2] & 0xff) << 16) | (data[offset + 3] << 24);
	}

	protected static long readLong(final byte[] data, final int offset) {
		/*
		 * no 0xff on the last one to keep the sign
		 */
		return (data[offset] & 0xff) | ((data[offset + 1] & 0xff) << 8) | ((data[offset + 2] & 0xff) << 16)
				| ((data[offset + 3] & 0xff) << 24) | ((data[offset + 4] & 0xff) << 32) | ((data[offset + 5] & 0xff) << 40)
				| ((data[offset + 6] & 0xff) << 48) | (data[offset + 7] << 56);
	}

	protected static int readShort(final byte[] data, final int offset) {
		/*
		 * no 0xff on the last one to keep the sign
		 */
		return (data[offset] & 0xff) | (data[offset + 1] << 8);
	}

}
