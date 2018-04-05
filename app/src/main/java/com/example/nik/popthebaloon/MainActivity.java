package com.example.nik.popthebaloon;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nik.popthebaloon.utility.HighScoreHelper;
import com.example.nik.popthebaloon.utility.SimpleAlertDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
implements Balloon.Listener {

    private int mNextCol, mWidthOfScreen, mHeightofScreen;
    private static final int MINIMUM_ANIM_DELAY = 500;
    private static final int MAXIMUM_ANIM_DELAY = 1500;
    private static final int MINIMUM_ANIM_TIME = 1000;
    private static final int MAXIMUM_ANIM_TIME = 8000;
    private static final int NUMBER_OF_PINS=5;
    private static final int BALLOONS_PER_LEVEL = 10;
    private ViewGroup mContentView;
    private int[] mBalloonCol = new int[3];
    private int mNextColor, mScreenWidth, mScreenHeight;
    private int mLevel;
    private int mScore;
    private int mPinsUsed;
    TextView mScoreDisplay,mLevelDisplay;
    private List <ImageView> mPinImages= new ArrayList<>();
    private List<Balloon> mBalloons = new ArrayList<>();

    private Button mGoButton;
    private boolean mPlaying;
    private boolean mGameStopped = true;
    private int mBalloonsPopped;
    private SoundHelper mSoundHelper;
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

                public void onClick(View view) {

                    setToFullScreen();
                }
            });

          mScoreDisplay=(TextView)findViewById(R.id.score_display);

                mLevelDisplay=(TextView)findViewById(R.id.level_display);
                mPinImages.add((ImageView) findViewById(R.id.pushpin1));
                mPinImages.add((ImageView) findViewById(R.id.pushpin2));;
                mPinImages.add((ImageView) findViewById(R.id.pushpin3));
                mPinImages.add((ImageView) findViewById(R.id.pushpin4));
                mPinImages.add((ImageView) findViewById(R.id.pushpin5));;
                mGoButton = (Button) findViewById(R.id.go_button);
        displayUpdate();
        mSoundHelper = new SoundHelper();

        mSoundHelper.prepareMusicPlayer(this);


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


    private void startGame()
    {

        setToFullScreen();

        mScore = 0;

        mLevel = 0;

        mPinsUsed = 0;

        for (ImageView pin :

                mPinImages) {

            pin.setImageResource(R.drawable.pin);
        }

        mGameStopped = false;

        startLevel();

        mSoundHelper.playMusic();
    }





    private void startLevel() {

        mLevel++;

        displayUpdate();


        BaloonLauncher launcher = new BalloonLauncher();

        launcher.execute(mLevel);

        mPlaying = true;

        mBalloonsPopped = 0;

        mGoButton.setText("Stop game");

    }

    private void finishLevel()
    {

        Toast.makeText(this, String.format("You finished level %d", mLevel),

                Toast.LENGTH_SHORT).show();

        mPlaying = false;

        mGoButton.setText(String.format("Start level %d", mLevel + 1));

    }






    public void goButton(View view) {
        if (mPlaying) {

        gameOver(false);
    }
    else if (mGameStopped) {
        startGame();

    } else {
        startLevel();
    }
    }

    @Override
    public void pop(Balloon baloon, boolean touch) {
        mBalloonsPopped++;
        mContentView.removeView( baloon );
        mBalloons.remove(baloon);
        if(touch){
            mScore++;
        } else{
            mPinsUsed++;
            if(mPinsUsed<=mPinImages.size()){

                mPinImages.get(mPinsUsed-1)
                        .setImageResource(R.drawable.pin_off);
            }
            if (mPinsUsed==NUMBER_OF_PINS){
                gameOver(true);
                return;

            } else {
                Toast.makeText(this, "Missed that one :(",Toast.LENGTH_SHORT).show();
            }
        }
        displayUpdate();
        if (mBalloonsPopped == BALLOONS_PER_LEVEL)
        {
            finishLevel();
        }
    }






    private void gameOver(boolean allPinsUsed) {

        Toast.makeText(this, "Game over!", Toast.LENGTH_SHORT).show();

        mSoundHelper.pauseMusic();


        for (Balloon balloon :

                mBalloons) {

            mContentView.removeView(balloon);

            balloon.setPopped(true);
        }

        mBalloons.clear();

        mPlaying = false;

        mGameStopped = true;

        mGoButton.setText("Start game");


        if (allPinsUsed) {

            if (HighScoreHelper.isTopScore(this, mScore)) {

                HighScoreHelper.setTopScore(this, mScore);

                SimpleAlertDialog dialog = SimpleAlertDialog.newInstance("New High Score!",

                        String.format("Your new high score is %d", mScore));

                dialog.show(getSupportFragmentManager(), null);
            }}}
    private void displayUpdate() {
        // TODO: 03/04/18 Update Display
        mScoreDisplay.setText(String.valueOf(mScore));
        mLevelDisplay.setText(String.valueOf(mLevel));
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
            while (mPlaying && balloonsLaunched < BALLOONS_PER_LEVEL) {

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



    private void launchBalloon(int x) {

        Balloon balloon = new Balloon(this, mBalloonCol[mNextCol], 150);
        mBalloons.add(balloon);
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
        balloon.BalloonGo(mHeightofScreen, duration);

    }

}
