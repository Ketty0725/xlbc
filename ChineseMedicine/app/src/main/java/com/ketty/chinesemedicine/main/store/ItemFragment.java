package com.ketty.chinesemedicine.main.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.StoreAdapter;
import com.ketty.chinesemedicine.databinding.FragmentItemBinding;
import com.ketty.chinesemedicine.entity.Product;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.T;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFragment extends RxFragment {
    private FragmentItemBinding bind;
    private static final String ARG_PARAM = "id";
    private long id;
    private StoreAdapter mAdapter;

    public static ItemFragment newInstance(long id) {
        ItemFragment fragment = new ItemFragment();
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
        bind = FragmentItemBinding.inflate(inflater, container, false);
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
        map.put("categoryId", id);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/product/getItemList", map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        List<Product> list = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Product>>() {}.getType());
                        initRecyclerView(list);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initRecyclerView(List<Product> list) {
        mAdapter = new StoreAdapter(list);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bind.recyclerView.setLayoutManager(layoutManager);
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mAdapter.setOnItemClickListener(new StoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Long id) {
                startActivity(id, ProductDetailsActivity.class);
            }

            @Override
            public void addShopCart(Long productId) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", productId);
                RetrofitManager
                        .getInstance()
                        .getApiService()
                        .postMethod("/product/getInventory", map)
                        .compose(RxHelper.observableIO2Main(getContext()))
                        .subscribe(new BaseObserver() {
                            @Override
                            public void onSuccess(Map<String, Object> response) {
                                String data = JsonHelper.parserObject2Json(response.get("result"));
                                Integer number = JsonHelper.parserJson2Object(data,Integer.class);
                                ItemFragment.this.addShopCart(productId, number);
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg) {

                            }
                        });
            }
        });
    }

    private void addShopCart(Long productId, Integer number) {
        if (number > 0) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", App.getMmkvUtil().getString("user","uId",null));
            map.put("productId", productId);
            RetrofitManager
                    .getInstance()
                    .getApiService()
                    .postMethod("/productshopcart/isExistsFromAll", map)
                    .compose(RxHelper.observableIO2Main(getContext()))
                    .subscribe(new BaseObserver() {
                        @Override
                        public void onSuccess(Map<String, Object> response) {
                            T.showShort("已经在购物车里了哦~");
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg) {
                            RetrofitManager
                                    .getInstance()
                                    .getApiService()
                                    .postMethod("/productshopcart/addOrIncrementToRedis", map)
                                    .compose(RxHelper.observableIO2Main(getContext()))
                                    .subscribe(new BaseObserver() {
                                        @Override
                                        public void onSuccess(Map<String, Object> response) {
                                            T.showShort("添加成功");
                                        }

                                        @Override
                                        public void onFailure(Throwable e, String errorMsg) {

                                        }
                                    });
                        }
                    });
        } else {
            T.showShort("库存不足");
        }
    }

    private void startActivity(Long id, Class<?> cls) {
        Intent intent = new Intent(getContext(), cls);
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }
}