package com.base.common.model.http;


import androidx.collection.SparseArrayCompat;

import com.base.common.app.BaseApp;
import com.base.common.utils.LogUtil;
import com.base.common.viewmodel.NetWorkObserver;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;


/**
 * 被注解{@link Cancel}标记的方法，请求时，会取消上一次未返回的请求
 * 只有加了 {@link Cancel} 标记的请求才会，检查并缓存
 */

public class HttpServiceProxy<T> implements InvocationHandler {

    /**
     * Map<String, Object>   key  方法名   value   Observable
     */
    private static Map<String, Object> map = new HashMap<>();

    /**
     * 缓存   在缓存之前会检查之前的请求，如果返回则替换为现在的。没返回则取消订阅后再替换
     * key  Observable 的hashCode()
     */
    private static SparseArrayCompat<NetWorkObserver> netWorkObserverArray = new SparseArrayCompat<>();


    /**
     * 缓存
     *
     * @param observable
     * @param netWorkObserver
     * @param <T>
     */
    public static <T> void putNetWorkObserver(Observable<T> observable, NetWorkObserver<T> netWorkObserver) {
        if (observable != null && netWorkObserver != null) {
            if (netWorkObserverArray.containsKey(observable.hashCode())) {
                netWorkObserverArray.put(observable.hashCode(), netWorkObserver);
            }
        }
    }


    public static <T> T getHttpService(Class<T> service) {
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new HttpServiceProxy(service));
    }


    private Class<T> service;


    public HttpServiceProxy(Class<T> service) {
        this.service = service;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object obj = method.invoke(HttpUtils.getInstance(BaseApp.getApplication().getBaseUrl()).getHttpServer(service), args);

        if (method.isAnnotationPresent(Cancel.class)) {
            String sstr = method.toString();
            Object observable = map.get(sstr);
            //如果不为空
            if (observable != null) {
                NetWorkObserver netWorkObserver = netWorkObserverArray.get(observable.hashCode(), null);
                //如果不为空且未返回,取消订阅
                if (netWorkObserver != null && !netWorkObserver.isDisposed()) {
                    LogUtil.d(sstr + "  " + Arrays.toString(args) + "    取消请求");
                    netWorkObserver.dispose();
                }
            }

            //缓存新的
            map.put(sstr, obj);
            netWorkObserverArray.put(obj.hashCode(), null);

        }
        return obj;
    }


}