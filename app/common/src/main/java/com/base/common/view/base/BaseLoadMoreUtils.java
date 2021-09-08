package com.base.common.view.base;


import com.base.common.model.bean.KeyValue;
import com.base.common.utils.UIUtils;
import com.base.common.view.adapter.ada.BaseRVAdapter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

import androidx.annotation.NonNull;

/**
 * 预加载工具类
 */
public class BaseLoadMoreUtils implements OnRefreshLoadMoreListener {


    private OnGetDataListener onGetDataListener;
    private boolean enableLoadMore;//是否启用加载更多
    private SmartRefreshLayout smartRefreshLayout;
    private BaseRVAdapter baseRVAdapter;//加载数据的适配器

    private int pageNo = 0;//当前下拉刷新是第几页
    private int ps = 8;
    private boolean isPreload = false;//是否正在预加载数据
    private boolean isWaitLoading = false;//是否正在等待加载数据

    private boolean isAutoPreload = true;//是否自动预加载


    private KeyValue<Integer, List, String> list_preload = new KeyValue<>();//缓存预加载的数据


    public BaseLoadMoreUtils(OnGetDataListener onGetDataListener, SmartRefreshLayout smartRefreshLayout, boolean... enableLoadMore) {
        this.onGetDataListener = onGetDataListener;
        this.smartRefreshLayout = smartRefreshLayout;
        this.smartRefreshLayout.setRefreshHeader(new ClassicsHeader(smartRefreshLayout.getContext()));
        if (enableLoadMore.length == 0) {
            this.enableLoadMore = true;
        } else {
            this.enableLoadMore = enableLoadMore[0];
        }

    }

    public int getPs() {
        return ps;
    }

    public boolean isPreload() {
        return isPreload;
    }

    public KeyValue<Integer, List, String> getList_preload() {
        return list_preload;
    }

    public void setAutoPreload(boolean autoPreload) {
        isAutoPreload = autoPreload;
    }

    /**
     *
     * @param refreshLayout  的加载更多回调
     */
    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        if (enableLoadMore) {
            int pn = pageNo + 1;

            if (!isAutoPreload) {
                isWaitLoading = true;//正在等待加载数据
                onGetDataListener.onGetData(pn);
                return;
            }

            //如果本地有预加载数据  则无需等待，直接显示
            if (list_preload.getKey() != null && list_preload.getKey() == pn) {
                isWaitLoading = false;
                showDataList(pn, list_preload.getValue());
            }
            //如果本地没有预加载数据，但正在加载，则等待加载结束
            else if (isPreload) {
                isWaitLoading = true;//等待数据加载完成
            }
            //本地没有预加载数据，也没有开始加载，则执行加载
            else {
                isWaitLoading = true;//正在等待加载数据
                onGetDataListener.onGetData(pn);
            }

        }

    }

    /**
     * 设置下拉刷新和上拉加载数据
     */
    public void setDataListRefreshLayout(BaseRVAdapter baseRVAdapter, int pn, int ps, List list) {
        this.baseRVAdapter = baseRVAdapter;
        this.ps = ps;
        if (pn <= pageNo) isWaitLoading = true;
        if (!isAutoPreload) {
            showDataList(pn, list);
            return;
        }
        //判断是立马显示，还是缓存
        //如果是第一页数据，不管处在任何状态，都需要立即显示
        if (pn == 1) {
            //清空缓存
            list_preload.setKey(-1);
            list_preload.setValue(null);
            showDataList(pn, list);
        } else {
            //如果正在等待加载，立马显示
            if (isWaitLoading) {
                showDataList(pn, list);
            }
            //如果是预加载数据，则缓存
            else if (isPreload) {
                isPreload = false;
                list_preload.setKey(pn);
                list_preload.setValue(list);
            }
        }
    }

    /**
     * @param pn   从 pn页面 开始刷新后面的数据，
     *             ps:  pn==2  ps==10  则 从position==10 的位置开始清除掉，然后将新的数据源附加到数据源的末尾
     * @param list
     */
    private void showUpdate(int pn, List list) {
        if (pn <= pageNo && pn > 1) {
            int pos = (pn - 1) * ps;
            baseRVAdapter.setDataList(pos, list);
        }
    }

    /**
     * 显示数据，并预加载数据
     */
    private void showDatas(int pn, List list) {

        //如果不是第一页，且数据为空，则数据加载完毕
        if (pn != 1 && UIUtils.isEmpty(list)) {
            if (pn <= pageNo && pn > 1) {
                showUpdate(pn, list);
            } else {
                smartRefreshLayout.setNoMoreData(true);
                UIUtils.showToastSafes("数据已全部加载完毕");
                return;
            }
        }

        if (baseRVAdapter != null) {
            if (UIUtils.isEmpty(list)) {
                //如果第一页的数据为空则显示空的
                if (pn == 1) {
                    baseRVAdapter.setDataList(list);
                } else {
                    showUpdate(pn, list);
                }
            } else {
                if (pn == 1) {
                    smartRefreshLayout.setNoMoreData(false);
                    baseRVAdapter.setDataList(list);
                } else {
                    if (pn <= pageNo && pn > 1) {
                        showUpdate(pn, list);
                    } else {
                        baseRVAdapter.loadMore(list);
                    }

                }
                isWaitLoading = false;//数据刚显示，
                //预加载数据,如果启用了加载更多
                if (onGetDataListener != null && enableLoadMore && isAutoPreload) {

                    isPreload = true;//正在预加载
                    onGetDataListener.onGetData(pn + 1);

//                    if (pn == 1) {
//                        UIUtils.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                onGetDataListener.onGetData(2);
//                            }
//                        }, 300);
//                    } else {
//                        onGetDataListener.onGetData(pn + 1);
//                    }

                }

            }
        }

        pageNo = pn;
    }

    /**
     * 是否启用下拉刷新（默认启用）
     *
     * @param enabled 是否启用
     * @return SmartRefreshLayout
     */
    public void setEnableRefresh(boolean enabled) {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setEnableRefresh(enabled);
        }
    }

    /**
     * Set whether to enable pull-up loading more (enabled by default).
     * 设置是否启用上拉加载更多（默认启用）
     *
     * @param enabled 是否启用
     * @return RefreshLayout
     */
    public void setEnableLoadMore(boolean enabled) {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.setEnableLoadMore(enabled);
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        onGetDataListener.onGetData(1);
    }

    public void onRefresh(int pageNo) {
        onGetDataListener.onGetData(pageNo);
    }

    public void update(int position) {
        int pn = position / ps + 1;
        onRefresh(pn);
    }


    /**
     *   显示数据
     *
     * @param pn
     * @param list
     */
    private void showDataList(int pn, List list) {
//        stopRefreshLayout();
        //如果没有刷新动画，立即添加结果
        if (smartRefreshLayout.getState() == RefreshState.None) {
            showDatas(pn, list);
        } else {
            showDatas(pn, list);

            //下拉刷刷新有关闭动画，等结束后加载数据，避免动画卡顿
//            UIUtils.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    showDatas(pn, list);
//                }
//            }, 800);


        }

    }


    /**
     * 关闭下拉刷新或加载更多
     */
    public void stopRefreshLayout() {
        //如果是预加载数据返回，则直接返回
        if (isPreload) return;
        if (smartRefreshLayout != null) {
            smartRefreshLayout.closeHeaderOrFooter();
        }
    }


}
