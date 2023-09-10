package com.ketty.chinesemedicine.main.mine.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.component.XEditText;
import com.ketty.chinesemedicine.databinding.ActivityBindingPhoneBinding;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.login.LoginStartActivity;
import com.ketty.chinesemedicine.util.DestroyActivityUtil;
import com.ketty.chinesemedicine.util.FileCacheUtils;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.NetworkUtils;
import com.ketty.chinesemedicine.util.T;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class BindingPhoneActivity extends BaseActivity implements View.OnClickListener {
    private ActivityBindingPhoneBinding bind;
    private TimeCount mTimeCount;
    private InputMethodManager imm;
    private String phone;
    private int type;
    private String captcha;
    private String uId;

    @Override
    protected View initLayout() {
        bind = ActivityBindingPhoneBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        uId = App.getMmkvUtil().getString("user","uId",null);
        Bundle bundle = getIntent().getExtras();
        phone = bundle.getString("phone");
        type = bundle.getInt("type");
        if (type == 0) {
            bind.tvTitle.setText("绑定新手机号");
        } else {
            if (type == 1) {
                bind.tvTitle.setText("现手机号验证");
            } else if (type == 2) {
                bind.tvTips.setText("注销申请");
                bind.tvTitle.setText("请先验证身份");
            }
            bind.etPhone.setTextEx(phone.substring(0,3) + "****" + phone.substring(7));
            bind.etPhone.setEnabled(false);
        }

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        bind.sure.setClickable(false);
        bind.sure.setEnabled(false);

        bind.etPhone.requestFocus();
        showSoftInput(bind.etPhone);
        initEditText();

        bind.tvCaptcha.setOnClickListener(this);
        bind.sure.setOnClickListener(this);
        bind.ivBack.setOnClickListener(this);

        TextChange textChange = new TextChange();
        bind.etPhone.setOnXTextChangeListener(textChange);
        bind.etCaptcha.setOnXTextChangeListener(textChange);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_captcha:
                if (!NetworkUtils.isAvailableByPing()) {
                    T.showShort("当前网络无法访问互联网，请检查后重试");
                } else {
                    if (type == 0) {
                        phone = bind.etPhone.getTrimmedString();
                    }
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("uPhone", phone);

                    RetrofitManager
                            .getInstance()
                            .getApiService()
                            .postMethod("/user/sendSMS", map)
                            .compose(RxHelper.observableIO2Main(this))
                            .subscribe(new BaseObserver() {
                                @Override
                                public void onSuccess(Map<String, Object> response) {
                                    String data = JsonHelper.parserObject2Json(response.get("result"));
                                    captcha = JsonHelper.parserJson2Object(data, String.class);
                                    mTimeCount = new TimeCount(60100, 1000);
                                    mTimeCount.start();
                                    bind.etCaptcha.requestFocus();
                                    showSoftInput(bind.etCaptcha);
                                }

                                @Override
                                public void onFailure(Throwable e, String errorMsg) {
                                    T.showShort("验证码发送失败");
                                }
                            });

                }
                break;
            case R.id.sure:
                if (type == 0) {
                    verify();
                } else {
                    String captchaStr = bind.etCaptcha.getTrimmedString();
                    if (captcha.equals(captchaStr)) {
                        if (type == 1) {
                            type = 0;
                            mTimeCount.cancel();
                            bind.tvCaptcha.setClickable(true);
                            bind.tvCaptcha.setEnabled(true);
                            bind.tvCaptcha.setText("获取验证码");
                            bind.tvCaptcha.setTextColor(0xFF5b93e0);
                            bind.tvTitle.setText("绑定新手机号");
                            bind.etPhone.setEnabled(true);
                            bind.etPhone.setTextEx("");
                            bind.etCaptcha.setTextEx("");
                            bind.etPhone.requestFocus();
                            showSoftInput(bind.etPhone);
                        } else if (type == 2) {
                            new CommonAlertDialog(this).builder()
                                    .setTitle("注销账号")
                                    .setMsg("注销后该账号下的所有数据都将被清空\n请慎重考虑")
                                    .setNegativeButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            logoutAccount();
                                        }
                                    }).setPositiveButton("再想想", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    }).setCancelable(false)
                                    .show();
                        }
                    } else {
                        T.showShort("手机验证码错误");
                    }
                }
                break;
        }
    }

    private void logoutAccount() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uId", uId);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/logoutAccount", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        T.showShort("注销成功");
                        App.getMmkvUtil().clear("user");
                        FileCacheUtils.cleanApplicationData(BindingPhoneActivity.this);
                        DestroyActivityUtil.destoryActivity("AccountActivity");
                        DestroyActivityUtil.destoryActivity("SettingActivity");
                        DestroyActivityUtil.destoryActivity("MainActivity");
                        startActivity(new Intent(BindingPhoneActivity.this, LoginStartActivity.class));
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void verify() {
        String phoneStr = bind.etPhone.getTrimmedString();
        String captchaStr = bind.etCaptcha.getTrimmedString();
        if (phone.equals(phoneStr) && captcha.equals(captchaStr)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("uId", uId);
            map.put("uPhone", phone);

            RetrofitManager
                    .getInstance()
                    .getApiService()
                    .postMethod("/user/selectPhoneUsed", map)
                    .compose(RxHelper.observableIO2Main(this))
                    .subscribe(new BaseObserver() {
                        @Override
                        public void onSuccess(Map<String, Object> response) {
                            String data = JsonHelper.parserObject2Json(response.get("result"));
                            User user = JsonHelper.parserJson2Object(data, User.class);
                            if (user != null) {
                                new CommonAlertDialog(BindingPhoneActivity.this).builder()
                                        .setTitle("更换绑定")
                                        .setMsg("该手机号当前绑定的账号为\n" + user.getuName() + "\n是否解绑并绑定至当前帐号？")
                                        .setNegativeButton("取消", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {

                                            }
                                        }).setPositiveButton("确定", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                update();
                                            }
                                        }).setCancelable(false)
                                        .show();
                            } else {
                                update();
                            }
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg) {

                        }
                    });
        } else {
            T.showShort("手机验证码错误");
        }
    }

    private void update() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uId", uId);
        map.put("uPhone", phone);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/updatePhone", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        T.showShort("绑定成功");
                        setResult(RESULT_OK,new Intent());
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private class TextChange implements XEditText.OnXTextChangeListener {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            initEditText();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void initEditText() {
        String str1 = bind.etPhone.getTrimmedString();
        String str2 = bind.etCaptcha.getTrimmedString();
        if (str1.length() == 11) {
            bind.tvCaptcha.setVisibility(View.VISIBLE);
            bind.etPhone.clearFocus();
            bind.etCaptcha.requestFocus();
            showSoftInput(bind.etCaptcha);
            if (str2.length() == 6) {
                bind.sure.setClickable(true);
                bind.sure.setEnabled(true);
                bind.sure.setLayoutBackground(0xFFff2442);
            } else {
                bind.sure.setLayoutBackground(0xFFffa5a5);
                bind.sure.setClickable(false);
                bind.sure.setEnabled(false);
            }
        } else {
            bind.etPhone.requestFocus();
            showSoftInput(bind.etPhone);
            bind.tvCaptcha.setVisibility(View.INVISIBLE);
        }
    }

    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            bind.tvCaptcha.setClickable(false);
            bind.tvCaptcha.setEnabled(false);
            bind.tvCaptcha.setText("重新发送" + "(" + millisUntilFinished / 1000 + "s" + ")");
            bind.tvCaptcha.setTextColor(0xFF999999);
        }

        @Override
        public void onFinish() {
            bind.tvCaptcha.setClickable(true);
            bind.tvCaptcha.setEnabled(true);
            bind.tvCaptcha.setText("重新发送");
            bind.tvCaptcha.setTextColor(0xFF5b93e0);
        }
    }

    public void showSoftInput(View view) {
        if (view != null && imm != null){
            Timer timer = new Timer();
            timer.schedule(new TimerTask(){
                @Override
                public void run() {
                    imm.showSoftInput(view, 0);
                }
            }, 300);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
                view.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void HideSoftInput(IBinder token) {
        if (token != null) {
            imm.hideSoftInputFromWindow(token, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeCount != null) {
            mTimeCount.cancel();
        }
        bind = null;
    }
}