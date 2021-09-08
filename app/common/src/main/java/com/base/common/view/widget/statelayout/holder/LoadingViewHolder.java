package com.base.common.view.widget.statelayout.holder;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.base.common.R;


public class LoadingViewHolder{

    public FrameLayout frameLayout;
    public TextView tvTip;
    public LoadingViewHolder(View view) {
        tvTip = (TextView) view.findViewById(R.id.tv_message);
        frameLayout = (FrameLayout) view.findViewById(R.id.loading_layout);
    }


}
