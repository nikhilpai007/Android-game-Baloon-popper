package com.example.nik.popthebaloon;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.example.nik.popthebaloon.utility.PixelHelper;

/**
 * Created by Nik on 03/04/18.
 */

public class Baloon extends android.support.v7.widget.AppCompatImageView implements ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private ValueAnimator mAnimator;

    public Baloon(Context context) {
        super( context );
    }
         public Baloon(Context context, int color, int rawHeight) {
            super( context );

            this.setImageResource( R.drawable.balloon);
            this.setColorFilter(color);

            int rawWidth = rawHeight/2;

            int dpHeight = PixelHelper.pixelsToDp( rawHeight, context );
            int dpWidth = PixelHelper.pixelsToDp( rawWidth, context );
             ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth, dpHeight );
             setLayoutParams( params );
    }

    public void baloongo(int heightscrn, int time ) {
        mAnimator = new ValueAnimator();
        mAnimator.setDuration( time );
        mAnimator.setFloatValues( heightscrn, 0f);
        mAnimator.setInterpolator(new LinearInterpolator(  ) );
        mAnimator.setTarget(this);
        mAnimator.addListener(this);
        mAnimator.addUpdateListener(this);
        mAnimator.start();

    }

    @Override
    public void onAnimationStart(Animator animator) {

    }

    @Override
    public void onAnimationEnd(Animator animator) {

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
}
