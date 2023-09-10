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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.component.XEditText;
import com.ketty.chinesemedicine.databinding.ActivityVerifyPhoneBinding;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.NetworkUtils;
import com.ketty.chinesemedicine.util.T;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class VerifyPhoneActivity extends BaseActivity implements View.OnClickListener {
    private ActivityVerifyPhoneBinding bind;
    private String phone;
    private String captcha;
    private TimeCount mTimeCount;
    private InputMethodManager imm;

    @Override
    protected View initLayout() {
        bind = ActivityVerifyPhoneBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        phone = bundle.getString("phone");

        bind.etPhone.setTextEx(phone.substring(0,3) + "****" + phone.substring(7));
        bind.etPhone.setEnabled(false);
        bind.tvNext.setClickable(false);
        sendCaptcha();

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initEditText();

        bind.tvCaptcha.setOnClickListener(this);
        bind.ivBack.setOnClickListener(this);
        bind.tvNext.setOnClickListener(this);

        TextChange textChange = new TextChange();
        bind.etCaptcha.setOnXTextChangeListener(textChange);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                String captchaStr = bind.etCaptcha.getTrimmedString();
                if (captcha.equals(captchaStr)) {
                    Intent intent = new Intent(this,SetNewPasswordActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", 0);
                    intent.putExtras(bundle);
                    mActivityLauncher.launch(intent);
                } else {
                    T.showShort("手机验证码错误");
                }
                break;
        }
    }

    private void sendCaptcha() {
        if (!NetworkUtils.isAvailableByPing()) {
            T.showShort("当前网络无法访问互联网，请检查后重试");
        } else {
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
    }

    private final ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                setResult(RESULT_OK,new Intent());
                finish();
            }
        }
    });


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
        String str = bind.etCaptcha.getTrimmedString();
        bind.tvCaptcha.setVisibility(View.VISIBLE);
        bind.etCaptcha.requestFocus();
        showSoftInput(bind.etCaptcha);
        if (str.length() == 6) {
            bind.tvNext.setClickable(true);
            bind.tvNext.setTextColor(0xFFff2442);
        } else {
            bind.tvNext.setTextColor(0xFFffa5a5);
            bind.tvNext.setClickable(false);
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