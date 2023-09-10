package com.ketty.chinesemedicine.login.phone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.MainActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.databinding.ActivityLoginForPhoneBinding;
import com.ketty.chinesemedicine.login.AgreeAndContinueDialog;
import com.ketty.chinesemedicine.util.ButtonClickUtils;
import com.ketty.chinesemedicine.util.DestroyActivityUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LoginUtil;
import com.ketty.chinesemedicine.util.MMKVUtil;
import com.ketty.chinesemedicine.util.NetworkUtils;
import com.ketty.chinesemedicine.util.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginForPhoneActivity extends BaseActivity implements View.OnClickListener {
    private ActivityLoginForPhoneBinding bind;
    private CaptchaFragment mCaptchaFragment = new CaptchaFragment();
    private PasswordFragment mPasswordFragment = new PasswordFragment();
    private FragmentManager fm = getSupportFragmentManager();
    private AgreeAndContinueDialog mAgreeAndContinueDialog;
    private LoginUtil mLoginUtil;

    @Override
    protected View initLayout() {
        DestroyActivityUtil.addDestoryActivityToMap(LoginForPhoneActivity.this, "LoginForPhoneActivity");
        bind = ActivityLoginForPhoneBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        initFragment(mCaptchaFragment);

        bind.ivBack.setOnClickListener(this);
        bind.modeSwitch.setOnClickListener(this);
//        mWechat.setOnClickListener(this);
        bind.icQq.setOnClickListener(this);
        mAgreeAndContinueDialog = new AgreeAndContinueDialog();
    }

    @Override
    public void onClick(View view) {
        //防止快速点击
        if (ButtonClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.mode_switch:
                String str = bind.modeSwitch.getText().toString();
                if (TextUtils.equals(str, "手机密码登录")) {
                    bind.modeSwitch.setText("验证码登录");
                    initFragment(mPasswordFragment);
                } else {
                    bind.modeSwitch.setText("手机密码登录");
                    initFragment(mCaptchaFragment);
                }
                break;
            /*case R.id.ic_wechat:
                mAgreeAndContinueDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullScreen);
                mAgreeAndContinueDialog.show(getSupportFragmentManager(), "AgreeAndContinueDialog");
                mAgreeAndContinueDialog.setStateListener(new AgreeAndContinueDialog.StateListener() {
                    @Override
                    public void close() {
                        mAgreeAndContinueDialog.dismiss();
                    }

                    @Override
                    public void agree() {

                        mAgreeAndContinueDialog.dismiss();
                    }
                });
                break;*/
            case R.id.ic_qq:
                mAgreeAndContinueDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullScreen);
                mAgreeAndContinueDialog.show(getSupportFragmentManager(), "AgreeAndContinueDialog");
                mAgreeAndContinueDialog.setStateListener(new AgreeAndContinueDialog.StateListener() {
                    @Override
                    public void close() {
                        mAgreeAndContinueDialog.dismiss();
                    }

                    @Override
                    public void agree() {
                        if (!NetworkUtils.isAvailableByPing()) {
                            T.showShort("当前网络无法访问互联网，请检查后重试");
                        } else {
                            mLoginUtil = new LoginUtil(handler, 1);
                            mLoginUtil.login("qq");
                        }
                        mAgreeAndContinueDialog.dismiss();
                    }
                });
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String[] data = (String[]) msg.obj;
            switch (msg.what) {
                case 1:
                    if (data.length > 0) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("uQqId", data[0]);
                        map.put("uQqName", data[1]);
                        map.put("uHeadicon", data[2]);

                        RetrofitManager
                                .getInstance()
                                .getApiService()
                                .postMethod("/user/loginForQQ", map)
                                .compose(RxHelper.observableIO2Main(LoginForPhoneActivity.this))
                                .subscribe(new BaseObserver() {
                                    @Override
                                    public void onSuccess(Map<String, Object> response) {
                                        MMKVUtil mmkvUtil = App.getMmkvUtil();
                                        String data = JsonHelper.parserObject2Json(response.get("result"));
                                        Long uId = JsonHelper.parserJson2Object(
                                                data, Long.class);
                                        mmkvUtil.putString("user", "uId", String.valueOf(uId));
                                        startActivity(new Intent(LoginForPhoneActivity.this, MainActivity.class));
                                        finish();
                                        DestroyActivityUtil.destoryActivity("LoginStartActivity");
                                    }

                                    @Override
                                    public void onFailure(Throwable e, String errorMsg) {

                                    }
                                });
                    }
                    break;
            }
        }
    };

    private void initFragment(Fragment fragment) {
        if (fragment != null) {
            fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }

    public interface MyTouchListener {
        void onTouchEvent(MotionEvent event);
    }

    // 保存MyTouchListener接口的列表
    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<LoginForPhoneActivity.MyTouchListener>();

    /**
     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void registerMyTouchListener(MyTouchListener listener) {
        myTouchListeners.add(listener);
    }

    /**
     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
     *
     * @param listener
     */
    public void unRegisterMyTouchListener(MyTouchListener listener) {
        myTouchListeners.remove(listener);
    }

    /**
     * 分发触摸事件给所有注册了MyTouchListener的接口
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        for (MyTouchListener listener : myTouchListeners) {
            listener.onTouchEvent(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}