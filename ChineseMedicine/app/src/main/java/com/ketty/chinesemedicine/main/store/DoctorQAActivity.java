package com.ketty.chinesemedicine.main.store;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.DoctorQAAdapter;
import com.ketty.chinesemedicine.component.LinearSpacingItemDecoration;
import com.ketty.chinesemedicine.databinding.ActivityDoctorQaactivityBinding;
import com.ketty.chinesemedicine.entity.ChineseherbAndImages;
import com.ketty.chinesemedicine.entity.Product;
import com.ketty.chinesemedicine.entity.Productdoctorqa;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoctorQAActivity extends BaseActivity implements View.OnClickListener {
    private ActivityDoctorQaactivityBinding bind;
    private Long id;

    @Override
    protected View initLayout() {
        bind = ActivityDoctorQaactivityBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong("id");
        initRequest();
        bind.ivBack.setOnClickListener(this);
    }

    private void initRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("productId", id);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/productdoctorqa/listByProductId", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        List<Productdoctorqa> list = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Productdoctorqa>>() {}.getType());
                        initData(list);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initData(List<Productdoctorqa> list) {
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DoctorQAAdapter mAdapter = new DoctorQAAdapter(list);
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        bind.recyclerView.addItemDecoration(new LinearSpacingItemDecoration(
                this,DisplayUtil.dip2px(this,15)));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}