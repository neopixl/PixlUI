package com.neopixl.pixlui.components.edittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.neopixl.pixlui.components.textview.FontFactory;
import com.neopixl.pixlui.intern.PixlUIContants;

public class EditText extends android.widget.EditText {

	private static String EDITTEXT_ATTRIBUTE_FONT_NAME = "typeface";
	private static String EDITTEXT_ATTRIBUTE_COPY_AND_PASTE = "copyandpaste";
	private static String EDITTEXT_ATTRIBUTE_CANCEL_CLIPBOARD_CONTENT = "clearclipboardcontent";

	public EditText(Context context) {
		super(context);
	}

	public EditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
		setDisableCopyAndPaste(context,attrs);
		setCancelClipboard(context,attrs);
	}

	public EditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
		setDisableCopyAndPaste(context,attrs);
		setCancelClipboard(context,attrs);
	}

	private void setCustomFont(Context ctx, AttributeSet attrs) {
		String typefaceName = attrs.getAttributeValue(
				PixlUIContants.SCHEMA_URL, EDITTEXT_ATTRIBUTE_FONT_NAME);

		if (typefaceName != null) {
			setPaintFlags(this.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG
					| Paint.LINEAR_TEXT_FLAG);
			setCustomFont(ctx, typefaceName);
		}
	}

	private void setDisableCopyAndPaste(Context ctx, AttributeSet attrs) {
		boolean disableCopyAndPaste = attrs.getAttributeBooleanValue(
				PixlUIContants.SCHEMA_URL, EDITTEXT_ATTRIBUTE_COPY_AND_PASTE, true);

		if(!disableCopyAndPaste && !isInEditMode()){
			disableCopyAndPaste();
		}
	}

	private void setCancelClipboard(Context ctx, AttributeSet attrs) {
		boolean cancelClipboard = attrs.getAttributeBooleanValue(
				PixlUIContants.SCHEMA_URL, EDITTEXT_ATTRIBUTE_CANCEL_CLIPBOARD_CONTENT, false);

		if(cancelClipboard && !isInEditMode()){
			cancelClipBoardContent();
		}
	}

	/**
	 * Use this method to set a custom font in your code (/assets/fonts/)
	 *  a
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
		}else{
			this.setCustomSelectionActionModeCallback(new Callback() {

				public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
					return false;
				}

				public void onDestroyActionMode(ActionMode mode) {
				}

				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					return false;
				}

				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
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
	 * Force show keyboard
	 */
	public void showKeyboard() {
		InputMethodManager mgr = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.showSoftInput(this, InputMethodManager.SHOW_FORCED);
		this.requestFocus();
		//Trick used to create a fake touch event on the editText
		MotionEvent event = MotionEvent.obtain(0, SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, this.getMeasuredWidth(), 0, 0);
		this.onTouchEvent(event);
		event.recycle();
	}
	
	/**
	 * Force hide keyboard
	 */
	public void hideKeyboard() {
		InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
		mgr.hideSoftInputFromWindow(this.getWindowToken(), 0);
		this.clearFocus();
	}
}