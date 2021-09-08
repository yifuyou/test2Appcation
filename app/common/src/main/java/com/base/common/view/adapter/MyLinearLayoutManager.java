package com.base.common.view.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.MeasureSpec.AT_MOST;

public class MyLinearLayoutManager extends LinearLayoutManager {


    private Boolean canScroll = null;//是否可以滑动

    public MyLinearLayoutManager(Context context) {
        super(context);
    }

    public MyLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);

    }

    public MyLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean canScrollHorizontally() {
        if (canScroll != null) {
            return canScroll;
        }
        return super.canScrollHorizontally();
    }


    @Override
    public boolean canScrollVertically() {
        if (canScroll != null) {
            return canScroll;
        }
        return super.canScrollVertically();
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            //try catch一下
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }


    public Boolean getCanScroll() {
        return canScroll;
    }

    public void setCanScroll(Boolean canScroll) {
        this.canScroll = canScroll;
    }
}
