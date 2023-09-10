package com.ketty.chinesemedicine.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.ketty.chinesemedicine.BaseActivity;

public class NetBroadcastReceiver extends BroadcastReceiver {
    public NetChangeListener listener = BaseActivity.listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            if (listener != null) {
                listener.onChangeListener();
            }
        }
    }

    // 自定义接口
    public interface NetChangeListener {
        void onChangeListener();
    }

}