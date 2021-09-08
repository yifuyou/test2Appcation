package com.base.common.model.http.jackSon;


import com.base.common.netBeanPackage.BaseResponse;
import com.base.common.utils.JavaMethod;
import com.base.common.utils.JsonUtils;
import com.base.common.utils.LogUtil;
import com.base.common.utils.UIUtils;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.type.SimpleType;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by yangkuo on 2018-09-29.
 */
public class JacksonResponseBodyConverterCustomer<T> implements Converter<ResponseBody, T> {
    private final ObjectReader adapter;
    private Type type;

    JacksonResponseBodyConverterCustomer(ObjectReader adapter, Type type) {
        this.adapter = adapter;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        // {"errno":1, "data":"Required Long parameter 'id' is not present", "errmsg":"处理异常" }
        String ss = value.source().readUtf8();
        int code = 0;
        String msg = "";
        try {
            return adapter.readValue(ss);
        } catch (JsonMappingException e) {
            LogUtil.e(e.getMessage());
            LogUtil.e("解析错误，正在使用自定义解析.....");
            SimpleType simpleType = JavaMethod.getFieldValue(adapter, "_valueType", SimpleType.class);
            try {
                Object obj = JacksonUtils.getJsonBean(ss, Object.class);
                T bean =  JsonUtils.transformMap2Bean(type, (Map) obj);
//                T bean = JacksonUtils.transformMap2Bean(simpleType, (Map) obj);
                LogUtil.e("自定义解析成功!");
                return bean;
            } catch (Exception e1) {
                LogUtil.e("自定义解析失败");
                Class clsBase = simpleType.getRawClass();
                try {
                    Object bean = clsBase.newInstance();

                    code = 301;
                    msg = "解析错误";
                    if (bean instanceof Map) {
                        Map map = (Map) bean;
                        map.put(BaseResponse.codeString, code);
                        map.put(BaseResponse.msgString, msg);
                    } else if (JavaMethod.isObject(clsBase)) {
                        JavaMethod.setFieldValue(bean, BaseResponse.codeString, code);
                        JavaMethod.setFieldValue(bean, BaseResponse.msgString, msg);
                    }


                    return (T) bean;
                } catch (IllegalAccessException | InstantiationException ex) {
                    ex.printStackTrace();
                }

                return null;
            }

        } finally {
            value.close();
        }
    }

}
