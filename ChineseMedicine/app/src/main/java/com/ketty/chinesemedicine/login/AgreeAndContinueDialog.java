package com.ketty.chinesemedicine.login;

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
import com.ketty.chinesemedicine.databinding.DialogAgreeTermsBinding;
import com.ketty.chinesemedicine.databinding.FragmentCaptchaBinding;

public class AgreeAndContinueDialog extends DialogFragment implements View.OnClickListener {
    private DialogAgreeTermsBinding bind;
    private StateListener mStateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DialogAgreeTermsBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind.ivClose.setOnClickListener(this);
        bind.agreeContinue.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                mStateListener.close();
                break;
            case R.id.agree_continue:
                mStateListener.agree();
                break;
        }
    }

    public interface StateListener {
        void close();
        void agree();
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
