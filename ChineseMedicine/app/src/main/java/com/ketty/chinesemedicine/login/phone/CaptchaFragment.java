package com.ketty.chinesemedicine.login.phone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.MainActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.component.XEditText;
import com.ketty.chinesemedicine.databinding.FragmentCaptchaBinding;
import com.ketty.chinesemedicine.login.AgreeAndContinueDialog;
import com.ketty.chinesemedicine.util.DestroyActivityUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;
import com.ketty.chinesemedicine.util.NetworkUtils;
import com.ketty.chinesemedicine.util.T;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class CaptchaFragment extends RxFragment implements View.OnClickListener {
    private FragmentCaptchaBinding bind;
    private TimeCount mTimeCount;
    private LoginViewModel mViewModel;
    private LoginForPhoneActivity.MyTouchListener myTouchListener;
    private InputMethodManager imm;
    private String captcha;
    private String phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        myTouchListener = new LoginForPhoneActivity.MyTouchListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View view = getActivity().getCurrentFocus();
                    if (isHideInput(view, event)) {
                        HideSoftInput(view.getWindowToken());
                        view.clearFocus();
                    }
                }
            }
        };
        // 将myTouchListener注册到分发列表
        ((LoginForPhoneActivity)this.getActivity()).registerMyTouchListener(myTouchListener);

        bind = FragmentCaptchaBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        initView();
    }

    private void initView() {
        bind.login.setClickable(false);

        bind.etPhone.requestFocus();
        showSoftInput(bind.etPhone);
        bind.etPhone.setTextToSeparate(mViewModel.getPhone().getValue());
        initEditText();

        bind.tvCaptcha.setOnClickListener(this);
        bind.login.setOnClickListener(this);

        TextChange textChange = new TextChange();
        bind.etPhone.setOnXTextChangeListener(textChange);
        bind.etCaptcha.setOnXTextChangeListener(textChange);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_captcha:
                if (!NetworkUtils.isAvailableByPing()) {
                    T.showShort("当前网络无法访问互联网，请检查后重试");
                } else {
                    phone = bind.etPhone.getTrimmedString();

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("uPhone", phone);

                    RetrofitManager
                            .getInstance()
                            .getApiService()
                            .postMethod("/user/sendSMS", map)
                            .compose(RxHelper.observableIO2Main(getContext()))
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
            case R.id.login:
                if (bind.scb.isChecked()) {
                    login();
                } else {
                    AgreeAndContinueDialog mAgreeAndContinueDialog = new AgreeAndContinueDialog();
                    mAgreeAndContinueDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullScreen);
                    mAgreeAndContinueDialog.show(getChildFragmentManager(),"AgreeAndContinueDialog");
                    mAgreeAndContinueDialog.setStateListener(new AgreeAndContinueDialog.StateListener() {
                        @Override
                        public void close() {
                            mAgreeAndContinueDialog.dismiss();
                        }

                        @Override
                        public void agree() {
                            login();
                            mAgreeAndContinueDialog.dismiss();
                        }
                    });
                }
                break;
        }
    }

    private void login() {
        String phoneStr = bind.etPhone.getTrimmedString();
        String captchaStr = bind.etCaptcha.getTrimmedString();
        LogUtils.i("phone --->"+phone);
        LogUtils.i("phoneStr --->"+phoneStr);
        LogUtils.i("captcha --->"+captcha);
        LogUtils.i("captchaStr --->"+captchaStr);
        if (phone.equals(phoneStr) && captcha.equals(captchaStr)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("uPhone", phone);

            RetrofitManager
                    .getInstance()
                    .getApiService()
                    .postMethod("/user/loginForCaptcha", map)
                    .compose(RxHelper.observableIO2Main(getContext()))
                    .subscribe(new BaseObserver() {
                        @Override
                        public void onSuccess(Map<String, Object> response) {
                            String data = JsonHelper.parserObject2Json(response.get("result"));
                            Long uId = JsonHelper.parserJson2Object(
                                    data, Long.class);
                            App.getMmkvUtil().putString("user", "uId", String.valueOf(uId));
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            mTimeCount.cancel();
                            DestroyActivityUtil.destoryActivity("LoginStartActivity");
                            DestroyActivityUtil.destoryActivity("LoginForPhoneActivity");
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg) {

                        }
                    });

        } else {
            T.showShort("手机验证码错误");
        }
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
            mViewModel.getPhone().setValue(bind.etPhone.getTrimmedString());
            bind.tvCaptcha.setVisibility(View.VISIBLE);
            bind.etPhone.clearFocus();
            bind.etCaptcha.requestFocus();
            showSoftInput(bind.etCaptcha);
            if (str2.length() == 6) {
                bind.login.setClickable(true);
                bind.login.setLayoutBackground(android.graphics.Color.parseColor("#ff2442"));
            } else {
                bind.login.setLayoutBackground(android.graphics.Color.parseColor("#ffa5a5"));
                bind.login.setClickable(false);
            }
        } else {
            bind.etPhone.requestFocus();
            showSoftInput(bind.etPhone);
            mViewModel.getPhone().setValue("");
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
            bind.tvCaptcha.setText("重新发送" + "(" + millisUntilFinished / 1000 + "s" + ")");
            bind.tvCaptcha.setTextColor(android.graphics.Color.parseColor("#999999"));
        }

        @Override
        public void onFinish() {
            bind.tvCaptcha.setClickable(true);
            bind.tvCaptcha.setText("重新发送");
            bind.tvCaptcha.setTextColor(android.graphics.Color.parseColor("#5b93e0"));
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
        if (token != null && imm != null) {
            imm.hideSoftInputFromWindow(token, 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mTimeCount != null) {
            mTimeCount.cancel();
        }
        ((LoginForPhoneActivity)this.getActivity()).unRegisterMyTouchListener(myTouchListener);
        bind = null;
    }
}