package com.yifuyou.common_http.convertor;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class JacksonConverterFactoryCustomer extends Converter.Factory {

    /**
     * Create an instance using a default {@link ObjectMapper} instance for conversion.
     */
    public static JacksonConverterFactoryCustomer create() {
        return create(new ObjectMapper());
    }

    /**
     * Create an instance using {@code mapper} for conversion.
     */
    public static JacksonConverterFactoryCustomer create(ObjectMapper mapper) {
        return new JacksonConverterFactoryCustomer(mapper);
    }

    private final ObjectMapper mapper;


    private JacksonConverterFactoryCustomer(ObjectMapper mapper) {
        if (mapper == null) throw new NullPointerException("mapper == null");
        this.mapper = mapper;
    }


    @Nullable
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        JavaType javaType=mapper.getTypeFactory().constructType(type);
        ObjectReader objectReader = mapper.readerFor(javaType);
        return new ResponseBodyConverter<>(objectReader,javaType);
    }

    @Nullable
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        JavaType javaType=mapper.getTypeFactory().constructType(type);
        ObjectWriter objectWriter = mapper.writerFor(javaType);

        return new RequestBodyConverter<>(objectWriter);
    }
}
