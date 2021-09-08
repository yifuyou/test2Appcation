package com.base.common.view.sidebar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class SideBarSortUtils {

    private RecyclerView recyclerView;
    private SideBarLayout sidebarView;
    private int mScrollState = -1;

    public SideBarSortUtils(RecyclerView recyclerView, SideBarLayout sidebarView) {
        this.recyclerView = recyclerView;
        this.sidebarView = sidebarView;
        connectData();
    }

    /**
     * 获取侧边栏字母，在recyclerView  中的位置
     *
     * @param word
     * @return
     */
    public abstract int getWordPosition(String word);

    /**
     * 获取recyclerView  position位置的字母
     *
     * @param position
     * @return
     */
    public abstract String getWordByPosition(int position);

    private void connectData() {
        //侧边栏滑动 --> item
        sidebarView.setSideBarLayout(new SideBarLayout.OnSideBarLayoutListener() {
            @Override
            public void onSideBarScrollUpdateItem(String word) {
                //循环判断点击的拼音导航栏和集合中姓名的首字母,如果相同recyclerView就跳转指定位置
                int pos = getWordPosition(word);
                if (pos > -1) {
                    recyclerView.smoothScrollToPosition(pos);
                }
            }
        });


        //item滑动 --> 侧边栏
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int scrollState) {
                super.onScrollStateChanged(recyclerView, scrollState);
                mScrollState = scrollState;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mScrollState != -1) {
                    //第一个可见的位置
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    //判断是当前layoutManager是否为LinearLayoutManager
                    // 只有LinearLayoutManager才有查找第一个和最后一个可见view位置的方法
                    int firstItemPosition = 0;
                    if (layoutManager instanceof LinearLayoutManager) {
                        LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                        //获取第一个可见view的位置
                        firstItemPosition = linearManager.findFirstVisibleItemPosition();
                    }

                    sidebarView.OnItemScrollUpdateText(getWordByPosition(firstItemPosition));
                    if (mScrollState == RecyclerView.SCROLL_STATE_IDLE) {
                        mScrollState = -1;
                    }
                }
            }
        });


    }


}
