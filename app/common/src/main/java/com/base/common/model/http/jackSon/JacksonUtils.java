package com.base.common.model.http.jackSon;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.base.common.utils.JavaMethod;
import com.base.common.utils.UIUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonStreamContext;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yangkuo on 2018-09-17.
 */
public class JacksonUtils {
    private static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            //设置找不到的属性则不序列化
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            //通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
            //Include.Include.ALWAYS 默认
            //Include.NON_DEFAULT 属性为默认值不序列化
            //Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化
            //Include.NON_NULL 属性为NULL 不序列化
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);



            SimpleModule module = new SimpleModule();

            module.addDeserializer(String.class, new CustomStringDeserializer());
            module.addDeserializer(Integer.class, new CustomIntegerDeserializer());
            module.addDeserializer(int.class, new CustomIntegerDeserializer());
            module.addDeserializer(Long.class, new CustomLongDeserializer());
            module.addDeserializer(long.class, new CustomLongDeserializer());
            module.addDeserializer(Float.class, new CustomFloatDeserializer());
            module.addDeserializer(Double.class, new CustomDoubleDeserializer());
            module.addDeserializer(BigDecimal.class, new CustomBigDecimalDeserializer());
            module.addDeserializer(Boolean.class, new CustomBooleanDeserializer());

            objectMapper.registerModule(module);

        }

        return objectMapper;
    }

    /**
     * json string 转换为 map 对象
     *
     * @param map
     * @return
     */
    public static String transMap2Json(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject(map);
        return jsonObject.toString();
    }


    public static String transBean2Json(Object obj) {
        if (obj == null) return null;
        try {
            return JacksonUtils.getObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将json转换为Javabean
     *
     * @param jsonString
     * @param cls
     * @return javaBean对象
     */
    public static <T> T getJsonBean(String jsonString, Class<T> cls) {
        if (TextUtils.isEmpty(jsonString)) return null;
        try {
            return JacksonUtils.getObjectMapper().readValue(jsonString, cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> getJsonBean(String jsonString, Class<?> classType, Class<?>... parameterClasses) {
        JavaType javaType = getCollectionType(classType, parameterClasses);
        return getJsonBean(jsonString, javaType);
    }


    public static <T> List<T> getConverList(String josn, Class<T> clz) {
        if (josn == null) josn = "";
        JavaType javaType = JacksonUtils.getObjectMapper().getTypeFactory().constructParametricType(List.class, clz);// clz.selGenType().getClass()
        return getJsonBean(josn, javaType);
    }

    private static <T> List<T> getJsonBean(String jsonString, JavaType cls) {
        if (jsonString == null) jsonString = "";
        try {
            return JacksonUtils.getObjectMapper().readValue(jsonString, cls);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return JacksonUtils.getObjectMapper().getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * @param map 用object 解析后对像是map
     * @param <T>
     */
    public static <T> T transformMap2Bean(SimpleType simpleType, Map map) {
        if (simpleType == null || map == null) return null;

        Class clsBase = simpleType.getRawClass();
        Field[] fields = clsBase.getDeclaredFields();
        try {
            Object bean = clsBase.newInstance();

            for (Field field : fields) {
                field.setAccessible(true);

                Class<?> cls = field.getType();
                Type type = field.getGenericType();
                Object obj = map.get(field.getName());

                //如果是泛型
                if (type instanceof TypeVariable) {
                    if (simpleType.containedTypeCount() > 0) {
                        if (obj instanceof Map) {
                            Object value = transformMap2Bean((SimpleType) simpleType.containedType(0), (Map) obj);
                            field.set(bean, value);
                        } else {
                            field.set(bean, JavaMethod.transformClass(obj, cls));
                        }
                    }
                }

                //ParameterizedType参数化类型，即泛型  如List<T>
                else if (type instanceof ParameterizedType) {
                    Type[] params = ((ParameterizedType) type).getActualTypeArguments();
                    if (params.length > 0) {
                        //如是list
                        if (List.class == cls || ArrayList.class == cls) {

                            //如果bean是list map中的数据也必须是list,否则置空
                            if (obj instanceof List) {
                                List lls = new ArrayList();
                                field.set(bean, lls);

                                List lllsMap = (List) obj;

                                //如果是泛型且已指定泛型的类型
                                if (params[0] instanceof TypeVariable && simpleType.containedTypeCount() > 0) {
                                    Class css = simpleType.containedType(0).getRawClass();
                                    //如果是基本类型或基本类型的封装对像
                                    if (JavaMethod.isPrimitiveOrObject(css)) {
                                        for (Object o : lllsMap) {
                                            lls.add(JavaMethod.transformClass(o, css));
                                        }
                                    } else {
                                        for (Object o : lllsMap) {
                                            if (o instanceof Map) {
                                                Object value = transformMap2Bean((SimpleType) simpleType.containedType(0), (Map) o);
                                                lls.add(value);
                                            }
                                        }
                                    }
                                }
                                //如果是已知对像
                                else {
                                    if (params[0] instanceof Class) {
                                        Class css = (Class) params[0];
                                        if (JavaMethod.isPrimitiveOrObject(css)) {
                                            for (Object o : lllsMap) {
                                                lls.add(JavaMethod.transformClass(o, css));
                                            }
                                        } else {
                                            for (Object o : lllsMap) {
                                                if (o instanceof Map) {
                                                    SimpleType simpleType1 = (SimpleType) TypeFactory.defaultInstance().constructType(css);
                                                    Object value = transformMap2Bean((SimpleType) TypeFactory.defaultInstance().constructType(css), (Map) o);
                                                    lls.add(value);
                                                }
                                            }
                                        }
                                    }
                                }


                            } else {
                                field.set(bean, new ArrayList<>());
                            }


                        }
                        //如果是泛型对像
                        else {

                            //如果是泛型且已指定泛型的类型且 解析的数值是map
                            if (params[0] instanceof TypeVariable && simpleType.containedTypeCount() > 0 && obj instanceof Map) {
                                Class css = simpleType.containedType(0).getRawClass();
                                Object value = transformMap2Bean((SimpleType) JacksonUtils.getCollectionType(cls, css), (Map) obj);
                                field.set(bean, value);
                            }
                            //是泛型对像 值不是map  直接置空
                            else {
                                field.set(bean, null);
                            }


                        }
                    }

                }

                //如果是基本类型或基本类型的封装对像
                else if (JavaMethod.isPrimitiveOrObject(cls)) {
                    field.set(bean, JavaMethod.transformClass(obj, cls));
                }
                //如果是对像
                else {
                    if (obj instanceof Map) {
                        Object value = transformMap2Bean((SimpleType) TypeFactory.defaultInstance().constructType(type), (Map) obj);
                        field.set(bean, value);
                    } else {
                        field.set(bean, JavaMethod.transformClass(obj, cls));
                    }
                }

            }

            return (T) bean;
        } catch (IllegalAccessException | InstantiationException e) {
            // e.printStackTrace();
        }


        return null;
    }


    private static class CustomStringDeserializer extends JsonDeserializer<String> {

        @Override
        public String deserialize(JsonParser jsonparser, DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
            return jsonparser.getText();
        }

        @Override
        public String getNullValue() {
            return "";
        }

    }

    private static class CustomIntegerDeserializer extends JsonDeserializer<Integer> {


        @Override
        public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
                String ss = p.getText();
                if (ss == null || ss.length() == 0 || ss.equals("-")|| ss.equals("NaN")) {
                    return 0;
                } else {
                    return JavaMethod.getInt(ss, 0);
//                    return new BigDecimal(ss).intValue();
                }

            }
            return p.getIntValue();
        }

        @Override
        public Integer getNullValue() {
            return 0;
        }

    }

    private static class CustomLongDeserializer extends JsonDeserializer<Long> {

        @Override
        public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
                String ss = p.getText();
                if (ss == null || ss.length() == 0 || ss.equals("-") || ss.equals("NaN")) {
                    return 0L;
                } else {
                    return new BigDecimal(ss).longValue();
                }

            }
            return p.getLongValue();
        }

        @Override
        public Long getNullValue() {
            return 0L;
        }
    }

    private static class CustomFloatDeserializer extends JsonDeserializer<Float> {


        @Override
        public Float deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
                String ss = p.getText();
                if (ss == null || ss.length() == 0 || ss.equals("-") || ss.equals("NaN")) {
                    return 0f;
                } else {
                    return new BigDecimal(ss).floatValue();
                }

            }
            return p.getFloatValue();
        }

        @Override
        public Float getNullValue() {
            return 0f;
        }
    }

    private static class CustomDoubleDeserializer extends JsonDeserializer<Double> {


        @Override
        public Double deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
                String ss = p.getText();
                if (ss == null || ss.length() == 0 || ss.equals("-") || ss.equals("NaN")) {
                    return 0d;
                } else {
                    return new BigDecimal(ss).doubleValue();
                }

            }
            return p.getDoubleValue();
        }

        @Override
        public Double getNullValue() {
            return 0d;
        }
    }

    private static class CustomBigDecimalDeserializer extends JsonDeserializer<BigDecimal> {


        @Override
        public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            BigDecimal bigDecimal;
            if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
                String ss = p.getText();
                if (ss == null || ss.length() == 0 || ss.equals("-") || ss.equals("NaN")) {
                    ss = "0";
                }

                bigDecimal = new BigDecimal(ss);
                if (bigDecimal.equals(BigDecimal.ZERO)) {
                    return BigDecimal.ZERO;
                }
            } else bigDecimal = p.getDecimalValue();

//            int index = bigDecimal.toString().indexOf(".") + 1;
//            int size = bigDecimal.toString().length();
//
//            if (index > 0 && size - index > 2) {
//                return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
//            }
            return bigDecimal;

        }

        @Override
        public BigDecimal getNullValue() {
            return BigDecimal.ZERO;
        }

    }

    private static class CustomBooleanDeserializer extends JsonDeserializer<Boolean> {


        @Override
        public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            if (p.getCurrentToken() == JsonToken.VALUE_STRING) {
                return Boolean.valueOf(p.getText());
            }
            return p.getBooleanValue();
        }

        @Override
        public Boolean getNullValue() {
            return false;
        }
    }

}
