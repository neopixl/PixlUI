package com.neopixl.pixlui.intern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.neopixl.pixlui.R;

import java.util.HashMap;
import java.util.Map;

public class PixlUIfaceManager {

    private static final String TAG = PixlUIfaceManager.class.getSimpleName();
    private static PixlUIfaceManager INSTANCE;

    /**
     * The Typeface cache, keyed by their asset file name.
     */
    private static final Map<String, Typeface> mCache = new HashMap<String, Typeface>();

    /**
     * Current context
     */
    private final Context mContext;


    public static boolean isLayoutEditorInterfaceCompatible(View target) {
        //Since API M, we can previsualize font in Layout Editor !
        if (target.isInEditMode()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * Returns the singleton instance of the PixlUIfaceManager.
     *
     * @return The PixlUIfaceManager
     */
    public static PixlUIfaceManager getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new PixlUIfaceManager(context);
        }
        return INSTANCE;
    }

    /**
     * Initializes the typeface manager with the given xml font file.
     *
     * @param context A context with which the xml font file and the assets can
     *                be accessed.
     * @return The number of unique font files in the xml file that exist. The
     * font files are not yet opened or verified, it only means that
     * they exist.
     */
    private PixlUIfaceManager(Context context) {
        mContext = context;
    }

    /**
     * Returns the typeface identified by the given font name and style. The
     * font must be a name defined in any nameset in the font xml file. The
     * style must be one of the constants defined by {@link Typeface}.
     *
     * @param name
     * @return
     */
    public Typeface getTypeface(String name) {
        return getTypeface(name, Typeface.NORMAL);
    }

    /**
     * Returns the typeface identified by the given font name and style. The
     * font must be a name defined in any nameset in the font xml file. The
     * style must be one of the constants defined by {@link Typeface}.
     *
     * @param name
     * @param style
     * @return
     */
    public Typeface getTypeface(String name, int style) {
        synchronized (mCache) {
            if (!mCache.containsKey(name)) {
                Typeface t = Typeface.createFromAsset(mContext.getAssets(),
                        String.format("fonts/%s", name));
                mCache.put(name, t);
                return t;
            } else {
                return mCache.get(name);
            }
        }
    }

    /**
     * Convenience method to set the typeface of the target view to the font
     * identified by fontName. The text style that will be used is {@code
     * Typeface#NORMAL}. Returns false if the font wasn't found or couldn't be
     * loaded, returns true otherwise.
     *
     * @param target   A TextView in which the font must be used.
     * @param fontName The name of the font. Must match a name defined in a name
     *                 tag in the xml font file.
     * @return {@code true} if the font could was set in the target, {@code
     * false} otherwise.
     */
    public boolean setTypeface(TextView target, String fontName) {
        return setTypeface(target, fontName, Typeface.NORMAL);
    }

    /**
     * Set the text style of the given TextView. The text style must be one of the constants defined
     * in {@link Typeface}. If the target had a custom font applied (as specified in the {@link
     * ExtraFontData}), that font will be used to find the appropriate text style.
     *
     * @param target    A TextView in which the text style must be set.
     * @param textStyle A text style: {@link Typeface#NORMAL}, {@link Typeface#BOLD}, {@link
     *                  Typeface#ITALIC} or {@link Typeface#BOLD_ITALIC}.
     * @return {@code true} if the text style was set in the target, {@code false} otherwise.
     */
    public boolean setTextStyle(TextView target, int textStyle) {
        ExtraFontData data = getFontData(target);
        data.style = textStyle;
        if (data.font == null) {
            // Default Android font
            target.setTypeface(target.getTypeface(), textStyle);
            return true;
        } else {
            // Custom font
            return setTypeface(target, data.font, textStyle);
        }
    }

    /**
     * Convenience method to set the typeface of the target view to the font
     * identified by fontName using the textStyle. The textStyle must be one of
     * the constants defined by {@code Typeface}. Returns false if the font
     * wasn't found or couldn't be loaded, returns true otherwise.
     *
     * @param target    A TextView in which the font must be used.
     * @param fontName  The name of the font. Must match a name defined in a name
     *                  tag in the xml font file.
     * @param textStyle A text style: normal, bold, italic or bold_italic.
     * @return {@code true} if the font could was set in the target, {@code
     * false} otherwise.
     */
    public boolean setTypeface(TextView target, String fontName, int textStyle) {
        Typeface tf = null;
        try {
            tf = getTypeface(fontName, textStyle);

            // If the font was retrieved successfully, store it in the data.
            ExtraFontData data = getFontData(target);
            data.font = fontName;
            data.style = textStyle;

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Could not get typeface " + fontName + " with style " + textStyle);
            return false;
        }

        target.setTypeface(tf);
        return true;
    }


    /**
     * Applies the font and all related custom properties found in the
     * attributes of the given AttributeSet or the default style to the given
     * target. Typically, the AttributeSet consists of the attributes contained
     * in the xml tag that defined the target. The target can be any TextView or
     * subclass thereof. The read properties will be stored in an {@link
     * ExtraFontData} instance stored as a tag with id {@link
     * R.id#_FontsExtraData} in the target view. If an instance was already set
     * as a tag in the target view, it will be reused. All encountered
     * properties are overridden. The properties in the data holder can be
     * changed later, but it will depend on the nature of the property whether
     * or not this change will take effect. Properties that are applied at
     * initialization will not be applied when changed and properties that are
     * applied during the render cycle will be applied when changed.
     *
     * @param target   A TextView, or any UI element that inherits from TextView.
     * @param attrs    The attributes from the xml tag that defined the target.
     * @param defStyle The style that is applied to the target element. This may
     *                 either be an attribute resource, whose value will be retrieved
     *                 from the current theme, or an explicit style resource.
     */
    public static void applyFont(TextView target, AttributeSet attrs, int defStyle, Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            if (target == null || !isLayoutEditorInterfaceCompatible(target))
                return;
        } else {

            if (target == null) {
                return;
            }
        }

