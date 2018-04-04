package com.example.nik.popthebaloon;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mContentView;

    private int[] mBalloonCol = new int[3];
    private int mNextCol, mWidthOfScreen, mHeightofScreen;
    public static final int MINIMUM_ANIM_DELAY = 500;
    public static final int MAXIMUM_ANIM_DELAY = 1500;
    public static final int MINIMUM_ANIM_TIME = 1000;
    public static final int MAXIMUM_ANIM_TIME = 8000;
    private int mLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBalloonCol [0]= Color.argb(225,225,0,0);
        mBalloonCol[1]= Color.argb(225,0,225,0);
        mBalloonCol[2]= Color.argb(225,0,0,225);


        getWindow().setBackgroundDrawableResource(R.drawable.background);

        mContentView = (ViewGroup) findViewById(R.id.activity_main);
        setToFullScreen();

        ViewTreeObserver viewTreeObserver=mContentView.getViewTreeObserver();
        if(viewTreeObserver.isAlive()){
            viewTreeObserver.addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    mWidthOfScreen =mContentView.getWidth();
                    mHeightofScreen =mContentView.getHeight();
                }
            } );
        }

        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToFullScreen();
            }
        });


    }

    private void setToFullScreen() {
        ViewGroup rootLayout = (ViewGroup) findViewById(R.id.activity_main);
        rootLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToFullScreen();
    }

    private void levels() {
        mLevel++;
        Launcher launcher = new Launcher();
        launcher.execute(mLevel);



    }

    public void goButton(View view) {
        levels();
    }

    private class Launcher extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            if (params.length != 1) {
                throw new AssertionError(
                        "Expected 1 param for current level");
            }

            int level = params[0];
            int maxDelay = Math.max(MINIMUM_ANIM_DELAY,
                    (MAXIMUM_ANIM_DELAY - ((level - 1) * 500)));
            int minDelay = maxDelay / 2;

            int balloonsLaunched = 0;
            while (balloonsLaunched < 3) {

//              Get a random horizontal position for the next balloon
                Random random = new Random(new Date().getTime());
                int xPosition = random.nextInt(mWidthOfScreen - 200);
                publishProgress(xPosition);
                balloonsLaunched++;

//              Wait a random number of milliseconds before looping
                int delay = random.nextInt(minDelay) + minDelay;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return null;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int xPosition = values[0];
            launchBalloon(xPosition);
        }

    }

    private void launchBalloon(int x) {

        Baloon balloon = new Baloon(this, mBalloonCol[mNextCol], 150);

        if (mNextCol + 1 == mBalloonCol.length) {
            mNextCol = 0;
        } else {
            mNextCol++;
        }

//      Set balloon vertical position and dimensions, add to container
        balloon.setX(x);
        balloon.setY(mHeightofScreen + balloon.getHeight());
        mContentView.addView(balloon);

//      Let 'er fly
        int duration = Math.max(MINIMUM_ANIM_TIME, MAXIMUM_ANIM_TIME - (mLevel * 1000));
        balloon.BaloonGo(mHeightofScreen, duration);

    }
}
