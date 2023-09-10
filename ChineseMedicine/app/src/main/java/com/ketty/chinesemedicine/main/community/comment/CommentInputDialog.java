package com.ketty.chinesemedicine.main.community.comment;

import static android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.databinding.DialogCommentInputBinding;
import com.ketty.chinesemedicine.databinding.FragmentCaptchaBinding;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.T;

public class CommentInputDialog extends DialogFragment {
    private DialogCommentInputBinding bind;
    private StateListener mStateListener;
    private int mLastDiff = 0;
    private int maxNumber = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DialogCommentInputBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bind.editor.requestFocus();
        bind.editor.setImeOptions(EditorInfo.IME_ACTION_SEND);
        bind.editor.setInputType(TYPE_TEXT_FLAG_MULTI_LINE);
        bind.editor.setSingleLine(false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            String editor = bundle.getString("editor");
            bind.editor.setHint(editor);
        }

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        bind.editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(bind.editor.getText().toString())) {
                    bind.slSend.setVisibility(View.GONE);
                } else {
                    bind.slSend.setVisibility(View.VISIBLE);
                }
                if (bind.editor.getLineCount() > 1) {
                    bind.shadowLayout.setCornerRadius(DisplayUtil.dip2px(getContext(),20));
                } else {
                    bind.shadowLayout.setCornerRadius(DisplayUtil.dip2px(getContext(),50));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                bind.tvNumber.setText(editable.length() + "/" + maxNumber);
            }
        });

        bind.editor.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEND:
                        if (bind.editor.getText().length() > 0) {
                            mStateListener.onTextSend(bind.editor.getText().toString());
                            dismiss();
                        } else {
                            T.showShort("请输入文字");
                            return true;
                        }
                    default:
                        return false;
                }
            }
        });

        bind.slSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStateListener.onTextSend(bind.editor.getText().toString());
                dismiss();
            }
        });

        bind.rlInputdlgView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                Rect r = new Rect();
                //获取当前界面可视部分
                getDialog().getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                //获取屏幕的高度
                int screenHeight = getDialog().getWindow().getDecorView().getRootView().getHeight();
                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                int heightDifference = screenHeight - r.bottom;

                if (heightDifference <= 0 && mLastDiff > 0) {
                    dismiss();
                }
                mLastDiff = heightDifference;
            }
        });

    }

    @Override
    public void onStart() {
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        params.dimAmount = 0.2F;
        params.windowAnimations = R.style.dialogAnim;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(true);
        super.onStart();
    }

    //创建回调，监听dialog的关闭
    public interface StateListener {
        void onTextSend(String msg);

        void dismiss();
    }

    //暴露接口
    public void setStateListener(StateListener listener) {
        this.mStateListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mLastDiff = 0;
        if (mStateListener != null) mStateListener.dismiss();
        bind = null;
    }
}
