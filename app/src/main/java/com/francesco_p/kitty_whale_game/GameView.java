package com.francesco_p.kitty_whale_game;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    // This is no instantiation. Just saying that the holder will be of a class implementing SurfaceHolder
    private SurfaceHolder holder;

    private String fontName = "fonts/KGWhenOceansRise.ttf";

    private GameThread gameThread;
    private Bitmap bmpWhale;
    private Bitmap bmpColumnTop;
    private Bitmap bmpColumnBottom;
    private Bitmap bmpGround;
    private Bitmap bmpBackground;
    private Bitmap bmpGetReady;
    private Bitmap bmpGameOver;

    private Bitmap bmpBtnPauseOn;
    private Bitmap bmpBtnPauseOff;
    private Bitmap bmpBtnSoundOn;
    private Bitmap bmpBtnSoundOff;

    private WhaleSprite whaleSprite;
    private Wall wall1;
    private Wall wall2;

    private Ground ground1;
    private Ground ground2;

    public boolean dead = false;
    public boolean callDied = false;
    public boolean scored1 = false;
    public boolean scored2 = false;
    public boolean gameStarted = false;
    public int score = 0;
    private int highscore = 0;

    // add game paused and sound
    public boolean gamePaused = false;
    public boolean soundOn = true;

    public Rect rectGround = new Rect(0, 0, 0, 0);

    private Paint paintText1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintText2 = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint paintText3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paintText4 = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float scoreFromTop = 30;

    private int mCanvasWidth;
    private int mCanvasHeight;

    private int origin_x = 0;
    private int origin_y = 0;

    private float orig_x1 = 0;
    private float orig_x2 = 0;

    private SharedPreferences prefs;

    // change this number for the height of the ground
    private double groundHeightModifier = 0.20d;

    // sound settings
    public static SoundClass soundPlayer;
    private boolean deadSoundPlayed = false;
    public boolean scoreSoundPlayed1 = false;
    public boolean scoreSoundPlayed2 = false;

    private VelocityTracker mVelocityTracker = null;

    private int transition = 0;
    private Rect rectBtnStart = new Rect(0, 0, 0, 0);
    private Rect rectBtnPause = new Rect(0, 0, 0, 0);
    private Rect rectBtnSound = new Rect(0, 0, 0, 0);


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Holder is now the internal/private mSurfaceHolder inherit
        // from the SurfaceView class, which is from an anonymous
        // class implementing SurfaceHolder interface.
        holder = getHolder();
        holder.addCallback(this);

        bmpWhale = BitmapFactory.decodeResource(getResources(), R.drawable.whale);
        bmpColumnTop = BitmapFactory.decodeResource(getResources(), R.drawable.column_top);
        bmpColumnBottom = BitmapFactory.decodeResource(getResources(), R.drawable.column_bottom);
        bmpGround = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        bmpBackground = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        bmpGetReady = BitmapFactory.decodeResource(getResources(), R.drawable.getready);
        bmpGameOver = BitmapFactory.decodeResource(getResources(), R.drawable.gameover);
        bmpBtnPauseOff = BitmapFactory.decodeResource(getResources(), R.drawable.btnpauseoff);
        bmpBtnPauseOn = BitmapFactory.decodeResource(getResources(), R.drawable.btnpauseon);
        bmpBtnSoundOff = BitmapFactory.decodeResource(getResources(), R.drawable.btnsoundoff);
        bmpBtnSoundOn = BitmapFactory.decodeResource(getResources(), R.drawable.btnsoundon);

        whaleSprite = new WhaleSprite(this, bmpWhale);

        ground1 = new Ground(this, bmpGround);
        ground2 = new Ground(this, bmpGround);

        wall1 = new Wall(this, bmpColumnTop, bmpColumnBottom);
        wall2 = new Wall(this, bmpColumnTop, bmpColumnBottom);

        soundPlayer = new SoundClass(getContext());

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    // This is always called at least once, after surfaceCreated
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        mCanvasWidth = width;
        mCanvasHeight = height;

        origin_x = mCanvasWidth / 2;
        origin_y = mCanvasHeight / 2;

        float scoreTextSize = getResources().getDimensionPixelSize(R.dimen.scoretextsize) + getResources().getDimensionPixelSize(R.dimen.scoretextsizemultiplier);

        scoreFromTop = mCanvasWidth / getResources().getInteger(R.integer.scorefromtop);

        bmpBackground = Bitmap.createScaledBitmap(bmpBackground, mCanvasWidth, mCanvasHeight, false);

        bmpGetReady = Bitmap.createScaledBitmap(bmpGetReady, mCanvasWidth, mCanvasHeight, false);
        bmpGameOver = Bitmap.createScaledBitmap(bmpGameOver, mCanvasWidth, mCanvasHeight, false);

        rectGround.set(0, (int) (mCanvasHeight - (mCanvasHeight * groundHeightModifier)), mCanvasWidth, mCanvasHeight);
        rectBtnStart.set((int) (mCanvasWidth / 2.75), (int) (mCanvasHeight / 1.47), (int) (mCanvasWidth / 1.5), (int) (mCanvasHeight / 1.28));
        rectBtnPause.set(0, 0, bmpBtnPauseOn.getWidth() + 10, bmpBtnPauseOn.getHeight() + 10);
        rectBtnSound.set(mCanvasWidth - bmpBtnSoundOn.getWidth() - 10, 0, mCanvasWidth, bmpBtnSoundOn.getHeight() + 10);

        whaleSprite.setSizes(mCanvasWidth, mCanvasHeight, rectGround);

        ground1.setSizes(mCanvasWidth, mCanvasHeight, rectGround, 1);
        ground2.setSizes(mCanvasWidth, mCanvasHeight, rectGround, 2);

        wall1.setSizes(mCanvasWidth, mCanvasHeight, 1);
        wall2.setSizes(mCanvasWidth, mCanvasHeight, 2);

        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), fontName);
        paintText1.setTypeface(tf);
        paintText1.setColor(Color.WHITE);
        paintText1.setAntiAlias(true);
        paintText1.setTextAlign(Align.CENTER);
        paintText1.setTextSize(scoreTextSize);

        paintText2.setTypeface(tf);
        paintText2.setAntiAlias(true);
        paintText2.setTextAlign(Align.CENTER);
        paintText2.setTextSize(scoreTextSize);

        paintText3.setTypeface(tf);
        paintText3.setAntiAlias(true);
        paintText3.setColor(Color.WHITE);
        paintText3.setTextAlign(Align.CENTER);
        paintText3.setTextSize(getResources().getDimensionPixelSize(R.dimen.gameovertextsize));

        paintText4.setTypeface(tf);
        paintText4.setAntiAlias(true);
        paintText4.setTextAlign(Align.CENTER);
        paintText4.setTextSize(getResources().getDimensionPixelSize(R.dimen.gameovertextsize));

        transition = mCanvasWidth;

        scoreFromTop = dpToPx(scoreFromTop);

        highscore = prefs.getInt("highscore", 0);

        soundOn = prefs.getBoolean("soundon", true);

        if (gameThread == null) {
            gameThread = new GameThread(this, holder);
            gameThread.setRunning(true);
            gameThread.start();
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean b) {
        try {
            gameThread.setRunning(b);
            if (b) {
                gameThread = null;
                gameThread = new GameThread(this, holder);
                gameThread.setRunning(true);
                gameThread.start();
            }
        } catch (Exception e) {
            Log.i("setRunning", "Thread catching up.");
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {

                if (mVelocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker.clear();
                }

                mVelocityTracker.addMovement(event);

                int x = (int) event.getX();
                int y = (int) event.getY();

                if (dead) {

                    // if we touch the play button on gameover screen
                    if (rectBtnStart.contains(x, y) && transition == 0) {

                        dead = false;
                        gameStarted = false;
                        deadSoundPlayed = false;
                        callDied = false;
                        scoreSoundPlayed1 = false;
                        transition = mCanvasWidth;
                        score = 0;

                    }

                } else if (rectBtnPause.contains(x, y)) {

                    if (gamePaused) {
                        gamePaused = false;
                    } else {
                        gamePaused = true;
                    }

                }

                // if we touch the sound button (top right corner), turn off sounds
                else if (rectBtnSound.contains(x, y)) {

                    if (soundOn) {
                        soundOn = false;

                    } else {
                        soundOn = true;
                    }
                    prefs.edit().putBoolean("sound on", soundOn).apply();

                } else if (gameStarted) {
                    whaleSprite.jump();
                } else if (transition == 0) {
                    gameStarted = true;
                    callDied = false;
                    transition = mCanvasWidth;
                }

                break;

            }

            case MotionEvent.ACTION_MOVE: {
                mVelocityTracker.addMovement(event);
                // When you want to determine the velocity, call
                // computeCurrentVelocity(). Then call getXVelocity()
                // and getYVelocity() to retrieve the velocity for each pointer ID.
                mVelocityTracker.computeCurrentVelocity(1000);
                // Log velocity of pixels per second
                // Best practice to use VelocityTrackerCompat where possible.
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                // Return a VelocityTracker object back to be re-used by others.
                mVelocityTracker.recycle();
                break;
            }

            case MotionEvent.ACTION_UP: {
                // Return a VelocityTracker object back to be re-used by others.
                break;
            }
        }
        return true;
    }

    private float dpToPx(float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, dp, getResources().getDisplayMetrics());
    }

    public void score1() {
        score += 1;
        scored1 = true;

        // add sound button code below
        if (!scoreSoundPlayed1 && soundOn) {
            soundPlayer.playSound(getContext(), soundPlayer.sound_eat);
            scoreSoundPlayed1 = true;
        }

        // save in app if it's a high score
        if (score > prefs.getInt("highscore", 0)) {
            highscore = score;
            prefs.edit().putInt("highscore", score).apply();
        }

    }

    public void score2() {
        score += 1;
        scored2 = true;

        // add sound button code below
        if (!scoreSoundPlayed2 && soundOn) {
            soundPlayer.playSound(getContext(), soundPlayer.sound_eat);
            scoreSoundPlayed2 = true;
        }

        // save in app if it's a high score
        if (score > prefs.getInt("highscore", 0)) {
            highscore = score;
            prefs.edit().putInt("highscore", score).apply();
        }
    }

    public void update() {

        whaleSprite.update(System.currentTimeMillis());
        ground1.update();
        ground2.update();

        if (gameStarted && !dead) {
            wall1.update(1);
            wall2.update(2);
        }

    }

    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);

        // draw background
        canvas.drawBitmap(bmpBackground, 0, 0, null);

        if (dead) {

            // play sound when whale dies
            if (!deadSoundPlayed) {

                // add sound button code below
                if (soundOn) {
                    soundPlayer.playSound(getContext(), soundPlayer.sound_lose);
                }

                orig_x1 = (float) (origin_x - (mCanvasWidth / 12));
                orig_x2 = (float) (origin_x + (mCanvasWidth / 12));

                deadSoundPlayed = true;
            }

            // draw game over screen
            canvas.drawBitmap(bmpGameOver, transition, 0, null);

            if (transition > 1) {
                transition -= mCanvasWidth / 10;
            }
            if (transition < 0) {
                transition = 0;
            }

            // draw score text1
            canvas.drawText(score + "", orig_x1 + transition, origin_y - paintText3.ascent(), paintText3);
            canvas.drawText(score + "", orig_x1 + transition, origin_y - paintText4.ascent(), paintText4);

            // draw score text2
            canvas.drawText(highscore + "", orig_x2 + transition, origin_y - paintText3.ascent(), paintText3);
            canvas.drawText(highscore + "", orig_x2 + transition, origin_y - paintText4.ascent(), paintText4);

            if (!callDied) {
                died();
            }

        } else {
            // draw whale
            whaleSprite.draw(canvas);

            if (gameStarted) {

                // draw walls
                wall1.draw(canvas, 1);
                wall2.draw(canvas, 2);

                // draw the score
                canvas.drawText(score + "", origin_x, scoreFromTop - paintText1.ascent(), paintText1);
                canvas.drawText(score + "", origin_x, scoreFromTop - paintText2.ascent(), paintText2);

            } else {
                // draw start screen
                canvas.drawBitmap(bmpGetReady, transition, 0, null);

                if (transition > 1) {
                    transition -= mCanvasWidth / 10;
                }
                if (transition < 0) {
                    transition = 0;
                }

            }

            // draw pause and sound buttons
            if (gamePaused) {
                canvas.drawBitmap(bmpBtnPauseOn, 10, 10, null);
            } else {
                canvas.drawBitmap(bmpBtnPauseOff, 10, 10, null);
            }
            if (soundOn) {
                canvas.drawBitmap(bmpBtnSoundOn, mCanvasWidth - bmpBtnSoundOn.getWidth() - 10, 10, null);
            } else {
                canvas.drawBitmap(bmpBtnSoundOff, mCanvasWidth - bmpBtnSoundOn.getWidth() - 10, 10, null);
            }

            Rect whaleBounds = whaleSprite.getBounds();

            // If whale collides with a wall, it dies and game, whale, walls are all reset
            if (gameStarted) {
                if (Rect.intersects(wall1.getRectTop(), whaleBounds) || Rect.intersects(wall1.getRectBottom(), whaleBounds) || Rect.intersects(wall2.getRectTop(), whaleBounds) || Rect.intersects(wall2.getRectBottom(), whaleBounds)) {
                    dead = true;
                }
            }

            // if already got a point, don't do score.
            if (!scored1) {
                if (Rect.intersects(wall1.getPlanktonRect1(), whaleBounds)) {
                    score1();
                }
            }

            if (!scored2) {
                if (Rect.intersects(wall2.getPlanktonRect2(), whaleBounds)) {
                    score2();
                }
            }

        }

    }

    // this is executed on death, just sets a random y value and tells Game that the whale died :(
    public void died() {
        whaleSprite.reset(mCanvasHeight);
        scored1 = false;
        scored2 = false;
        wall1.reset(1);
        wall2.reset(2);
        callDied = true;
    }

    public void recycleBitmaps() {
        whaleSprite.recycleBitmaps();
        wall1.recycleBitmaps();
        wall2.recycleBitmaps();
        ground1.recycleBitmaps();
        ground2.recycleBitmaps();
        bmpWhale.recycle();
        bmpColumnTop.recycle();
        bmpColumnBottom.recycle();
        bmpGround.recycle();
        bmpBackground.recycle();
        bmpGetReady.recycle();
        bmpGameOver.recycle();
        bmpBtnPauseOn.recycle();
        bmpBtnPauseOff.recycle();
        bmpBtnSoundOff.recycle();
        bmpBtnSoundOn.recycle();
    }

}