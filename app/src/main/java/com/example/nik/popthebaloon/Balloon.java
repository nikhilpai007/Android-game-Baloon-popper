package com.example.nik.popthebaloon;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.nik.popthebaloon.utility.PixelHelper;

import static com.example.nik.popthebaloon.R.drawable.baloon;

/**
 * Created by Nik on 03/04/18.
 */


class Balloon extends ImageView {

    public Balloon(Context context) {super(context);}

    public Balloon(Context context, int color, int rawHeight) {
        super(context);

        this.setImageResource(R.drawable.baloon);
        this.setColorFilter(color);

        int rawWidth = rawHeight/2;

        int dpHeight = PixelHelper.pixelsToDp(rawHeight, context);
        int dpWidth = PixelHelper.pixelsToDp(rawWidth, context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(dpWidth, dpHeight);
        setLayoutParams(params);
    }
}
