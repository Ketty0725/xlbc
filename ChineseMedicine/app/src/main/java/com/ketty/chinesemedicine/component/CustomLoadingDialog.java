package com.ketty.chinesemedicine.component;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.ketty.chinesemedicine.R;

public class CustomLoadingDialog extends Dialog {

    public CustomLoadingDialog(Context context) {
        super(context, com.luck.picture.lib.R.style.Picture_Theme_AlertDialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_alert_dialog);
        setDialogSize();
    }

    private void setDialogSize() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        getWindow().setWindowAnimations(com.luck.picture.lib.R.style.PictureThemeDialogWindowStyle);
        getWindow().setAttributes(params);
    }
}