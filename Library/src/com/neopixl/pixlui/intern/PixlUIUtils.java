package com.neopixl.pixlui.intern;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.util.AttributeSet;

public class PixlUIUtils {

    public static void setCustomFont(Context ctx, FontStyleView view, int[] attrs, int typefaceId, AttributeSet set, int defStyle) {

        // Retrieve style attributes.
        TypedArray a = ctx.obtainStyledAttributes(set, attrs, defStyle, 0);
        String typefaceName = a.getString(typefaceId);
        a.recycle();

        if(typefaceName != null) {
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.LINEAR_TEXT_FLAG);
            view.setCustomFont(ctx, typefaceName);
        }
    }
}
