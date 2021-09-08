package com.base.common.view.adapter.ada;

import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;

import com.base.common.view.adapter.bean.FooterBean;
import com.base.common.view.adapter.bean.HeaderBean;
import com.base.common.view.adapter.connector.BaseViewHolder;
import com.base.common.view.adapter.connector.BaseItemTypeInterface;
import com.base.common.view.adapter.connector.OnItemClickInterface;

/**
 * 单一类型item的
 * 必须为虚类 {@link BaseRVAdapter#putMultiItemType(int, BaseItemTypeInterface)}    用了反射
 *
 * @param <T>
 * @param <D>
 */
public abstract class SingleRecyclerAdapter<T, D extends ViewDataBinding> extends GeneralRecyclerAdapter implements BaseItemTypeInterface<T, D> {

    private int layRes;
    private int itemType = TYPE_CHILD;


    public SingleRecyclerAdapter() {

    }

    public SingleRecyclerAdapter(LifecycleOwner lifecycleOwner) {
        super(lifecycleOwner);
    }

    public SingleRecyclerAdapter(@LayoutRes int layRes) {
        this.layRes = layRes;
        super.init();
    }


    public T getListBean(int position) {
        if (position <= getLastPosition() && position >= getFirstPosition()) {
            return (T) getItemBean(position);
        } else return null;
    }


    @Override
    public int getLayoutId() {
        return layRes;
    }


    @Override
    public int getItemType() {
        return itemType;
    }

    @Override
    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    /**
     * 判断是否是当前类型
     *
     * @param position
     * @param bean
     * @return
     */
    public final boolean isCurrentItemType(int position, @NonNull T bean) {

        if (bean instanceof HeaderBean) {
            return ((HeaderBean) bean).getItemType() == getItemType();
        } else if (bean instanceof FooterBean) {
            return ((FooterBean) bean).getItemType() == getItemType();
        }

        return isCurrentChildItemType(position, bean);
    }

    public boolean isCurrentChildItemType(int position, T bean) {
        return true;
    }


    @Override
    public boolean isFullSpanType() {
        return false;
    }

    @Override
    public void initMultiItemType() {
        putMultiItemType(BaseItemTypeInterface.TYPE_CHILD, this);
    }


    @Override
    public void onCreateViewHolder(D binding, BaseViewHolder viewHolder) {

    }

    @Override
    public void onBindViewHolder(D binding, int position, T bean) {

    }

    @Override
    public void onBindViewHolder(D binding, int position, BaseViewHolder viewHolder, T bean) {

    }

    @Override
    public void onViewDetachedFromWindow(D binding, BaseViewHolder viewHolder) {

    }

    @Override
    public void onViewAttachedToWindow(D binding, BaseViewHolder viewHolder) {

    }

    /**
     * 长按事件是有返回值的，如果返回false，两个事件都会有响应，如果返回true则只响应长按事件
     * onclickType 0 点击事件，1 长按事件
     *
     * @param view        //点击的view
     * @param onclickType //点击事件类型
     * @param position    当前的位置
     * @param bean        当前的数据
     */
    @Override
    public final void onItemClick(View view, BaseRVAdapter baseRVAdapter, BaseViewHolder viewHolder, D binding, int onclickType, int position, T bean) {
        boolean bb = onItemTypeClick(view, baseRVAdapter, onclickType, position, bean);
        if (bb) return;
        if (baseRVAdapter != null && baseRVAdapter.getOnItemClickCallback() != null) {
            baseRVAdapter.getOnItemClickCallback().onItemClick(view, onclickType, getItemType(), position, bean);
        }
    }

    /**
     * 会被{@link com.base.common.view.adapter.ada.BaseRVAdapter#setOnItemClickInterface(OnItemClickInterface)} 拦截
     *
     * @param view        //点击的view
     * @param onclickType //点击事件类型
     * @param position    当前的位置
     * @param bean        当前的数据
     * @return true 表示拦截回调  false不拦截回调
     */
    public boolean onItemTypeClick(View view, BaseRVAdapter baseRVAdapter, int onclickType, int position, T bean) {
        return false;
    }


    @Override
    public final boolean onItemClick(View view, BaseRVAdapter baseRVAdapter, int onclickType, int itemType, int position, Object bean) {
        return super.onItemClick(view, baseRVAdapter, onclickType, itemType, position, bean);
    }


}
