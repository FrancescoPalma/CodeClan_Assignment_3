package com.francesco_p.kitty_whale_game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
    private GameView gameView;
    private boolean running = false;
    private final SurfaceHolder holder;

    // Desired frames per second
    private int FPS = 50;
    // nanoseconds per frame
    private long period = 1000000000L / FPS;
    // no. of frames that can be skipped in any one animation loop
    // i.e the games state is updated but not rendered

    /**
     * Number of frames with a delay of 0 ms before the animation thread yields to other running threads.
     */
    private static final int NO_DELAYS_PER_YIELD = 16;

    public GameThread(GameView gameView, SurfaceHolder holder) {
	  this.gameView = gameView;
      this.holder = holder;

      // How often we update the screen - change in res/values/dashboard.xml
      FPS = gameView.getResources().getInteger(R.integer.overallgamespeed);
    }

    public void run() {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;

        Canvas canvas;

        int MAX_FRAME_SKIPS = 5;

        beforeTime = System.nanoTime();

        while (running) {

            canvas = holder.lockCanvas();

            gameView.update();

            try {
                synchronized (holder) {
                    if (canvas != null) {
                        canvas.drawColor(Color.WHITE);
                        gameView.draw(canvas);
                    }
                }
            }

            finally {
                if (canvas != null) {
        	        gameView.getHolder().unlockCanvasAndPost(canvas);
                }
            }

            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = (period - timeDiff) - overSleepTime;

            if (sleepTime > 0) {   // some time left in this cycle
                try {
                    Thread.sleep(sleepTime / 1000000L);  // nano -> ms
                } catch (InterruptedException ex) {
                    Log.e("error", ex + "");
                }
                overSleepTime = (System.nanoTime() - afterTime) - sleepTime;
            } else {    // sleepTime <= 0; frame took longer than the period
                excess -= sleepTime;  // store excess time value
                overSleepTime = 0L;

                if (++noDelays >= NO_DELAYS_PER_YIELD) {
                    Thread.yield();   // give another thread a chance to run
                    noDelays = 0;
                }
            }

            beforeTime = System.nanoTime();

            /*
            * If frame animation is taking too long, update the game state without rendering it, to get the updates/sec
            * nearer to the required FPS.
            */
            int skips = 0;
            while ((excess > period) && (skips < MAX_FRAME_SKIPS)) {
                excess -= period;
                gameView.update();      // update state but don't render
                skips++;
            }
        }

    }
    
    public void setRunning(boolean b) {
        running = b;
    }

}  
