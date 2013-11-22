/*
 Copyright 2013 Neopixl

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this

file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under

the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 

ANY KIND, either express or implied. See the License for the specific language governing

permissions and limitations under the License.
 */
package com.neopixl.pixlui.components.edittext;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import com.android.export.AllCapsTransformationMethod;
import com.neopixl.pixlui.components.textview.FontFactory;
import com.neopixl.pixlui.intern.PixlUIContants;

/**
 * Provide more possibility with EditText and enable new methods on old api
 * 
 * @author Olivier Demolliens. @odemolliens Dev with Neopixl
 */
public class EditText extends android.widget.EditText {

	/**
	 * XML Attribute
	 */
	private static final String EDITTEXT_ATTRIBUTE_FONT_NAME = "typeface";
	private static final String EDITTEXT_ATTRIBUTE_COPY_AND_PASTE = "copyandpaste";
	private static final String EDITTEXT_ATTRIBUTE_CANCEL_CLIPBOARD_CONTENT = "clearclipboardcontent";
	private static final String EDITTEXT_OS_ATTRIBUTE_TEXT_ALL_CAPS = "textAllCaps";

	/**
	 * Provider Keyboard
	 */
	private static final String EDITTEXT_KEYBOARD_SENSE = "com.htc.android.htcime/.HTCIMEService";

	/**
	 * Listeners
	 */
	private EditTextBatchListener listenerBatch;
	private EditTextFocusListener listenerFocus;

	/**
	 * State
	 */
	private boolean mOldDeviceKeyboard;
	private boolean mOldDeviceTextAllCaps;

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {

		return new CustomInputConnection(
				super.onCreateInputConnection(outAttrs), true, this);
	}

	public EditText(Context context) {
		super(context);
		editTextVersion();
	}

