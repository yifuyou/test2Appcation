package com.base.common.model.bean;

public class BaseDataWrapper<T> {
    private int code;

    private String msg;

    private T data;

    private T page;

    public T getPage() {
        return page;
    }

    public void setPage(T page) {
        this.page = page;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
