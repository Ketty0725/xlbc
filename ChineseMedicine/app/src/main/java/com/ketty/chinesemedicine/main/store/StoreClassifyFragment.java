package com.ketty.chinesemedicine.main.store;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.ClassfiyMenuTabAdapter;
import com.ketty.chinesemedicine.adapter.FragmentViewPagerAdapter;
import com.ketty.chinesemedicine.databinding.FragmentStoreClassifyBinding;
import com.ketty.chinesemedicine.entity.Productcategory;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.widget.TabView;

public class StoreClassifyFragment extends RxFragment {
    private FragmentStoreClassifyBinding bind;
    private static final String ARG_PARAM = "id";
    private long id;
    private ClassfiyMenuTabAdapter mAdapter;
    private FragmentViewPagerAdapter mViewPagerAdapter;

    public static StoreClassifyFragment newInstance(long id) {
        StoreClassifyFragment fragment = new StoreClassifyFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentStoreClassifyBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRequest();
    }

    private void initRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("parentId", id);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/productcategory/getListByChild", map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        List<Productcategory> list = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Productcategory>>() {}.getType());
                        initViewPager(list);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initViewPager(List<Productcategory> list) {
        if (list.size() > 0) {
            List<String> mTitleList = new ArrayList<>();
            List<Fragment> mFragmentList = new ArrayList<>();

            for (Productcategory p : list) {
                mTitleList.add(p.getCategoryName());
                mFragmentList.add(ItemFragment.newInstance(p.getCategoryId()));
            }

            mAdapter = new ClassfiyMenuTabAdapter(mTitleList);
            bind.tabLayout.setTabAdapter(mAdapter);

            mViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(),getLifecycle(),mFragmentList);
            bind.viewPager.setAdapter(mViewPagerAdapter);
            bind.viewPager.setSaveEnabled(false);
            try {
                Field mRecyclerView = bind.viewPager.getClass().getDeclaredField("mRecyclerView");
                mRecyclerView.setAccessible(true);
                RecyclerView recyclerView = (RecyclerView) mRecyclerView.get(bind.viewPager);
                recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            } catch (Exception ignore) {
            }

            setupWithViewPager(bind.viewPager, bind.tabLayout);
        }
    }

    public void setupWithViewPager(@Nullable ViewPager2 viewPager, @Nullable VerticalTabLayout tabLayout) {
        tabLayout.addOnTabSelectedListener(new VerticalTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabView tab, int position) {
                if (viewPager != null && viewPager.getAdapter().getItemCount() >= position) {
                    viewPager.setCurrentItem(position);
                }
            }
            @Override
            public void onTabReselected(TabView tab, int position) {
            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.setTabSelected(position,true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }
}