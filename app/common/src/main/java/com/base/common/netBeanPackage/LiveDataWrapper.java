package com.base.common.netBeanPackage;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.base.common.model.bean.Status;
import com.base.common.model.http.err.ApiException;


/**
 * 数据包裹
 */


public class LiveDataWrapper<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final ApiException error;


    private int count = 1;//是多少个请求一起

    private int current = 1;//当前是这一组请求中的第几个请求

    private LiveDataWrapper(@NonNull Status status, @Nullable T data, @Nullable ApiException error, int count, int current) {
        this.status = status;
        this.data = data;
        this.error = error;
        this.count = count;
        this.current = current;
    }


    public static <T> LiveDataWrapper<T> success(@NonNull T data, int count, int current) {
        return new LiveDataWrapper<>(Status.SUCCESS, data, null, count, current);
    }

    public static <T> LiveDataWrapper<T> error(@NonNull ApiException error, @Nullable T data, int count, int current) {
        return new LiveDataWrapper<>(Status.ERROR, data, error, count, current);
    }

    public static <T> LiveDataWrapper<T> appError(@NonNull ApiException error, @Nullable T data, int count, int current) {
        return new LiveDataWrapper<>(Status.APP_ERROR, data, error, count, current);
    }

    public static <T> LiveDataWrapper<T> loading(@Nullable T data) {
        return new LiveDataWrapper<>(Status.LOADING, data, null, 1, 1);
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }


    //    public static <T> LiveDataWrapper<T> onComplete() {
//        return new LiveDataWrapper<>(Status.COMPLETE, null, null);
//    }

}
