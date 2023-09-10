package com.ketty.chinesemedicine.Rxhttp;

import com.google.gson.JsonParseException;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.ketty.chinesemedicine.entity.Result;
import com.ketty.chinesemedicine.util.LogUtils;
import com.ketty.chinesemedicine.util.T;

import org.json.JSONException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public abstract class BaseObserver implements Observer<Result> {
    private static String Tag = "retrofit-observer";

    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof HttpException) {
            onException(ExceptionReason.BAD_NETWORK);
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            onException(ExceptionReason.CONNECT_ERROR);
        } else if (e instanceof InterruptedIOException) {
            onException(ExceptionReason.CONNECT_TIMEOUT);
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {
            onException(ExceptionReason.PARSE_ERROR);
        } else {
            onException(ExceptionReason.UNKNOWN_ERROR);
        }
        LogUtils.e(Tag, Tag + ":" + e.getMessage());
        onFailure(null, e.getMessage());
    }

    @Override
    public void onNext(Result response) {
        if (response.isSuccess()) {
            onSuccess(response.getData());
            LogUtils.e("请求成功");
        } else {
            LogUtils.e("请求失败");
            LogUtils.e(Tag, Tag + ":" + response.getMessage());
            onFailure(null, response.getMessage());
        }
    }

    public abstract void onSuccess(Map<String, Object> response);

    public abstract void onFailure(Throwable e, String errorMsg);

    /**
     * 请求异常
     * @param reason
     */
    public void onException(ExceptionReason reason) {
        switch (reason) {
            case CONNECT_ERROR:
                T.showShort("网络连接失败，请检查网络状态");
                break;
            case CONNECT_TIMEOUT:
                T.showShort("网络超时，请检查网络状态");
                break;
            case BAD_NETWORK:
                T.showShort("网络不给力，请稍后重试");
                break;
            case PARSE_ERROR:
                T.showShort("解析失败，攻城狮正在修复");
                break;
            case UNKNOWN_ERROR:
            default:
                T.showShort("未知错误");
                break;
        }
    }

    /**
     * 枚举类：请求异常
     */
    public enum ExceptionReason {
        /**
         * 解析数据失败
         */
        PARSE_ERROR,
        /**
         * 网络问题
         */
        BAD_NETWORK,
        /**
         * 连接错误
         */
        CONNECT_ERROR,
        /**
         * 连接超时
         */
        CONNECT_TIMEOUT,
        /**
         * 未知错误
         */
        UNKNOWN_ERROR,
    }
}
