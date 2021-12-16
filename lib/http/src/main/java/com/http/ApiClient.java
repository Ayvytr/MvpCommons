package com.http;

import android.util.LruCache;

import com.ayvytr.okhttploginterceptor.LoggingInterceptor;

import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * Created by huqj on  2018/7/5.
 */
public class ApiClient {
    private Retrofit mRetrofit;
    private OkHttpClient mOkHttpClient;
    private static final int DEFAULT_TIMEOUT = 20;
    private static Interceptor mInterceptor;
    private static String mBaseUrl;
    private static SSLContext mSSLContext;
    private LruCache<Class, Object> mCache = new LruCache<>(10);

    private static X509TrustManager trustAllCert = new X509TrustManager() {
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
    };

    private ApiClient(){

    }

    public static ApiClient getInstance() {
        return SingletonHolder.sInstance;

    }

    private static class SingletonHolder {
        private static final ApiClient sInstance = new ApiClient();
    }

    public static void init(String baseUrl, Interceptor interceptor) {
        mBaseUrl = baseUrl;
        mInterceptor = interceptor;
        getInstance().clearHttp();
    }

    public static void init(String baseUrl, Interceptor interceptor, SSLContext sslContext) {
        mBaseUrl = baseUrl;
        mInterceptor = interceptor;
        mSSLContext = sslContext;
        getInstance().clearHttp();
    }


    private void clearHttp(){
        mRetrofit = null;
        mOkHttpClient = null;
    }

    public void initHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.sslSocketFactory(new MySSL(trustAllCert, mSSLContext), trustAllCert);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        builder.addInterceptor(mInterceptor)
                .addInterceptor(new LoggingInterceptor(BuildConfig.DEBUG));
        mOkHttpClient = builder.build();
    }


    public OkHttpClient getHttpClient() {
        if (mOkHttpClient == null) {
           initHttpClient();
        }
        return mOkHttpClient;
    }

    public Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .client(getHttpClient())
                    .addConverterFactory(ResponseConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(mBaseUrl)
                    .build();
        }
        return mRetrofit;
    }

    public <T> T create(final Class<T> server) {
        T t = null;
        try {
            t = (T) mCache.get(server);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (t == null) {
            Object object = getRetrofit().create(server);
            mCache.put(server, object);
            t = (T) object;
        }
        return t;
    }
}
