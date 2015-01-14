package com.neopixl.pixlui.components.edittext;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.neopixl.pixlui.R;
import com.neopixl.pixlui.components.textview.FontFactory;
import com.neopixl.pixlui.intern.FontStyleView;
import com.neopixl.pixlui.intern.PixlUIUtils;

public class EditText extends android.widget.EditText implements FontStyleView {

	public EditText(Context context) {
		this(context, null);
	}

	public EditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public EditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs, defStyle);
	}

	private void setCustomFont(Context ctx, AttributeSet attrs, int defStyle) {
        PixlUIUtils.setCustomFont(ctx, this,
                R.styleable.com_neopixl_pixlui_components_edittext_EditText,
                R.styleable.com_neopixl_pixlui_components_edittext_EditText_typeface,
                attrs, defStyle);
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

}