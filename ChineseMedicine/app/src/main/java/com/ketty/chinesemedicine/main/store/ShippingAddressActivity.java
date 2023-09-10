package com.ketty.chinesemedicine.main.store;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.ShoppingAddressAdapter;
import com.ketty.chinesemedicine.databinding.ActivityShippingAddressBinding;
import com.ketty.chinesemedicine.entity.Shoppingaddress;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.MMKVUtil;
import com.ketty.chinesemedicine.util.T;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShippingAddressActivity extends BaseActivity implements View.OnClickListener {
    private ActivityShippingAddressBinding bind;
    private ShoppingAddressAdapter mAdapter;
    private MMKVUtil mmkvUtil;
    private String uId;
    private int type;

    @Override
    protected View initLayout() {
        bind = ActivityShippingAddressBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        mmkvUtil = App.getMmkvUtil();
        uId = mmkvUtil.getString("user","uId",null);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");
        bind.slAddNewAddress.setOnClickListener(this);
        bind.ivBack.setOnClickListener(this);
        initRequest();
    }

    private void initRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", uId);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/shoppingaddress/getByUserId", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        List<Shoppingaddress> list = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Shoppingaddress>>() {}.getType());
                        if (list.size() > 0) {
                            initRecyclerView(list);
                            bind.llEmptyLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initRecyclerView(List<Shoppingaddress> list) {
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShoppingAddressAdapter(list);
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mAdapter.setOnItemClickListener(new ShoppingAddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Shoppingaddress bean) {
                if (type == 0) {
                    Intent intent = new Intent();
                    intent.putExtra("data",bean);
                    setResult(1,intent);
                    finish();
                } else {
                    Intent intent = new Intent(ShippingAddressActivity.this, AddNewAddressActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type",1);
                    bundle.putSerializable("data", (Serializable) bean);
                    intent.putExtras(bundle);
                    mActivityLauncher.launch(intent);
                }
            }

            @Override
            public void onItemDelete(Shoppingaddress bean) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", bean.getId());
                RetrofitManager
                        .getInstance()
                        .getApiService()
                        .postMethod("/shoppingaddress/delete", map)
                        .compose(RxHelper.observableIO2Main(ShippingAddressActivity.this))
                        .subscribe(new BaseObserver() {
                            @Override
                            public void onSuccess(Map<String, Object> response) {
                                T.showShort("删除成功");
                                mAdapter.delete(bean);
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg) {

                            }
                        });
            }

            @Override
            public void onItemAlter(Shoppingaddress bean) {
                Intent intent = new Intent(ShippingAddressActivity.this, AddNewAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type",1);
                bundle.putSerializable("data", (Serializable) bean);
                intent.putExtras(bundle);
                mActivityLauncher.launch(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.sl_add_new_address:
                Intent intent = new Intent(this, AddNewAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type",0);
                intent.putExtras(bundle);
                mActivityLauncher.launch(intent);
                break;
        }
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
        map.put("userId", uId);
        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/shoppingaddress/getByUserId", map)
                .compose(RxHelper.observableIO2Main(ShippingAddressActivity.this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        List<Shoppingaddress> list = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Shoppingaddress>>() {}.getType());
                        if (list.size() > 0) {
                            mAdapter.update(list);
                            bind.llEmptyLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }

}