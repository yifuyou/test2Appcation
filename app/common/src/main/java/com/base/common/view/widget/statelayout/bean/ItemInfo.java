package com.base.common.view.widget.statelayout.bean;

/**
 * Created by sugar on 2018/3/29.
 */

public class ItemInfo {
    private int resId;
    private String tip;

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public ItemInfo() {
    }

    public ItemInfo(String tip) {
        this.tip = tip;
    }

    public ItemInfo(int resId, String tip) {
        this.resId = resId;
        this.tip = tip;
    }
}
