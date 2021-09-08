package com.base.common.view.adapter.ada;

import androidx.databinding.ViewDataBinding;

import com.base.common.view.adapter.connector.BaseViewHolder;

public abstract class SingleUniversalAdapter<T> extends SingleRecyclerAdapter<T, ViewDataBinding> {


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


}
