package com.neopixl.pixlui.components.textview;

import android.content.res.TypedArray;
import android.util.Log;
import com.neopixl.pixlui.R;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * TextView with custom font by XML or Code
 * This class provided too a font factory
 * @author odemolliens
 *
 */
public class TextView extends EllipsizingTextView {

	public TextView(Context context) {
		this(context, null);
    }

    public TextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
		setCustomFont(context, attrs, defStyle);
	}

    private void setCustomFont(Context ctx, AttributeSet attrs, int defStyle) {

        // Retrieve style attributes.
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.com_neopixl_pixlui_components_textview_TextView, defStyle, 0);
        String typefaceName = a.getString(R.styleable.com_neopixl_pixlui_components_textview_TextView_typeface);
        a.recycle();

        if(typefaceName != null) {
            setPaintFlags(this.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.LINEAR_TEXT_FLAG);
            setCustomFont(ctx, typefaceName);
		}
	}

	/**
	 * Use this method to set a custom font in your code (/assets/fonts/)
	 * @param ctx
	 * @param Font Name, don't forget to add file extension
	 * @return
	 */
	public boolean setCustomFont(Context ctx, String font) {
		Typeface tf = FontFactory.getInstance(ctx).getFont(font);
		if(tf != null) {
			setTypeface(tf);  
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onSizeChanged (int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}
}