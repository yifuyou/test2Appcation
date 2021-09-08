package com.base.common.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

import com.gyf.immersionbar.OnKeyboardListener;

public class SoftKeyBoardUtils {

    public static void setOnKeyboardListener(View decorView, int statusBar, int navigationBarHeight, OnKeyboardListener onKeyboardListener) {

        final boolean[] isVisiableForLast = {false};

        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                //计算出可见屏幕的高度,不包含statusBar  和   navigationBar  的高度
                int displayHight = rect.bottom - rect.top;
                //获得屏幕内容可显示的整体的高度
                int hight = decorView.getHeight() - statusBar - navigationBarHeight;
                //获得键盘高度
                int keyboardHeight = hight - displayHight;

                boolean visible = (double) displayHight / hight < 0.8;
                if (visible != isVisiableForLast[0]) {
                    onKeyboardListener.onKeyboardChange(visible, keyboardHeight);
                }
                isVisiableForLast[0] = visible;
            }
        });
    }


}
