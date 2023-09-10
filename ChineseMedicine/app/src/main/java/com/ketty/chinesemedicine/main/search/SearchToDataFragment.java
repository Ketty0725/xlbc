package com.ketty.chinesemedicine.main.search;

import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEHERB;
import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEPATENTMEDICINE;
import static com.ketty.chinesemedicine.main.search.SearchType.MEDICATEDDIET;
import static com.ketty.chinesemedicine.main.search.SearchType.PRESCRIPTION;
import static com.ketty.chinesemedicine.main.search.SearchType.TYPHOIDTHEORY;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.databinding.FragmentCaptchaBinding;
import com.ketty.chinesemedicine.databinding.FragmentSearchToDataBinding;
import com.ketty.chinesemedicine.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchToDataFragment extends Fragment {
    private FragmentSearchToDataBinding bind;
    private static final String ARG_PARAM1 = "type";
    private static final String ARG_PARAM2 = "title";
    private int type;
    private String title;
    private FragmentManager fm;

    public static SearchToDataFragment newInstance(int type, String title) {
        SearchToDataFragment fragment = new SearchToDataFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, type);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_PARAM1);
            title = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentSearchToDataBinding.inflate(inflater, container, false);
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

        if (type != CHINESEHERB && type != PRESCRIPTION && type != CHINESEPATENTMEDICINE
                && type != MEDICATEDDIET && type != TYPHOIDTHEORY) {
            type = 0;
        }

        LogUtils.i(String.valueOf(type));
        LogUtils.i("title",title);
        initTabLayout();
    }

    private void initTabLayout() {
        List<String> mTitleList = new ArrayList<>();
        mTitleList.add("中药");
        mTitleList.add("方剂");
        mTitleList.add("中成药");
        mTitleList.add("药膳");
        mTitleList.add("伤寒论");

        bind.tabLayout.setTitle(mTitleList);
        bind.tabLayout.getTabAt(type).select();
        initFragment(DataListFragment.newInstance(type,title));

        bind.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                initFragment(DataListFragment.newInstance(tab.getPosition(), title));
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