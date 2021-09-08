package com.base.common.model.bean;


/**
 * @author sugar
 * 关于微信、支付宝支付的返回信息
 */
public class PayResponse {


    private int payWay;

    private int payStatus;

    private String payStatusExplain;

    public PayResponse() {
    }

    public PayResponse(int payWay, int payStatus) {
        this.payWay = payWay;
        this.payStatus = payStatus;
    }

    public int getPayWay() {
        return payWay;
    }

    public void setPayWay(int payWay) {
        this.payWay = payWay;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;

        switch (payStatus) {
            case PayStatus.STATUS_SUCCESS:
                setPayStatusExplain("支付成功");
                break;
            case PayStatus.STATUS_CANCEL:
                setPayStatusExplain("支付取消");
                break;
            case PayStatus.STATUS_FAIL:
                setPayStatusExplain("支付失败");
                break;
            default:
                setPayStatusExplain("支付失败");
                break;
        }

    }

    public String getPayStatusExplain() {
        return payStatusExplain;
    }

    public void setPayStatusExplain(String payStatusExplain) {
        this.payStatusExplain = payStatusExplain;
    }

    /**
     * 支付方式
     * 1: 微信支付
     * 2: 阿里支付
     */
    public interface PayWay {

        int WAY_WECHAT = 1;
        int WAY_ALI = 2;

    }

    public interface PayStatus {

        /**
         * 0.  支付成功
         * -1. 支付取消 (调起支付页面后取消)
         * -2. 支付失败
         * -3. 在支付页面未做任何操作返回 （未调起支付页面返回）
         */

        int STATUS_SUCCESS = 0;
        int STATUS_CANCEL = -1;
        int STATUS_FAIL = -2;
        int STATUS_DONOTHING = -3;
        int STATUS_UNKOWN = -99;
    }


}
