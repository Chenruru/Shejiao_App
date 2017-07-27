/**
 * 
 */
package test.bwie.com.shejiaoapp.speex;

import android.os.Handler;


import java.io.File;

public class SpeexPlayer {
	private String fileName = null;
	private SpeexDecoder speexdec = null;
	private Handler handler ;
	private long sendtimer ;
	private long atimer ;


	public SpeexPlayer(String fileName,Handler handler) {

		this.fileName = fileName;
		this.handler = handler ;
		this.sendtimer = sendtimer ;
		this.atimer = atimer;
		try {
			speexdec = new SpeexDecoder(new File(this.fileName),handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startPlay() {
		RecordPlayThread rpt = new RecordPlayThread();

		Thread th = new Thread(rpt);
		th.start();
	}
	
	public void stopPlay(boolean release){
		speexdec.isStop = true ;
		speexdec.releaseMusic = release;
		System.err.println("stopPlay stop ");

	}

	boolean isPlay = true;

	class RecordPlayThread extends Thread {

		public void run() {
			try {
				if (speexdec != null)
					speexdec.decode();
			} catch (Exception t) {
				t.printStackTrace();
			}
		}
	};
}
