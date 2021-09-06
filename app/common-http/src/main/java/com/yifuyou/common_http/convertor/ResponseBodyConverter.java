package com.yifuyou.common_http.convertor;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectReader;
import com.yifuyou.common_http.util.LogUtil;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class ResponseBodyConverter<T> implements Converter<ResponseBody,T> {
    private ObjectReader reader;
    private Type type;

    ResponseBodyConverter(ObjectReader reader,Type type){
        this.reader=reader;
        this.type=type;
    }



    @Nullable
    @Override
    public T convert(ResponseBody value) throws IOException {
        String ss = value.source().readUtf8();
        try {
            return reader.readValue(ss);
        }catch (Exception e){
            try{

                LogUtil.e("ResponseBodyConverter","(尝试强制转换为 String)",e);
                T result = (T)ss;
                LogUtil.e("ResponseBodyConverter","(尝试强制成功)",e);
                return result;
            }catch (Exception ex){
                LogUtil.e("ResponseBodyConverter","(强制转换 -String 失败)",e);
            }

            LogUtil.e("ResponseBodyConverter",e.getMessage(),e);

        }
        return null;
    }
}
