package com.base.common.view.widget.statelayout.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.common.view.widget.statelayout.holder.ItemViewHolder;
import com.base.common.view.widget.statelayout.holder.LoadingViewHolder;


public class ViewHelper {

    public static final int ERROR = 1;
    public static final int EMPTY = 2;
    public static final int TIMEOUT = 3;
    public static final int NOT_NETWORK = 4;
    public static final int LOADING = 5;


    public TextView getTvTip(int type, View view) {
        switch (type) {
            case ERROR:
                return ((ItemViewHolder) view.getTag()).tvTip;
            case EMPTY:
                return ((ItemViewHolder) view.getTag()).tvTip;
            case TIMEOUT:
                return ((ItemViewHolder) view.getTag()).tvTip;
            case NOT_NETWORK:
                return ((ItemViewHolder) view.getTag()).tvTip;
            case LOADING:
                return ((LoadingViewHolder) view.getTag()).tvTip;
            default:
                return null;
        }
    }

    public ImageView getImg(int type, View view) {
        switch (type) {
            case ERROR:
                return ((ItemViewHolder) view.getTag()).ivImg;
            case EMPTY:
                return ((ItemViewHolder) view.getTag()).ivImg;
            case TIMEOUT:
                return ((ItemViewHolder) view.getTag()).ivImg;
            case NOT_NETWORK:
                return ((ItemViewHolder) view.getTag()).ivImg;
            default:
                return null;
        }
    }


}
