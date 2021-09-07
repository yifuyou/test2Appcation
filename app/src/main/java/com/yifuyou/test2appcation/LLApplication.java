package com.yifuyou.test2appcation;

import android.app.Application;

import com.yifuyou.common_http.RequestAPI;

public class LLApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RequestAPI.init();

    }

}
