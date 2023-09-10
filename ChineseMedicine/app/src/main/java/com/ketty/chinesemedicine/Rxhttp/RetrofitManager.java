package com.ketty.chinesemedicine.Rxhttp;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.ketty.chinesemedicine.BuildConfig;
import com.ketty.chinesemedicine.util.HttpUrlPath;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 新版封装-网络框架
 */
public class RetrofitManager {
    /**
     * 连接超时
     */
    private static final int CONNECT_TIMEOUT = 15 * 1000;
    /**
     * 读取超时
     */
    private static final int READ_TIMEOUT = 15 * 1000;
    /**
     * 写入超时
     */
    private static final int WRITE_TIMEOUT = 15 * 1000;

    private static final String TAG = "Retrofit-Interceptor";

    private static RetrofitManager retrofitManager;
    private ApiService apiService;
    private OkHttpClient okHttpClient;

    //单例调用
    public static RetrofitManager getInstance() {
        if (retrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if (retrofitManager == null) {
                    retrofitManager = new RetrofitManager();
                }
            }
        }
        return retrofitManager;
    }

    private OkHttpClient getOkHttpClient() {
        return okHttpClient == null ? initClient() : okHttpClient;
    }

    public ApiService getApiService() {
        return apiService == null ? initRetrofit() : apiService;
    }

    /**
     * 封装OkHttpClient，网络请求的主体配置
     */
    private OkHttpClient initClient() {
        //配置过滤器
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i(TAG, "Http-Request-Response:" + message);
            }
        });

        //不同环境下，过滤器的展示数据有所不同
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        //进行配置
        OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder();
        OkHttpClient client = okHttpClient
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(httpLoggingInterceptor)
                //出错重连
                .retryOnConnectionFailure(true)
                .build();
        return client;
    }

    /**
     * 封装- Retrofit
     */
    ApiService initRetrofit() {
        OkHttpClient client = getOkHttpClient();
        Retrofit retrofit = new Retrofit
                .Builder()
                .baseUrl(HttpUrlPath.url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ApiService.class);
    }
}
