package com.ketty.chinesemedicine.main.store.orderform;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.databinding.ActivityOrderDetailsBinding;
import com.ketty.chinesemedicine.entity.Orderform;
import com.ketty.chinesemedicine.entity.Product;
import com.ketty.chinesemedicine.entity.Shoppingaddress;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.T;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetailsActivity extends BaseActivity implements View.OnClickListener {
    private ActivityOrderDetailsBinding bind;
    private Orderform orderform;
    private boolean isAlter = false;

    @Override
    protected View initLayout() {
        bind = ActivityOrderDetailsBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        orderform = (Orderform) bundle.getSerializable("data");
        initRequest();

        bind.ivBack.setOnClickListener(this);
        bind.slSubmitOrder.setOnClickListener(this);
    }

    private void setState() {
        switch (orderform.getState()) {
            case 0:
                bind.tvTitle.setText("待发货");
                bind.tvTitle.setTextColor(0xFFff2442);
                bind.slSubmitOrder.setLayoutBackground(0xFFFFFFFF);
                bind.slSubmitOrder.setStrokeColor(0xFFCDCDCD);
                bind.tvSubmitOrder.setText("取消订单");
                bind.tvSubmitOrder.setTextColor(0xFF333333);
                break;
            case 1:
                bind.tvTitle.setText("待收货");
                bind.tvTitle.setTextColor(0xFFff2442);
                bind.slSubmitOrder.setLayoutBackground(0xFFff2442);
                bind.slSubmitOrder.setStrokeColor(0xFFff2442);
                bind.tvSubmitOrder.setText("确认收货");
                bind.tvSubmitOrder.setTextColor(0xFFFFFFFF);
                break;
            case 2:
                bind.tvTitle.setText("已完成");
                bind.tvTitle.setTextColor(0xFF333333);
                bind.slSubmitOrder.setLayoutBackground(0xFFFFFFFF);
                bind.slSubmitOrder.setStrokeColor(0xFFCDCDCD);
                bind.tvSubmitOrder.setText("删除订单");
                bind.tvSubmitOrder.setTextColor(0xFF333333);
                break;
        }
    }

    private void initRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", orderform.getProductId());

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/product/getById", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        Product product = JsonHelper.parserJson2Object(
                                data, Product.class);
                        Glide.with(OrderDetailsActivity.this).load(product.getImage()).into(bind.ivThumbnail);
                        bind.tvName.setText(product.getName());
                        String text = "￥" + product.getPrice();
                        List<TextColorSizeHelper.SpanInfo> spanInfos = new ArrayList<TextColorSizeHelper.SpanInfo>();
                        spanInfos.add(
                                new TextColorSizeHelper.SpanInfo(
                                        text.substring(0, 1),
                                        DisplayUtil.dip2px(OrderDetailsActivity.this, 14),
                                        Color.parseColor("#333333"),
                                        false
                                )
                        );
                        spanInfos.add(
                                new TextColorSizeHelper.SpanInfo(
                                        text.substring(1, text.indexOf(".")),
                                        DisplayUtil.dip2px(OrderDetailsActivity.this, 18),
                                        Color.parseColor("#333333"),
                                        false
                                )
                        );
                        spanInfos.add(
                                new TextColorSizeHelper.SpanInfo(
                                        text.substring(text.indexOf(".")),
                                        DisplayUtil.dip2px(OrderDetailsActivity.this, 14),
                                        Color.parseColor("#333333"),
                                        false
                                )
                        );
                        bind.tvPrice.setText(TextColorSizeHelper.getTextSpan(OrderDetailsActivity.this, text, spanInfos));
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("id", orderform.getAddressId());

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/shoppingaddress/getById", map2)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        Shoppingaddress shoppingaddress = JsonHelper.parserJson2Object(
                                data, Shoppingaddress.class);
                        String receiptInfo = shoppingaddress.getName() + " "
                                + shoppingaddress.getPhone() + "\n"
                                + shoppingaddress.getArea() + "\n"
                                + shoppingaddress.getAddress();
                        bind.tvReceiptInfo.setText(receiptInfo);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });

        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("id", orderform.getId());

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/orderform/getById", map3)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        orderform = JsonHelper.parserJson2Object(
                                data, Orderform.class);
                        bind.tvOrderId.setText(String.valueOf(orderform.getId()));
                        bind.tvNumber.setText("x" + orderform.getNumber());
                        String text = "￥" + orderform.getPrice();
                        List<TextColorSizeHelper.SpanInfo> spanInfos = new ArrayList<TextColorSizeHelper.SpanInfo>();
                        spanInfos.add(
                                new TextColorSizeHelper.SpanInfo(
                                        text.substring(0, 1),
                                        DisplayUtil.dip2px(OrderDetailsActivity.this, 14),
                                        Color.parseColor("#FF6E00"),
                                        false
                                )
                        );
                        spanInfos.add(
                                new TextColorSizeHelper.SpanInfo(
                                        text.substring(1, text.indexOf(".")),
                                        DisplayUtil.dip2px(OrderDetailsActivity.this, 18),
                                        Color.parseColor("#FF6E00"),
                                        false
                                )
                        );
                        spanInfos.add(
                                new TextColorSizeHelper.SpanInfo(
                                        text.substring(text.indexOf(".")),
                                        DisplayUtil.dip2px(OrderDetailsActivity.this, 14),
                                        Color.parseColor("#FF6E00"),
                                        false
                                )
                        );
                        bind.tvTotalPrice.setText(TextColorSizeHelper.getTextSpan(OrderDetailsActivity.this, text, spanInfos));

                        bind.tvCreateTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderform.getCreateTime()));
                        if (orderform.getState() == 1) {
                            bind.tgShipmentTime.setVisibility(View.VISIBLE);
                            bind.tvShipmentTime.setVisibility(View.VISIBLE);
                            bind.tvShipmentTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderform.getShipmentTime()));
                            bind.tgFinishTime.setVisibility(View.GONE);
                            bind.tvFinishTime.setVisibility(View.GONE);
                        } else if (orderform.getState() == 2) {
                            bind.tgShipmentTime.setVisibility(View.VISIBLE);
                            bind.tvShipmentTime.setVisibility(View.VISIBLE);
                            bind.tvShipmentTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderform.getShipmentTime()));
                            bind.tgFinishTime.setVisibility(View.VISIBLE);
                            bind.tvFinishTime.setVisibility(View.VISIBLE);
                            bind.tvFinishTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(orderform.getFinishTime()));
                        } else {
                            bind.tgShipmentTime.setVisibility(View.GONE);
                            bind.tvShipmentTime.setVisibility(View.GONE);
                            bind.tgFinishTime.setVisibility(View.GONE);
                            bind.tvFinishTime.setVisibility(View.GONE);
                        }
                        setState();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                if (isAlter) {
                    setResult(RESULT_OK,new Intent());
                }
                finish();
                break;
            case R.id.sl_submit_order:
                switch (orderform.getState()) {
                    case 0:
                        alterState(0,"确认取消该订单？","取消订单成功");
                        break;
                    case 1:
                        alterState(2,"确认已经收到商品？","确认收货成功");
                        break;
                    case 2:
                        alterState(-1,"确认删除该订单？","删除订单成功");
                        break;
                }
                break;
        }
    }

    private void alterState(Integer newState, String msg1, String msg2) {
        new CommonAlertDialog(this).builder()
                .setTitle(msg1)
                .setNegativeButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("id",orderform.getId());
                        map.put("state",newState);

                        RetrofitManager
                                .getInstance()
                                .getApiService()
                                .postMethod("/orderform/alterState", map)
                                .compose(RxHelper.observableIO2Main(OrderDetailsActivity.this))
                                .subscribe(new BaseObserver() {
                                    @Override
                                    public void onSuccess(Map<String, Object> response) {
                                        T.showShort(msg2);
                                        if (orderform.getState() == 2) {
                                            setResult(RESULT_OK,new Intent());
                                            finish();
                                        } else {
                                            initRequest();
                                            isAlter = true;
                                        }
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (isAlter) {
                setResult(RESULT_OK,new Intent());
            }
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (isAlter) {
            setResult(RESULT_OK,new Intent());
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}