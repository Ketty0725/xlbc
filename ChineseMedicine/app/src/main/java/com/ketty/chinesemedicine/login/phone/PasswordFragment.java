package com.ketty.chinesemedicine.login.phone;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.ketty.chinesemedicine.databinding.FragmentPasswordBinding;
import com.ketty.chinesemedicine.login.AgreeAndContinueDialog;
import com.ketty.chinesemedicine.util.DestroyActivityUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.MMKVUtil;
import com.ketty.chinesemedicine.util.T;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PasswordFragment extends RxFragment implements View.OnClickListener {
    private FragmentPasswordBinding bind;
    private LoginViewModel mViewModel;
    private LoginForPhoneActivity.MyTouchListener myTouchListener;
    private InputMethodManager imm;

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

        bind = FragmentPasswordBinding.inflate(inflater, container, false);
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
        bind.login.setEnabled(false);

        bind.etPhone.requestFocus();
        showSoftInput(bind.etPhone);
        bind.etPhone.setTextToSeparate(mViewModel.getPhone().getValue());
        initEditText();

        bind.login.setOnClickListener(this);

        TextChange textChange = new TextChange();
        bind.etPhone.setOnXTextChangeListener(textChange);
        bind.etPassword.setOnXTextChangeListener(textChange);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
        String phone = bind.etPhone.getTrimmedString();
        String password = bind.etPassword.getTrimmedString();

        HashMap<String, Object> map = new HashMap<>();
        map.put("uPhone", phone);
        map.put("uPassword", password);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/loginForPassword", map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        MMKVUtil mmkvUtil = App.getMmkvUtil();
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        Long uId = JsonHelper.parserJson2Object(data, Long.class);
                        mmkvUtil.putString("user", "uId", String.valueOf(uId));
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        DestroyActivityUtil.destoryActivity("LoginStartActivity");
                        DestroyActivityUtil.destoryActivity("LoginForPhoneActivity");
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        T.showShort(errorMsg);
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
        String str2 = bind.etPassword.getTrimmedString();
        if (str1.length() == 11) {
            mViewModel.getPhone().setValue(bind.etPhone.getTrimmedString());
            bind.etPhone.clearFocus();
            bind.etPassword.requestFocus();
            showSoftInput(bind.etPassword);
            if (str2.length() > 0) {
                bind.login.setClickable(true);
                bind.login.setEnabled(true);
                bind.login.setLayoutBackground(android.graphics.Color.parseColor("#ff2442"));
            } else {
                bind.login.setLayoutBackground(android.graphics.Color.parseColor("#ffa5a5"));
                bind.login.setClickable(false);
                bind.login.setEnabled(false);
            }
        } else {
            bind.etPhone.requestFocus();
            showSoftInput(bind.etPhone);
            mViewModel.getPhone().setValue("");
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
        ((LoginForPhoneActivity)this.getActivity()).unRegisterMyTouchListener(myTouchListener);
        bind = null;
    }
}