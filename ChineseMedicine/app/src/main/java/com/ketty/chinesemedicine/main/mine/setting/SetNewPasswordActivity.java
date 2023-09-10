package com.ketty.chinesemedicine.main.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;

import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.component.XEditText;
import com.ketty.chinesemedicine.databinding.ActivitySetNewPasswordBinding;
import com.ketty.chinesemedicine.util.T;

import java.util.HashMap;
import java.util.Map;

public class SetNewPasswordActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySetNewPasswordBinding bind;
    private int type;
    private String uId;

    @Override
    protected View initLayout() {
        bind = ActivitySetNewPasswordBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        uId = App.getMmkvUtil().getString("user","uId",null);
        bind.tvFinish.setClickable(false);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");

        TextChange textChange = new TextChange();

        if (type == 0) {
            bind.llOld.setVisibility(View.GONE);
        } else {
            bind.llOld.setVisibility(View.VISIBLE);
            bind.etPasswordOld.setOnXTextChangeListener(textChange);
        }

        bind.etPassword.setOnXTextChangeListener(textChange);
        bind.etPasswordAgain.setOnXTextChangeListener(textChange);

        bind.tvFinish.setOnClickListener(this);
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
        String password = bind.etPassword.getTrimmedString();
        String passwordAgain = bind.etPasswordAgain.getTrimmedString();
        if (type == 1) {
            String oldPassword = bind.etPasswordOld.getTrimmedString();
            if (oldPassword.length() >= 6 && password.length() >= 6 && passwordAgain.length() >= 6) {
                bind.tvFinish.setClickable(true);
                bind.tvFinish.setTextColor(0xFFff2442);
            } else {
                bind.tvFinish.setClickable(false);
                bind.tvFinish.setTextColor(0xFFffa5a5);
            }
        } else {
            if (password.length() >= 6 && passwordAgain.length() >= 6) {
                bind.tvFinish.setClickable(true);
                bind.tvFinish.setTextColor(0xFFff2442);
            } else {
                bind.tvFinish.setClickable(false);
                bind.tvFinish.setTextColor(0xFFffa5a5);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_finish:
                String password = bind.etPassword.getTrimmedString();
                String passwordAgain = bind.etPasswordAgain.getTrimmedString();
                if (password.equals(passwordAgain)) {
                    if (type == 0) {
                        update(password);
                    } else {
                        String oldPassword = bind.etPasswordOld.getTrimmedString();
                        verifyOldPassword(oldPassword, password);
                    }
                } else {
                    new CommonAlertDialog(this).builder()
                            .setTitle("提示")
                            .setMsg("两次密码输入不一致")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).setCancelable(false)
                            .show();
                }
                break;
        }
    }

    private void verifyOldPassword(String oldPassword, String newPassword) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uId", uId);
        map.put("uPassword", oldPassword);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/verifyOldPassword", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        update(newPassword);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        new CommonAlertDialog(SetNewPasswordActivity.this).builder()
                                .setTitle("提示")
                                .setMsg("原密码输入错误")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                    }
                                }).setCancelable(false)
                                .show();
                    }
                });
    }

    private void update(String password) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uId", uId);
        map.put("uPassword", password);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/updatePassword", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        T.showShort("设置成功");
                        setResult(RESULT_OK,new Intent());
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }

}