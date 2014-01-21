![Logo](https://raw.github.com/neopixl/PixlUI/master/Sample/res/drawable-xhdpi/small.png ) PixlUI
======

Provide few methods for visual elements.


CheckBox:
- Custom font
- Text All caps (works on all API version)

Button:
- Custom font
- Text All caps (works on all API version)

EditText:
- Custom font
- Copy/Cut/Paste (enable/disable) (works on all API version)
- Cancel clipboard content (works on all API version)
- Text All caps (works on all API version) - (in progress)
- Focus listener
- Batch listener (replace TextWatcher, wich that you can intercept DEL touch on all API)

Image View:
- Alpha  (works on all API version)

RelativeLayout:
- Alpha  (works on all API version)


TextView:
- Contains a fix to do proper ellipsizing
- Custom font
- Text All caps (works on all API version)

Screenshot
==========
![Screen1](https://raw.github.com/neopixl/PixlUI/master/screenshot.png )


How use it ?
==========

1 . Import [pixlui.jar](https://github.com/neopixl/PixlUI/raw/master/Sample/libs/pixlui.jar "pixlui.jar") in your project.

2 . Add your custom fonts in /assets/fonts/

3 . Use it in XML:

```xml
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:pixlui="http://schemas.android.com/apk/com.neopixl.pixlui"
    xmlns:tools="http://schemas.android.com/tools" >

    <com.neopixl.pixlui.components.textview.TextView
        android:id="@+id/textView1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@string/hello_world"
        pixlui:copyandpaste="false"
        pixlui:clearclipboardcontent="true"
        pixlui:typeface="GearedSlab.ttf" />
</RelativeLayout>
```


Copyright
==========


  	Copyright 2013 Neopixl - Olivier Demolliens

	Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
	
	file except in compliance with the License. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software distributed under
	
	the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
	
	ANY KIND, either express or implied. See the License for the specific language governing
	
	permissions and limitations under the License.
