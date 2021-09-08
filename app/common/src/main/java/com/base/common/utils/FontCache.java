package com.base.common.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * @author sugar
 */
public class FontCache {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    public static String XI = "fonts/HYLiLiangHeiJ.ttf";
    public static String XX = "fonts/HYYaKuHeiW.ttf";

    public static void init(Context context) {
        Typeface xiType = Typeface.createFromAsset(context.getAssets(), XI);
        fontCache.put(XI, xiType);
    }

    public static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontname, typeface);
        }

        return typeface;
    }
}