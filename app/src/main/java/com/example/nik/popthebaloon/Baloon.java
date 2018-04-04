package com.example.nik.popthebaloon;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nik.popthebaloon.utility.PixelHelper;

public class Baloon extends ImageView {

    public Baloon(Context context) {
        super( context );
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
}
