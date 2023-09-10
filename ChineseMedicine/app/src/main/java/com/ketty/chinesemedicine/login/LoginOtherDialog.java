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
import com.ketty.chinesemedicine.databinding.DialogLoginOtherBinding;
import com.ketty.chinesemedicine.databinding.FragmentCaptchaBinding;

public class LoginOtherDialog extends DialogFragment implements View.OnClickListener {
    private DialogLoginOtherBinding bind;
    private StateListener mStateListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DialogLoginOtherBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind.ivClose.setOnClickListener(this);
//        mQQ.setOnClickListener(this);
        bind.icPhone.setOnClickListener(this);
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
            /*case R.id.ic_qq:
                if (!mCheckBox.isChecked()) {
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
                            mStateListener.go2qq();
                            mAgreeAndContinueDialog.dismiss();
                        }
                    });
                } else {
                    mStateListener.go2qq();
                }
                break;*/
            case R.id.ic_phone:
                mStateListener.go2phone();
                break;
        }
    }

    public interface StateListener {
        void close();
//        void go2qq();
        void go2phone();
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
