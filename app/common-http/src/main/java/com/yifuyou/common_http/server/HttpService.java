package com.yifuyou.common_http.server;

import com.yifuyou.common_http.common.CommonResult;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface HttpService {

    @GET("/")
    Observable<CommonResult<String>> get();

    @FormUrlEncoded
    @POST("/1oivpwb1")
    Observable<CommonResult<String>> post(@Field("data") String data);


}
