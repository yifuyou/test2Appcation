package com.yifuyou.common_http.util;

import android.util.Log;

import com.yifuyou.common_http.common.BuildConfig;

public class LogUtil {
    public static final String TAG = BuildConfig.LIBRARY_PACKAGE_NAME;
    public static final Boolean DEBUG = BuildConfig.DEBUG;

    public static void i(String tag,String msg){
        if(DEBUG)
        Log.i(TAG, tag+" i: "+ msg);
    }
    public static void d(String tag,String msg){
        if(DEBUG)
            Log.d(TAG, tag+" d: "+ msg);
    }
    public static void e(String tag,String msg,Exception e){
        if(DEBUG)
            Log.i(TAG, tag+" e: "+ msg,e);
    }
    public static void v(String tag,String msg){
        if(DEBUG)
            Log.v(TAG, tag+" v: "+ msg);
    }
    public static void w(String tag,String msg){
        if(DEBUG)
            Log.w(TAG, tag+" w: "+ msg);
    }

}
