package com.ketty.chinesemedicine.main.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.databinding.ActivityAuthenticationBinding;

public class AuthenticationActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAuthenticationBinding bind;
    private String phone;

    @Override
    protected View initLayout() {
        bind = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        phone = bundle.getString("phone");

        bind.ivBack.setOnClickListener(this);
        bind.shadowPhone.setOnClickListener(this);
        bind.shadowPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Bundle bundle;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.shadow_phone:
                intent = new Intent(this,VerifyPhoneActivity.class);
                bundle = new Bundle();
                bundle.putString("phone", phone);
                intent.putExtras(bundle);
                mActivityLauncher.launch(intent);
                break;
            case R.id.shadow_password:
                intent = new Intent(this,SetNewPasswordActivity.class);
                bundle = new Bundle();
                bundle.putInt("type", 1);
                intent.putExtras(bundle);
                mActivityLauncher.launch(intent);
                break;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}