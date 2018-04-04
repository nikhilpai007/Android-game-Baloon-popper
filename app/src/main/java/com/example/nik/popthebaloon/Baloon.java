package com.example.nik.popthebaloon;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.nik.popthebaloon.utility.PixelHelper;

public class Baloon extends ImageView implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    private ValueAnimator mAnimator;
    private Listeneer listeneer;
    private boolean pooped;

    public Baloon(Context context) {
        super( context );

        listeneer = (Listeneer) context;
    }

    public Baloon(Context context, int color, int rawHeight) {
        super( context );

        this.setImageResource( R.drawable.balloon );
        this.setColorFilter( color );

        int rawWidth = rawHeight / 2;

        int dpHeight = PixelHelper.pixelsToDp( rawHeight, context );
        int dpWidth = PixelHelper.pixelsToDp( rawWidth, context );
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams( dpWidth, dpHeight );
        setLayoutParams( params );

    }

    public void BaloonGo(int HeightofScreen, int time ) {
         mAnimator = new ValueAnimator();
         mAnimator.setDuration( time );
         mAnimator.setFloatValues( HeightofScreen, 0f);
         mAnimator.setInterpolator( new LinearInterpolator(  ) );
         mAnimator.setTarget( this );
         mAnimator.addListener( this );
         mAnimator.addUpdateListener( this );
         mAnimator.start();


    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {
        if(!pooped){
            listeneer.pop( this,false );
        }

    }

    @Override
    public void onAnimationCancel(Animator animator) {

    }

    @Override
    public void onAnimationRepeat(Animator animator) {

    }

    @Override
    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        setY( (float) valueAnimator.getAnimatedValue() );

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!pooped && event.getAction() == MotionEvent.ACTION_DOWN ){
            listeneer.pop( this, true );
            pooped = true;
            mAnimator.cancel();

        }
        return super.onTouchEvent( event );
    }

    public void setPopped(boolean b) {
        pooped =b;
    if(b){
        mAnimator.cancel();
    }


    }

    public interface Listeneer {
        void pop(Baloon baloon, boolean touch);

    }
}
