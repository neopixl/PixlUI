package com.neopixl.pixlui.components.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

import com.android.export.AllCapsTransformationMethod;
import com.neopixl.pixlui.intern.PixlUIContants;

/**
 * Does not work - In Progress
 * @author odemolliens - jclemot
 *
 */
public class AutoResizeTextView extends TextView {

	// Attributes
	private Paint mTestPaint;
	private float defaultTextSize;

	/**
	 * State
	 */
	private boolean mOldDeviceTextAllCaps;

	/**
	 * XML attribute
	 */
	private static String TEXTVIEW_ATTRIBUTE_FONT_NAME = "typeface";
	private static final String TEXTVIEW_OS_ATTRIBUTE_TEXT_ALL_CAPS = "textAllCaps";

	public AutoResizeTextView(Context context) {
		super(context);
		editTextVersion();
		initialize();
	}

	public AutoResizeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		editTextVersion();
		setCustomFont(context, attrs);
		initialize();
	}

	private void initialize() {
		mTestPaint = new Paint();
		mTestPaint.set(this.getPaint());
		defaultTextSize = getTextSize();
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
	@SuppressLint("NewApi")
	private void setAllCaps(Context ctx, AttributeSet attrs) {

		if (!isInEditMode()) {
			int indexSize = attrs.getAttributeCount();

			boolean allCaps = false;

			for (int i = 0; i < indexSize; i++) {
				if (attrs.getAttributeName(i).equals(TEXTVIEW_OS_ATTRIBUTE_TEXT_ALL_CAPS)) {
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
				setTransformationMethod(new AllCapsTransformationMethod(getContext()));
			} else {
				setTransformationMethod(null);
			}
		} else {
			super.setAllCaps(allCaps);
		}
	}

	public boolean isOldDeviceTextAllCaps() {
		return mOldDeviceTextAllCaps;
	}

	public void setOldDeviceTextAllCaps(boolean mOldDeviceTextAllCaps) {
		this.mOldDeviceTextAllCaps = mOldDeviceTextAllCaps;
	}

	/*
	 * Re size the font so the specified text fits in the text box assuming the text box is the specified width.
	 */
	private void refitText(String text, int textWidth) {

		if (textWidth <= 0 || text.length() == 0)
			return;

		int targetWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();

		// this is most likely a non-relevant call
		if (targetWidth <= 2)
			return;

		// text already fits with the xml-defined font size?
		mTestPaint.set(this.getPaint());
		mTestPaint.setTextSize(defaultTextSize);
		if (mTestPaint.measureText(text) <= targetWidth) {
			this.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultTextSize);
			return;
		}

		// adjust text size using binary search for efficiency
		float hi = defaultTextSize;
		float lo = 2;
		final float threshold = 0.5f; // How close we have to be
		while (hi - lo > threshold) {
			float size = (hi + lo) / 2;
			mTestPaint.setTextSize(size);
			if (mTestPaint.measureText(text) >= targetWidth)
				hi = size; // too big
			else
				lo = size; // too small

		}

		// Use lo so that we undershoot rather than overshoot
		this.setTextSize(TypedValue.COMPLEX_UNIT_PX, lo);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
		int height = getMeasuredHeight();
		refitText(this.getText().toString(), parentWidth);
		this.setMeasuredDimension(parentWidth, height);
	}

	@Override
	protected void onTextChanged(final CharSequence text, final int start, final int before, final int after) {
		refitText(text.toString(), this.getWidth());
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (w != oldw || h != oldh) {
			refitText(this.getText().toString(), w);
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
	protected boolean setCustomFont(Context ctx, String font) {
		Typeface tf = FontFactory.getInstance(ctx).getFont(font);
		if (tf != null) {
			setTypeface(tf);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * XML methods
	 * 
	 * @param ctx
	 * @param attrs
	 */
	private void setCustomFont(Context ctx, AttributeSet attrs) {

		String typefaceName = attrs.getAttributeValue(PixlUIContants.SCHEMA_URL, TEXTVIEW_ATTRIBUTE_FONT_NAME);

		if (typefaceName != null) {
			setPaintFlags(this.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG | Paint.LINEAR_TEXT_FLAG);
			setCustomFont(ctx, typefaceName);
		}
	}
}