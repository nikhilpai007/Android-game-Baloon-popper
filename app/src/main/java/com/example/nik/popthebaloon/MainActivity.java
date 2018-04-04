package com.example.nik.popthebaloon;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class MainActivity extends AppCompatActivity {

    private ViewGroup mContentView;

    private int[] mBalloonCol = new int[3];
    private int mNextCol, mWidthOfScreen, mHeightofScreen;

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

        mContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    Baloon b;
                    b = new Baloon(MainActivity.this, mBalloonCol[mNextCol], 100);
                    b.setX(motionEvent.getX());
                    b.setY(mHeightofScreen);
                    mContentView.addView(b);
                    b.BaloonGo( mHeightofScreen, 2500 );

                    if(mNextCol + 1 == mBalloonCol.length) {
                        mNextCol = 0;

                    }else {
                        mNextCol ++ ;
                    }

                }

                return false;
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
}
