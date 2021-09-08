package com.base.common.view.adapter.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.base.common.BR;

public class ChildBaseBean extends BaseObservable {

    private boolean isSelected = false;//是否选中
    private boolean isChecked = false;//是否选中
    private boolean isExpandable = false;//是否展开
    private boolean enable = true;

    private int state;//记录状态

    @Bindable
    public boolean isEnable() {
        return enable;
    }


    @Bindable
    public boolean getSelected() {
        return isSelected;
    }

    @Bindable
    public boolean getChecked() {
        return isChecked;
    }

    @Bindable
    public boolean getExpandable() {
        return isExpandable;
    }

    @Bindable
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        notifyPropertyChanged(BR.state);
    }


    public void setEnable(boolean enable) {
        this.enable = enable;
        notifyPropertyChanged(BR.enable);
    }


    public void setSelected(boolean selected) {
        isSelected = selected;
        notifyPropertyChanged(BR.selected);
    }


    public void setChecked(boolean checked) {
        isChecked = checked;
        notifyPropertyChanged(BR.checked);
    }


    public void setExpandable(boolean expandable) {
        isExpandable = expandable;
        notifyPropertyChanged(BR.expandable);
    }

}
