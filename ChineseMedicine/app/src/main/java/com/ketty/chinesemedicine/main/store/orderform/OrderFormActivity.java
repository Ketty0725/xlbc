package com.ketty.chinesemedicine.main.store.orderform;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.adapter.FragmentViewPagerAdapter;
import com.ketty.chinesemedicine.databinding.ActivityOrderFormBinding;

import java.util.ArrayList;
import java.util.List;

public class OrderFormActivity extends BaseActivity {
    private ActivityOrderFormBinding bind;
    private final OrderFormFragment firstFragment = OrderFormFragment.newInstance(-1);
    private final OrderFormFragment secondFragment = OrderFormFragment.newInstance(0);
    private final OrderFormFragment thirdFragment = OrderFormFragment.newInstance(1);
    private final OrderFormFragment fourthFragment = OrderFormFragment.newInstance(2);

    @Override
    protected View initLayout() {
        bind = ActivityOrderFormBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        initViewPager();
        bind.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(2,new Intent());
                finish();
            }
        });
    }

    private void initViewPager() {
        List<String> mTitleList = new ArrayList<>();
        mTitleList.add("全部");
        mTitleList.add("待发货");
        mTitleList.add("待收货");
        mTitleList.add("已完成");

        List<Fragment> mFragmentList = new ArrayList<>();
        mFragmentList.add(firstFragment);
        mFragmentList.add(secondFragment);
        mFragmentList.add(thirdFragment);
        mFragmentList.add(fourthFragment);

        FragmentViewPagerAdapter mViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),getLifecycle(),mFragmentList);
        bind.viewPager.setAdapter(mViewPagerAdapter);
        bind.viewPager.setSaveEnabled(false);

        new TabLayoutMediator(bind.tabLayout, bind.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mTitleList.get(position));
            }
        }).attach();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(2,new Intent());
            finish();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}