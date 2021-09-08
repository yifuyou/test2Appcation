package com.base.common.model.http;

import android.content.Context;
import android.util.Log;


import com.base.common.utils.UIUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by JC on 2020-03-13.
 * 使用https请求时  需要验证证书时使用 此方法 获取 sslSocketFactory
 * 服务器 返回的证书 只有一个 时 可以用这个类
 */
public class SSLSocketFactoryUtils {

    private static final String HOST_NAME = "xxxxxx";// 请求服务器的主机名称

    private static String CERTIFICATE_NAME = "wuliang.cer";// 下载的证书的文件名称
    // 证书

    private X509Certificate x509Certificate;

    // 需要配置给okhttp的SSLSocketFactory
    private SSLSocketFactory mSslSocketFactory;

    private SSLContext sslContext;

    // 证书管理者
    private MyTrustManager mTrustManager;

    private static SSLSocketFactoryUtils instance;

    private SSLSocketFactoryUtils() {
        initSslSocketFactory();
    }

    private void initSslSocketFactory() {
        try {
            sslContext = SSLContext.getInstance("TLS");
            // 从assets文件夹下根据证书名字读取证书,变成一个可用的证书对象
            //ContextUtils.getContext()
            x509Certificate = readCert(UIUtils.getContext(), CERTIFICATE_NAME);
            // 校验服务端和本地证书是否一致
            mTrustManager = new MyTrustManager(x509Certificate);
            // 初始化必要的对象,固定格式直接使用即可
            sslContext.init(null, new TrustManager[]{mTrustManager}, new java.security.SecureRandom());
            mSslSocketFactory = sslContext.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SSLSocketFactoryUtils getInstance() {
        if (instance == null) {
            instance = new SSLSocketFactoryUtils();
        }
        return instance;
    }

    /**
     * 根据asset下证书的名字取出证书,然后变成流,在变成证书对象
     */
    private static X509Certificate readCert(Context context, String assetName) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(assetName);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        X509Certificate cert = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) cf.generateCertificate(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Throwable ex) {
            }
        }
        return cert;
    }

    public HostnameVerifier getHostnameVerifier() {
        return hostnameVerifier;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return mSslSocketFactory;
    }

    public MyTrustManager getTrustManager() {
        return mTrustManager;
    }

    /**
     * 实现了 X509TrustManager
     * 通过此类中的 checkServerTrusted 方法来确认服务器证书是否正确
     */
    private static final class MyTrustManager implements X509TrustManager {
        X509Certificate cert;

        MyTrustManager(X509Certificate cert) {
            this.cert = cert;
        }

        @Override // 我们在客户端只做服务器端证书校验。
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        /**
         * @param chain 服务端返回的证书数组,因为服务器可能有多个https证书,我们在这里的
         *              逻辑就是拿到第一个证书,然后和本地证书判断,如果不一致,异常!!!
         */
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // 确认服务器端证书和代码中 hard code 的 CRT 证书相同。
            // 这里因为我们服务器只有一个证书,没有遍历,如果有多个,这里是for循环取出挨个判断
            if (chain[0].equals(this.cert)) {
                return;
            }
            throw new CertificateException("checkServerTrusted No trusted server cert found!");
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    /**
     * 服务器域名验证,拿到请求接口的域名和本地配的域名进行比较,如果一样返回True
     */
    private final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {

            // hostname:本次请求的域名，如果域名是我们认可的域名，那么返回true，
            // 本次请求继续执行，返回false会中断本次请求
            // return hostname.equals(HOST_NAME);

            Certificate[] localCertificates = new Certificate[0];
            try {
                // 获取证书链中的所有证书
                localCertificates = session.getPeerCertificates();
            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            }
            // 打印所有证书内容
            for (Certificate c : localCertificates) {
                Log.d("OkHttp", "verify: " + c.toString());
            }
            // 如果不进行校验，直接return true，代表信任所有请求
            return true;
        }
    };

}
