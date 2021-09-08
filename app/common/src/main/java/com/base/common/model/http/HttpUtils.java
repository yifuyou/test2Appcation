package com.base.common.model.http;

import android.text.TextUtils;

import com.base.common.BuildConfig;
import com.base.common.app.LoginUtils;
import com.base.common.model.http.jackSon.HttpLog;
import com.base.common.model.http.jackSon.JacksonConverterFactoryCustomer;
import com.base.common.model.http.jackSon.JacksonUtils;
import com.base.common.model.http.upLoad.FileConverterFactory;
import com.base.common.utils.JavaMethod;
import com.base.common.utils.LogUtil;
import com.base.common.utils.MD5AndSHA;
import com.base.common.utils.UIUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;

/**
 * 网络工具类
 */

public class HttpUtils {

    private static HttpUtils instance;

    private Object mHostService;//主要使用的
    private Object mUpLoadFilesService;//文件上传

    private String baseUrl;

    private HttpUtils(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public static HttpUtils getInstance(String baseUrl) {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils(baseUrl);
                }
            }
        }
        return instance;
    }


    public <T> T getHttpServer(Class<T> t) {
        if (null == mHostService) {
            synchronized (HttpUtils.class) {
                if (null == mHostService) {
                    mHostService = getBuilder(baseUrl).build().create(t);
                }
            }
        }
        return (T) mHostService;
    }


    //文件上传的
    public <T> T getUpLoadFilesService(Class<T> t) {
        if (null == mUpLoadFilesService) {
            synchronized (HttpUtils.class) {
                if (null == mUpLoadFilesService) {
                    mUpLoadFilesService = getUPloadBuilder(baseUrl).build().create(t);
                }
            }
        }
        return (T) mUpLoadFilesService;
    }


    /**
     * 图片或文件上传
     *
     * @param apiUrl
     * @return
     */
    private Retrofit.Builder getUPloadBuilder(String apiUrl) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.MILLISECONDS)
                .readTimeout(60000, TimeUnit.MILLISECONDS)
                .writeTimeout(60000, TimeUnit.MILLISECONDS)
//                .addInterceptor(getInterceptor())
                .addNetworkInterceptor(getResponseIntercept())
                //校验SSL证书  SSLTrustAllUtil.createSSLSocketFactory()
//                .sslSocketFactory(SSLTrustAllUtil.createSSLSocketFactory())
//                .sslSocketFactory(SSLSocketFactoryUtils.getInstance().getSslSocketFactory())
                //校验主机名，用于域名验证
                //.hostnameVerifier(SSLSocketFactoryUtils.getInstance().getHostnameVerifier() )
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                })
                .protocols(Collections.singletonList(Protocol.HTTP_1_1));
//        if (!isTest) {
//            builder.proxy(Proxy.NO_PROXY);
//        }
        builder.build();

        Retrofit.Builder rebuilder = new Retrofit.Builder();
        rebuilder.client(builder.build());
        //设置远程地址
        rebuilder.baseUrl(apiUrl)
                .addConverterFactory(new FileConverterFactory())
