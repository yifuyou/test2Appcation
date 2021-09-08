package com.base.common.model.http.jackSon;


import com.base.common.BuildConfig;
import com.base.common.utils.LogUtil;

import okhttp3.logging.HttpLoggingInterceptor;

public class HttpLog implements HttpLoggingInterceptor.Logger {

    private int size_log = 1024;//打印日志的长度

    @Override
    public void log(String message) {
        if (BuildConfig.DEBUG && (message.startsWith("{\"") || message.startsWith("[{\""))) {


            String ss = message.replaceAll(",\"", ",\n\"");

            if (ss.length() > size_log) {
                int index_last = 0;
                for (; ; ) {
                    int index = ss.indexOf("\n", index_last + size_log);
                    //如果已到尾部
                    if (index == -1) {
                        index = ss.length();
                    }
                    LogUtil.d("Http", " \n" + ss.substring(index_last, index));
                    if (index == ss.length()) break;
                    index_last = index;
                }
            } else {
                LogUtil.d("Http", " \n" + ss);
            }

        } else {
            LogUtil.d("Http", message);
        }
    }
}
