package com.base.common.viewmodel;

import com.base.common.model.bean.xml.xmlBaseResponse.RoleInfoResponseEnvelope;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.DisposableSubscriber;

/**
 * 网络回调做一些基本处理
 */

public abstract class BaseSubscriberXML<T extends RoleInfoResponseEnvelope> extends DisposableSubscriber<T> implements Observer<T> {

    private int tag;
//    private RoleInfoResponseEnvelope envelope;

    public BaseSubscriberXML(int tag) {
//        this.envelope = envelope;
        this.tag = tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    /**
     * 在正式发起请求前或请求中将状态设置为loading
     */
    @Override
    protected void onStart() {
        super.onStart();
//        RxBus.getInstance().post(tag, LiveDataWrapper.loading(null));
    }

    /**
     * 对服务端返回的数据做一些状态处理
     *
     * @param o
     */
    @Override
    public void onNext(T o) {
        if (null != o) {
            if ("0".equals(o.body.roleInfoResponse.Etrack_ProcInterfaceResult)) {
//                RxBus.getInstance().post(tag, LiveDataWrapper.success(o));
                return;
            }
//            RxBus.getInstance().post(tag, LiveDataWrapper.error(new ApiException(new Throwable(o.body.roleInfoResponse.retXml),0), null));
        }
    }

    @Override
    public void onComplete() {
//        RxBus.getInstance().post(tag, LiveDataWrapper.error(new Throwable(), null));
    }

    /**
     * 异常处理
     *
     * @param t
     */
    @Override
    public void onError(Throwable t) {
        if (null != t)
            t.printStackTrace();
//        ApiException apiException = ApiException.handleException(t);
//        RxBus.getInstance().post(tag, LiveDataWrapper.error(apiException, null));
//        if (apiException.getCode() == 401)
//            LiveEventBus.get(BaseConstant.LOG_IN).setValue(LiveDataWrapper.error(apiException, null));
    }

    @Override
    public void onSubscribe(Disposable d) {
    }
}
