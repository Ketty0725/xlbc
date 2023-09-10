package com.ketty.chinesemedicine.main.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.databinding.FragmentSearchToCommunityBinding;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchToCommunityFragment extends RxFragment {
    private FragmentSearchToCommunityBinding bind;
    private static final String ARG_PARAM1 = "title";
    private String title;
    private FragmentManager fm;

    public static SearchToCommunityFragment newInstance(String title) {
        SearchToCommunityFragment fragment = new SearchToCommunityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentSearchToCommunityBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        fm = getChildFragmentManager();

        List<String> mTitleList = new ArrayList<>();
        mTitleList.add("笔记");
        mTitleList.add("用户");

        bind.tabLayout.setTitle(mTitleList);
        bind.tabLayout.getTabAt(0).select();
        initFragment(SearchToCommunityNoteFragment.newInstance(title));

        bind.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    initFragment(SearchToCommunityNoteFragment.newInstance(title));
                } else {
                    initFragment(SearchToCommunityUserFragment.newInstance(title));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initFragment(Fragment fragment) {
        if (fragment != null) {
            fm.beginTransaction().replace(R.id.frame_layout, fragment).commit();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }
}