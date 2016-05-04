package com.francesco_p.kitty_whale_game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.TypedValue;

import java.util.Random;

public class Wall {
	
	private Random rnd = new Random();
	
    private int x = 0; 
    private int y = 0;
    
    private int mCanvasWidth = 0;
    private int mCanvasHeight = 0;
    
    private int planktonWidth = 0;
    private int planktonHeight = 0;
    
    private int groundHeight = 0;
    private int speed = 0;
    private int GAP = 0;
    
    private GameView gameView;
    private Bitmap bmpColumnTop = null;
    private Bitmap bmpColumnBottom = null;
    private Bitmap bmpPlankton1 = null;
    private Bitmap bmpPlankton2 = null;

    private int actualColumnHeight = 0;
    private int columnWidth = 0;

  	private Rect columnRectTop = new Rect(0,0,0,0);
  	private Rect columnRectBottom = new Rect(0,0,0,0);
  	private Rect planktonRect1 = new Rect(0,0,0,0);
  	private Rect planktonRect2 = new Rect(0,0,0,0);
    
    // change these variables for speed and gap between columns
    private int speedModifier = 0; // lower number is faster - default is 75 - change in dashboard.xml
    private float gapModifier = 3.6f; // smaller number is a bigger gap! - default is 3.6f - change in dashboard.xml
    private int distanceBetweenColumns = 0; // larger number is a bigger distance - default is 200 - change in dashboard.xml

   
    public Wall(GameView gameView, Bitmap bmpColumn1, Bitmap bmpColumn2) {
          this.gameView=gameView;
          this.bmpColumnTop=bmpColumn1;
          this.bmpColumnBottom=bmpColumn2;
          
          bmpPlankton1 = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.plankton);
          bmpPlankton2 = BitmapFactory.decodeResource(gameView.getResources(), R.drawable.plankton);
          
          distanceBetweenColumns = gameView.getResources().getInteger(R.integer.distancebetweencolumns); 
          speedModifier = gameView.getResources().getInteger(R.integer.wallspeed);
          
