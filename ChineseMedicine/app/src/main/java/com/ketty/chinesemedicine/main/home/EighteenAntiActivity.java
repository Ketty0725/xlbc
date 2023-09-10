package com.ketty.chinesemedicine.main.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.databinding.ActivityChineseHerbBinding;
import com.ketty.chinesemedicine.databinding.ActivityDetailsBinding;
import com.ketty.chinesemedicine.databinding.ActivityEighteenAntiBinding;

public class EighteenAntiActivity extends BaseActivity implements View.OnClickListener {
    private ActivityEighteenAntiBinding bind;
    private String title;

    @Override
    protected View initLayout() {
        bind = ActivityEighteenAntiBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        bind.ivBack.setOnClickListener(this);

        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        bind.tvTitle.setText(title);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}