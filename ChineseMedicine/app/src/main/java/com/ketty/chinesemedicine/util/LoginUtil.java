package com.ketty.chinesemedicine.util;

import static android.R.attr.action;

import android.os.Handler;
import android.os.Message;

import com.mob.tools.utils.UIHandler;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginUtil implements PlatformActionListener, Handler.Callback {
    private static final int MSG_ACTION_CCALLBACK = 0;
    private Handler handler;
    private int code;

    public LoginUtil(Handler handler, int code) {
        this.handler = handler;
        this.code = code;
    }

    public void login(String code) {
        switch (code) {
            case "wechat":
                Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
                wechat.setPlatformActionListener(this);
                wechat.SSOSetting(false);
                authorize(wechat);
                break;
            case "qq":
                Platform qq = ShareSDK.getPlatform(QQ.NAME);
                qq.setPlatformActionListener(this);
                qq.SSOSetting(false);
                authorize(qq);
                break;
            case "sina":
                Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
                sina.setPlatformActionListener(this);
                sina.SSOSetting(false);
                authorize(sina);
                break;
        }
    }

    //授权
    private void authorize(Platform plat) {
        if (plat.isAuthValid()) { //如果授权就删除授权资料
            plat.removeAccount(true);
        }
        plat.showUser(null);//授权并获取用户信息
    }

    //登陆授权成功的回调
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> res) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);   //发送消息
    }

    //登陆授权错误的回调
    @Override
    public void onError(Platform platform, int i, Throwable t) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    //登陆授权取消的回调
    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = new Message();
        msg.what = MSG_ACTION_CCALLBACK;
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = platform;
        UIHandler.sendMessage(msg, this);
    }

    @Override
    public boolean handleMessage(Message message) {
        switch (message.arg1) {
            case 1: {
                Platform platform = (Platform) message.obj;
                PlatformDb platDb = platform.getDb();

                if (platDb.getPlatformNname().equals(Wechat.NAME)) {

                } else if (platDb.getPlatformNname().equals(SinaWeibo.NAME)) {

                } else if (platDb.getPlatformNname().equals(QQ.NAME)) {
                    String[] data = new String[3];
                    data[0] = platDb.getUserId();
                    data[1] = platDb.getUserName();
                    data[2] = platDb.getUserIcon();

                    Message msg = Message.obtain();
                    msg.what = code;
                    msg.obj = data;
                    handler.sendMessage(msg);
                }
            }
            break;
            case 2: { // 失败
                T.showShort("授权失败");
            }
            break;
            case 3: { // 取消
                T.showShort("授权取消");
            }
            break;
        }
        return false;
    }

}
