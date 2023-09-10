package com.ketty.chinesemedicine.main.home.chineseherb;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.databinding.ActivityChineseHerbFlowCategoryBinding;
import com.ketty.chinesemedicine.databinding.ActivityDetailsBinding;
import com.ketty.chinesemedicine.main.home.FlowCategoryFragment;
import com.ketty.chinesemedicine.main.search.SearchHistoryActivity;

public class ChineseHerbFlowCategoryActivity extends BaseActivity implements View.OnClickListener {
    private ActivityChineseHerbFlowCategoryBinding bind;
    private String title;

    @Override
    protected View initLayout() {
        bind = ActivityChineseHerbFlowCategoryBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        bind.ivBack.setOnClickListener(this);
        bind.shadowSearch.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        bind.tvTitle.setText(title);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, FlowCategoryFragment.newInstance(title))
                .commit();
    }

    private void startActivity(Class<?> cls, String title) {
        Intent intent = new Intent(ChineseHerbFlowCategoryActivity.this, cls);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.shadow_search:
                startActivity(SearchHistoryActivity.class, title);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }


}