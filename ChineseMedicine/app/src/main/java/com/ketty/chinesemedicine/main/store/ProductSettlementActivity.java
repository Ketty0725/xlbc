package com.ketty.chinesemedicine.main.store;

import android.content.Intent;
import android.graphics.Color;
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
import com.ketty.chinesemedicine.adapter.SettlementAdapter;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.databinding.ActivityProductSettlementBinding;
import com.ketty.chinesemedicine.entity.Orderform;
import com.ketty.chinesemedicine.entity.Product;
import com.ketty.chinesemedicine.entity.Productshopcart;
import com.ketty.chinesemedicine.entity.Shoppingaddress;
import com.ketty.chinesemedicine.main.store.orderform.OrderFormActivity;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.MMKVUtil;
import com.ketty.chinesemedicine.util.T;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public class ProductSettlementActivity extends BaseActivity implements View.OnClickListener {
    private ActivityProductSettlementBinding bind;
    private MMKVUtil mmkvUtil;
    private String uId;
    private List<Productshopcart> list;
    private List<Product> products;
    private Shoppingaddress shoppingaddress;

    @Override
    protected View initLayout() {
        bind = ActivityProductSettlementBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        mmkvUtil = App.getMmkvUtil();
        uId = mmkvUtil.getString("user","uId",null);
        Bundle bundle = getIntent().getExtras();
        list = (List<Productshopcart>) bundle.getSerializable("list");
        String price = bundle.getString("price");
        List<TextColorSizeHelper.SpanInfo> spanInfos = new ArrayList<TextColorSizeHelper.SpanInfo>();
        spanInfos.add(
                new TextColorSizeHelper.SpanInfo(
                        price.substring(0, 1),
                        DisplayUtil.dip2px(this, 14),
                        Color.parseColor("#ff2442"),
                        false
                )
        );
        spanInfos.add(
                new TextColorSizeHelper.SpanInfo(
                        price.substring(1, price.indexOf(".")),
                        DisplayUtil.dip2px(this, 18),
                        Color.parseColor("#ff2442"),
                        false
                )
        );
        spanInfos.add(
                new TextColorSizeHelper.SpanInfo(
                        price.substring(price.indexOf(".")),
                        DisplayUtil.dip2px(this, 14),
                        Color.parseColor("#ff2442"),
                        false
                )
        );
        bind.tvPrice.setText(TextColorSizeHelper.getTextSpan(this, price, spanInfos));

        bind.ivBack.setOnClickListener(this);
        bind.slSubmitOrder.setOnClickListener(this);
        bind.slAddress.setOnClickListener(this);
        initRequest();
    }

    private void initRequest() {
        List<Long> ids = new ArrayList<>();
        for (Productshopcart bean : list) {
            ids.add(bean.getProductId());
        }
        String data = JsonHelper.parserList2Json(ids, new TypeToken<List<Long>>() {}.getType());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), data);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/product/getByIds", body)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        products = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Product>>() {}.getType());
                        initRecyclerView();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });

        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", uId);
        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/shoppingaddress/getDefaultByUserId", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        shoppingaddress = JsonHelper.parserJson2Object(
                                data, Shoppingaddress.class);
                        if (shoppingaddress != null) {
                            bind.tvName.setText(shoppingaddress.getName());
                            bind.tvPhone.setText(shoppingaddress.getPhone());
                            bind.tvAddress.setText(shoppingaddress.getAddress());
                        } else {
                            bind.tvAddress.setVisibility(View.INVISIBLE);
                            bind.tvPhone.setVisibility(View.INVISIBLE);
                            bind.tvName.setVisibility(View.INVISIBLE);
                            bind.tvNormal.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initRecyclerView() {
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SettlementAdapter mAdapter = new SettlementAdapter(list,products);
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mAdapter.setOnItemClickListener(new SettlementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Long productId) {
                startActivity(productId, ProductDetailsActivity.class);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.sl_submit_order:
                new CommonAlertDialog(this).builder()
                        .setTitle("提交订单")
                        .setMsg("确认提交订单？")
                        .setNegativeButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                submitOrder();
                            }
                        }).setPositiveButton("再想想", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setCancelable(false).show();
                break;
            case R.id.sl_address:
                Intent intent = new Intent(this, ShippingAddressActivity.class);
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
            if (result.getResultCode() == 1) {
                shoppingaddress = (Shoppingaddress) result.getData().getSerializableExtra("data");
                bind.tvName.setText(shoppingaddress.getName());
                bind.tvPhone.setText(shoppingaddress.getPhone());
                bind.tvAddress.setText(shoppingaddress.getAddress());
            } else if (result.getResultCode() == 2) {
                setResult(RESULT_OK,new Intent());
                finish();
            }
        }
    });

    private void submitOrder() {
        List<Orderform> orderList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Orderform order = new Orderform();
            order.setId(null);
            order.setUserId(list.get(i).getUserId());
            order.setProductId(list.get(i).getProductId());
            order.setAddressId(shoppingaddress.getId());
            order.setNumber(list.get(i).getNumber());
            float price = products.get(i).getPrice().floatValue() * list.get(i).getNumber();
            price = Math.round(price * 100) / 100f;
            order.setPrice(new BigDecimal(Float.toString(price)));
            order.setState(0);
            orderList.add(order);
        }

        String data = JsonHelper.parserList2Json(orderList, new TypeToken<List<Orderform>>() {}.getType());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), data);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/orderform/add", body)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        T.showShort("提交订单成功");
                        removeShopCart();
                        Intent intent = new Intent(ProductSettlementActivity.this, OrderFormActivity.class);
                        mActivityLauncher.launch(intent);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void removeShopCart() {
        String data = JsonHelper.parserList2Json(list, new TypeToken<List<Productshopcart>>() {}.getType());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), data);
        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/productshopcart/deleteToRedis", body)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {

                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void startActivity(Long id, Class<?> cls) {
        Intent intent = new Intent(this, cls);
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }

}