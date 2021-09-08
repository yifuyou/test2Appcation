package com.base.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtils {


    /**
     * @param type 要么是类型(BaseResponse)，要么是泛型(List<String>)
     * @param map
     * @param <T>
     * @return
     */
    public static <T> T transformMap2Bean(Type type, Object map) {
        if (type == null || map == null) return null;
        //ParameterizedType参数化类型，即泛型  如List<T>
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            return parseParameterizedType(paramType, map);
        } else if (type instanceof Class) {
            if (map instanceof Map) {
                return parseMap2Bean((Class<T>) type, (Map) map);
            }

        }
        return null;
    }


    /**
     * 解析map到bean
     *
     * @param clsBase
     * @param map
     * @param params  泛型的参数类型
     * @param <T>
     * @return
     */
    private static <T> T parseMap2Bean(Class<T> clsBase, Map map, Type... params) {
        if (clsBase == null || map == null) return null;
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
                    if (params.length > 0) {
                        //如果泛型的参数是参数化类型
                        if (params[0] instanceof ParameterizedType) {
                            Object values = parseParameterizedType((ParameterizedType) params[0], obj);
                            field.set(bean, values);
                        }
                        //如果是对像
                        else if (params[0] instanceof Class) {
                            Class css = (Class) params[0];
                            //如果是基本类型
                            if (JavaMethod.isPrimitiveOrObject(css)) {
                                field.set(bean, JavaMethod.transformClass(obj, cls));
                            } else {
                                //如果是对像
                                if (obj instanceof Map) {
                                    Object values = parseMap2Bean(css, (Map) obj);
                                    field.set(bean, values);
                                }
                            }
                        }
                    }
                }
                //ParameterizedType参数化类型，即泛型  如List<T>
                else if (type instanceof ParameterizedType) {
                    ParameterizedType paramType = (ParameterizedType) type;
                    Object values = parseParameterizedType(paramType, obj);
                    field.set(bean, values);
                } else if (type instanceof Class) {
                    Class css = (Class) type;
                    //如果是基本类型
                    if (JavaMethod.isPrimitiveOrObject(css)) {
                        field.set(bean, JavaMethod.transformClass(obj, cls));
                    } else {
                        //如果是对像
                        if (obj instanceof Map) {
                            Object values = parseMap2Bean(css, (Map) obj);
                            field.set(bean, values);
                        }
                    }
                }
            }
            return (T) bean;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    //解析泛型参数,类型参数来自  参数化类型的getActualTypeArguments（）
    private static <T> T parseParameterizedType(ParameterizedType paramType, Object values) {
        if (paramType == null || values == null) return null;
        //当前的类型
        Type rawType = paramType.getRawType();
        Type[] params = paramType.getActualTypeArguments();
        if (rawType instanceof List) {
            List lls = new ArrayList();

            if (values instanceof List) {
                List lllsMap = (List) values;

                //如果参数化类型的参数是参数化类型
                if (params[0] instanceof ParameterizedType) {
                    for (Object o : lllsMap) {
                        Object value = parseParameterizedType((ParameterizedType) params[0], o);
                        lllsMap.add(value);
                    }
                }
                //如果参数化类型的参数是 对像
                else if (params[0] instanceof Class) {
                    Class css = (Class) params[0];
                    //如果是基本类型或基本类型的封装对像
                    if (JavaMethod.isPrimitiveOrObject(css)) {
                        for (Object o : lllsMap) {
                            lls.add(JavaMethod.transformClass(o, css));
                        }
                    } else {
                        for (Object o : lllsMap) {
                            if (o instanceof Map) {
                                Object value = parseMap2Bean(css, (Map) o);
                                lls.add(value);
                            }
                        }
                    }
                }
            }

            return (T) lls;
        } else {
            //如果json数据是  map
            if (values instanceof Map) {
                return parseMap2Bean((Class<T>) rawType, (Map) values, params);
            }
        }
        return null;
    }


}
