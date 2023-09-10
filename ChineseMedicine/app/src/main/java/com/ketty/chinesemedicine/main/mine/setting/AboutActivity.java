package com.ketty.chinesemedicine.main.mine.setting;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;

import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.BuildConfig;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.databinding.ActivityAboutBinding;
import com.ketty.chinesemedicine.util.T;
import com.pgyersdk.update.DownloadFileListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;

import java.io.File;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAboutBinding bind;
    private UpdateDialog dialog;

    @Override
    protected View initLayout() {
        bind = ActivityAboutBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        String versionName = BuildConfig.VERSION_NAME;
        bind.tvVersion.setText("版本 v" + versionName);

        bind.ivBack.setOnClickListener(this);
        bind.slCheckUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.sl_check_update:
                checkUpdate();
                break;
        }
    }

    private void checkUpdate() {
        new PgyUpdateManager.Builder()
                .setUserCanRetry(false)
                .setDeleteHistroyApk(false)
                .setUpdateManagerListener(new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {
                        T.showShort("当前已是最新版本啦~");
                    }
                    @Override
                    public void onUpdateAvailable(AppBean appBean) {
                        dialog = new UpdateDialog();
                        Bundle bundle = new Bundle();
                        bundle.putString("versionName", appBean.getVersionName());
                        bundle.putString("releaseNote", appBean.getReleaseNote());
                        dialog.setArguments(bundle);
                        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullScreen);
                        dialog.show(getSupportFragmentManager(), "UpdateDialog");
                        dialog.setStateListener(new UpdateDialog.StateListener() {
                            @Override
                            public void close() {
                                dialog.dismiss();
                            }

                            @Override
                            public void update() {
                                T.showShort("开始下载更新...");
                                PgyUpdateManager.downLoadApk(appBean.getDownloadURL());
                            }

                            @Override
                            public void install(File file) {
                                PgyUpdateManager.installApk(file);
                            }
                        });
                    }

                    @Override
                    public void checkUpdateFailed(Exception e) {
                        T.showShort("检查更新失败，请稍后再试");
                    }
                })
                .setDownloadFileListener(new DownloadFileListener() {
                    @Override
                    public void downloadFailed() {
                        T.showShort("下载失败，请重试");
                        dialog.downloadFailed();
                    }

                    @Override
                    public void downloadSuccessful(File file) {
                        T.showShort("下载完成");
                        PgyUpdateManager.installApk(file);
                        dialog.downloadSuccessful(file);
                    }

                    @Override
                    public void onProgressUpdate(Integer... integers) {
                        for (Integer integer : integers) {
                            dialog.initDownload(integer);
                        }
                    }})
                .register();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}