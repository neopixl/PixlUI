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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.Layout;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Better ellipsizing for TextView
 * @author Olivier Demolliens. @odemolliens
 * Dev with Neopixl
 */

public class EllipsizingTextView extends TextView {

	private static final String TEXTVIEW_ELLIPSIZING_ELLIPSIS = "\u2026";
	private static final Pattern TEXTVIEW_ELLIPSIZING_DEFAULT_END_PUNCTUATION = Pattern.compile("[\\.,\u2026;:\\s]*$", Pattern.DOTALL);

	public interface EllipsizeListener {
		void ellipsizeStateChanged(boolean ellipsized);
	}

	private final List<EllipsizeListener> ellipsizeListeners = new ArrayList<EllipsizeListener>();

	private boolean isEllipsized;
	private boolean isStale;
	private boolean programmaticChange;

	private String fullText;

	private int maxLines;

	private float lineSpacingMultiplier = 1.0f;
	private float lineAdditionalVerticalPadding = 0.0f;
	
	private boolean canBeEllipsized = false;

	private Pattern endPunctuationPattern;

	public EllipsizingTextView(Context context) {
		this(context, null);
	}
	
	public EllipsizingTextView(Context context, boolean canBeEllipsized) {
		this(context, null);
		setCanBeEllipsized(canBeEllipsized);
	}

	public EllipsizingTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public EllipsizingTextView(Context context, AttributeSet attrs, boolean canBeEllipsized) {
		this(context, attrs, 0);
		setCanBeEllipsized(canBeEllipsized);
	}

	@SuppressLint("Recycle")
	public EllipsizingTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		if(canBeEllipsized){
			super.setEllipsize(null);
			initialize(context, attrs);
		}
	}
	
	public EllipsizingTextView(Context context, AttributeSet attrs, int defStyle, boolean canBeEllipsized) {
		super(context, attrs, defStyle);
		setCanBeEllipsized(canBeEllipsized);
		
		if(canBeEllipsized){
			super.setEllipsize(null);
			initialize(context, attrs);
		}
	}
	
	@SuppressLint("Recycle")
	private void initialize(Context context, AttributeSet attrs) {
		TypedArray a = context.obtainStyledAttributes(attrs, new int[] { android.R.attr.maxLines });
		setMaxLines(a.getInt(0, Integer.MAX_VALUE));
		setEndPunctuationPattern(TEXTVIEW_ELLIPSIZING_DEFAULT_END_PUNCTUATION);
	}

	public void setEndPunctuationPattern(Pattern pattern) {
		this.endPunctuationPattern = pattern;
	}

	public void addEllipsizeListener(EllipsizeListener listener) {
		if (listener == null) {
			throw new NullPointerException();
		}
		ellipsizeListeners.add(listener);
	}

	public void removeEllipsizeListener(EllipsizeListener listener) {
		ellipsizeListeners.remove(listener);
	}

	public boolean isEllipsized() {
		return isEllipsized;
	}

	@Override
	public void setMaxLines(int maxLines) {
		super.setMaxLines(maxLines);
		this.maxLines = maxLines;
		isStale = true;
	}

	public int getMaxLines() {
		return maxLines;
	}

	public boolean ellipsizingLastFullyVisibleLine() {
		return maxLines == Integer.MAX_VALUE;
	}

	@Override
	public void setLineSpacing(float add, float mult) {
		this.lineAdditionalVerticalPadding = add;
		this.lineSpacingMultiplier = mult;
		super.setLineSpacing(add, mult);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int before,
			int after) {
		super.onTextChanged(text, start, before, after);
		if (!programmaticChange && canBeEllipsized) {
			fullText = text.toString();
			isStale = true;
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (ellipsizingLastFullyVisibleLine() && canBeEllipsized) {
			isStale = true;
		}
	}

	public void setPadding(int left, int top, int right, int bottom) {
		super.setPadding(left, top, right, bottom);
		if (ellipsizingLastFullyVisibleLine() && canBeEllipsized) {
			isStale = true;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (isStale && canBeEllipsized) {
			resetText();
		}
		super.onDraw(canvas);
	}

	private void resetText() {
		String workingText = fullText;
		boolean ellipsized = false;
		Layout layout = createWorkingLayout(workingText);
		int linesCount = getLinesCount();
		if (layout.getLineCount() > linesCount) {
			workingText = fullText.substring(0, layout.getLineEnd(linesCount - 1)).trim();
			while (createWorkingLayout(workingText + TEXTVIEW_ELLIPSIZING_ELLIPSIS).getLineCount() > linesCount) {
				int lastSpace = workingText.lastIndexOf(' ');
				if (lastSpace == -1) {
					break;
				}
				workingText = workingText.substring(0, lastSpace);
			}
			workingText = endPunctuationPattern.matcher(workingText).replaceFirst("");
			workingText = workingText + TEXTVIEW_ELLIPSIZING_ELLIPSIS;
			ellipsized = true;
		}
		if (!workingText.equals(getText())) {
			programmaticChange = true;
			try {
				setText(workingText);
			} finally {
				programmaticChange = false;
			}
		}
		isStale = false;
		if (ellipsized != isEllipsized) {
			isEllipsized = ellipsized;
			for (EllipsizeListener listener : ellipsizeListeners) {
				listener.ellipsizeStateChanged(ellipsized);
			}
		}
	}

	/**
	 * Get how many lines of text we are allowed to display.
	 */
	private int getLinesCount() {
		if (ellipsizingLastFullyVisibleLine()) {
			int fullyVisibleLinesCount = getFullyVisibleLinesCount();
			if (fullyVisibleLinesCount == -1) {
				return 1;
			} else {
				return fullyVisibleLinesCount;
			}
		} else {
			return maxLines;
		}
	}

	/**
	 * Get how many lines of text we can display so their full height is visible.
	 */
	private int getFullyVisibleLinesCount() {
		Layout layout = createWorkingLayout("");
		int height = getHeight() - getPaddingTop() - getPaddingBottom();
		int lineHeight = layout.getLineBottom(0);
		return height / lineHeight;
	}

	private Layout createWorkingLayout(String workingText) {
		return new StaticLayout(workingText, getPaint(),
				getWidth() - getPaddingLeft() - getPaddingRight(),
				Alignment.ALIGN_NORMAL, lineSpacingMultiplier,
				lineAdditionalVerticalPadding, false /* includepad */);
	}

	@Override
	public void setEllipsize(TruncateAt where) {
		// Ellipsize settings are not respected
	}

	public boolean isCanBeEllipsized() {
		return canBeEllipsized;
	}

	public void setCanBeEllipsized(boolean canBeEllipsized) {
		this.canBeEllipsized = canBeEllipsized;
	}
}