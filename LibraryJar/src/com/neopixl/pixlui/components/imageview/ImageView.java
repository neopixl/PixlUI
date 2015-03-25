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
package com.neopixl.pixlui.components.imageview;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

/**
 * Add alpha method for old api
 * @author Olivier Demolliens. @odemolliens
 * Dev with Neopixl
 */
public class ImageView extends android.widget.ImageView {

	/**
	 * XML Attribute
	 */
	private static final String IMAGEVIEW_OS_ATTRIBUTE_TEXT_ALPHA = "alpha";

	/**
	 * State
	 */
	private boolean mOldDeviceTextAlpha;

	/**
	 * Attribute value
	 * @param context
	 */
	private float mAlpha = 1;

	public ImageView(Context context) {
		super(context);
		editTextVersion();
	}

	public ImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		editTextVersion();
		if (isOldDeviceTextAlpha()) {
			setAlpha(context, attrs);
		}
	}

	public ImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		editTextVersion();
		if (isOldDeviceTextAlpha()) {
			setAlpha(context, attrs);
		}
	}

	/**
	 * Define what version of code we need to use
	 */
	private void editTextVersion() {
		if (android.os.Build.VERSION.SDK_INT < 11) {
			setOldDeviceTextAlpha(true);
		} else {
			setOldDeviceTextAlpha(false);
		}
	}

	/**
	 * XML methods
	 *
	 * @param ctx
	 * @param attrs
	 */
	private void setAlpha(Context ctx, AttributeSet attrs) {

		if(!isInEditMode()){
			int indexSize = attrs.getAttributeCount();

			float xmlAlpha = 1;

			for (int i = 0; i < indexSize; i++) {
				if (attrs.getAttributeName(i).equals(
						IMAGEVIEW_OS_ATTRIBUTE_TEXT_ALPHA)) {
					xmlAlpha = attrs.getAttributeFloatValue(i, 1);
					break;
				}
			}
			if (xmlAlpha != 1) {
				setAlpha(xmlAlpha);
			}
		}
	}

	/**
	 * Enable apha for old api
	 * @param alpha
	 */
	@SuppressLint("NewApi")
	@Override
	public void setAlpha(float alpha) {
		if (this.isOldDeviceTextAlpha()) {
			set_Alpha(alpha);
		}else{
			super.setAlpha(alpha);
		}
	}

	/**
	 * Enable apha for old api
	 * @param alpha
	 */
	@Override
	@Deprecated
	public void setAlpha(int alpha) {
		if (this.isOldDeviceTextAlpha()) {
			set_Alpha(alpha);
		}else{
			super.setAlpha(alpha);
		}
	}

	@Override
	public void onDraw(Canvas canvas){
		if(mAlpha!=1){
			int drawedAlpha = (int) (mAlpha * 100);
			canvas.saveLayerAlpha(0, 0, canvas.getWidth(), canvas.getHeight(), drawedAlpha, Canvas.HAS_ALPHA_LAYER_SAVE_FLAG);
		}
		super.onDraw(canvas);
	}

	public boolean isOldDeviceTextAlpha() {
		return mOldDeviceTextAlpha;
	}

	public void setOldDeviceTextAlpha(boolean mOldDeviceTextAlpha) {
		this.mOldDeviceTextAlpha = mOldDeviceTextAlpha;
	}

	public float get_Alpha() {
		return mAlpha;
	}

	public void set_Alpha(float mAlpha) {
		this.mAlpha = mAlpha;
	}

}