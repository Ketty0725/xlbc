package com.ketty.chinesemedicine.main.home.typhoidtheory;

import android.content.Context;
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
import com.ketty.chinesemedicine.databinding.ActivityTyphoidTheoryCategoryBinding;
import com.ketty.chinesemedicine.main.search.SearchHistoryActivity;
import com.lihang.ShadowLayout;

import java.util.ArrayList;
import java.util.List;

public class TyphoidTheoryCategoryActivity extends BaseActivity implements View.OnClickListener {
    private ActivityTyphoidTheoryCategoryBinding bind;
    private String title;

    @Override
    protected View initLayout() {
        bind = ActivityTyphoidTheoryCategoryBinding.inflate(getLayoutInflater());
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
        bind.slPrescription.setOnClickListener(this);
        bind.slChineseHerb.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");

        initViewPager();
    }

    private void initViewPager() {
        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(TyphoidTheoryCategoryFragment.newInstance("经方"));
        mFragmentList.add(TyphoidTheoryCategoryFragment.newInstance("中药"));

        FragmentViewPagerAdapter mViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),getLifecycle(),mFragmentList);
        bind.viewpager.setAdapter(mViewPagerAdapter);
        bind.viewpager.setSaveEnabled(false);
        bind.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    setToggleTabs(bind.slChineseHerb,bind.tvChineseHerb,bind.slPrescription,bind.tvPrescription);
                } else if (position == 1) {
                    setToggleTabs(bind.slPrescription,bind.tvPrescription,bind.slChineseHerb,bind.tvChineseHerb);
                }
            }
        });
    }

    private void startActivity(String title, Class<?> cls) {
        Intent intent = new Intent(getContext(), cls);
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
            case R.id.sl_prescription:
                setToggleTabs(bind.slChineseHerb,bind.tvChineseHerb,bind.slPrescription,bind.tvPrescription);
                bind.viewpager.setCurrentItem(0);
                break;
            case R.id.sl_chinese_herb:
                setToggleTabs(bind.slPrescription,bind.tvPrescription,bind.slChineseHerb,bind.tvChineseHerb);
                bind.viewpager.setCurrentItem(1);
                break;
        }
    }

    private void setToggleTabs(ShadowLayout sl1, TextView tv1, ShadowLayout sl2, TextView tv2) {
        sl1.setLayoutBackground(0xFF58bec0);
        tv1.setTextColor(0xFFffffff);
        sl2.setLayoutBackground(0xFFffffff);
        tv2.setTextColor(0xFF58bec0);
    }

    private Context getContext() {
        return TyphoidTheoryCategoryActivity.this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}