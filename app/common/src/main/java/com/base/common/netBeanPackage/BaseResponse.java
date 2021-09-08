package com.base.common.netBeanPackage;


import androidx.databinding.BaseObservable;

public class BaseResponse extends BaseObservable {


    public static boolean isSuccess(int code) {
        return code == 200 || code == 0;
    }

    //项目的通用字段
    public static final String codeString = "code";
    public static final String msgString = "msg";

    private int code = -1;
    private String msg;

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


}