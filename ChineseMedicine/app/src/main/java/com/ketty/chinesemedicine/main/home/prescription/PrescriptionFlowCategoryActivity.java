package com.ketty.chinesemedicine.main.home.prescription;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.AppBarLayout;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.adapter.FragmentViewPagerAdapter;
import com.ketty.chinesemedicine.databinding.ActivityDetailsBinding;
import com.ketty.chinesemedicine.databinding.ActivityPrescriptionFlowCategoryBinding;
import com.ketty.chinesemedicine.main.home.FlowCategoryFragment;
import com.ketty.chinesemedicine.main.search.SearchHistoryActivity;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionFlowCategoryActivity extends BaseActivity implements View.OnClickListener {
    private ActivityPrescriptionFlowCategoryBinding bind;
    private String title;

    @Override
    protected View initLayout() {
        bind = ActivityPrescriptionFlowCategoryBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        bind.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int Offset = Math.abs(verticalOffset);
                bind.motionLayout.setProgress((float)Offset / appBarLayout.getTotalScrollRange());
            }
        });

        bind.ivBack.setOnClickListener(this);
        bind.shadowSearch.setOnClickListener(this);
        bind.ivSearchNormal.setOnClickListener(this);
        bind.ivSearch.setOnClickListener(this);
        bind.tvCommon.setOnClickListener(this);
        bind.tvClassic.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");

        initViewPager();
    }

    private void initViewPager() {
        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(FlowCategoryFragment.newInstance("常用方"));
        mFragmentList.add(PrescriptionListCategoryFragment.newInstance("经方"));

        FragmentViewPagerAdapter mViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),getLifecycle(),mFragmentList);
        bind.viewpager.setAdapter(mViewPagerAdapter);
        bind.viewpager.setSaveEnabled(false);
        bind.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setToggleTabs(bind.tvCommon, bind.tvClassic);
                } else if (position == 1) {
                    setToggleTabs(bind.tvClassic, bind.tvCommon);
                }
            }
        });
    }

    private void startActivity(String title, Class<?> cls) {
        Intent intent = new Intent(PrescriptionFlowCategoryActivity.this, cls);
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
            case R.id.iv_search_normal:
            case R.id.iv_search:
                startActivity(title, SearchHistoryActivity.class);
                break;
            case R.id.tv_common:
                setToggleTabs(bind.tvCommon, bind.tvClassic);
                bind.viewpager.setCurrentItem(0);
                break;
            case R.id.tv_classic:
                setToggleTabs(bind.tvClassic, bind.tvCommon);
                bind.viewpager.setCurrentItem(1);
                break;
        }
    }

    private void setToggleTabs(TextView tv1, TextView tv2) {
        tv1.setTextColor(0xFF58bec0);
        tv2.setTextColor(0xFF6b6a6f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }

}