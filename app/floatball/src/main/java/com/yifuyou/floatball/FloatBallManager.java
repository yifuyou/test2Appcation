package com.yifuyou.floatball;


import android.content.Context;

public class FloatBallManager {
    private static Context mContext;


    public static void init(Context context){
        mContext=context;
        checkPermission();
    }
    private static void checkPermission(){
        String[] permissions={"android.permission.FOREGROUND_SERVICE",
                "android.permission.MANAGE_EXTERNAL_STORAGE",
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE"
        };
        for (String per : permissions) {
            mContext.checkSelfPermission(per);
        }
    }


}
