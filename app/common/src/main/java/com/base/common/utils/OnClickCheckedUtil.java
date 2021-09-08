package com.base.common.utils;


import android.view.View;

public abstract class OnClickCheckedUtil implements View.OnClickListener {

    public static long mLastClick = 0L;

    /**
     * 点击检测, 防止抖动，多次执行 阈值 threShold  ms
     */
    public static boolean onClickChecked(int threShold) {
        long now = System.currentTimeMillis();
        boolean b = now - mLastClick > threShold;
        if (b) mLastClick = now;
        return b;
    }


    public abstract void onClicked(View view);

    @Override
    public void onClick(View v) {
        if (!onClickChecked(300)) {
            return;
        }
        onClicked(v);
    }


}
