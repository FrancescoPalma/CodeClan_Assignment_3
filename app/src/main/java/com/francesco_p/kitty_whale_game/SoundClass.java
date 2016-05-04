package com.francesco_p.kitty_whale_game;

import java.util.HashMap;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.Builder;
import android.media.SoundPool.OnLoadCompleteListener;

public class SoundClass {
	
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundsMap;
	boolean loaded = false;
	
	public int sound_eat = 1;
	public int sound_lose = 2;

	@SuppressLint({"UseSparseArrays", "NewApi"})
    @TargetApi(21)
	public SoundClass(final Context myContext) {

		Thread t = new Thread(new Runnable() {
	            @Override
	            public void run() {
		
					if((android.os.Build.VERSION.SDK_INT) >= 21) {
			
						Builder sp21 = new SoundPool.Builder();
						sp21.setMaxStreams(10);
	    				sp21.setAudioAttributes(new AudioAttributes.Builder()
	    				.setUsage(AudioAttributes.USAGE_GAME)
	    				.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
	    				.build());
	    				soundPool = sp21.build();
					}
	    
	    			else {
						soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
	    			}
		
					soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {
						@Override
						public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
							loaded = true;
						}
					});

					soundsMap = new HashMap<>();
					soundsMap.put(sound_eat, soundPool.load(myContext, R.raw.eat, 1));
					soundsMap.put(sound_lose, soundPool.load(myContext, R.raw.lose, 1));

				}
	    
		});
		t.start();

	}
	
	public void playSound(Context context, int sound) {
		AudioManager mgr = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCurrent / streamVolumeMax;
		float fSpeed = 1.0f;

		// Is the sound loaded?
		if (loaded) {
			soundPool.play(soundsMap.get(sound), volume, volume, 1, 0, fSpeed);
		}
	}

}

