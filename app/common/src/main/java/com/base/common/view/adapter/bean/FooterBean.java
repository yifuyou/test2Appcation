package com.base.common.view.adapter.bean;

public class FooterBean extends ChildBaseBean {


    /**
     * 0   首次加载，后面还有数据、
     * -1  首次加载就已加载完毕，后面没有数据了
     * 1   加载完成,后面没有数据了
     * 2   加载更多，后面还有数据
     */
//    private int status;
    private int amountCount;//总数
    private int childCount;//当前显示的数
    private int itemType;

    private String content;

    public FooterBean() {

    }

    public FooterBean(int status) {
        setState(status);
//        this.status = status;
    }

    public FooterBean(int status, String content) {
        setState(status);
//        this.status = status;
        this.content = content;
    }

//    @Bindable
//    public int getStatus() {
//        return status;
//    }
//
//    public void setStatus(int status) {
//        this.status = status;
//        notifyPropertyChanged(com.base.common.BR.status);
//    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getAmountCount() {
        return amountCount;
    }

    public void setAmountCount(int amountCount) {
        this.amountCount = amountCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }
}