package com.ketty.chinesemedicine.main.store;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.adapter.StoreAdapter;

public class ProductTitleBar extends FrameLayout {
    public LinearLayout leoBar;
    public TextView txt_title;
    public RelativeLayout bar_left_btn;
    public ImageView image_left;
    public TextView bar_right_text;
    public RelativeLayout bar_right_btn;
    public ImageView image_right;
    //这个用来添加子view
    public RelativeLayout view_container;
    public TextView line;

    public ProductTitleBar(@NonNull Context context) {
        this(context, null);
    }

    public ProductTitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProductTitleBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.product_toolbar_layout, this);
        leoBar = findViewById(R.id.leoBar);
        txt_title = findViewById(R.id.txt_title);
        bar_left_btn = findViewById(R.id.bar_left_btn);
        image_left = findViewById(R.id.image_left);
        bar_right_text = findViewById(R.id.bar_right_text);
        bar_right_btn = findViewById(R.id.bar_right_btn);
        image_right = findViewById(R.id.image_right);
        view_container = findViewById(R.id.view_container);
        line = findViewById(R.id.line);
        init(attrs);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            View view = getChildAt(1);
            removeViewInLayout(view);
            if (view != null) {
                view_container.addView(view);
            }
        }
    }


    private void init(AttributeSet attrs) {
        //自定义属性，
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.LeoTitleBar);

        //背景颜色值
        int backColor = typedArray.getColor(R.styleable.LeoTitleBar_hl_background, Color.WHITE);
        leoBar.setBackgroundColor(backColor);

        //toolbar标题
        int titleColor = typedArray.getColor(R.styleable.LeoTitleBar_hl_textTitleColor, Color.BLACK);
        txt_title.setTextColor(titleColor);
        bar_right_text.setTextColor(titleColor);
        int titleSize = (int) typedArray.getDimension(R.styleable.LeoTitleBar_hl_textTitleSize, 18);
        txt_title.setTextSize(titleSize);
        String titleStr = typedArray.getString(R.styleable.LeoTitleBar_hl_textTitle);
        if (TextUtils.isEmpty(titleStr)) {
            txt_title.setText("");
        } else {
            txt_title.setText(titleStr);
        }

        boolean isbold = typedArray.getBoolean(R.styleable.LeoTitleBar_hl_bold, false);
        if (isbold) {
            txt_title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));//加粗
        }
        //左边图标
        boolean isShowLeftBtn = typedArray.getBoolean(R.styleable.LeoTitleBar_hl_showLeftBtn, true);
        if (isShowLeftBtn) {
            bar_left_btn.setVisibility(View.VISIBLE);
        } else {
            bar_left_btn.setVisibility(View.GONE);
        }

        Drawable leftDrawable = typedArray.getDrawable(R.styleable.LeoTitleBar_hl_leftBtnDrawable);
        if (leftDrawable != null) {
            image_left.setImageDrawable(leftDrawable);
            image_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnClickListener.onClick(view);
                }
            });
        } else {
            image_left.setVisibility(View.GONE);
        }

        /*
         * 右边
         * */

        //右边是否显示文字
        String rightStr = typedArray.getString(R.styleable.LeoTitleBar_hl_showRightText);
        if (TextUtils.isEmpty(rightStr)) {
            bar_right_text.setVisibility(View.GONE);
        } else {
            bar_right_text.setText(rightStr);
        }

        //右边是否先是图标
        Drawable rightDrawable = typedArray.getDrawable(R.styleable.LeoTitleBar_hl_rightBtnDrawable);
        if (rightDrawable == null) {
            bar_right_btn.setVisibility(View.GONE);
        } else {
            bar_right_btn.setVisibility(View.VISIBLE);
            image_right.setImageDrawable(rightDrawable);
        }

        //分割线颜色，如果bar背景颜色和window背景颜色一致，需要分割线
        int divide_color = typedArray.getColor(R.styleable.LeoTitleBar_hl_divideColor, Color.TRANSPARENT);
        line.setBackgroundColor(divide_color);
    }

    public void setBarRightText(String text) {
        bar_right_text.setVisibility(View.VISIBLE);
        bar_right_text.setText(text);
    }

    public interface OnClickListener {
        void onClick(View view);
    }

    private OnClickListener mOnClickListener;

    public void setOnClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
    }
}
