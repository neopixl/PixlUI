package com.neopixl.pixlui.components.edittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.neopixl.pixlui.intern.PixlUIfaceManager;

public class ExtractEditText extends android.inputmethodservice.ExtractEditText {

    public ExtractEditText(Context context) {
        super(context, null);
    }

    public ExtractEditText(Context context, AttributeSet attrs) {
        this(context, attrs,android.R.attr.editTextStyle);
    }

    public ExtractEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

            PixlUIfaceManager.applyFont(this, attrs, defStyle, context);
    }

    private final PixlUIfaceManager.DrawCallback drawCallback = new PixlUIfaceManager.DrawCallback() {
        @SuppressLint("WrongCall")
        @Override
        public void onDraw(Canvas canvas) {
            ExtractEditText.super.onDraw(canvas);
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        PixlUIfaceManager.onDrawHelper(canvas, this, drawCallback);
        super.onDraw(canvas);
    }

    /**
     * Use this method to set a custom font in your code (/assets/fonts/)
     *
     * @param ctx
     * @param font Name, don't forget to add file extension
     * @return
     */
    public boolean setCustomFont(Context ctx, String font) {
        Typeface tf = PixlUIfaceManager.getInstance(ctx).getTypeface(font);
        if (tf != null) {
            setTypeface(tf);
            return true;
        } else {
            return false;
        }
    }

}