package com.yifuyou.common_http;

import com.yifuyou.common_http.proxy.HttpProxy;
import com.yifuyou.common_http.server.HttpService;
import com.yifuyou.common_http.util.LogUtil;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class RequestAPI {
    private static HttpService service;

    private static RequestAPI api;

    private RequestAPI(){}

    public static void init(){
         service = HttpProxy.getHttpService(HttpService.class);
         api=new RequestAPI();
    }

    public static void request(String url, CallBack callBack ,String obj){
        api.inNewThread(()->{

            Observable observable = service.post(obj);
            observable.subscribe(new Observer() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }
                @Override
                public void onNext(@androidx.annotation.NonNull Object o) {
                    LogUtil.i("request-"+url,"request success, return"+o.toString() );
                    callBack.onSuccess(o);
                }
                @Override
                public void onError(@NonNull Throwable e) {
                    LogUtil.i("request-"+url,"request onError!!!!!");
                    callBack.onFail();
                }

                @Override
                public void onComplete() {

                }
            });
            observable.subscribe().dispose();



        });


    }




    private void inNewThread(Runnable runnable){
        new Thread(runnable).start();
    }



    public interface CallBack{
        void onSuccess(Object ss);
        void onFail();
    }


}
