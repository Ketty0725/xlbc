package com.ketty.chinesemedicine.main.mine.concern;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.adapter.FragmentViewPagerAdapter;
import com.ketty.chinesemedicine.databinding.ActivityConcernListBinding;

import java.util.ArrayList;
import java.util.List;

public class ConcernListActivity extends BaseActivity {
    private ActivityConcernListBinding bind;
    private int type;
    private long userId;
    private String userName;

    @Override
    protected View initLayout() {
        bind = ActivityConcernListBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");
        userId = bundle.getLong("userId");
        userName = bundle.getString("userName");
        bind.tvTitle.setText(userName);

        bind.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initViewPager();
    }

    private void initViewPager() {
        List<String> mTitleList = new ArrayList<>();
        mTitleList.add("关注");
        mTitleList.add("粉丝");

        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(ConcernListFragment.newInstance(0, userId));
        mFragmentList.add(ConcernListFragment.newInstance(1, userId));

        FragmentViewPagerAdapter mViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),getLifecycle(),mFragmentList);
        bind.viewpager.setAdapter(mViewPagerAdapter);
        bind.viewpager.setSaveEnabled(false);

        new TabLayoutMediator(bind.tabLayout, bind.viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mTitleList.get(position));
            }
        }).attach();

        bind.tabLayout.getTabAt(type).select();
        bind.viewpager.setCurrentItem(type, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}