	public EditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		editTextVersion();
		setCustomFont(context, attrs);
		setDisableCopyAndPaste(context, attrs);
		setCancelClipboard(context, attrs);
		if (isOldDeviceTextAllCaps()) {
			setAllCaps(context, attrs);
		}
	}

	public EditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		editTextVersion();
		setCustomFont(context, attrs);
		setDisableCopyAndPaste(context, attrs);
		setCancelClipboard(context, attrs);
		if (isOldDeviceTextAllCaps()) {
			setAllCaps(context, attrs);
		}
	}

	/**
	 * Define what version of code we need to use
	 */
	private void editTextVersion() {
		if (android.os.Build.VERSION.SDK_INT < 14) {
			setOldDeviceKeyboard(true);
			setOldDeviceTextAllCaps(true);
		} else {
			// Special fix for HTC Sense Keyboard
			if (isASenseKeyboard()) {
				setOldDeviceKeyboard(true);
			} else {
				setOldDeviceKeyboard(false);
			}

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
				PixlUIContants.SCHEMA_URL, EDITTEXT_ATTRIBUTE_FONT_NAME);

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
	private void setDisableCopyAndPaste(Context ctx, AttributeSet attrs) {
		boolean disableCopyAndPaste = attrs.getAttributeBooleanValue(
				PixlUIContants.SCHEMA_URL, EDITTEXT_ATTRIBUTE_COPY_AND_PASTE,
				true);

		if (!disableCopyAndPaste && !isInEditMode()) {
			disableCopyAndPaste();
		}
	}

	/**
	 * XML methods
	 * 
	 * @param ctx
	 * @param attrs
	 */
	private void setCancelClipboard(Context ctx, AttributeSet attrs) {
		boolean cancelClipboard = attrs.getAttributeBooleanValue(
				PixlUIContants.SCHEMA_URL,
				EDITTEXT_ATTRIBUTE_CANCEL_CLIPBOARD_CONTENT, false);

		if (cancelClipboard && !isInEditMode()) {
			cancelClipBoardContent();
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
						EDITTEXT_OS_ATTRIBUTE_TEXT_ALL_CAPS)) {
					allCaps = attrs.getAttributeBooleanValue(i, false);
					break;
				}
			}

			if (allCaps && !isInEditMode()) {
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

		//FIXME: if user input new char, it's generate a crash on Paint methods

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
	 * Use this method to set a custom font in your code (/assets/fonts/) a
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

	/**
	 * Disable cut/copy/paste
	 */
	@SuppressLint("NewApi")
	public void disableCopyAndPaste() {
		if (android.os.Build.VERSION.SDK_INT < 11) {
			this.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
				public void onCreateContextMenu(ContextMenu menu, View v,
						ContextMenuInfo menuInfo) {
					menu.clear();
				}
			});
		} else {
			this.setCustomSelectionActionModeCallback(new Callback() {

				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}

				public void onDestroyActionMode(ActionMode mode) {
				}

				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					return false;
				}

				public boolean onActionItemClicked(ActionMode mode,
						MenuItem item) {
					return false;
				}
			});
		}
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	/*
	 * Cancel clipboard content
	 */
	public void cancelClipBoardContent() {

		if (android.os.Build.VERSION.SDK_INT < 11) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getContext()
					.getSystemService(Context.CLIPBOARD_SERVICE);
			if (clipboard != null) {
				clipboard.setText("");
			}
		} else {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getContext()
					.getSystemService(Context.CLIPBOARD_SERVICE);
			if (clipboard != null && clipboard.getPrimaryClip() != null
					&& clipboard.getPrimaryClip().getItemCount() > 0) {
				android.content.ClipData clip = android.content.ClipData
						.newPlainText("", "");
				clipboard.setPrimaryClip(clip);
			}
		}
	}

	/**
	 * Used to intercept the focus
	 */
	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
		EditTextFocusListener listener = getFocusListener();
		if (listener != null) {
			if (focused) {
				listener.requestFocus(this);
			} else {
				listener.loseFocus(this);
			}
		}

		super.onFocusChanged(focused, direction, previouslyFocusedRect);
	}

	/**
	 * Force show keyboard
	 */
	public void showKeyboard() {
		InputMethodManager mgr = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.showSoftInput(this, InputMethodManager.SHOW_FORCED);
		this.requestFocus();
		// Trick used to create a fake touch event on the editText
		MotionEvent event = MotionEvent.obtain(0, SystemClock.uptimeMillis(),
				MotionEvent.ACTION_UP, this.getMeasuredWidth(), 0, 0);
		this.onTouchEvent(event);
		event.recycle();
	}

	/**
	 * Force hide keyboard
	 */
	public void hideKeyboard() {
		InputMethodManager mgr = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
		this.clearFocus();
	}

	/**
	 * Get keyboard name (usefull to know if user used a custom keyboard, like
	 * HackerKeyboard or SenseKeyboard or...)
	 * 
	 * @return keyboard name + package
	 * 
	 */
	private String getKeyboardName() {
		return Settings.Secure.getString(getContext().getContentResolver(),
				Settings.Secure.DEFAULT_INPUT_METHOD);
	}

	/**
	 * Return if cellphone use a sense keyboard (HTC)
	 * 
	 * @return
	 */
	private boolean isASenseKeyboard() {
		if (getKeyboardName().equals(EDITTEXT_KEYBOARD_SENSE)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Test if user use a custom keyboard
	 * 
	 * @return
	 */
	public boolean isUsingCustomInputMethod() {
		InputMethodManager imm = (InputMethodManager) getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		List<InputMethodInfo> mInputMethodProperties = imm
				.getEnabledInputMethodList();
		final int N = mInputMethodProperties.size();
		for (int i = 0; i < N; i++) {
			InputMethodInfo imi = mInputMethodProperties.get(i);
			if (imi.getId().equals(
					Settings.Secure.getString(
							getContext().getContentResolver(),
							Settings.Secure.DEFAULT_INPUT_METHOD))) {
				if ((imi.getServiceInfo().applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					return true;
				}
			}
		}
		return false;
	}

	private class CustomInputConnection extends InputConnectionWrapper {

		private int mLastLength;
		private EditText mEdittext;
		private KeyEvent mKeyEvent;

		public CustomInputConnection(InputConnection target, boolean mutable,
				EditText editText) {
			super(target, mutable);
			setEdittext(editText);
		}

		@Override
		public boolean beginBatchEdit() {
			mLastLength = length();
			mKeyEvent = null;
			return super.beginBatchEdit();
		}

		@Override
		public boolean sendKeyEvent(KeyEvent event) {
			mKeyEvent = event;

			if (getEdittext().isOldDeviceKeyboard()) {
				if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
					String text = getEdittext().getText().toString();
					EditTextBatchListener listener = getEdittext()
							.getBatchListener();

					if (listener != null) {
						if (text.length() == 0
								&& event.getAction() == KeyEvent.ACTION_UP) {
							listener.deleteKeyboardButton(getEdittext(), true);
						} else {
							listener.deleteKeyboardButton(getEdittext(), false);
						}
					}
				} else {
					if (event.getAction() == KeyEvent.ACTION_UP) {
						listenerBatch.addNewChar(getEdittext());
					}
				}
			}

			return super.sendKeyEvent(event);
		}

		@Override
		public boolean endBatchEdit() {
			final int newLength = length();

			EditTextBatchListener listener = getEdittext().getBatchListener();

			if (listener != null && !getEdittext().isOldDeviceKeyboard()) {
				if (newLength <= mLastLength) {
					if (mLastLength - newLength == 1) {

						if (mKeyEvent == null) {
							listener.deleteKeyboardButton(getEdittext(), false);
						} else {
							char unicodeChar = (char) mKeyEvent
									.getUnicodeChar();
							String text = getEdittext().getText().toString();
							text = text + unicodeChar;
							getEdittext().setText(text);
							listener.addNewChar(getEdittext());
						}

					} else if (mLastLength == 0 && newLength == 0) {

						if (mKeyEvent == null) {
							listener.deleteKeyboardButton(getEdittext(), true);
						} else {
							char unicodeChar = (char) mKeyEvent
									.getUnicodeChar();
							String text = getEdittext().getText().toString();
							text = text + unicodeChar;
							getEdittext().setText(text);
							listener.addNewChar(getEdittext());
						}

					} else {
						if (mKeyEvent != null) {
							char unicodeChar = (char) mKeyEvent
									.getUnicodeChar();
							String text = getEdittext().getText().toString();
							text = text + unicodeChar;
							getEdittext().setText(text);
							listener.addNewChar(getEdittext());
						}
					}
				} else {
					listener.addNewChar(getEdittext());
				}
			}
			return super.endBatchEdit();
		}

		public EditText getEdittext() {
			return mEdittext;
		}

		public void setEdittext(EditText mEdittext) {
			this.mEdittext = mEdittext;
		}
	}

	public boolean isOldDeviceTextAllCaps() {
		return mOldDeviceTextAllCaps;
	}

	public void setOldDeviceTextAllCaps(boolean mOldDeviceTextAllCaps) {
		this.mOldDeviceTextAllCaps = mOldDeviceTextAllCaps;
	}

	public boolean isOldDeviceKeyboard() {
		return mOldDeviceKeyboard;
	}

	public void setOldDeviceKeyboard(boolean mOldDevice) {
		this.mOldDeviceKeyboard = mOldDevice;
	}

	private EditTextBatchListener getBatchListener() {
		return listenerBatch;
	}

	public void setBatchListener(EditTextBatchListener listener) {
		this.listenerBatch = listener;
	}

	private EditTextFocusListener getFocusListener() {
		return listenerFocus;
	}

	public void setFocusListener(EditTextFocusListener listenerFocus) {
		this.listenerFocus = listenerFocus;
	}
}