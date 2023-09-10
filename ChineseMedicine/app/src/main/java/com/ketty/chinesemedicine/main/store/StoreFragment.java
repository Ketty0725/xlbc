package com.ketty.chinesemedicine.main.store;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.databinding.FragmentStoreBinding;
import com.ketty.chinesemedicine.entity.Productcategory;
import com.ketty.chinesemedicine.main.search.SearchHistoryActivity;
import com.ketty.chinesemedicine.main.store.orderform.OrderFormActivity;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;
import java.util.Map;

public class StoreFragment extends RxFragment implements View.OnClickListener {
    private FragmentStoreBinding bind;
    private StoreClassifyFragment mFragment;
    private FragmentManager fm;

    private static volatile StoreFragment obj = null;

    public static StoreFragment getInstance() {
        if (obj == null) {
            synchronized(StoreFragment.class) {
                if (obj == null) {
                    obj = new StoreFragment();
                }
            }
        }
        return obj;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentStoreBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRequest();
    }

    private void initRequest() {
        RetrofitManager
                .getInstance()
                .getApiService()
                .getMethod("/productcategory/getListByFather")
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        List<Productcategory> list = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Productcategory>>() {}.getType());
                        initView(list);
                        initTabLayout(list);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initView(List<Productcategory> list) {
        fm = getChildFragmentManager();
        mFragment = StoreClassifyFragment.newInstance(list.get(0).getCategoryId());
        initFragment(mFragment);
        bind.ivShopCart.setOnClickListener(this);
        bind.ivOrderForm.setOnClickListener(this);
        bind.searchView.setOnClickListener(this);
    }

    private void initFragment(Fragment fragment) {
        if (fragment != null) {
            fm.beginTransaction().replace(R.id.frame_layout, fragment).commit();
        }
    }

    private void initTabLayout(List<Productcategory> list) {
        bind.tabLayout.setData(list);

        bind.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mFragment = StoreClassifyFragment.newInstance(list.get(tab.getPosition()).getCategoryId());
                initFragment(mFragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_shop_cart:
                startActivity(new Intent(getContext(),ShoppingCartActivity.class));
                break;
            case R.id.iv_order_form:
                startActivity(new Intent(getContext(), OrderFormActivity.class));
                break;
            case R.id.search_view:
                Intent intent = new Intent(getContext(), SearchHistoryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("title", "商城");
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