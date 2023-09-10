package com.ketty.chinesemedicine;

import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.util.NetBroadcastReceiver;
import com.ketty.chinesemedicine.util.NetworkUtils;
import com.ketty.chinesemedicine.util.StatusBarStyle;
import com.ketty.chinesemedicine.util.T;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

public abstract class BaseActivity extends RxAppCompatActivity implements NetBroadcastReceiver.NetChangeListener {
    public static NetBroadcastReceiver.NetChangeListener listener;
    private CommonAlertDialog alertDialog = null;
    private NetBroadcastReceiver netBroadcastReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //全部禁止横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        int type = StatusBarStyle.setStatusBarLightMode(this,true);
        StatusBarStyle.setStatusBarLightMode(this,type);
        getWindow().setEnterTransition(new Fade().setDuration(500));
        getWindow().setExitTransition(new Fade().setDuration(500));
        super.onCreate(savedInstanceState);
        setContentView(initLayout());
        listener = this;
//        checkNet();
        initView();
    }

    @Override
    protected void onResume() {
        //Android 7.0以上需要动态注册
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //实例化IntentFilter对象
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            if (netBroadcastReceiver == null) {
                netBroadcastReceiver = new NetBroadcastReceiver();
            }
            //注册广播接收
            registerReceiver(netBroadcastReceiver, filter);
        }
        super.onResume();
    }

    //onPause()方法注销
    @Override
    protected void onPause() {
        unregisterReceiver(netBroadcastReceiver);
        super.onPause();
    }

    protected abstract View initLayout();

    protected abstract void initView();

    /**
     * 网络变化之后的类型
     */
    @Override
    public void onChangeListener() {
        if (!NetworkUtils.isConnected()) {
            showNetDialog();
        } else {
            hideNetDialog();
            if (!NetworkUtils.isAvailableByPing()) {
                T.showShort("当前网络无法访问互联网");
            }
        }
    }

    /**
     * 隐藏设置网络框
     */
    private void hideNetDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
        alertDialog = null;
    }

    /**
     * 初始化时判断有没有网络
     */
    private boolean checkNet() {
        if (!NetworkUtils.isConnected()) {
            //网络异常，请检查网络
            showNetDialog();
        } else if (!NetworkUtils.isAvailableByPing()) {
            T.showShort("当前网络无法访问互联网");
        }
        return NetworkUtils.isConnected();
    }

    /**
     * 弹出设置网络框
     */
    private void showNetDialog() {
        if (alertDialog == null) {
            alertDialog = new CommonAlertDialog(this).builder()
                    .setTitle("网络异常")
                    .setMsg("网络连接断开，请检查网络设置")
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).setPositiveButton("设置", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            NetworkUtils.openWirelessSettings();
                        }
                    }).setCancelable(false);
        }
        alertDialog.show();
    }

}

