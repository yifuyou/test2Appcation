package com.base.common.view.adapter.connector;


import androidx.annotation.LayoutRes;
import androidx.databinding.ViewDataBinding;


/**
 * 必须是虚类 ，不然反射不到数据信息
 * 通用的adapter类型  放弃了ViewDataBinding的便利,对ui的更改通过xml中数据的绑定，使更换layoutID 成为了可能
 * 为了通用性，下面的方法均不了重写
 * 该类只关心  数据源Bean  对xml是哪个根本不关心，故增强了通用性
 *
 * @param <T>
 */
public abstract class BaseUniversalItemMultiType<T> extends BaseItemMultiType<T, ViewDataBinding> {

    public BaseUniversalItemMultiType(@LayoutRes int layRes) {
        super(layRes);
    }

    @Override
    public final void onCreateViewHolder(ViewDataBinding binding, BaseViewHolder viewHolder) {
        super.onCreateViewHolder(binding, viewHolder);
    }

    @Override
    public final void onBindViewHolder(ViewDataBinding binding, int position, BaseViewHolder viewHolder, T bean) {
        super.onBindViewHolder(binding, position, viewHolder, bean);
    }

    @Override
    public final void onBindViewHolder(ViewDataBinding binding, int position, T bean) {
        super.onBindViewHolder(binding, position, bean);
    }

    @Override
    public final void onViewAttachedToWindow(ViewDataBinding binding, BaseViewHolder viewHolder) {
        super.onViewAttachedToWindow(binding, viewHolder);
    }

    @Override
    public final void onViewDetachedFromWindow(ViewDataBinding binding, BaseViewHolder viewHolder) {
        super.onViewDetachedFromWindow(binding, viewHolder);
    }

}
