package com.base.common.view.widget.statelayout.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.base.common.R;
import com.base.common.utils.OnClickCheckedUtil;
import com.base.common.view.widget.statelayout.StateLayout;
import com.base.common.view.widget.statelayout.bean.ItemInfo;
import com.base.common.view.widget.statelayout.holder.ItemViewHolder;
import com.base.common.view.widget.statelayout.holder.LoadingViewHolder;


public class LayoutHelper {

    /**
     * 解析布局中的可选参数
     *
     * @param context
     * @param attrs
     * @param stateLayout
     */
    public static void parseAttr(Context context, AttributeSet attrs, StateLayout stateLayout) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.StateLayout, 0, 0);
        try {
            int errorImg = a.getResourceId(R.styleable.StateLayout_errorImg, -1);
            String errorText = a.getString(R.styleable.StateLayout_errorText);
            stateLayout.setErrorItem(new ItemInfo(errorImg, errorText));

            int timeOutImg = a.getResourceId(R.styleable.StateLayout_timeOutImg, -1);
            String timeOutText = a.getString(R.styleable.StateLayout_timeOutText);
            stateLayout.setTimeOutItem(new ItemInfo(timeOutImg, timeOutText));

            int emptyImg = a.getResourceId(R.styleable.StateLayout_emptyImg, -1);
            String emptyText = a.getString(R.styleable.StateLayout_emptyText);
            stateLayout.setEmptyItem(new ItemInfo(emptyImg, emptyText));

            int noNetworkImg = a.getResourceId(R.styleable.StateLayout_noNetworkImg, -1);
            String noNetworkText = a.getString(R.styleable.StateLayout_noNetworkText);
            stateLayout.setNoNetworkItem(new ItemInfo(noNetworkImg, noNetworkText));

            int loginImg = a.getResourceId(R.styleable.StateLayout_loginImg, -1);
            String loginText = a.getString(R.styleable.StateLayout_loginText);
            stateLayout.setLoginItem(new ItemInfo(loginImg, loginText));

            String loadingText = a.getString(R.styleable.StateLayout_loadingText);
            stateLayout.setLoadingItem(new ItemInfo(loadingText));
        } finally {
            a.recycle();
        }
    }

    /**
     * 获取初始的错误View
     *
     * @param layoutInflater 布局填充器
     * @param item           错误bean
     * @param layout         容器
     * @return 错误View
     */
    public static View getErrorView(LayoutInflater layoutInflater, ItemInfo item, final StateLayout layout) {
        View view = layoutInflater.inflate(R.layout.layout_error, null);
        return getView(item, layout, view);
    }

    public static View getView(ItemInfo item, final StateLayout layout, View view) {
        if (item != null) {
            ItemViewHolder holder = new ItemViewHolder(view);
            view.setTag(holder);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
            if (item.getResId() != -1) {
                holder.ivImg.setImageResource(item.getResId());
            }
            view.setOnClickListener(new OnClickCheckedUtil() {
                @Override
                public void onClicked(View view) {
                    if (layout.getRefreshLListener() != null) {
                        layout.showLoadingView();
                        layout.getRefreshLListener().onRefreshClick();
                    }
                }
            });
        }
        return view;
    }

    /**
     * 获取初始的没有网络View
     *
     * @param layoutInflater 布局填充器
     * @param item           没有网络bean
     * @param layout         容器
     * @return 没有网络View
     */
    public static View getNoNetworkView(LayoutInflater layoutInflater, ItemInfo item,
                                        final StateLayout layout) {
        View view = layoutInflater.inflate(R.layout.layout_no_network, null);
        return getView(item, layout, view);
    }

    /**
     * 获取初始的加载View
     *
     * @param layoutInflater 布局填充器
     * @param item           加载bean
     * @return 加载View
     */
    public static View getLoadingView(LayoutInflater layoutInflater, ItemInfo item) {
        View view = layoutInflater.inflate(R.layout.layout_loading, null);
        if (item != null) {
            LoadingViewHolder holder = new LoadingViewHolder(view);
            view.setTag(holder);

            ProgressBar progressBar = new ProgressBar(view.getContext());
            progressBar.setIndeterminateDrawable(view.getResources().getDrawable(R.drawable.bg_loading_animate));
            holder.frameLayout.addView(progressBar);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
        }
        return view;
    }

    /**
     * 获取初始的超时View
     *
     * @param layoutInflater 布局填充器
     * @param item           超时bean
     * @param layout         容器
     * @return 超时View
     */
    public static View getTimeOutView(LayoutInflater layoutInflater, ItemInfo item, final StateLayout layout) {
        View view = layoutInflater.inflate(R.layout.layout_time_out, null);
        return getView(item, layout, view);
    }

    /**
     * 获取初始的空数据View
     *
     * @param layoutInflater 布局填充器
     * @param item           空数据bean
     * @return 空数据View
     */
    public static View getEmptyView(LayoutInflater layoutInflater, ItemInfo item, final StateLayout layout) {
        View view = layoutInflater.inflate(R.layout.layout_empty, null);
        if (item != null) {
            ItemViewHolder holder = new ItemViewHolder(view);
            view.setTag(holder);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
            if (item.getResId() != -1) {
                holder.ivImg.setImageResource(item.getResId());
            }

            view.findViewById(R.id.llBodyContent).setOnClickListener(new OnClickCheckedUtil() {
                @Override
                public void onClicked(View view) {
                    if (layout.getRefreshLListener() != null) {
                        layout.showLoadingView();
                        layout.getRefreshLListener().onRefreshClick();
                    }
                }
            });
        }
        return view;
    }


    /**
     * 获取初始的空数据View
     *
     * @param layoutInflater 布局填充器
     * @param item           空数据bean
     * @return 空数据View
     */
    public static View getLoginView(LayoutInflater layoutInflater, ItemInfo item,
                                    final StateLayout layout) {
        View view = layoutInflater.inflate(R.layout.layout_login, null);
        if (item != null) {
            ItemViewHolder holder = new ItemViewHolder(view);
            view.setTag(holder);

            if (!TextUtils.isEmpty(item.getTip())) {
                holder.tvTip.setText(item.getTip());
            }
            if (item.getResId() != -1) {
                holder.ivImg.setImageResource(item.getResId());
            }
            view.findViewById(R.id.button).setOnClickListener(new OnClickCheckedUtil() {
                @Override
                public void onClicked(View view) {
                    if (layout.getRefreshLListener() != null) {
                        layout.getRefreshLListener().onLoginClick(view.getContext());
                    }
                }
            });
        }
        return view;
    }

}
