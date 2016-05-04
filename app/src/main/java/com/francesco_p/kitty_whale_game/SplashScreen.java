package com.francesco_p.kitty_whale_game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.RelativeLayout;

public class SplashScreen extends Activity {
	
	private int timer = 3000;
	private int resId = R.drawable.launch_image;
	private boolean imageSwap = false;
	public static boolean closeapp = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);
       
		final Handler h = new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
            	
            	if (imageSwap) {
            	startMainScreen();
            	h.removeCallbacksAndMessages(null);
            	}
            	else {
            		// change image here
            		RelativeLayout layout =(RelativeLayout)findViewById(R.id.rlsplash);
            		layout.setBackgroundResource(resId);
            		imageSwap = true;
					h.postDelayed(this, timer);
            	}
            
            }
        }, timer);
		
	}

	private void startMainScreen() {
		startActivity(new Intent(this, Start.class));
	}

	 @Override  
     protected void onResume(){  
          super.onResume();
          
          if (closeapp) {
        	  closeapp = false;
        	  finish();
          }
	 }
	
}
