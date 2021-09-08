package com.base.common.viewmodel;


import androidx.lifecycle.MutableLiveData;

import com.base.common.app.LoginUtils;
import com.base.common.model.http.err.ApiException;
import com.base.common.model.http.err.ERROR;
import com.base.common.model.http.jackSon.JacksonUtils;
import com.base.common.netBeanPackage.BaseResponse;
import com.base.common.netBeanPackage.LiveDataWrapper;
import com.base.common.utils.JavaMethod;
import com.base.common.utils.LogUtil;

import java.util.Map;

import io.reactivex.observers.DisposableObserver;

/**
 * 网络回调做一些基本处理
 * <p>
 * 如果存在外部错误会回调{@link #onError(Throwable)}  不会再回调{@link #onComplete()}
 * 但app内部错误会回调  {@link #onComplete()}
 */

public class NetWorkObserver<T> extends DisposableObserver<T> implements ERROR {

    protected MutableLiveData<LiveDataWrapper<T>> mLiveDataWrapper;

    private int count = 1;//是多少个请求一起
    private int current = 0;//当前是这一组请求中的第几个请求

    private String url = "";


    public NetWorkObserver(String url, int count) {
        this.mLiveDataWrapper = new MutableLiveData<>();
        this.count = count;
        this.url = url;
    }


    public MutableLiveData<LiveDataWrapper<T>> getWrapper() {
        return mLiveDataWrapper;
    }


    /**
     * 在正式发起请求前或请求中将状态设置为loading
     */
    @Override
    protected void onStart() {
        super.onStart();
        mLiveDataWrapper.setValue(LiveDataWrapper.loading((T) null));
    }


    /**
     * 对服务端返回的数据做一些状态处理
     *
     * @param o
     */
    @Override
    public void onNext(T o) {
        current++;// 外部错误时会中断请求队列
        int code = -1;
        String msg = "";
        if (o instanceof Map) {
            Map map = (Map) o;
            code = JavaMethod.transformClass(map.get(BaseResponse.codeString), int.class, -1);

            if (code == -1) {
                msg = JacksonUtils.transMap2Json(map);
            } else msg = JavaMethod.transformClass(map.get(BaseResponse.msgString), String.class);

        } else if (o instanceof BaseResponse) {
            code = ((BaseResponse) o).getCode();
            if (code == -1) {
                msg = JacksonUtils.transBean2Json(o);
            } else msg = ((BaseResponse) o).getMsg();
        } else if (null != o) {
            code = JavaMethod.getFieldValue(o, BaseResponse.codeString, int.class);
            msg = JavaMethod.getFieldValue(o, BaseResponse.msgString, String.class);
        }

        if (BaseResponse.isSuccess(code)) {
            onSuccess(o);
        } else {
            //内部错误时
            onAppErr(code, msg);
        }
    }


    /**
     * app内部错误
     */
    private void onAppErr(int code, String errMsg) {
        ApiException apiException = new ApiException(new Throwable(errMsg), code);
        //对状态码进行转码  内部错误码和外部错误码重合了
        switch (code) {
            case 401:
                LoginUtils.loginOut();
                apiException.setCode(ERROR.LOGIN_OUT);
                break;
            case 399:
                apiException.setCode(ERROR.INSUFFICIENT_BALANCE);
                break;
            default:
                apiException.setCode(ERROR.PARMSERR);
                break;
        }
        apiException.setUrl(url);
        mLiveDataWrapper.setValue(LiveDataWrapper.appError(apiException, (T) null, count, current));
    }

    /**
     * 外部错误   异常处理
     *
     * @param t
     */
    @Override
    public void onError(Throwable t) {
        current++;// 外部错误时会中断请求队列
        if (null != t) {
            LogUtil.e(t.getMessage());
            t.printStackTrace();
        }

        mLiveDataWrapper.setValue(LiveDataWrapper.error(ApiException.handleException(t), (T) null, count, current));
    }


    //成功后回调
    public void onSuccess(T bean) {
        mLiveDataWrapper.setValue(LiveDataWrapper.success(bean, count, current));
    }


    /**
     * 所有请求都成功返回时回调,出现内部错误时也会回调
     */
    @Override
    public void onComplete() {
//        mLiveDataWrapper.setValue(LiveDataWrapper.onComplete());
        LogUtil.d(url + " dispose()");
        dispose();
    }


}
