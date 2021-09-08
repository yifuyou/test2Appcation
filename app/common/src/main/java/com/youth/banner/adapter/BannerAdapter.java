package com.youth.banner.adapter;


import androidx.annotation.NonNull;

import com.base.common.view.adapter.ada.BaseRVAdapter;
import com.base.common.view.adapter.connector.BaseViewHolder;
import com.youth.banner.config.BannerConfig;
import com.youth.banner.util.BannerUtils;

import java.util.ArrayList;
import java.util.List;


public abstract class BannerAdapter<T> extends BaseRVAdapter {



    private int mIncreaseCount = BannerConfig.INCREASE_COUNT;


    @Override
    public int getItemViewType(int position) {
        int pos = getRealPosition(position);
        return super.getItemViewType(pos);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        int pos = getRealPosition(position);
        super.onBindViewHolder(holder, pos);
    }


    /**
     * 设置实体集合（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param datas
     */
    public void setDatas(List<T> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        getDatas().clear();
        getDatas().addAll(datas);
        notifyDataSetChanged();
    }


    @Override
    public void setDataList(int startPos, List list) {
//        super.setDataList(startPos, list);

        setDatas(list);
    }


    @Override
    public void setDataList(List list) {
//        super.setDataList(list);
        setDatas(list);
    }

    @Override
    public void loadMore(List newItems) {
//        super.loadMore(newItems);
        if (newItems == null) return;
        getDatas().addAll(newItems);
        notifyDataSetChanged();
    }


    /**
     * 获取指定的实体（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param position 真实的position
     * @return
     */
    public T getData(int position) {
        return (T)getItemBean(position);
    }

    /**
     * 获取指定的实体（可以在自己的adapter自定义，不一定非要使用）
     *
     * @param position 这里传的position不是真实的，获取时转换了一次
     * @return
     */
    public T getRealData(int position) {
        return (T)getDatas().get(getRealPosition(position));
    }


    @Override
    public int getItemCount() {
        return getRealCount() > 1 ? getRealCount() + mIncreaseCount : getRealCount();
    }

    public int getRealCount() {
        return getDatas() == null ? 0 : getDatas().size();
    }

    public int getRealPosition(int position) {
        return BannerUtils.getRealPosition(mIncreaseCount == BannerConfig.INCREASE_COUNT, position, getRealCount());
    }


    public void setIncreaseCount(int increaseCount) {
        this.mIncreaseCount = increaseCount;
    }


}