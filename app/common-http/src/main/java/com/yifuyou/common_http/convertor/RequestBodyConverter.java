package com.yifuyou.common_http.convertor;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

public class RequestBodyConverter<T> implements Converter<T,RequestBody> {
    private final static MediaType MEDIA_TYPE= okhttp3.MediaType.parse("application/json; charset=UTF-8");
    ObjectWriter writer;

    RequestBodyConverter(ObjectWriter writer){
        this.writer=writer;
    }

    @Nullable
    @Override
    public RequestBody convert(T value) throws IOException {

        byte[] bytes = writer.writeValueAsBytes(value);
        return RequestBody.create(MEDIA_TYPE,bytes);
    }
}
