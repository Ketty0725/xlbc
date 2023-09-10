package com.ketty.chinesemedicine.main.home;

import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEPATENTMEDICINE;
import static com.ketty.chinesemedicine.main.search.SearchType.MEDICATEDDIET;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.ListCategoryAdapter;
import com.ketty.chinesemedicine.databinding.ActivityDetailsBinding;
import com.ketty.chinesemedicine.databinding.ActivityListCategoryBinding;
import com.ketty.chinesemedicine.entity.Medicateddiet;
import com.ketty.chinesemedicine.main.search.SearchHistoryActivity;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;

import java.util.List;
import java.util.Map;

public class ListCategoryActivity extends BaseActivity implements View.OnClickListener {
    private ActivityListCategoryBinding bind;
    private String title;
    private ListCategoryAdapter mAdapter;
    private int type;

    @Override
    protected View initLayout() {
        bind = ActivityListCategoryBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        bind.ivBack.setOnClickListener(this);
        bind.shadowSearch.setOnClickListener(this);

        initData();
    }

    private void initRecyclerView(List<?> list) {
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ListCategoryAdapter(type,list);
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        if (TextUtils.equals(title, "中成药")) {
            setAdapter(ChinesePatentMedicineActivity.class);
        } else if (TextUtils.equals(title, "药膳")) {
            setAdapter(MedicatedDietActivity.class);
        }
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        bind.tvTitle.setText(title);

        String path = "";
        if (TextUtils.equals(title, "中成药")) {
            bind.tvSearch.setText("请输入药名");
            type = CHINESEPATENTMEDICINE;
            path = "/chinesepatentmedicine/getNameList";
        } else if (TextUtils.equals(title, "药膳")) {
            bind.tvSearch.setText("请输入药膳名称");
            type = MEDICATEDDIET;
            path = "/medicateddiet/getNameList";
        }

        initRequest(path);
    }

    private void setAdapter(Class<?> cls) {
        mAdapter.setOnItemClickListener(new ListCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String title) {
                startActivity(cls, title);
            }
        });
    }

    private void startActivity(Class<?> cls, String title) {
        Intent intent = new Intent(ListCategoryActivity.this, cls);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.shadow_search:
                startActivity(SearchHistoryActivity.class, title);
                break;
        }
    }

    private void initRequest(String path) {
        RetrofitManager
                .getInstance()
                .getApiService()
                .getMethod(path)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        LogUtils.e("请求成功");
                        bind.progressBar.setVisibility(View.GONE);
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        switch (type) {
                            case CHINESEPATENTMEDICINE:
                                List<String> list = JsonHelper.parserJson2List(
                                        data, new TypeToken<List<String>>() {}.getType());
                                initRecyclerView(list);
                                break;
                            case MEDICATEDDIET:
                                List<Medicateddiet> medicateddietList = JsonHelper.parserJson2List(
                                        data, new TypeToken<List<Medicateddiet>>() {}.getType());
                                initRecyclerView(medicateddietList);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        LogUtils.e("请求失败");
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}