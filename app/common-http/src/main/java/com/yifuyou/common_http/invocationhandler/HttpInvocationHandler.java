package com.yifuyou.common_http.invocationhandler;

import com.yifuyou.common_http.common.CommonString;
import com.yifuyou.common_http.convertor.JacksonConverterFactoryCustomer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.Duration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;

public class HttpInvocationHandler<T> implements InvocationHandler {
    private  T instance;
    public HttpInvocationHandler(Class<T> cls){
        OkHttpClient client=new OkHttpClient.Builder()
                .callTimeout(Duration.ofMillis(6000L))
                .build();


        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(CommonString.TEST_URL)
                .client(client)
                .addConverterFactory(JacksonConverterFactoryCustomer.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();
        instance = retrofit.create(cls);
    }


    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {

            return method.invoke(instance,objects);


    }
}