        ExtraFontData data = getFontData(target);

        // First get the font attribute from the textAppearance:
        Theme theme = target.getContext().getTheme();
        // Get the text appearance that's currently in use
        TypedArray a = theme.obtainStyledAttributes(attrs,
                new int[]{android.R.attr.textAppearance}, defStyle, 0);
        int textAppearanceStyle = -1;
        try {
            textAppearanceStyle = a.getResourceId(0, -1);
        } finally {
            a.recycle();
        }
        // Get the font and style defined in the text appearance
        TypedArray appearance = null;
        if (textAppearanceStyle != -1)
            appearance = theme.obtainStyledAttributes(textAppearanceStyle,
                    R.styleable.Fonts);
        getAttributes(appearance, data);

        // Then get the font attribute from the FontTextView itself:
        a = theme.obtainStyledAttributes(attrs, R.styleable.Fonts, defStyle, 0);
        getAttributes(a, data);

        // Now we have the font, apply it
        if (data.font != null) {
            getInstance(context).setTypeface(target, data.font, data.style);
        }

    }

    /**
     * Fetches the font attributes from the given typed array and overrides all
     * properties in the given data holder that are present in the typed array.
     *
     * @param a    A TypedArray from which the attributes will be fetched. It will
     *             be recycled if not null.
     * @param data The data holder in which all read properties are stored.
     */
    private static void getAttributes(final TypedArray a, final ExtraFontData data) {
        if (a == null)
            return;
        try {
            // Iterate over all attributes in 'Android-style'
            // (similar to the implementation of the TextView constructor)
            int n = a.getIndexCount();
            for (int i = 0; i < n; i++) {
                int attr = a.getIndex(i);
                if (attr == R.styleable.Fonts_typeface) {
                    data.font = a.getString(attr);
                } else if (attr == R.styleable.Fonts_android_textStyle) {
                    data.style = a.getInt(attr, Typeface.NORMAL);
                } else if (attr == R.styleable.Fonts__BorderWidth) {
                    data.borderWidth = a.getDimensionPixelSize(attr, 0);
                } else if (attr == R.styleable.Fonts__BorderColor) {
                    data.borderColor = a.getColor(attr, Color.BLACK);
                }
            }
        } finally {
            a.recycle();
        }
    }

    public static void onDrawHelper(Canvas canvas, TextView target, DrawCallback drawCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            if (target.isInEditMode())
                return;
        }
        final ExtraFontData data = getFontData(target, false);
        if (data == null)
            return;

        if (data.borderWidth > 0) {
            final Paint paint = target.getPaint();

            // setup stroke
            final Style oldStyle = paint.getStyle();
            final ColorStateList oldTextColors = target.getTextColors();
            final float oldStrokeWidth = paint.getStrokeWidth();

            target.setTextColor(data.borderColor);
            paint.setStyle(Style.STROKE);
            paint.setStrokeWidth(data.borderWidth);
            callDrawCallback(drawCallback, canvas);

            target.setTextColor(oldTextColors);
            paint.setStyle(oldStyle);
            paint.setStrokeWidth(oldStrokeWidth);
        }
    }

    /**
     * Calls the draw callback with the given canvas. Use this method instead of
     * calling it yourself, as lint is fooled by the method name 'onDraw' and
     * thinks we are intervening with the render cycle. With this method, we can
     * isolate the suppress lint annotation to the only warning we want to
     * suppress.
     *
     * @param drawCallback
     * @param canvas
     */
    @SuppressLint("WrongCall")
    private static void callDrawCallback(DrawCallback drawCallback, Canvas canvas) {
        drawCallback.onDraw(canvas);
    }

    /**
     * A data holder in which properties are stored that are not part of the
     * default text view attributes, but which are applicable to all custom Font
     * widgets. By storing this data holder in the corresponding view instance,
     * we can apply the properties at any time with a shared static method.
     *
     * @author Jelle Fresen <jelle@innovattic.com>
     */
    public static class ExtraFontData {
        public String font;
        public int style;
        public int borderWidth;
        public int borderColor;

        public ExtraFontData(TextView target) {
            // By default, the font is not changed
            font = null;
            // By default, we apply a regular typeface
            style = Typeface.NORMAL;
            // By default, we don't add a border around the text
            borderWidth = 0;
            // By default, *if* there is a border, it will be black
            borderColor = Color.BLACK;
            // Store the data in the TextView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
                target.setTag(R.id._FontsExtraData, this);
            }
        }
    }

    public static ExtraFontData getFontData(TextView target) {
        return getFontData(target, true);
    }

    public static ExtraFontData getFontData(TextView target, boolean createIfMissing) {
        ExtraFontData data = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.DONUT) {
            data = (ExtraFontData) target.getTag(R.id._FontsExtraData);
        }
        if (data == null && createIfMissing) {
            data = new ExtraFontData(target);
        }
        return data;
    }

    public interface DrawCallback {
        public void onDraw(Canvas canvas);
    }

}
