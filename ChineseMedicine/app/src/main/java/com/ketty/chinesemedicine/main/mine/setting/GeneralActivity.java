package com.ketty.chinesemedicine.main.mine.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.databinding.ActivityGeneralBinding;
import com.ketty.chinesemedicine.util.FileCacheUtils;
import com.ketty.chinesemedicine.util.T;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GeneralActivity extends BaseActivity implements View.OnClickListener {
    private ActivityGeneralBinding bind;

    @Override
    protected View initLayout() {
        bind = ActivityGeneralBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        bind.shadowSystemPermissions.setOnClickListener(this);
        bind.shadowStorageSpace.setOnClickListener(this);
        getCacheSize();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shadow_system_permissions:
                getAppDetailSettingIntent(getApplicationContext());
                break;
            case R.id.shadow_storage_space:
                new CommonAlertDialog(this).builder()
                        .setTitle("清理缓存")
                        .setMsg("清理缓存后图片等数据可能需要重新加载")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                FileCacheUtils.cleanInternalCache(GeneralActivity.this);
                                FileCacheUtils.cleanExternalCache(GeneralActivity.this);
                                FileCacheUtils.cleanDatabases(GeneralActivity.this);
                                getCacheSize();
                                T.showShort("清理成功");
                            }
                        }).setCancelable(false)
                        .show();
                break;
        }
    }

    private void getCacheSize() {
        try {
            List<File> files = new ArrayList<>();
            files.add(getCacheDir());
            files.add(getExternalCacheDir());
            files.add(new File("/data/data/" + getPackageName() + "/databases"));
            String size = FileCacheUtils.getCacheSize(files);
            bind.tvStorageSize.setText(size);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getAppDetailSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}