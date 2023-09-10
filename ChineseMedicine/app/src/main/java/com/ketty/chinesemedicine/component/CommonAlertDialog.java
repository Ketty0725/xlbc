package com.ketty.chinesemedicine.component;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ketty.chinesemedicine.R;
import com.lihang.ShadowLayout;

public class CommonAlertDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout layout_bg, dialog_group;
    private TextView tv_title, tv_msg, tv_neg, tv_pos;
    private EditText et_result;
    private ImageView dialog_line, img_line;
    private ShadowLayout sl_neg, sl_pos;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showEditText = false;
    private boolean showLayout = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;

    public CommonAlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public CommonAlertDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.toast_common_dialog, null);

        // 获取自定义Dialog布局中的控件
        layout_bg = (LinearLayout) view.findViewById(R.id.layout_bg);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);
        tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setVisibility(View.GONE);
        et_result = (EditText) view.findViewById(R.id.et_result);
        et_result.setVisibility(View.GONE);
        dialog_group = (LinearLayout) view.findViewById(R.id.dialog_group);
        dialog_group.setVisibility(View.GONE);
        dialog_line = (ImageView) view.findViewById(R.id.dialog_line);
        sl_neg = (ShadowLayout) view.findViewById(R.id.sl_neg);
        sl_neg.setVisibility(View.GONE);
        sl_pos = (ShadowLayout) view.findViewById(R.id.sl_pos);
        sl_pos.setVisibility(View.GONE);
        tv_neg = (TextView) view.findViewById(R.id.tv_neg);
        tv_neg.setVisibility(View.GONE);
        tv_pos = (TextView) view.findViewById(R.id.tv_pos);
        tv_pos.setVisibility(View.GONE);
        img_line = (ImageView) view.findViewById(R.id.img_line);
        img_line.setVisibility(View.GONE);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        layout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.8),
                LayoutParams.WRAP_CONTENT));

        return this;
    }

    public CommonAlertDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            tv_title.setText("标题");
        } else {
            tv_title.setText(title);
        }
        return this;
    }

    public CommonAlertDialog setEditText(String msg) {
        showEditText = true;
        if ("".equals(msg)) {
            et_result.setHint("内容");
        } else {
            et_result.setHint(msg);
        }
        return this;
    }

    public CommonAlertDialog setEditType(int editType) {
        et_result.setInputType(editType);
        return this;
    }

    public String getResult() {
        return et_result.getText().toString();
    }

    public CommonAlertDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            tv_msg.setText("内容");
        } else {
            tv_msg.setText(msg);
        }
        return this;
    }

    public CommonAlertDialog setView(View view) {
        showLayout = true;
        if (view == null) {
            showLayout = false;
        } else
            dialog_group.addView(view, android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        return this;
    }

    public CommonAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public CommonAlertDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public CommonAlertDialog setPositiveButton(String text, final OnClickListener listener) {
        showPosBtn = true;
        if ("".equals(text)) {
            tv_pos.setText("确定");
        } else {
            tv_pos.setText(text);
        }
        sl_pos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    public CommonAlertDialog setNegativeButton(String text, final OnClickListener listener) {
        showNegBtn = true;
        if ("".equals(text)) {
            tv_neg.setText("取消");
        } else {
            tv_neg.setText(text);
        }
        sl_neg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
                dialog.dismiss();
            }
        });
        return this;
    }

    private void setLayout() {
        if (!showTitle && !showMsg) {
            tv_title.setText("提示");
            tv_title.setVisibility(View.VISIBLE);
        }

        if (showTitle) {
            tv_title.setVisibility(View.VISIBLE);
        }

        if (showEditText) {
            et_result.setVisibility(View.VISIBLE);
        }

        if (showMsg) {
            tv_msg.setVisibility(View.VISIBLE);
        }

        if (showLayout) {
            dialog_group.setVisibility(View.VISIBLE);
            dialog_line.setVisibility(View.GONE);
        }

        if (!showPosBtn && !showNegBtn) {
            tv_pos.setText("确定");
            tv_pos.setVisibility(View.VISIBLE);
            sl_pos.setVisibility(View.VISIBLE);
            sl_pos.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }

        if (showPosBtn && showNegBtn) {
            tv_pos.setVisibility(View.VISIBLE);
            tv_neg.setVisibility(View.VISIBLE);
            sl_pos.setVisibility(View.VISIBLE);
            sl_neg.setVisibility(View.VISIBLE);
            img_line.setVisibility(View.VISIBLE);
        }

        if (showPosBtn && !showNegBtn) {
            tv_pos.setVisibility(View.VISIBLE);
            sl_pos.setVisibility(View.VISIBLE);
        }

        if (!showPosBtn && showNegBtn) {
            tv_neg.setVisibility(View.VISIBLE);
            sl_neg.setVisibility(View.VISIBLE);
        }
    }

    public void show() {
        setLayout();
        dialog.show();
    }

    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }
}
