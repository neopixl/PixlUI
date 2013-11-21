package com.neopixl.pixlui.components.edittext;

/**
 * Usefull if you need to track edittext focus
 * @author odemolliens
 *
 */
public interface EditTextFocusListener
{
	public void requestFocus(EditText edittext);
	public void loseFocus(EditText edittext);
}