package com.ketty.chinesemedicine.main.store.orderform;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.OrderFormAdapter;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.databinding.FragmentOrderFormBinding;
import com.ketty.chinesemedicine.entity.Orderform;
import com.ketty.chinesemedicine.entity.Product;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.T;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public class OrderFormFragment extends RxFragment {
    private FragmentOrderFormBinding bind;
    private static final String ARG_PARAM = "param";
    private int state;
    private OrderFormAdapter mAdapter;
    private List<Orderform> list;
    private List<Product> products;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isResumed()){
                resume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()){
            resume();
        }
    }

    private void resume() {
        initRequest();
    }

    public static OrderFormFragment newInstance(int state) {
        OrderFormFragment fragment = new OrderFormFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, state);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            state = getArguments().getInt(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentOrderFormBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
    }

    private void initRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",App.getMmkvUtil().getString("user","uId",null));
        map.put("state",state);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/orderform/getListByState", map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        list = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Orderform>>() {}.getType());

                        if (list.size() > 0) {
                            List<Long> ids = new ArrayList<>();
                            for (Orderform order : list) {
                                ids.add(order.getProductId());
                            }
                            data = JsonHelper.parserList2Json(ids, new TypeToken<List<Long>>() {}.getType());
                            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), data);

                            RetrofitManager
                                    .getInstance()
                                    .getApiService()
                                    .postMethod("/product/getByIds", body)
                                    .compose(RxHelper.observableIO2Main(getContext()))
                                    .subscribe(new BaseObserver() {
                                        @Override
                                        public void onSuccess(Map<String, Object> response) {
                                            String data = JsonHelper.parserObject2Json(response.get("result"));
                                            products = JsonHelper.parserJson2List(
                                                    data, new TypeToken<List<Product>>() {}.getType());
                                            mAdapter.setData(list,products);
                                        }

                                        @Override
                                        public void onFailure(Throwable e, String errorMsg) {

                                        }
                                    });
                        } else {
                            products = new ArrayList<>();
                            mAdapter.setData(list,products);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initRecyclerView() {
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new OrderFormAdapter();
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mAdapter.setOnItemClickListener(new OrderFormAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Orderform bean) {
                Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) bean);
                intent.putExtras(bundle);
                mActivityLauncher.launch(intent);
            }

            @Override
            public void onAlterState(Orderform bean, Integer newState, String msg1, String msg2) {
                new CommonAlertDialog(getContext()).builder()
                        .setTitle(msg1)
                        .setNegativeButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("id",bean.getId());
                                map.put("state",newState);

                                RetrofitManager
                                        .getInstance()
                                        .getApiService()
                                        .postMethod("/orderform/alterState", map)
                                        .compose(RxHelper.observableIO2Main(getContext()))
                                        .subscribe(new BaseObserver() {
                                            @Override
                                            public void onSuccess(Map<String, Object> response) {
                                                T.showShort(msg2);
                                                updateData();
                                            }

                                            @Override
                                            public void onFailure(Throwable e, String errorMsg) {

                                            }
                                        });
                            }
                        }).setPositiveButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setCancelable(false).show();
            }
        });
    }

    private final ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                updateData();
            }
        }
    });

    private void updateData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId",App.getMmkvUtil().getString("user","uId",null));
        map.put("state",state);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/orderform/getListByState", map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        list = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Orderform>>() {}.getType());

                        if (list.size() > 0) {
                            List<Long> ids = new ArrayList<>();
                            for (Orderform order : list) {
                                ids.add(order.getProductId());
                            }
                            data = JsonHelper.parserList2Json(ids, new TypeToken<List<Long>>() {}.getType());
                            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), data);

                            RetrofitManager
                                    .getInstance()
                                    .getApiService()
                                    .postMethod("/product/getByIds", body)
                                    .compose(RxHelper.observableIO2Main(getContext()))
                                    .subscribe(new BaseObserver() {
                                        @Override
                                        public void onSuccess(Map<String, Object> response) {
                                            String data = JsonHelper.parserObject2Json(response.get("result"));
                                            products = JsonHelper.parserJson2List(
                                                    data, new TypeToken<List<Product>>() {}.getType());
                                            mAdapter.update(list,products);
                                        }

                                        @Override
                                        public void onFailure(Throwable e, String errorMsg) {

                                        }
                                    });
                        } else {
                            products.clear();
                            mAdapter.update(list,products);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

}