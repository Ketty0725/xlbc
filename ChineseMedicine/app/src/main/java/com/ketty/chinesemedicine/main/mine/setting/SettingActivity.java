package com.ketty.chinesemedicine.main.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.databinding.ActivitySettingBinding;
import com.ketty.chinesemedicine.login.LoginStartActivity;
import com.ketty.chinesemedicine.main.store.ShippingAddressActivity;
import com.ketty.chinesemedicine.util.DestroyActivityUtil;
import com.ketty.chinesemedicine.util.FileCacheUtils;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySettingBinding bind;

    @Override
    protected View initLayout() {
        DestroyActivityUtil.addDestoryActivityToMap(SettingActivity.this, "SettingActivity");
        bind = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        bind.ivBack.setOnClickListener(this);
        bind.shadowSignOut.setOnClickListener(this);
        bind.shadowAccount.setOnClickListener(this);
        bind.shadowGeneral.setOnClickListener(this);
        bind.shadowAddress.setOnClickListener(this);
        bind.shadowAbout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.shadow_account:
                startActivity(new Intent(this, AccountActivity.class));
                break;
            case R.id.shadow_address:
                Intent intent = new Intent(this, ShippingAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type",1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.shadow_general:
                startActivity(new Intent(this, GeneralActivity.class));
                break;
            case R.id.shadow_sign_out:
                new CommonAlertDialog(this).builder()
                        .setTitle("登出")
                        .setMsg("确定登出？")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                App.getMmkvUtil().clear("user");
                                FileCacheUtils.cleanDatabases(SettingActivity.this);
                                startActivity(new Intent(SettingActivity.this, LoginStartActivity.class));
                                finish();
                                DestroyActivityUtil.destoryActivity("MainActivity");
                            }
                        }).setCancelable(false)
                        .show();
                break;
            case R.id.shadow_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}