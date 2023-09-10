package com.ketty.chinesemedicine.main.home.prescription;

import static com.ketty.chinesemedicine.main.search.SearchType.PRESCRIPTION;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.ListCategoryAdapter;
import com.ketty.chinesemedicine.databinding.FragmentCaptchaBinding;
import com.ketty.chinesemedicine.databinding.FragmentPrescriptionListCategoryBinding;
import com.ketty.chinesemedicine.entity.Prescription;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.List;
import java.util.Map;

public class PrescriptionListCategoryFragment extends RxFragment {
    private FragmentPrescriptionListCategoryBinding bind;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    public static PrescriptionListCategoryFragment newInstance(String param1) {
        PrescriptionListCategoryFragment fragment = new PrescriptionListCategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentPrescriptionListCategoryBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        initRequest();
    }

    private void initRecyclerView(List<Prescription> list) {
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListCategoryAdapter mAdapter = new ListCategoryAdapter(PRESCRIPTION,list);
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mAdapter.setOnItemClickListener(new ListCategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String title) {
                startActivity(title, PrescriptionActivity.class);
            }
        });
    }

    private void startActivity(String title, Class<?> cls) {
        Intent intent = new Intent(getContext(), cls);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initRequest() {
        RetrofitManager
                .getInstance()
                .getApiService()
                .getMethod("/prescription/getAllClassic")
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        LogUtils.e("请求成功");
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        List<Prescription> list = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Prescription>>() {}.getType());
                        initRecyclerView(list);
                        bind.progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        LogUtils.e("请求失败");
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

}