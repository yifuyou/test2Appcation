package com.base.common.view.adapter.connector;

import android.view.View;

import androidx.databinding.ViewDataBinding;

import com.base.common.view.adapter.ada.BaseRVAdapter;

/**
 * 一般在ItemType中使用
 *
 * @param <T>
 */
public interface OnItemTypeClickListener<T, D extends ViewDataBinding> {

    int onClickEvent = 0;
    int onLongClickEvent = 1;

    /**
     * 长按事件是有返回值的，如果返回false，两个事件都会有响应，如果返回true则只响应长按事件
     * onclickType 0 点击事件，1 长按事件
     *
     * @param view        //点击的view
     * @param onclickType //点击事件类型
     * @param position    当前的位置
     * @param bean        当前的数据
     */
    void onItemClick(View view, BaseRVAdapter baseRVAdapter, BaseViewHolder viewHolder, D binding, int onclickType, int position, T bean);
}
