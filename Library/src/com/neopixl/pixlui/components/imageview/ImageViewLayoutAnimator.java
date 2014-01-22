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
import android.util.AttributeSet;
import android.view.animation.Animation;

public class ImageViewLayoutAnimator extends ImageView
{
	private Animation inAnimation;
	private Animation outAnimation;

	
	@SuppressLint("NewApi")
	public ImageViewLayoutAnimator(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public ImageViewLayoutAnimator(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setInAnimation(Animation inAnimation)
	{
		this.inAnimation = inAnimation;
	}

	public void setOutAnimation(Animation outAnimation)
	{
		this.outAnimation = outAnimation;
	}

	@Override
	public void setVisibility(int visibility)
	{
		if (getVisibility() != visibility)
		{
			if (visibility == VISIBLE)
			{
				if (inAnimation != null) {
					startAnimation(inAnimation);
				}
			}
			else if ((visibility == INVISIBLE) || (visibility == GONE))
			{
				if (outAnimation != null){
					startAnimation(outAnimation);
				}
			}
		}

		super.setVisibility(visibility);
	}
}
