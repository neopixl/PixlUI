package com.neopixl.pixlui.components.checkbox;

import com.android.export.AllCapsTransformationMethod;
import com.neopixl.pixlui.components.textview.FontFactory;
import com.neopixl.pixlui.intern.PixlUIContants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Provide more possibility with CheckBox and enable new methods on old api
 * 
 * @author Olivier Demolliens. @odemolliens Dev with Neopixl
 */
public class CheckBox extends android.widget.CheckBox {


	private static String BUTTON_ATTRIBUTE_FONT_NAME = "typeface";
	private static final String BUTTON_OS_ATTRIBUTE_TEXT_ALL_CAPS = "textAllCaps";

	/**
	 * State
	 */
	private boolean mOldDeviceTextAllCaps;

	public CheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		checkBoxVersion();
		setCustomFont(context, attrs);
		if (isOldDeviceTextAllCaps()) {
			setAllCaps(context, attrs);
		}
	}

	public CheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		checkBoxVersion();
		setCustomFont(context, attrs);
		if (isOldDeviceTextAllCaps()) {
			setAllCaps(context, attrs);
		}
	}

	public CheckBox(Context context) {
		super(context);
		checkBoxVersion();
	}

	/**
	 * Define what version of code we need to use
	 */
	private void checkBoxVersion() {
		if (android.os.Build.VERSION.SDK_INT < 14) {
			setOldDeviceTextAllCaps(true);
		} else {
			setOldDeviceTextAllCaps(false);
		}
	}

	/**
	 * XML methods
	 * 
	 * @param ctx
	 * @param attrs
	 */
	private void setCustomFont(Context ctx, AttributeSet attrs) {
		String typefaceName = attrs.getAttributeValue(
				PixlUIContants.SCHEMA_URL, BUTTON_ATTRIBUTE_FONT_NAME);

		if (typefaceName != null) {
			setPaintFlags(this.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG
					| Paint.LINEAR_TEXT_FLAG);
			setCustomFont(ctx, typefaceName);
		}
	}

	/**
	 * XML methods
	 * 
	 * @param ctx
	 * @param attrs
	 */
	private void setAllCaps(Context ctx, AttributeSet attrs) {

		if(!isInEditMode()){
			int indexSize = attrs.getAttributeCount();

			boolean allCaps = false;

			for (int i = 0; i < indexSize; i++) {
				if (attrs.getAttributeName(i).equals(
						BUTTON_OS_ATTRIBUTE_TEXT_ALL_CAPS)) {
					allCaps = attrs.getAttributeBooleanValue(i, false);
					break;
				}
			}

			if (allCaps) {
				setAllCaps(allCaps);
			}
		}
	}

	/**
	 * Use this method to uppercase all char in text.
	 * 
	 * @param allCaps
	 */
	@SuppressLint("NewApi")
	@Override
	public void setAllCaps(boolean allCaps) {
		if (this.isOldDeviceTextAllCaps()) {
			if (allCaps) {
				setTransformationMethod(new AllCapsTransformationMethod(
						getContext()));
			} else {
				setTransformationMethod(null);
			}
		} else {
			super.setAllCaps(allCaps);
		}
	}

	/**
	 * Use this method to set a custom font in your code (/assets/fonts/)
	 * 
	 * @param ctx
	 * @param Font
	 *            Name, don't forget to add file extension
	 * @return
	 */
	public boolean setCustomFont(Context ctx, String font) {
		Typeface tf = FontFactory.getInstance(ctx).getFont(font);
		if (tf != null) {
			setTypeface(tf);
			return true;
		} else {
			return false;
		}
	}

	public boolean isOldDeviceTextAllCaps() {
		return mOldDeviceTextAllCaps;
	}

	public void setOldDeviceTextAllCaps(boolean mOldDeviceTextAllCaps) {
		this.mOldDeviceTextAllCaps = mOldDeviceTextAllCaps;
	}


}
