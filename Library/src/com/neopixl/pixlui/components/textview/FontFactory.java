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

import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

/**
 * Manage font
 * @author Olivier Demolliens. @odemolliens
 * Dev with Neopixl
 */
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