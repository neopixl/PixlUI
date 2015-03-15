package com.neopixl.pixlui.intern;

import android.content.Context;

public interface FontStyleView {
    public boolean setCustomFont(Context ctx, String font);
    public void setPaintFlags(int flags);
    public int getPaintFlags();
}
