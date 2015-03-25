package com.neopixl.pixlui.components.radiobutton;

import com.android.export.AllCapsTransformationMethod;
import com.neopixl.pixlui.components.textview.FontFactory;
import com.neopixl.pixlui.intern.PixlUIContants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

/*
 Copyright 2014 Olivier Demolliens

 Licensed under the Apache License, Version 2.0 (the "License"); you may not use this

 file except in compliance with the License. You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software distributed under

 the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 

 ANY KIND, either express or implied. See the License for the specific language governing

 permissions and limitations under the License.
 */
public class RadioButton extends android.widget.RadioButton {

	/**
	 * XML attribute
	 */
	private static String RADIOBUTTON_ATTRIBUTE_FONT_NAME = "typeface";
	private static final String RADIOBUTTON_OS_ATTRIBUTE_TEXT_ALL_CAPS = "textAllCaps";

	/**
	 * State
	 */
	private boolean mOldDeviceTextAllCaps;

	public RadioButton(Context context) {
		super(context);
		radioButtonVersion();
	}

	public RadioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		radioButtonVersion();
		setCustomFont(context, attrs);
		if (isOldDeviceTextAllCaps()) {
			setAllCaps(context, attrs);
		}
	}

	public RadioButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		radioButtonVersion();
		setCustomFont(context, attrs);
		if (isOldDeviceTextAllCaps()) {
			setAllCaps(context, attrs);
		}
	}

	/**
	 * Define what version of code we need to use
	 */
	private void radioButtonVersion() {
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
				PixlUIContants.SCHEMA_URL, RADIOBUTTON_ATTRIBUTE_FONT_NAME);

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
	@SuppressLint("NewApi")
	private void setAllCaps(Context ctx, AttributeSet attrs) {

		if (!isInEditMode()) {
			int indexSize = attrs.getAttributeCount();

			boolean allCaps = false;

			for (int i = 0; i < indexSize; i++) {
				if (attrs.getAttributeName(i).equals(
						RADIOBUTTON_OS_ATTRIBUTE_TEXT_ALL_CAPS)) {
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
