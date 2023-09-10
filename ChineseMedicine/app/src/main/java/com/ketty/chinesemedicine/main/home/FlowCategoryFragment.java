package com.ketty.chinesemedicine.main.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.FlowCategoryFatherAdapter;
import com.ketty.chinesemedicine.databinding.FragmentCaptchaBinding;
import com.ketty.chinesemedicine.databinding.FragmentFlowCategoryBinding;
import com.ketty.chinesemedicine.databinding.FragmentPrescriptionListCategoryBinding;
import com.ketty.chinesemedicine.entity.FlowCategory;
import com.ketty.chinesemedicine.main.home.chineseherb.ChineseHerbActivity;
import com.ketty.chinesemedicine.main.home.chineseherb.ChineseHerbCategoryActivity;
import com.ketty.chinesemedicine.main.home.prescription.PrescriptionActivity;
import com.ketty.chinesemedicine.main.home.prescription.PrescriptionCategoryActivity;
import com.ketty.chinesemedicine.main.search.SearchHistoryActivity;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;
import com.lihang.ShadowLayout;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FlowCategoryFragment extends RxFragment implements View.OnClickListener {
    private FragmentFlowCategoryBinding bind;
    private static final String ARG_PARAM = "title";
    private String title;
    private List<FlowCategory> mList;
    private List<String> mTitleList;
    private LinearLayoutManager mManager;
    private FlowCategoryFatherAdapter mAdapter;

    public static FlowCategoryFragment newInstance(String title) {
        FlowCategoryFragment fragment = new FlowCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentFlowCategoryBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        bind.shadowUnfold.setVisibility(View.GONE);
        bind.recyclerView.setVisibility(View.GONE);

        bind.shadowUnfold.setOnClickListener(this);

        if (TextUtils.equals(title, "中药")) {
            initRequest("/chineseherb/getAll");
        } else if (TextUtils.equals(title, "常用方")) {
            initRequest("/prescription/getAllCommon");
        }
    }

    private void initRequest(String apiName) {
        RetrofitManager
                .getInstance()
                .getApiService()
                .getMethod(apiName)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        LogUtils.e("请求成功");
                        bind.progressBar.setVisibility(View.GONE);
                        bind.shadowUnfold.setVisibility(View.VISIBLE);
                        bind.recyclerView.setVisibility(View.VISIBLE);
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        mList = JsonHelper.parserJson2List(data, new TypeToken<List<FlowCategory>>() {}.getType());
                        initRecyclerView();
                        initData();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        LogUtils.e("请求失败");
                    }
                });

    }

    private void initRecyclerView() {
        mManager = new LinearLayoutManager(getContext());
        bind.recyclerView.setLayoutManager(mManager);
        mAdapter = new FlowCategoryFatherAdapter(mList);
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
    }

    private void initData() {
        if (TextUtils.equals(title, "中药")) {
            setAdapter(ChineseHerbActivity.class, ChineseHerbCategoryActivity.class);
        } else if (TextUtils.equals(title, "常用方")) {
            setAdapter(PrescriptionActivity.class, PrescriptionCategoryActivity.class);
        }

        mTitleList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            mTitleList.add(mList.get(i).getFatherTitle().substring(0,2));
        }
    }

    private void setAdapter(Class<?> c1, Class<?> c2) {
        mAdapter.setOnItemClickListener(new FlowCategoryFatherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String tag) {
                startActivity(c1, tag);
            }

            @Override
            public void onTitleClick(String title) {
                startActivity(c2, title);
            }
        });
    }

    private void startActivity(Class<?> cls, String title) {
        Intent intent = new Intent(getContext(), cls);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shadow_unfold:
                int mFirstVisiblePosition = 0;
                RecyclerView.LayoutManager layoutManager = bind.recyclerView.getLayoutManager();
                if (layoutManager instanceof LinearLayoutManager) {
                    LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                    mFirstVisiblePosition = linearManager.findFirstVisibleItemPosition();
                }
                CategoryDialog categoryDialog = new CategoryDialog(mTitleList,0);
                Bundle bundle = new Bundle();
                bundle.putInt("position",mFirstVisiblePosition);
                categoryDialog.setArguments(bundle);
                categoryDialog.show(getChildFragmentManager(), "CategoryDialog");
                categoryDialog.setStateListener(new CategoryDialog.StateListener() {
                    @Override
                    public void onClick(int position) {
                        /*final LinearSmoothScroller mScroller = new TopSmoothScroller(CategoryActivity.this);
                        mScroller.setTargetPosition(position);
                        mManager.startSmoothScroll(mScroller);*/
                        mManager.scrollToPositionWithOffset(position,0);
                        LogUtils.i("CategoryActivity", String.valueOf(position));
                    }
                });
                break;
        }
    }

    public static class TopSmoothScroller extends LinearSmoothScroller {
        TopSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected int getHorizontalSnapPreference() {
            return SNAP_TO_START;
        }

        @Override
        protected int getVerticalSnapPreference() {
            return SNAP_TO_START;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }
}