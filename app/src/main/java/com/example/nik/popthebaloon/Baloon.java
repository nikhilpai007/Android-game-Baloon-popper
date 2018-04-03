package com.example.nik.popthebaloon;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nik.popthebaloon.utility.PixelHelper;

/**
 * Created by Nik on 03/04/18.
 */

public class Baloon extends android.support.v7.widget.AppCompatImageView {

    public Baloon(Context context) {
        super( context );
    }
         public Baloon(Context context, int color, int rawHeight) {
            super( context );

            this.setImageResource( R.drawable.baloon);
            this.setColorFilter(color);

            int rawWidth = rawHeight/2;

            int dpHeight = PixelHelper.pixelsToDp( rawHeight, context );
            int dpWidth = PixelHelper.pixelsToDp( rawWidth, context );
             ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth, dpHeight );
    }
}
