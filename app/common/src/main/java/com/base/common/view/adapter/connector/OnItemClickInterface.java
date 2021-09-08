package com.base.common.view.adapter.connector;

import android.view.View;

public interface OnItemClickInterface {
    int onClickEvent = 0;
    int onLongClickEvent = 1;

    /**
     * 长按事件是有返回值的，如果返回false，两个事件都会有响应，如果返回true则只响应长按事件
     * onclickType 0 点击事件，1 长按事件
     *
     * @param view        //点击的view
     * @param onclickType //点击事件类型
     * @param itemType    当前的类型
     * @param position    当前的位置
     * @param bean        当前的数据
     * @return true//表示已消费不在向后传递
     */
    boolean onItemClick(View view, int onclickType, int itemType, int position, Object bean);
}
