package com.neopixl.pixlui.components.textview;

import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

public class FontFactory {
	private static FontFactory instance;
	private HashMap<String, Typeface> fontMap = new HashMap<String, Typeface>();
	private Context context;

	private FontFactory(Context context) {
		this.context = context;
	}

	public static FontFactory getInstance(Context context) {
		if(instance==null){
			return instance = new FontFactory(context);
		}else{
			return instance;
		}
	}

	public Typeface getFont(String font) {
		Typeface typeface = fontMap.get(font);
		if (typeface == null) {
			try {
				typeface = Typeface.createFromAsset(context.getResources().getAssets(), "fonts/" + font);
				fontMap.put(font, typeface);  
			} catch (Exception e) {
				Log.e("FontFactory", "Could not get typeface: "+e.getMessage() +" with name: "+font);
				return null;
			}

		}
		return typeface;
	}
}