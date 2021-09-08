package com.base.common.view.adapter.connector;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.base.common.utils.OnClickCheckedUtil;
import com.base.common.view.adapter.ada.BaseRVAdapter;


public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    public int state;//当前viewHolder


    private ViewDataBinding mBinding;
    public BaseItemTypeInterface baseItemMultiTypeInterface;
    private Object value;//当前的数据
    private OnItemClickInterface onItemClickInterface;

    private BaseRVAdapter adapter;//绑定的adapter

    private Object cacheValue;//缓存用于离开屏幕时回收的数据

    public OnItemClickInterface getOnItemClickInterface() {
        return onItemClickInterface;
    }

    public BaseViewHolder(ViewGroup viewGroup, BaseItemTypeInterface baseItemMultiTypeInterface, @Nullable OnItemClickInterface onItemClickInterface) {
        // 注意要依附 viewGroup，不然显示item不全!!
        super(DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), baseItemMultiTypeInterface.getLayoutId(), viewGroup, false).getRoot());
        this.baseItemMultiTypeInterface = baseItemMultiTypeInterface;

        this.onItemClickInterface = onItemClickInterface;
        // 得到这个View绑定的Binding
        mBinding = DataBindingUtil.getBinding(this.itemView);

        baseItemMultiTypeInterface.onCreateViewHolder(mBinding, this);

        //反射调用mBinding的setBaseViewHolder()方法
//        JavaMethod.invokeSetMethod(mBinding, "set" + getClass().getSimpleName(), this);

        mBinding.setVariable(com.base.common.BR.click, this);
    }


    public BaseItemTypeInterface getBaseItemMultiTypeInterface() {
        return baseItemMultiTypeInterface;
    }

    /**
     * 当数据改变时，binding会在下一帧去改变数据，如果我们需要立即改变，就去调用executePendingBindings方法。
     */
    public void onBaseBindViewHolder(BaseRVAdapter adapter, Object object) {
        mBinding.setVariable(com.base.common.BR.item, object);
        value = object;
        this.adapter = adapter;
        mBinding.executePendingBindings();
    }

    /**
     * 一般不是adapter调用绑定数据
     *
     * @param position
     * @param bean
     */
    public void onBindViewHolder(int position, Object bean) {
        value = bean;
        getBaseItemMultiTypeInterface().onBindViewHolder(mBinding, position, bean);
    }


    public ViewDataBinding getBinding() {
        return mBinding;
    }

    public <T> T getDataBinding() {
        return (T) mBinding;
    }

    /**
     * 用时需要判空
     *
     * @return
     */
    public BaseRVAdapter getAdapter() {
        return adapter;
    }


    /**
     * {@link com.base.common.view.adapter.ada.BaseRVAdapter} 和{@link BaseItemMultiType}  和有点击事件
     * BaseRVAdapter 的点击事件return true 时可以拦截 BaseMultiItemType 的点击事件
     * 将里层的点击事件传出
     *
     * @param view
     */

    public void onClick(View view) {
        if (!OnClickCheckedUtil.onClickChecked(300)) {
            return;
        }

        if (baseItemMultiTypeInterface != null) {
            if (onItemClickInterface != null) {
                boolean bb = onItemClickInterface.onItemClick(view, OnItemClickInterface.onClickEvent, baseItemMultiTypeInterface.getItemType(), getAdapterPosition(), value);
                if (bb) return;
            }
            baseItemMultiTypeInterface.onItemClick(view, adapter, this, mBinding, OnItemClickInterface.onClickEvent, getAdapterPosition(), value);
        }
    }


    public boolean onLongClick(View view) {
        if (baseItemMultiTypeInterface != null) {
            if (onItemClickInterface != null) {
                boolean bb = onItemClickInterface.onItemClick(view, OnItemClickInterface.onLongClickEvent, baseItemMultiTypeInterface.getItemType(), getAdapterPosition(), value);
                if (!bb) {
                    baseItemMultiTypeInterface.onItemClick(view, adapter, this, mBinding, OnItemClickInterface.onClickEvent, getAdapterPosition(), value);
                }
            }
        }
        return true;
    }


    public void setCacheValue(Object cacheValue) {
        this.cacheValue = cacheValue;
    }

    public Object getCacheValue() {
        return cacheValue;
    }


}
