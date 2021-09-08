package com.base.common.view.adapter.bean;



public class HeaderBean extends ChildBaseBean {

    private int itemType;
    //    private int state;
    private int currentTab = 0;
    private String stateContext;

    public HeaderBean() {

    }

    public HeaderBean(int state) {
//        this.state = state;
        setState(state);
    }


    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

//    @Bindable
//    public int getState() {
//        return state;
//    }
//
//    public void setState(int state) {
//        this.state = state;
//        notifyPropertyChanged(com.base.common.BR.state);
//    }


    public String getStateContext() {
        return stateContext;
    }

    public void setStateContext(String stateContext) {
        this.stateContext = stateContext;
    }

    public int getCurrentTab() {
        return currentTab;
    }

    public void setCurrentTab(int mCurrentTab) {
        this.currentTab = mCurrentTab;
    }
}