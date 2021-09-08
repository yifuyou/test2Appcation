package com.base.common.model.http.upLoad;


import com.base.common.model.bean.UploadImageBean;
import com.base.common.netBeanPackage.BaseResponse;
import com.base.common.utils.UIUtils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * 自定义文件上传、下载 观察者 (增强 RequestBaseObserver)
 */
public abstract class LoadCallBack extends DisposableObserver<Object> implements Observer<Object> {

    public LoadCallBack() {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onNext(Object t) {
        if (t instanceof Double) {
            String percent = doubleToKeepTwoDecimalPlaces(((Double) t).doubleValue());
            onProgress(percent);
        } else {
            if (t instanceof UploadImageBean) {
                UploadImageBean bean = (UploadImageBean) t;
                int code = bean.getCode();
                String msg = bean.getMsg();
                String url = bean.getData();
                if (!UIUtils.isEmpty(code) && BaseResponse.isSuccess(code) && !UIUtils.isEmpty(url)) {
                    onSuccess(url);
                } else {
                    onError(new Throwable(code + "  " + msg));
                }


            }
        }
    }


    /**
     * 上传、下载 需重写此方法，更新进度
     *
     * @param percent 进度百分比 数
     */
    protected void onProgress(String percent) {

    }

    /**
     * 请求成功 回调
     *
     * @param url 请求返回的数据
     */
    protected abstract void onSuccess(String url);


    /**
     * 文件上传出错
     *
     * @param e
     */
    @Override
    public void onError(Throwable e) {
        if (e != null) {

        }
    }


    @Override
    public void onComplete() {

    }


    /**
     * double类型数字  保留一位小数(四舍五入)
     * DecimalFormat转换最简便
     *
     * @param doubleDigital
     * @return String
     */
    public static String doubleToKeepTwoDecimalPlaces(double doubleDigital) {
        DecimalFormat df = new DecimalFormat("##0.0");
        return df.format(doubleDigital);
    }

}
