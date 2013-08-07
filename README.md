![Logo](https://raw.github.com/neopixl/PixlUI/master/Sample/res/drawable-xhdpi/small.png ) PixlUI
======


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
        pixlui:typeface="GearedSlab.ttf" />
</RelativeLayout>
```


Copyright
==========


  	Copyright 2013 Neopixl

	Licensed under the Apache License, Version 2.0 (the "License"); you may not use this
	
	file except in compliance with the License. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software distributed under
	
	the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 
	
	ANY KIND, either express or implied. See the License for the specific language governing
	
	permissions and limitations under the License.
