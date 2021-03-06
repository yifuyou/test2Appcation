package com.base.common.view.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.base.common.utils.UIUtils;

public class LooperHorizontalPagerLayoutManager extends LinearLayoutManager {

    private static final String TAG = "LooperHorizontalPagerLayoutManager";
    private boolean looperEnable = true;

    private LooperPagerSnapHelper looperPagerSnapHelper;
    private RecyclerView recyclerView;

    public LooperHorizontalPagerLayoutManager(Context context) {
        this(context, RecyclerView.HORIZONTAL, false);
    }

    public LooperHorizontalPagerLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        init();
    }

    public LooperHorizontalPagerLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(RecyclerView.HORIZONTAL);
        init();
    }


    public void setLooperEnable(boolean looperEnable) {
        this.looperEnable = looperEnable;
    }

    private void init() {
        looperPagerSnapHelper = new LooperPagerSnapHelper();
    }


    private Runnable runnable = new Runnable() {

        @Override
        public void run() {
            UIUtils.postDelayed(this, 3000);
            if (getItemCount() == 1) return;
            int position = findFirstCompletelyVisibleItemPosition();
            View itemView = findViewByPosition(position);
            if (itemView == null) return;
            int w = itemView.getWidth();

            if (recyclerView != null) {
                recyclerView.smoothScrollBy(w, 0);
            }

        }
    };


    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        recyclerView = view;
        if (looperPagerSnapHelper != null) {
            looperPagerSnapHelper.attachToRecyclerView(view);
            UIUtils.postDelayed(runnable, 3000);
        }
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0) {
            return;
        }
        //preLayout?????????????????????????????????
        if (state.isPreLayout()) {
            return;
        }
        //?????????????????????scrap??????????????????????????????view????????????
        detachAndScrapAttachedViews(recycler);

        int autualWidth = 0;
        for (int i = 0; i < getItemCount(); i++) {
            //??????????????????????????????view??????
            View itemView = recycler.getViewForPosition(i);
            addView(itemView);
            //??????itemView?????????
            measureChildWithMargins(itemView, 0, 0);
            int width = getDecoratedMeasuredWidth(itemView);
            int height = getDecoratedMeasuredHeight(itemView);

            if (i == 0) {
                //???????????????
                int w = getWidth();
                autualWidth = (w - width) / 2;
            }


            //??????itemView?????????????????????
            layoutDecorated(itemView, autualWidth, 0, autualWidth + width, height);

            autualWidth += width;
            //????????????????????????itemView?????????????????????RecyclerView??????????????????????????????
            if (autualWidth > getWidth()) {
                break;
            }
        }


    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 1) return 0;
        //1.?????????????????????????????????view
        int travl = fill(dx, recycler, state);
        if (travl == 0) {
            return 0;
        }

        //2.??????
        offsetChildrenHorizontal(travl * -1);

        //3.???????????????????????????
        recyclerHideView(dx, recycler, state);
        return travl;
    }

    /**
     * ??????????????????????????????
     */
    private int fill(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dx > 0) {
            //??????1.????????????
            View lastView = getChildAt(getChildCount() - 1);
            if (lastView == null) {
                return 0;
            }
            int lastPos = getPosition(lastView);
            //??????2.?????????????????????itemView???????????????????????????????????????
            if (lastView.getRight() <= getWidth()) {
                View scrap = null;
                //??????3.???????????????????????????itemView????????????
                // ???????????????????????????????????????itemView????????????????????????????????????????????????????????????
                if (lastPos == getItemCount() - 1) {
                    if (looperEnable) {
                        scrap = recycler.getViewForPosition(0);
                    } else {
                        dx = 0;
                    }
                } else {
                    scrap = recycler.getViewForPosition(lastPos + 1);
                }
                if (scrap == null) {
                    return dx;
                }
                //??????4.?????????itemViewadd??????????????????????????????
                addView(scrap);
                measureChildWithMargins(scrap, 0, 0);
                int width = getDecoratedMeasuredWidth(scrap);
                int height = getDecoratedMeasuredHeight(scrap);
                layoutDecorated(scrap, lastView.getRight(), 0, lastView.getRight() + width, height);
                return dx;
            }
        } else {
            //????????????
            View firstView = getChildAt(0);
            if (firstView == null) {
                return 0;
            }
            int firstPos = getPosition(firstView);

            if (firstView.getLeft() >= 0) {
                View scrap = null;
                if (firstPos == 0) {
                    if (looperEnable) {
                        scrap = recycler.getViewForPosition(getItemCount() - 1);
                    } else {
                        dx = 0;
                    }
                } else {
                    scrap = recycler.getViewForPosition(firstPos - 1);
                }
                if (scrap == null) {
                    return 0;
                }
                addView(scrap, 0);
                measureChildWithMargins(scrap, 0, 0);
                int width = getDecoratedMeasuredWidth(scrap);
                int height = getDecoratedMeasuredHeight(scrap);
                layoutDecorated(scrap, firstView.getLeft() - width, 0,
                        firstView.getLeft(), height);
            }
        }
        return dx;
    }

    /**
     * ????????????????????????view
     */
    private void recyclerHideView(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view == null) {
                continue;
            }
            if (dx > 0) {
                //???????????????????????????????????????????????????view
                if (view.getRight() < 0) {
                    removeAndRecycleView(view, recycler);
//                    Log.d(TAG, "??????: ?????? ??????view  childCount=" + getChildCount());
                }
            } else {
                //???????????????????????????????????????????????????view
                if (view.getLeft() > getWidth()) {
                    removeAndRecycleView(view, recycler);
//                    Log.d(TAG, "??????: ?????? ??????view  childCount=" + getChildCount());
                }
            }
        }

    }
}
