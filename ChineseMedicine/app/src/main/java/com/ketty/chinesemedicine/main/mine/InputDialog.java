package com.ketty.chinesemedicine.main.mine;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.databinding.DialogTextInputBinding;
import com.ketty.chinesemedicine.databinding.FragmentCaptchaBinding;

public class InputDialog extends DialogFragment {
    private DialogTextInputBinding bind;
    private StateListener mStateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DialogTextInputBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind.etAbout.requestFocus();
        Bundle bundle = getArguments();
        if (bundle != null) {
            String about = bundle.getString("about");
            bind.etAbout.setText(about);
        }
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        if (TextUtils.isEmpty(bind.etAbout.getText().toString())) {
            bind.tvConfirm.setTextColor(Color.parseColor("#A6A6A6"));
        } else {
            bind.tvConfirm.setTextColor(Color.parseColor("#ff2442"));
        }

        bind.etAbout.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(bind.etAbout.getText().toString())) {
                    bind.tvConfirm.setTextColor(Color.parseColor("#A6A6A6"));
                } else {
                    bind.tvConfirm.setTextColor(Color.parseColor("#ff2442"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        bind.tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用回调 传值
                mStateListener.close(bind.etAbout.getText().toString());
            }
        });

    }

    @Override
    public void onStart() {
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogAnim;
        super.onStart();
    }

    //创建回调，监听dialog的关闭
    public interface StateListener {
        void close(String str);
    }

    //暴露接口
    public void setStateListener(StateListener listener) {
        this.mStateListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }
}
