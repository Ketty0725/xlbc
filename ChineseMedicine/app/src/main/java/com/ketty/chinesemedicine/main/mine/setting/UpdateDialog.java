package com.ketty.chinesemedicine.main.mine.setting;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.databinding.DialogUpdateBinding;
import com.ketty.chinesemedicine.util.NetworkUtils;
import com.ketty.chinesemedicine.util.T;

import java.io.File;

public class UpdateDialog extends DialogFragment implements View.OnClickListener {
    private DialogUpdateBinding bind;
    private StateListener mStateListener;
    private String versionName;
    private String releaseNote;
    private File file = null;
    private boolean isDownload = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DialogUpdateBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            versionName = bundle.getString("versionName");
            releaseNote = bundle.getString("releaseNote");
            bind.tvTitle.setText("是否升级到" + versionName + "版本？");
            bind.tvContent.setText(releaseNote);
        }
        bind.downloadProgress.setMaxProgress(100);

        bind.ivClose.setOnClickListener(this);
        bind.slUpdate.setOnClickListener(this);
    }

    public void initDownload(int progress) {
        bind.downloadProgress.setProgress(progress);
    }

    public void downloadSuccessful(File file) {
        if (file != null) {
            this.file = file;
            isDownload = true;
            bind.downloadProgress.setProgress(0);
            bind.downloadProgress.setVisibility(View.GONE);
            bind.slUpdate.setVisibility(View.VISIBLE);
            bind.tvUpdate.setText("立即安装");
        }
    }

    public void downloadFailed() {
        isDownload = false;
        bind.downloadProgress.setProgress(0);
        bind.downloadProgress.setVisibility(View.GONE);
        bind.slUpdate.setVisibility(View.VISIBLE);
        bind.tvUpdate.setText("重新下载");
    }

    @Override
    public void onStart() {
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = getWidth();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        params.windowAnimations = R.style.dialogAnim;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        super.onStart();
    }

    protected int getWidth() {
        int width = getResources().getDisplayMetrics().widthPixels;
        //设置弹窗高度为屏幕高度的3/4
        return width - width / 3;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                mStateListener.close();
                break;
            case R.id.sl_update:
                if (isDownload) {
                    mStateListener.install(file);
                } else {
                    if (!NetworkUtils.isAvailableByPing()) {
                        T.showShort("当前网络无法访问互联网，请检查后重试");
                    } else {
                        mStateListener.update();
                        bind.slUpdate.setVisibility(View.GONE);
                        bind.downloadProgress.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    public interface StateListener {
        void close();
        void update();
        void install(File file);
    }

    public void setStateListener(StateListener listener) {
        this.mStateListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }
}
