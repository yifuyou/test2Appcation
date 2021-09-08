package com.base.common.view.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class MyGridLayoutManager extends GridLayoutManager {


    public MyGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public MyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            //try catch一下

            if (!isFullHeight()) {
                super.onLayoutChildren(recycler, state);
                return;
            }


            if (getItemCount() <= 0) {
                return;
            }
            //preLayout主要支持动画，直接跳过
            if (state.isPreLayout()) {
                return;
            }
            //将视图分离放入scrap缓存中，以准备重新对view进行排版
            detachAndScrapAttachedViews(recycler);

            int left = getPaddingLeft();
            int top = getPaddingTop();
            int column = 0, row = 0;//当前是第几行，第几列
            int spanCount = getSpanCount();
            int count = getItemCount();
            for (int i = 0; i < getItemCount(); i++) {
                //初始化，将在屏幕内的view填充
                View itemView = recycler.getViewForPosition(i);
                addView(itemView);
                //测量itemView的宽高
                measureChildWithMargins(itemView, 0, 0);
                int width = getDecoratedMeasuredWidth(itemView);
                int height = getDecoratedMeasuredHeight(itemView);

                //根据itemView的宽高进行布局
                layoutDecorated(itemView, left, top, left + width, top + height);

                //i项的跨度
                int spanSize = getSpanSizeLookup().getSpanSize(i);
                column += spanSize;
                if (column < spanCount) {
                    left = left + width;
                } else {
                    row += 1;//标记到下一行
                    column = 0;
                    left = getPaddingLeft();
                    top += height;
                }

            }

        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }


    public boolean isFullHeight() {
        return false;
    }
}
