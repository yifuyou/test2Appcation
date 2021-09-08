package com.base.common.view.adapter.splite_line;

import android.graphics.Rect;
import android.view.View;

import com.base.common.view.adapter.MyLinearLayoutManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 在上下左右分别添加  space的宽度
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpacesItemDecoration(int space) {
        super();
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.right = space;
        outRect.bottom = space;

        if (isHorizontal(parent)) {
            outRect.left = space;
            outRect.top = space;
        } else {
            outRect.top = space;
            outRect.left = space;
        }

    }




    /**
     * 是否横向布局,由layoutManager决定
     *
     * @param parent
     * @return
     */
    protected boolean isHorizontal(RecyclerView parent) {
        boolean isHorizontal = false;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        //获取layoutManager的布局方向
        if (layoutManager instanceof LinearLayoutManager) {
            isHorizontal = ((LinearLayoutManager) layoutManager).getOrientation() == LinearLayoutManager.HORIZONTAL;
        }
        return isHorizontal;
    }



}