//                .addConverterFactory(JacksonConverterFactory.create(JacksonUtils.getObjectMapper()))
                .addConverterFactory(JacksonConverterFactoryCustomer.create(JacksonUtils.getObjectMapper()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return rebuilder;
    }


    /**
     * 图片上传的
     *
     * @return
     */
    private HttpLoggingInterceptor getResponseIntercept() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtil.d("net 请求or响应", message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }


    /**
     * 主要网络请求的
     *
     * @param apiUrl
     * @return
     */
    private Retrofit.Builder getBuilder(String apiUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.client(getOkHttpClient());
        //设置远程地址
        builder.baseUrl(apiUrl)
//                .addConverterFactory(new NullOnEmptyConverterFactory())
//                .addConverterFactory(GsonConverterFactoryCustomer.create(getGson()))
//                .addConverterFactory(JacksonConverterFactory.create(JacksonUtils.getObjectMapper()))
                .addConverterFactory(JacksonConverterFactoryCustomer.create(JacksonUtils.getObjectMapper()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder;
    }


    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .addInterceptor(new HttpHeadInterceptor())
                .addInterceptor(getInterceptor())

                //校验SSL证书  SSLTrustAllUtil.createSSLSocketFactory()
                .sslSocketFactory(SSLTrustAllUtil.createSSLSocketFactory());
//        .sslSocketFactory(SSLSocketFactoryUtils.getInstance().getSslSocketFactory());
        //校验主机名，用于域名验证
        //.hostnameVerifier(SSLSocketFactoryUtils.getInstance().getHostnameVerifier() )

//        okBuilder.addNetworkInterceptor(new StethoInterceptor());
//        if (!isTest) {
//            okBuilder.proxy(Proxy.NO_PROXY);
//        }
//        okBuilder.addInterceptor(dataParseInterceptor());
        return okBuilder.build();

    }

    /**
     * 日志打印
     *
     * @return
     */
    private HttpLoggingInterceptor getInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLog());
        if (BuildConfig.DEBUG) {
            // 测试
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            // 打包
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return interceptor;
    }


    public static final Charset UTF_8 = Charset.forName("UTF-8");

    private class HttpHeadInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();

            //要添加的公共参数
            Map<String, String> paramsMap = new HashMap<>();
            //语言
            Locale locale = Locale.getDefault();
            String language = locale.toString();
            paramsMap.put("language", language);

            addParams(request, builder, paramsMap);


            //            builder.addHeader("Connection", "close");
//            builder.header("User-Agent", Build.VERSION.SDK_INT + ";" + Build.BRAND + " " + Build.MODEL + ";" + SysUtils.getVersionName());


            builder.header("Content-Type", "application/json;charset=UTF-8");
//            builder.addHeader("androidId", SystemUtils.getDeviceId());
            builder.addHeader("userId", String.valueOf(LoginUtils.getUserId()));
//            builder.header("channel", UIUtils.getApplicationMetaData(UIUtils.getContext(), "UMENG_CHANNEL"));
            if (TextUtils.isEmpty(request.header("Authorization"))) {
                builder.addHeader("Authorization", LoginUtils.getToken());
            }


//            String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//            String source = "fortune_app";
//            builder.addHeader("X-Requested-With", "XMLHttpRequest");
//            builder.addHeader("source", source);
//            builder.addHeader("timestamp", timestamp);
//
//            if (request.method().equals("POST")) {
//
//                if (request.body() instanceof FormBody) {
//                    FormBody body = (FormBody) request.body();
//                    if (body.size() > 0) {
//                        //添加sign
//                        LinkedHashMap<String, Object> params_new = new LinkedHashMap<>();
//                        for (int i = 0; i < body.size(); i++) {
//                            params_new.put(body.name(i), body.value(i));
//                        }
//                        addHeadSign(params_new, builder, source, timestamp);
//                    }
//                } else if (request.body() instanceof RequestBody) {
//                    RequestBody requestBody = (RequestBody) request.body();
//
//                    Buffer buffer = new Buffer();
//                    //编码设为UTF-8
//                    Charset charset = Charset.forName("UTF-8");
//                    MediaType contentType = requestBody.contentType();
//                    if (contentType != null) {
//                        charset = contentType.charset(charset);
//                    }
//
//                    requestBody.writeTo(buffer);
//                    String str = buffer.readString(charset);
//
//                    Map<String, Object> map = JsonUtils.transJson2Map(str);
//                    addHeadSign(map, builder, source, timestamp);
//                }
//            }


            return chain.proceed(builder.build());
        }

    }

    private OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 添加参数
     *
     * @param request        chain.request();
     * @param requestBuilder request.newBuilder()
     * @param paramsMap      要添加的公共参数
     * @throws IOException
     */
    private void addParams(Request request, Request.Builder requestBuilder, Map<String, String> paramsMap) throws IOException {
        if (request == null || UIUtils.isEmpty(paramsMap)) return;
//        Request.Builder requestBuilder = request.newBuilder();
        HttpUrl.Builder urlBuilder = request.url().newBuilder();


        // Get 请求时  将公共参数拼在后面
        if ("GET".equals(request.method())) {

            //这里可以添加一些公共get参数
            for (String s : paramsMap.keySet()) {
                urlBuilder.addEncodedQueryParameter(s, paramsMap.get(s));
            }

//            urlBuilder.addEncodedQueryParameter("timestamp", timestamp);
//            HttpUrl httpUrl = urlBuilder.build();
//            // 打印所有get参数
//            Set<String> paramKeys = httpUrl.queryParameterNames();
//            for (String key : paramKeys) {
//                String value = httpUrl.queryParameter(key);
//                paramsMap.put(key.toLowerCase(), value);
//                LogUtil.e("GET 参数  k_v:" + key + "=" + value);
//            }

            // 将最终的url填充到request中
            requestBuilder.url(urlBuilder.build());
            //request = request.newBuilder().url(httpUrl).build();
        } else if (request.method().equals("POST")) {
            //POST 请求
            // FormBody和url不太一样，若需添加公共参数，需要新建一个构造器
            FormBody.Builder bodyBuilder = new FormBody.Builder();


            // 把已有的post参数添加到新的构造器
            if (request.body() instanceof FormBody) {
                FormBody formBody = (FormBody) request.body();
                for (int i = 0; i < formBody.size(); i++) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                }

                LogUtil.d("POST 参数为 FormBody");
                // 这里可以添加一些公共post参数
                for (String s : paramsMap.keySet()) {
                    if (paramsMap.get(s) != null) {
                        bodyBuilder.addEncoded(s, paramsMap.get(s));
                    }
                }

                FormBody newBody = bodyBuilder.build();
                // 打印所有post参数
                for (int i = 0; i < newBody.size(); i++) {
                    LogUtil.d("POST 参数  k_v: " + newBody.name(i) + "=" + newBody.value(i));
                }
                // 将最终的表单body填充到request中
                requestBuilder.post(newBody);
            } else if (request.body() instanceof RequestBody) {
                RequestBody requestBody = (RequestBody) request.body();
                Buffer buffer = new Buffer();
                //编码设为UTF-8
                Charset charset = Charset.forName("UTF-8");
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(charset);
                }
                requestBody.writeTo(buffer);
                String str = buffer.readString(charset);
                LogUtil.d("POST 参数为 RequestBody");
                //json转map<String,User>
                JavaType jvt = JacksonUtils.getCollectionType(HashMap.class, String.class, Object.class);
                Map<String, Object> map = JacksonUtils.getObjectMapper().readValue(str, jvt);
                for (String s : paramsMap.keySet()) {
                    map.put("app_key", paramsMap.get(s));
                }
                requestBuilder.post(RequestBody.create(contentType, JacksonUtils.transMap2Json(map)));
            }
        }


    }


    private void addHeadSign(Map<String, Object> map, Request.Builder builder, String source, String timestamp) {
        StringBuilder stringBuffer = new StringBuilder();
        if (map != null && map.size() > 0) {
            //对map排序
            LinkedHashMap<String, Object> params = JavaMethod.sortMapByKey(map);
            for (Map.Entry<String, Object> stringStringEntry : params.entrySet()) {
                stringBuffer.append(stringStringEntry.getKey());
                stringBuffer.append("=");
                stringBuffer.append(stringStringEntry.getValue());
            }
        }


        stringBuffer.append(source);
        stringBuffer.append(timestamp);

        String sign = MD5AndSHA.md5Encode(stringBuffer.toString());

        builder.addHeader("sign", sign);

    }


}
