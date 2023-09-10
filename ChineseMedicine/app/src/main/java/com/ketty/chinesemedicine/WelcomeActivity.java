package com.ketty.chinesemedicine;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.ketty.chinesemedicine.databinding.ActivityWelcomeBinding;
import com.ketty.chinesemedicine.login.LoginStartActivity;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends BaseActivity {
    private ActivityWelcomeBinding bind;
    private int recLen = 6;
    private Timer timer = new Timer();
    private Handler handler;
    private Runnable runnable;

    @Override
    protected View initLayout() {
        bind = ActivityWelcomeBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        hideBottomUIMenu(); //隐藏虚拟键
        int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        //设置当前窗体为全屏显示
        getWindow().setFlags(flag, flag);

        bind.videoview.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video));
        //播放
        bind.videoview.start();

        timer.schedule(task, 1000, 1000);

        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, LoginStartActivity.class));
                finish();
            }
        }, 6000);

        bind.slSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this, LoginStartActivity.class));
                finish();
                if (runnable != null) {
                    handler.removeCallbacks(runnable);
                }
                if (timer != null) {
                    timer.cancel();
                }
            }
        });
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recLen--;
                    if (recLen < 0) {
                        timer.cancel();
                        bind.slSkip.setVisibility(View.GONE);
                    } else if (recLen <= 5) {
                        if (recLen == 5) {
                            bind.slSkip.setVisibility(View.VISIBLE);
                        }
                        bind.tvSkip.setText("跳过 " + recLen);
                    }
                }
            });
        }
    };

    /* 隐藏虚拟按键，并且全屏 */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            v.setSystemUiVisibility(uiOptions);
        } else {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (runnable != null) {
            handler.removeCallbacks(runnable);
        }
        if (timer != null) {
            timer.cancel();
        }
        bind = null;
    }
}