package com.neopixl.pixlui.components.edittext;

/**
 * Usefull if you need to track user input (especially delete button)
 * @author odemolliens
 *
 */
public interface EditTextBatchListener
{
	public void addNewChar(EditText edittext);
	public void deleteKeyboardButton(EditText edittext, boolean emptyText);
}
