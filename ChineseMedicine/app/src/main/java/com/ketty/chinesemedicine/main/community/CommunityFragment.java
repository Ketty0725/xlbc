package com.ketty.chinesemedicine.main.community;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.adapter.FragmentViewPagerAdapter;
import com.ketty.chinesemedicine.databinding.FragmentCommunityBinding;
import com.ketty.chinesemedicine.main.search.SearchHistoryActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommunityFragment extends Fragment implements View.OnClickListener {
    private FragmentCommunityBinding bind;
    private FragmentViewPagerAdapter mFragmentViewPagerAdapter;
    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    private final cClassifyFragment firstFragment = cClassifyFragment.newInstance(1);
    private final cClassifyFragment secondFragment = cClassifyFragment.newInstance(2);
    private final cClassifyFragment thirdFragment = cClassifyFragment.newInstance(3);

    private static volatile CommunityFragment obj = null;

    public static CommunityFragment getInstance() {
        if (obj == null) {
            synchronized(CommunityFragment.class) {
                if (obj == null) {
                    obj = new CommunityFragment();
                }
            }
        }
        return obj;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentCommunityBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewPager();
        initView();
    }

    private void initView() {
        bind.searchView.setOnClickListener(this);
    }

    private void initViewPager() {
        String[] str = {"附近", "推荐", "关注"};
        mTitleList = Arrays.asList(str);

        mFragmentList = new ArrayList<>();
        mFragmentList.add(firstFragment);
        mFragmentList.add(secondFragment);
        mFragmentList.add(thirdFragment);

        mFragmentViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(),getLifecycle(),mFragmentList);
        bind.viewPager.setAdapter(mFragmentViewPagerAdapter);
        bind.viewPager.setSaveEnabled(false);

        new TabLayoutMediator(bind.tabLayout, bind.viewPager, new TabLayoutMediator.TabConfigurationStrategy(){
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setCustomView(R.layout.tablayout_item);
                if (tab.getCustomView() != null) {
                    TextView text = tab.getCustomView().findViewById(R.id.tab_layout_text);
                    text.setText(mTitleList.get(position));
                }
            }
        }).attach();

        bind.viewPager.setCurrentItem(1,false);
        bind.tabLayout.getTabAt(1).select();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_view:
                Intent intent = new Intent(getContext(), SearchHistoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "社区");
                intent.putExtras(bundle);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }
}