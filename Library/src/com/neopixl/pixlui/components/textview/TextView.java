/*
 Copyright 2013 Neopixl - Olivier Demolliens

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this

file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under

the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 

ANY KIND, either express or implied. See the License for the specific language governing

permissions and limitations under the License.
 */
package com.neopixl.pixlui.components.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.android.export.AllCapsTransformationMethod;
import com.neopixl.pixlui.intern.PixlUIContants;

/**
 * Provide more possibility with Button and enable new methods on old api
 * 
 * @author Olivier Demolliens. @odemolliens Dev with Neopixl
 */
public class TextView extends EllipsizingTextView {

	/**
	 * XML attribute
	 */
	private static String TEXTVIEW_ATTRIBUTE_FONT_NAME = "typeface";
	private static final String TEXTVIEW_OS_ATTRIBUTE_TEXT_ALL_CAPS = "textAllCaps";

	/**
	 * State
	 */
	private boolean mOldDeviceTextAllCaps;

	public TextView(Context context) {
		super(context,false);
		editTextVersion();
	}

	public TextView(Context context, AttributeSet attrs) {
		super(context, attrs,false);
		editTextVersion();
		setCustomFont(context, attrs);
		if (isOldDeviceTextAllCaps()) {
			setAllCaps(context, attrs);
		}
	}

	public TextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle,false);
		editTextVersion();
		setCustomFont(context, attrs);
		if (isOldDeviceTextAllCaps()) {
			setAllCaps(context, attrs);
		}
	}

	public TextView(Context context, boolean canBeEllipsized) {
		super(context,canBeEllipsized);
		editTextVersion();
	}

	public TextView(Context context, AttributeSet attrs, boolean canBeEllipsized) {
		super(context, attrs,canBeEllipsized);
		editTextVersion();
		setCustomFont(context, attrs);
		if (isOldDeviceTextAllCaps()) {
			setAllCaps(context, attrs);
		}
	}

	public TextView(Context context, AttributeSet attrs, int defStyle, boolean canBeEllipsized) {
		super(context, attrs, defStyle,canBeEllipsized);
		editTextVersion();
		setCustomFont(context, attrs);
		if (isOldDeviceTextAllCaps()) {
			setAllCaps(context, attrs);
		}
	}

	/**
	 * Define what version of code we need to use
	 */
	private void editTextVersion() {
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
				PixlUIContants.SCHEMA_URL, TEXTVIEW_ATTRIBUTE_FONT_NAME);

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

		if(!isInEditMode()){
			int indexSize = attrs.getAttributeCount();

			boolean allCaps = false;

			for (int i = 0; i < indexSize; i++) {
				if (attrs.getAttributeName(i).equals(
						TEXTVIEW_OS_ATTRIBUTE_TEXT_ALL_CAPS)) {
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