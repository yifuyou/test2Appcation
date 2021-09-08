package com.base.common.viewmodel;

import androidx.annotation.NonNull;

import com.base.common.netBeanPackage.BaseResponse;
import com.base.common.utils.JavaMethod;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;

public abstract class NetWorkFunction<T, R> implements Function<T, R> {

    private int code = -1;
    private String msg = "";

    @Override
    public R apply(@NonNull T t) throws Exception {

        if (t instanceof Map) {
            Map map = (Map) t;
            code = JavaMethod.transformClass(map.get(BaseResponse.codeString), int.class, -1);
            msg = JavaMethod.transformClass(map.get(BaseResponse.msgString), String.class);
        } else if (t instanceof BaseResponse) {
            code = ((BaseResponse) t).getCode();
            msg = ((BaseResponse) t).getMsg();
        } else {
            code = JavaMethod.getFieldValue(t, BaseResponse.codeString, int.class);
            msg = JavaMethod.getFieldValue(t, BaseResponse.msgString, String.class);
        }

        if (code == -1) {
            return onSuccess(t);
        } else if (BaseResponse.isSuccess(code)) {
            if (isEmptyData(t)) return onEmptyData(t);
            else return onSuccess(t);
        } else return onFailure(t);

    }


    public abstract R onSuccess(T t);

    public abstract R onFailure(T t);

    public R onEmptyData(T t) {
        return onFailure(t);
    }

    //判断结果是否为空
    public boolean isEmptyData(T data) {
        return false;
    }


    public <T> T setBeanCode(T t) {
        if (t == null || t instanceof Map) {
            if (t == null) t = (T) new HashMap();
            Map map = (Map) t;
            map.put(BaseResponse.codeString, code);
            map.put(BaseResponse.msgString, msg);
        } else if (t instanceof BaseResponse) {
            ((BaseResponse) t).setCode(code);
            ((BaseResponse) t).setMsg(msg);
        } else {
            JavaMethod.setFieldValue(t, BaseResponse.codeString, code);
            JavaMethod.setFieldValue(t, BaseResponse.msgString, msg);
        }
        return t;
    }

    public <T> Observable<T> getFailureObservable(T t) {
        T finalT = setBeanCode(t);
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                emitter.onNext(finalT);
                emitter.onComplete();
            }
        });

    }


    public <T> Observable<T> getObservable(T t) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                emitter.onNext(t);
                emitter.onComplete();
            }
        });
    }

}
