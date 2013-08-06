package com.neopixl.pixlui.components.textview;

import com.neopixl.pixlui.intern.PixlUIContants;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * TextView with custom font
 * @author odemolliens
 *
 */
public class TextViewWithFont extends EllipsizingTextView {
	
	private static String TEXTVIEW_ATTRIBUTE_FONT_NAME = "typeface";
	
    public TextViewWithFont(Context context) {
        super(context);
    }

    public TextViewWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public TextViewWithFont(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {

    	String typefaceName = attrs.getAttributeValue(PixlUIContants.SCHEMA_URL, TEXTVIEW_ATTRIBUTE_FONT_NAME);

    	if(typefaceName!=null){
    		setPaintFlags(this.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.LINEAR_TEXT_FLAG);
            setCustomFont(ctx, typefaceName);
    	}
    }

    public boolean setCustomFont(Context ctx, String font) {
        Typeface tf = FontFactory.getInstance(ctx).getFont(font);
        if(tf != null){
        	setTypeface(tf);  
        	return true;
        }else{
        	return false;
        }
    }

	@Override
	protected void onSizeChanged (int w, int h, int oldw, int oldh) {
	    super.onSizeChanged(w, h, oldw, oldh);
	}

}