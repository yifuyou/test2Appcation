package com.base.common.utils;

import android.util.Log;

import com.base.common.BuildConfig;


//在代码中要打印log,就直接DebugUtil.debug(....).然后如果发布的时候,就直接把这个类的DEBUG 改成false,这样所有的log就不会再打印在控制台.
public class LogUtil {

    public static final String TAG = BuildConfig.LIBRARY_PACKAGE_NAME;
//  public static final String TAG = BuildConfig.APPLICATION_ID;

    public static final boolean DEBUG = BuildConfig.DEBUG;


    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(TAG + "   " + tag, msg);
        }
    }

    public static void d(String msg) {
        if (DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String tag, String error) {
        if (DEBUG) {
            Log.e(TAG + "   " + tag, error);
        }
    }

    public static void e(String error) {
        if (DEBUG) {
            Log.e(TAG, error);
        }
    }


}