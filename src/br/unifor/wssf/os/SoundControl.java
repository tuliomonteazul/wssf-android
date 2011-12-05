package br.unifor.wssf.os;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;

public class SoundControl {

	private Context context;
	private static SoundControl instance;
	
	private SoundControl(Context context) {
		this.context = context;
	}
	
	public static SoundControl getInstance(Context context) {
		if (instance == null) {
			instance = new SoundControl(context);
		}
		return instance;
	}

	public void beep() {
		try {
			Uri soundUri = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			MediaPlayer mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setDataSource(context, soundUri);
			final AudioManager audioManager = (AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.setLooping(false);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
