package com.francesco_p.kitty_whale_game;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class WhaleSprite {

	// the X coordinate of the whale (top left of the image)
	private int x = 0;
	// the Y coordinate of the whale (top left of the image)
	private int y = 0;
    
    private GameView gameView;
    private Bitmap bmpWhale = null;
    
    private int mCanvasWidth;  
    private int mCanvasHeight;
    
    private int whaleSpriteHeight = 0;
    private int whaleSpriteWidth = 0;

	// the rectangle to be drawn from the animation bitmap
  	private Rect sourceRect;
  	private Rect destRect;
  	
  	private int frameNr = 3;	// number of frames in animation
	private int currentFrame;	// the current frame
	private long frameTicker;	// the time of the last frame update
	private int framePeriod;	// milliseconds between each frame (1000/fps)
	
	private int groundHeight = 0;
	
	private int rotation = 1;
    
    // change here the speed and acceleration
    private float acceleration; // Used in the gravity simulation below - default is 290 - change in dashboard.xml
    private float speed;        // The speed at which the whale will fall (constantly increased by acceleration) - default is 290 - change in dashboard.xml
    private float jump;        //  how high the whale jumps when you touch the screen - smaller value is higher jump - default is 43.53  - change in dashboard.xml
    private float startspeed;
   
    public WhaleSprite(GameView gameView, Bitmap bmpWhale) {
          this.gameView=gameView;
          this.bmpWhale=bmpWhale;
	}
	
	public Rect getBounds() {
		return destRect;
	}
	
    public void reset(int height){        // called after the whale dies
		y = (height - groundHeight) / 2;  // resets position, speed, etc.
		speed = startspeed;
		rotation = 1;
    }
    
    public void setSizes(int width, int height, Rect rectg) {  

        mCanvasWidth = width;  
        mCanvasHeight = height;  
        
        groundHeight = rectg.height();
        
        int origin_x = mCanvasWidth / 2;
        int origin_y = (mCanvasHeight - groundHeight) / 2;
        x = origin_x/4;    // The x position of the whale. Does not change at any time. Should be exactly centered left
        y = origin_y;	   // The STARTING y position of the whale. Will change constantly

		DisplayMetrics displayMetrics = gameView.getResources().getDisplayMetrics();
		float dpHeight = displayMetrics.heightPixels * displayMetrics.density;
		float dpWidth = displayMetrics.widthPixels * displayMetrics.density;

        speed = 0;

		speed = dpHeight/(gameView.getResources().getInteger(R.integer.whalefallspeed)*10);
		startspeed = speed;
        acceleration = dpHeight/(gameView.getResources().getInteger(R.integer.whaleaccelerationspeed)*10);

        TypedValue typedValue = new TypedValue();
        gameView.getResources().getValue(R.dimen.whalejumpheight, typedValue, true);
        jump = typedValue.getFloat();

        currentFrame = 0;
        int frameCount = 3;
		int fps = 10; // the frames per second you want the animation
		frameNr = frameCount;
		whaleSpriteWidth = bmpWhale.getWidth() / frameCount;
		whaleSpriteHeight = bmpWhale.getHeight();
		sourceRect = new Rect(0, 0, whaleSpriteWidth, whaleSpriteHeight);
		destRect = new Rect(x, y, x + whaleSpriteWidth, y + whaleSpriteHeight);
		framePeriod = 1000 / fps;
		frameTicker = 0l;
 	}

    public void update(long gameTime) {
          if (gameTime > frameTicker + framePeriod) {
      		frameTicker = gameTime;
      		// increment the frame
      		currentFrame++;
      		if (currentFrame >= frameNr) {
      			currentFrame = 0;
      		}
		  }

          if (gameView.gameStarted && !gameView.dead) {
        		// only moves if the whale is between the top and bottom of the window
  				if ( ( y > 0 ) && ( y < mCanvasHeight-gameView.rectGround.height())) {

					// game pause added if statement below
					if (!gameView.gamePaused) {

						speed += acceleration;  // This is the gravity - the speed is just increased by acceleration all the time, even after a jump
						y += speed;             // The actual movement, y location equals (where it was) + (how far it should go)
						rotation += 2;

						if (rotation > 45) {
							rotation = 45;
						}

					}

  				}

				// or else the game resets (Whale is dead!)
			  	else {
					gameView.dead = true;	// whale is dead! This is used in the Main method to reset the walls after a death
				}
		  }

		  int srcX = (currentFrame * whaleSpriteWidth);
    	  sourceRect.set(srcX, 0, srcX+whaleSpriteWidth, whaleSpriteHeight);
    	  destRect = new Rect(x, y, x + whaleSpriteWidth, y + whaleSpriteHeight);

    }
   
    public void draw(Canvas canvas) {

          // this rotates the whale up and down
          canvas.save();
          canvas.rotate(rotation, destRect.centerX(), destRect.centerY());

          canvas.drawBitmap(bmpWhale, sourceRect, destRect, null);

          canvas.restore();

    }

 	// This is called when the whale jumps (on touch)
 	public void jump() {
 		speed = (int) ((mCanvasWidth / jump) * -1);
 		rotation = -20;
 	}

	public void recycleBitmaps() {
		bmpWhale.recycle();
	}

}
