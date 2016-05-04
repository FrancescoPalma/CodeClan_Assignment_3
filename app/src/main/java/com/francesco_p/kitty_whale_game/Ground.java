package com.francesco_p.kitty_whale_game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Ground {

	private int groundWidth = 0;
    private int groundHeight = 0;
    private int mCanvasHeight = 0;
    private int x = 0;
    private int speed = 0;
    private Bitmap bmpGround = null;
    private int speedModifier = 60; // change this for speed of the ground moving, lower means faster

    public Ground(GameView gameView, Bitmap bmpGround) {
        this.bmpGround=bmpGround;
    }

    public void setSizes(int width, int height, Rect rectg, int ground) {

    	groundWidth = width;
    	mCanvasHeight = height;
        groundHeight = rectg.height();
        // set speed from dimens
        speed = (groundWidth / speedModifier) * -1;
        // scale bitmap so that it draws faster
        bmpGround = Bitmap.createScaledBitmap(bmpGround, groundWidth+10, groundHeight, false);

        // if first wall
        if (ground == 1) {
        	x = 0;
        } // if second wall
        else {
        	x = groundWidth;
        }

    }

    public void update() {
        // move wall
        x += speed;
        // pushes the ground back to just off screen on the right when it gets offscreen on the left (the loop)
        if (x <= 0 - groundWidth) {
            x = groundWidth;
        }
    }

    public void draw(Canvas canvas) {
        try {
            canvas.drawBitmap(bmpGround, x, mCanvasHeight - groundHeight, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void recycleBitmaps() {
        bmpGround.recycle();
    }

}

