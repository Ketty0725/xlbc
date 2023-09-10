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
import com.ketty.chinesemedicine.adapter.ShoppingCartAdapter;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.databinding.ActivityShoppingCartBinding;
import com.ketty.chinesemedicine.entity.Product;
import com.ketty.chinesemedicine.entity.Productshopcart;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;
import com.ketty.chinesemedicine.util.MMKVUtil;
import com.ketty.chinesemedicine.util.T;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;

public class ShoppingCartActivity extends BaseActivity implements View.OnClickListener {
    private ActivityShoppingCartBinding bind;
    private MMKVUtil mmkvUtil;
    private String uId;
    private ShoppingCartAdapter mAdapter;
    private List<Productshopcart> list;
    private List<Productshopcart> productSelected = new ArrayList<>();
    private final ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                deleteProduct();
            }
        }
    });

    @Override
    protected View initLayout() {
        bind = ActivityShoppingCartBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        mmkvUtil = App.getMmkvUtil();
        uId = mmkvUtil.getString("user","uId",null);
        bind.ivBack.setOnClickListener(this);
        bind.scb.setOnClickListener(this);
        bind.tvSelectAll.setOnClickListener(this);
        bind.tvManage.setOnClickListener(this);
        bind.slInstantlyBuy.setOnClickListener(this);
        bind.slDelete.setOnClickListener(this);
        initRequest();
    }

    private void initRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", uId);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/productshopcart/getByUserId", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("Productshopcart"));
                        list = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Productshopcart>>() {}.getType());

                        data = JsonHelper.parserObject2Json(response.get("Product"));
                        List<Product> products = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Product>>() {}.getType());
                        initRecyclerView(list, products);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initRecyclerView(List<Productshopcart> list, List<Product> products) {
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ShoppingCartAdapter(list, products);
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mAdapter.setOnItemClickListener(new ShoppingCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Long productId) {
                startActivity(productId, ProductDetailsActivity.class);
            }

            @Override
            public void onCalculatePrice(float price) {
                String text = "￥" + price;
                List<TextColorSizeHelper.SpanInfo> spanInfos = new ArrayList<TextColorSizeHelper.SpanInfo>();
                spanInfos.add(
                        new TextColorSizeHelper.SpanInfo(
                                text.substring(0, 1),
                                DisplayUtil.dip2px(ShoppingCartActivity.this, 14),
                                Color.parseColor("#ff2442"),
                                false
                        )
                );
                spanInfos.add(
                        new TextColorSizeHelper.SpanInfo(
                                text.substring(1, text.indexOf(".")),
                                DisplayUtil.dip2px(ShoppingCartActivity.this, 18),
                                Color.parseColor("#ff2442"),
                                false
                        )
                );
                spanInfos.add(
                        new TextColorSizeHelper.SpanInfo(
                                text.substring(text.indexOf(".")),
                                DisplayUtil.dip2px(ShoppingCartActivity.this, 14),
                                Color.parseColor("#ff2442"),
                                false
                        )
                );
                bind.tvPrice.setText(TextColorSizeHelper.getTextSpan(ShoppingCartActivity.this, text, spanInfos));
            }

            @Override
            public void onAllChecked(boolean checked) {
                bind.scb.setChecked(checked,true);
            }

            @Override
            public void onFinished(List<Productshopcart> productSelected) {
                ShoppingCartActivity.this.productSelected = productSelected;
                LogUtils.i("productSelected.size() -->"+productSelected.size());
                if (productSelected.size() > 0) {
                    bind.slInstantlyBuy.setClickable(true);
                    bind.slInstantlyBuy.setLayoutBackground(0xFFff2442);
                } else {
                    bind.slInstantlyBuy.setLayoutBackground(0xFFffa5a5);
                    bind.slInstantlyBuy.setClickable(false);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.scb:
            case R.id.tv_select_all:
                if (list.size() > 0) {
                    if (bind.scb.isChecked()) {
                        bind.scb.setChecked(false,true);
                        mAdapter.revertSelected();
                    } else {
                        bind.scb.setChecked(true,true);
                        mAdapter.selectedAll();
                    }
                }
                break;
            case R.id.tv_manage:
                if (bind.tvManage.getText().equals("管理")) {
                    bind.tvManage.setText("完成");
                    bind.tvTotal.setVisibility(View.GONE);
                    bind.tvPrice.setVisibility(View.GONE);
                    bind.slInstantlyBuy.setVisibility(View.GONE);
                    bind.slDelete.setVisibility(View.VISIBLE);
                } else if (bind.tvManage.getText().equals("完成")) {
                    bind.tvManage.setText("管理");
                    bind.slDelete.setVisibility(View.GONE);
                    bind.tvTotal.setVisibility(View.VISIBLE);
                    bind.tvPrice.setVisibility(View.VISIBLE);
                    bind.slInstantlyBuy.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.sl_delete:
                if (productSelected.size() > 0) {
                    new CommonAlertDialog(this).builder()
                            .setTitle("移出购物车")
                            .setMsg("确认将这"+productSelected.size()+"个商品删除？")
                            .setNegativeButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    deleteProduct();
                                }
                            }).setPositiveButton("再想想", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).setCancelable(false)
                            .show();
                } else {
                    T.showShort("还未选中商品哦~");
                }
                break;
            case R.id.sl_instantly_buy:
                if (productSelected.size() > 0) {
                    Intent intent = new Intent(this, ProductSettlementActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("price",bind.tvPrice.getText().toString());
                    bundle.putSerializable("list", (Serializable) productSelected);
                    intent.putExtras(bundle);
                    mActivityLauncher.launch(intent);
                }
                break;
        }
    }

    private void deleteProduct() {
        String data = JsonHelper.parserList2Json(productSelected, new TypeToken<List<Productshopcart>>() {}.getType());
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=UTF-8"), data);
        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/productshopcart/deleteToRedis", body)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        for (Productshopcart bean : productSelected) {
                            mAdapter.remove(bean);
                        }
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