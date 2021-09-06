package com.yifuyou.common_http.proxy;

import com.yifuyou.common_http.invocationhandler.HttpInvocationHandler;
import com.yifuyou.common_http.server.HttpService;

import java.lang.reflect.Proxy;

public class HttpProxy<T>  {

    public static <T>T getHttpService(Class<T> cls) {
        return (T) Proxy.newProxyInstance(cls.getClassLoader(),new Class[]{HttpService.class},new HttpInvocationHandler(cls));

    }

}
