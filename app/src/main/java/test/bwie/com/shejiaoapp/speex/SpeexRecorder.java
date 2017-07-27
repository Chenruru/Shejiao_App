package test.bwie.com.shejiaoapp.speex;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;

public class SpeexRecorder implements Runnable {
	private Handler mHandler;
	private volatile boolean isRecording;
	private final Object mutex = new Object();
	private static final int frequency = 8000;
	private static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	public static int packagesize = 160;
	public String fileName = null;
	public static double gdb = 0.00;
	private AudioRecord recordInstance;
	SpeexEncoder encoder ;
	public SpeexRecorder(String fileName,Handler handler) {
		super();
		this.fileName = fileName;
		this.mHandler = handler;
	}

	public void run() {

		try {
			 encoder = new SpeexEncoder(this.fileName);
			Thread encodeThread = new Thread(encoder);
			encoder.setRecording(true);
			encodeThread.start();

			synchronized (mutex) {

				while (!this.isRecording) {
					try {
						mutex.wait();
					} catch (InterruptedException e) {
						throw new IllegalStateException("Wait() interrupted!", e);
					}
				}
			}
			android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

			int bufferRead = 0;
			int bufferSize = AudioRecord.getMinBufferSize(frequency, AudioFormat.CHANNEL_IN_MONO, audioEncoding);

			short[] tempBuffer = new short[packagesize];

			recordInstance = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, AudioFormat.CHANNEL_IN_MONO, audioEncoding, bufferSize);
			if(recordInstance.getState() == AudioRecord.STATE_UNINITIALIZED){
				if(recordInstance != null){
					recordInstance.release();
					recordInstance = null;
				}
				return ;
			}
			recordInstance.startRecording();
			gdb = 0.00;




			while (this.isRecording) {
				bufferRead = recordInstance.read(tempBuffer, 0, packagesize);
				// bufferRead = recordInstance.read(tempBuffer, 0, 320);
				if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
					throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
				} else if (bufferRead == AudioRecord.ERROR_BAD_VALUE) {
					throw new IllegalStateException("read() returned AudioRecord.ERROR_BAD_VALUE");
				} else if (bufferRead == AudioRecord.ERROR_INVALID_OPERATION) {
					throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
				}
				encoder.putData(tempBuffer, bufferRead);
				getAmplitude(tempBuffer, bufferRead);
			}
			recordInstance.stop();
			recordInstance.release();
			recordInstance = null;
			encoder.setRecording(false);

		} catch (Exception e) {

			if(recordInstance != null){
				recordInstance.release();
				recordInstance = null;
			}
			e.printStackTrace();
		} 

	}


	public void stopRecoding(){
		try {
			if(recordInstance == null){
				recordInstance.stop();
				recordInstance.release();
			}
			recordInstance = null;
			if(encoder != null){
				encoder.setRecording(false);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setRecording(boolean isRecording) {
		synchronized (mutex) {
			this.isRecording = isRecording;
			if (this.isRecording) {
				mutex.notify();
			}
		}
	}

	public boolean isRecording() {
		synchronized (mutex) {
			return isRecording;
		}
	}

	public void getAmplitude(short[] tempBuffer, int bufferRead) {
		int v = 0;
		for (int i = 0; i < tempBuffer.length; i++) {
			v += tempBuffer[i] * tempBuffer[i];
		}
		gdb = 10 * Math.log10(v / (double) bufferRead);
	}
}