          TypedValue typedValue = new TypedValue();
          gameView.getResources().getValue(R.dimen.wallgap, typedValue, true);
          gapModifier = typedValue.getFloat();
    }
    
	public Rect getRectTop() {
		return columnRectTop;
	}
	
	public Rect getRectBottom() {
		return columnRectBottom;
	}
	
	public Rect getPlanktonRect1() {
		return planktonRect1;
	}
	
	public Rect getPlanktonRect2() {
		return planktonRect2;
	}
	
	public void reset(int wall) {

        // was y = rnd.nextInt((mCanvasHeight - groundHeight) - 400) + 200;
        y = rnd.nextInt((mCanvasHeight - groundHeight) - 500) + 300;

        if (y <= GAP) {

            y = GAP + 100;
        }

        // if first wall
        if (wall == 1) {
            x = mCanvasWidth;

        }
        // if second wall wall2
        // change distanceBetweenColumns for wider horizontal gap
        else {
            // was x = (int) (mCanvasWidth + ((mCanvasWidth/2)+(columnWidth/2)));
            x = (mCanvasWidth + ((mCanvasWidth/2)+(columnWidth/2)) + distanceBetweenColumns);

        }

        columnRectTop = new Rect(mCanvasWidth,0, bmpColumnTop.getWidth(), bmpColumnTop.getHeight());
        columnRectBottom = new Rect(mCanvasWidth,0, bmpColumnBottom.getWidth(), bmpColumnBottom.getHeight());
        planktonRect1 = new Rect(mCanvasWidth,0, planktonWidth, planktonHeight);
        planktonRect2 = new Rect(mCanvasWidth,0, planktonWidth, planktonHeight);

	}
	
    public void setSizes(int width, int height, int wall) {  
    	
    	mCanvasWidth = width;  
        mCanvasHeight = height; 
        
        planktonWidth = bmpPlankton1.getWidth();
        planktonHeight = bmpPlankton1.getHeight();
        
        groundHeight = gameView.rectGround.height();
       
        columnRectTop = new Rect(mCanvasWidth,0, bmpColumnTop.getWidth(), bmpColumnTop.getHeight());
        columnRectBottom = new Rect(mCanvasWidth,0, bmpColumnBottom.getWidth(), bmpColumnBottom.getHeight());
        planktonRect1 = new Rect(mCanvasWidth,0, planktonWidth, planktonHeight);
        planktonRect2 = new Rect(mCanvasWidth,0, planktonWidth, planktonHeight);
        
        columnWidth = bmpColumnTop.getWidth();
        actualColumnHeight = bmpColumnTop.getHeight();
        // columnHeight = (mCanvasHeight - groundHeight) - y;
        
        // set wall speed
        speed = (mCanvasWidth / speedModifier) * -1;
        // set the gap between the walls
        GAP = (int) (mCanvasWidth / gapModifier);


        // if first wall 
        if (wall == 1) {
        	x = mCanvasWidth;
        
        }
        // if second wall wall2
        // change distanceBetweenColumns for wider horizontal gap
        else {
            // was x = (int) (mCanvasWidth + ((mCanvasWidth/2)+(columnWidth/2)));
        	x = (mCanvasWidth + ((mCanvasWidth/2)+(columnWidth/2)) + distanceBetweenColumns);

        }

        // was y = rnd.nextInt((mCanvasHeight - groundHeight) - 400) + 200;
        y = rnd.nextInt((mCanvasHeight - groundHeight) - 500) + 300;

        if (y <= GAP) {
        	
        	y = GAP + 100;
        }

    }

    public void update(int wall) {

        //game paused added below
        if (!gameView.gamePaused) {

    	  // move wall
          x += speed;  //scrolls the wall

        }

          // update collision Rect // was 0 below
          columnRectTop.set(x, y-actualColumnHeight, x+(columnWidth), y - GAP); // top part
          columnRectBottom.set(x, y, x+(columnWidth), y+(actualColumnHeight)); // bottom part
          
          
          if (columnRectTop.top > 0) {
        	  columnRectTop.top = 0;
          }
          if (columnRectBottom.bottom < (mCanvasHeight - groundHeight)) {
        	  columnRectBottom.bottom = mCanvasHeight - groundHeight - 2;
          }

        // move the plankton a bit higher
        // change this number here for bigger gap, lower number = higher plankton.
        int heightAdj = mCanvasHeight/77;

          if (wall == 1) {
              planktonRect1.set(x+((columnWidth/2)-(planktonWidth/2)),(columnRectBottom.top-planktonHeight)-heightAdj,x+((columnWidth/2)-(planktonWidth/2))+planktonWidth,columnRectBottom.top-heightAdj);
        }
        if (wall == 2) {
            planktonRect2.set(x+((columnWidth/2)-(planktonWidth/2)),(columnRectBottom.top-planktonHeight)-heightAdj,x+((columnWidth/2)-(planktonWidth/2))+planktonWidth,columnRectBottom.top-heightAdj);
        }

  		    //pushes the wall back to just off screen on the right when it gets offscreen on the left (the loop)
  			if (x <= -distanceBetweenColumns - columnWidth) {
  				
  				gameView.scored1 = false;
  				gameView.scored2 = false;
  				gameView.scoreSoundPlayed1 = false;
  				gameView.scoreSoundPlayed2 = false;

                // change distanceBetweenColumns for wider horizontal gap
                x = mCanvasWidth + distanceBetweenColumns;

                //	was y = rnd.nextInt((mCanvasHeight - groundHeight) - 400) + 200;
  				y = rnd.nextInt((mCanvasHeight - groundHeight) - 500) + 300;
  				
  				if (y <= GAP) {
  		        	y = GAP + 100;
  		        }
  				
  			}

    }

    public void draw(Canvas canvas, int wall) {

          if (!gameView.scored1 && wall == 1) {
          canvas.drawBitmap(bmpPlankton1, null, planktonRect1, null);
          }

          if (!gameView.scored2 && wall == 2) {
              canvas.drawBitmap(bmpPlankton2, null, planktonRect2, null);
          }

          canvas.drawBitmap(bmpColumnTop, null, columnRectTop, null);
          canvas.drawBitmap(bmpColumnBottom, null, columnRectBottom, null);

    }

	public void recycleBitmaps() {
		bmpColumnTop.recycle();
	    bmpColumnBottom.recycle();
	    bmpPlankton1.recycle();
	    bmpPlankton2.recycle();
	}
    
}  
