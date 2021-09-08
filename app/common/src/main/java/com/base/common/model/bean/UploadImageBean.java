package com.base.common.model.bean;

public class UploadImageBean {

    /**
     * code : 0
     * msg : 成功
     * data : https://find-server.oss-cn-hangzhou.aliyuncs.com/images/20210128/f206579b950b4533ac5376fe91c1abcc.jpg
     */

    private int code;
    private String msg;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
