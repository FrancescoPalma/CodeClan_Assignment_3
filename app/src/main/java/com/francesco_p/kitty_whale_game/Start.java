package com.francesco_p.kitty_whale_game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.FrameLayout;

public class Start extends Activity {

    GameView gameView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.maingame);

        FrameLayout game = (FrameLayout) findViewById(R.id.flGameView);

        gameView = new GameView(this, null);

        game.addView(gameView);

    }

    @Override
    public void onPause() {
        super.onPause();

        if (gameView != null) {
            gameView.setRunning(false);
        }

    }

        
    @Override
    protected void onResume() {
        super.onResume();
           
        if (gameView != null) {
           gameView.setRunning(true);
        }
  
    }
             
    @Override
    protected void onStop() {
        super.onStop();
    }
   
    @Override
    public void onBackPressed() {
       gameView.setRunning(false);
       doDialogBuilder();
    }
   
   private void doDialogBuilder() {

	        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
	        alertDialog.setTitle("Exit");
	        alertDialog.setCancelable(false);
	        // Setting Dialog Message
	        alertDialog.setMessage("Are you sure you want to exit?");
	 
	        // On pressing Settings button
	        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog,int which) {
                    gameView.setRunning(false);
	            	gameView.recycleBitmaps();
	            	SplashScreen.closeapp = true;
	                finish();
	            }
	        });
	 
	        // on pressing cancel button
	        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int which) {
	                dialog.cancel();
	                gameView.setRunning(true);
	            }
	        });

	        alertDialog.show();

   }

}
