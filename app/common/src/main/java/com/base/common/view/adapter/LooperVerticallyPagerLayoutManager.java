package com.base.common.view.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.base.common.utils.UIUtils;

public class LooperVerticallyPagerLayoutManager extends LinearLayoutManager {

    private static final String TAG = "LooperVerticallyPagerLayoutManager";
    private boolean looperEnable = true;

    private LooperPagerSnapHelper looperPagerSnapHelper;
    private RecyclerView recyclerView;

    public LooperVerticallyPagerLayoutManager(Context context) {
        this(context, RecyclerView.VERTICAL, false);
    }

    public LooperVerticallyPagerLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        init();
    }

    public LooperVerticallyPagerLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setOrientation(RecyclerView.VERTICAL);
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
//            int w = itemView.getWidth();
            int h = itemView.getHeight();
            if (recyclerView != null) {
                recyclerView.smoothScrollBy(0, h);
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
        return false;
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }


    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() <= 0) {
            return;
        }
        //preLayout主要支持动画，直接跳过
        if (state.isPreLayout()) {
            return;
        }
        //将视图分离放入scrap缓存中，以准备重新对view进行排版
        detachAndScrapAttachedViews(recycler);

        int autualHight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            //初始化，将在屏幕内的view填充
            View itemView = recycler.getViewForPosition(i);
            addView(itemView);
            //测量itemView的宽高
            measureChildWithMargins(itemView, 0, 0);
            int width = getDecoratedMeasuredWidth(itemView);
            int height = getDecoratedMeasuredHeight(itemView);

            if (i == 0) {
                //第一个局中
                int h = getHeight();
                autualHight = (h - height) / 2;
            }


            //根据itemView的宽高进行布局
            layoutDecorated(itemView, 0, autualHight, width, autualHight + height);

            autualHight += height;
            //如果当前布局过的itemView的宽度总和大于RecyclerView的宽，则不再进行布局
            if (autualHight > getHeight()) {
                break;
            }
        }


    }


    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (getItemCount() == 1) return 0;
        //1.上下滑动的时候，填充子view
        int travl = fill(dy, recycler, state);
        if (travl == 0) {
            return 0;
        }

        //2.滚动
        offsetChildrenVertical(travl * -1);

        //3.回收已经离开界面的
        recyclerHideView(dy, recycler, state);
        return travl;
    }

    /**
     * 上下滑动的时候，填充
     */
    private int fill(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (dy > 0) {
            //标注1.向上滚动
            View lastView = getChildAt(getChildCount() - 1);
            if (lastView == null) {
                return 0;
            }
            int lastPos = getPosition(lastView);
            //标注2.可见的最后一个itemView完全滑进来了，需要补充新的
            if (lastView.getRight() <= getWidth()) {
                View scrap = null;
                //标注3.判断可见的最后一个itemView的索引，
                // 如果是最后一个，则将下一个itemView设置为第一个，否则设置为当前索引的下一个
                if (lastPos == getItemCount() - 1) {
                    if (looperEnable) {
                        scrap = recycler.getViewForPosition(0);
                    } else {
                        dy = 0;
                    }
                } else {
                    scrap = recycler.getViewForPosition(lastPos + 1);
                }
                if (scrap == null) {
                    return dy;
                }
                //标注4.将新的itemViewadd进来并对其测量和布局
                addView(scrap);
                measureChildWithMargins(scrap, 0, 0);
                int width = getDecoratedMeasuredWidth(scrap);
                int height = getDecoratedMeasuredHeight(scrap);
                layoutDecorated(scrap, 0, lastView.getBottom(), width, lastView.getBottom() + height);
                return dy;
            }
        } else {
            //向下滚动
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
                        dy = 0;
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

                layoutDecorated(scrap, 0, firstView.getTop() - height, width, firstView.getTop());
            }
        }

        return dy;
    }

    /**
     * 回收界面不可见的view
     */
    private void recyclerHideView(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view == null) {
                continue;
            }
            if (dy > 0) {
                //向上滚动，移除一个下边不在内容里的view
                if (view.getBottom() < 0) {
                    removeAndRecycleView(view, recycler);
//                    Log.d(TAG, "循环: 移除 一个view  childCount=" + getChildCount());
                }
            } else {
                //向下滚动，移除一个顶部边不在内容里的view
                if (view.getTop() > getHeight()) {
                    removeAndRecycleView(view, recycler);
//                    Log.d(TAG, "循环: 移除 一个view  childCount=" + getChildCount());
                }
            }
        }

    }
}
