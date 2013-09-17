package com.neopixl.pixlui.components.button;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.neopixl.pixlui.components.textview.FontFactory;
import com.neopixl.pixlui.intern.PixlUIConstants;

public class Button extends android.widget.Button {

	private static String BUTTON_ATTRIBUTE_FONT_NAME = "typeface";

	public Button(Context context) {
		super(context);
	}

	public Button(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	public Button(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}

	private void setCustomFont(Context ctx, AttributeSet attrs) {
		String typefaceName = attrs.getAttributeValue(PixlUIConstants.SCHEMA_URL, BUTTON_ATTRIBUTE_FONT_NAME);

		if(typefaceName!=null){
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
		if(tf != null){
			setTypeface(tf);  
			return true;
		}else{
			return false;
		}
	}

}