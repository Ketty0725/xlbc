package com.ketty.chinesemedicine.component.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.ketty.chinesemedicine.R;
import com.lihang.ShadowLayout;

public class TipsAlertDialog {
    private Context context;
    private Dialog dialog;
    private LinearLayout layout_bg, dialog_group;
    private TextView tv_title, tv_msg, tv_sure;
    private ShadowLayout sl_sure;
    private Display display;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showLayout = false;
    private boolean showSure = false;

    public TipsAlertDialog(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    public TipsAlertDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(R.layout.toast_tips_dialog, null);

        // 获取自定义Dialog布局中的控件
        layout_bg = (LinearLayout) view.findViewById(R.id.layout_bg);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setVisibility(View.GONE);
        tv_msg = (TextView) view.findViewById(R.id.tv_msg);
        tv_msg.setVisibility(View.GONE);
        dialog_group = (LinearLayout) view.findViewById(R.id.dialog_group);
        dialog_group.setVisibility(View.GONE);
        sl_sure = (ShadowLayout) view.findViewById(R.id.sl_sure);
        sl_sure.setVisibility(View.GONE);
        tv_sure = (TextView) view.findViewById(R.id.tv_sure);
        tv_sure.setVisibility(View.GONE);

        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.AlertDialogStyle);
        dialog.setContentView(view);

        // 调整dialog背景大小
        layout_bg.setLayoutParams(new FrameLayout.LayoutParams((int) (display.getWidth() * 0.8),
                LayoutParams.WRAP_CONTENT));

        return this;
    }

    public TipsAlertDialog setTitle(String title) {
        showTitle = true;
        if ("".equals(title)) {
            tv_title.setText("标题");
        } else {
            tv_title.setText(title);
        }
        return this;
    }

    public TipsAlertDialog setMsg(String msg) {
        showMsg = true;
        if ("".equals(msg)) {
            tv_msg.setText("内容");
        } else {
            tv_msg.setText(msg);
        }
        return this;
    }

    public TipsAlertDialog setView(View view) {
        showLayout = true;
        if (view == null) {
            showLayout = false;
        } else
            dialog_group.addView(view, android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        return this;
    }

    public TipsAlertDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public TipsAlertDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public TipsAlertDialog setSureButton(String text, final OnClickListener listener) {
        showSure = true;
        if ("".equals(text)) {
            tv_sure.setText("确定");
        } else {
            tv_sure.setText(text);
        }
        sl_sure.setOnClickListener(new OnClickListener() {
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

        if (showMsg) {
            tv_msg.setVisibility(View.VISIBLE);
        }

        if (showLayout) {
            dialog_group.setVisibility(View.VISIBLE);
        }

        if (showSure) {
            tv_sure.setVisibility(View.VISIBLE);
            sl_sure.setVisibility(View.VISIBLE);
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
