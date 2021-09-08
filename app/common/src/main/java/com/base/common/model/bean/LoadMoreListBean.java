package com.base.common.model.bean;

import java.util.List;

public class LoadMoreListBean<T> {


    private List<T> list;
    /**
     * totalCount : 0
     * pageSize : 10
     * totalPage : 0
     * currPage : 1
     * list : []
     */

    private int totalCount;
    private int pageSize;
    private int totalPage;
    private int currPage;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }
}
