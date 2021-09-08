package com.base.common.view.adapter.ada;

import androidx.collection.SparseArrayCompat;
import androidx.lifecycle.LifecycleOwner;

import com.base.common.view.adapter.bean.FooterBean;
import com.base.common.view.adapter.bean.HeaderBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 定义添加的头和尾部
 */
public abstract class BaseAddHeadAndFootAdapter extends BaseRVAdapter {

    //存储头部 key 为itemType  value  期望排序的位置
    protected SparseArrayCompat<Integer> mHeaderViews;
    //存储尾部View
    protected SparseArrayCompat<Integer> mFootViews;

    public BaseAddHeadAndFootAdapter() {

    }

    public BaseAddHeadAndFootAdapter(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    @Override
    public void init() {
        mHeaderViews = new SparseArrayCompat<>();
        mFootViews = new SparseArrayCompat<>();
        super.init();
    }



    public <B extends HeaderBean> void addHeaderView(B headBean, int itemType, int position) {
        if (headBean != null) {
            headBean.setItemType(itemType);
            addHeaderView(headBean, position);
        }
    }

    /**
     * 添加头部要整体刷新，不然显示会少
     *
     * @param headBean
     * @param position
     * @param <B>
     */

    public <B extends HeaderBean> void addHeaderViewEnd(B headBean, int itemType, int position) {
        if (headBean != null) {
            addHeaderView(headBean, itemType, position);
            notifyDataSetChanged();
        }
    }



    /**
     * 添加头部
     *
     * @param headBean 头部数据
     * @param position 如果存在则插入头部的位置
     */
    private <B extends HeaderBean> void addHeaderView(B headBean, int position) {
        if (headBean == null) return;

        int itemType = headBean.getItemType();
        //如果已拥有则更新
        if (isHasHeaderByType(itemType)) {
            //查找当前类型的位置
            for (int i = 0; i < getHeadCount(); i++) {
                if (itemType == getItemViewType(i)) {
                    update(i, headBean);
                    break;
                }
            }
        } else {
            int pp = -1;
            //添加头部，先判断头部的位置  从第一个位置逐个遍历
            for (int i = 0; i < getHeadCount(); i++) {
                //获取排序位置
                int posSort = mHeaderViews.get(getItemViewType(i), 0);
                //如果当前的排序<=该位置的排序，则插入到该位置
                if (position <= posSort) {
                    pp = i;
                    break;
                }
            }

            //如果找不到插入位置，则插入末尾
            if (pp == -1) {
                pp = getFirstPosition();
            }

            mHeaderViews.put(itemType, position);
            add(pp, headBean);
        }
    }




    public <B extends FooterBean> void addFooterView(B footBean, int itemType) {
        if (footBean != null) {
            footBean.setItemType(itemType);
            addFooterView(footBean);
//            notifyDataSetChanged();
        }
    }


    private  <B extends FooterBean> void addFooterView(B footBean) {
        int itemType = footBean.getItemType();

        //如果已拥有则更新
        if (isHasFooterByType(itemType)) {
            //查找当前类型的位置
            for (int itemCount = getItemCount() - 1; itemCount >= 0; itemCount--) {
                if (itemType == getItemViewType(itemCount)) {
                    update(itemCount, footBean);
                    break;
                }
            }
        } else {
            mFootViews.put(itemType, 0);
            add(getItemCount(), footBean);
        }


    }


    public void removeHeard(int position) {
        if (isHasHeader(position)) {
            mHeaderViews.remove(getItemViewType(position));
            remove(position);
        }
    }

    /**
     * @param index
     */
    public void removeFooter(int index) {
        int position = getItemCount() - getFootCount() + index;
        if (position < 0 || position > getItemCount() - 1) return;
        if (isHasFooter(position)) {
            remove(position);
            mFootViews.removeAt(index);
        }
    }

    public void removeHeardForItemType(int itemType) {
        if (isHasHeaderByType(itemType)) {
            int position = getFirstPosition(itemType, 0);
            remove(position);
            mHeaderViews.remove(itemType);
        }
    }

    public void removeFooterForItemType(int itemType) {
        if (isHasFooterByType(itemType)) {
            for (int itemCount = getItemCount() - 1; itemCount >= 0; itemCount--) {
                if (getItemViewType(itemCount) == itemType) {
                    remove(itemCount);
                    mFootViews.remove(itemType);
                    break;
                }
            }
        }
    }


    public void removeAllHeard() {
        if (getHeadCount() > 0) {
            clear(0, getFirstPosition() - 1);
            mHeaderViews.clear();
        }
    }

    public void removeAllFooter() {
        if (getFootCount() > 0) {
            clear(getLastPosition() + 1, getItemCount() - 1);
            mFootViews.clear();
        }
    }

    /**
     * 清除所有项
     */
    public void clear() {
        removeAllFooter();
        removeAllHeard();
        super.clear();
    }

    /**
     * 清除多项
     *
     * @param startPosition
     * @param endPosition
     */
    public final void clear(int startPosition, int endPosition) {
        if (startPosition < getHeadCount()) {
            int cou = getHeadCount();
            for (int i = cou - 1; i >= startPosition; i--) {
                mHeaderViews.removeAt(i);
            }
        }
        if (endPosition > getLastPosition()) {
            int cou = endPosition - getLastPosition();
            for (int i = cou - 1; i >= 0; i--) {
                mFootViews.removeAt(i);
            }
        }
        super.clear(startPosition, endPosition);
    }

    public boolean isHasFooter(int position) {
        return isHasFooterByType(getItemViewType(position));
    }

    public boolean isHasHeader(int position) {
        return isHasHeaderByType(getItemViewType(position));
    }

    public boolean isHasFooterByType(int itemType) {
        if (mFootViews.size() == 0) return false;
        return mFootViews.indexOfKey(itemType) > -1;
    }

    public boolean isHasHeaderByType(int itemType) {
        if (mHeaderViews.size() == 0) return false;
        return mHeaderViews.indexOfKey(itemType) > -1;
    }


    public int getHeadCount() {
        return mHeaderViews.size();
    }

    public int getFootCount() {
        return mFootViews.size();
    }


    @Override
    public int getFirstPosition() {
        return getHeadCount();
    }

    /**
     * @return 有可能为-1  当只有一个foot时为-1
     */
    @Override
    public int getLastPosition() {
//        if (getItemCount() == getHeadCount() + getFootCount()) return -1;
        return getItemCount() - getFootCount() - 1;
    }


    public int getChildCount() {
        return getItemCount() - getHeadCount() - getFootCount();
    }


    /**
     * @param indexFoot 从0开始  -1未找到
     * @return
     */
    public int getFootPosition(int indexFoot) {

        if (indexFoot < mFootViews.size()) {

            int itemType = mFootViews.get(indexFoot);
            for (int itemCount = getItemCount() - 1; itemCount >= 0; itemCount--) {
                if (getItemViewType(itemCount) == itemType) {
                    return itemCount;
                }
            }

        }

        return -1;
    }


    /**
     * 批量更新
     *
     * @param startPosition
     * @param newList
     */
    public final void updateRange(int startPosition, List newList) {
        super.updateRange(startPosition, newList, getFootCount());
    }

    /**
     * 加载更多
     *
     * @param newItems
     */
    public void loadMore(List newItems) {
        super.loadMore(newItems, getFootCount());
    }

}
