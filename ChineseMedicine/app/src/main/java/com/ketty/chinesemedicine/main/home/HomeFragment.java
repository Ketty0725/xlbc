package com.ketty.chinesemedicine.main.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.draggable.library.extension.ImageViewerHelper;
import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.ImageBannerAdapter;
import com.ketty.chinesemedicine.databinding.FragmentHomeBinding;
import com.ketty.chinesemedicine.main.home.chineseherb.ChineseHerbFlowCategoryActivity;
import com.ketty.chinesemedicine.main.home.prescription.PrescriptionFlowCategoryActivity;
import com.ketty.chinesemedicine.main.home.typhoidtheory.TyphoidTheoryCategoryActivity;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.scwang.smart.refresh.header.listener.OnTwoLevelListener;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private FragmentHomeBinding bind;

    private static volatile HomeFragment obj = null;

    public static HomeFragment getInstance() {
        if (obj == null) {
            synchronized (HomeFragment.class) {
                if (obj == null) {
                    obj = new HomeFragment();
                }
            }
        }
        return obj;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentHomeBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initRefresh();
        initRequest();
    }

    private void initRefresh() {
        bind.refreshLayout.setEnableLoadMore(false);
        bind.refreshLayout.setOnMultiListener(new SimpleMultiListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            }
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
            }
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                bind.toolbar.setAlpha(1 - Math.min(percent, 1));
                bind.secondFloor.setVisibility(View.VISIBLE);
                bind.secondFloor.setTranslationY(Math.min(offset - bind.secondFloor.getHeight()
                        + bind.toolbar.getHeight(), bind.refreshLayout.getLayout().getHeight()
                        - bind.secondFloor.getHeight()));
            }
            @Override
            public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
                if (oldState == RefreshState.TwoLevel) {
                    bind.secondFloorContent.animate().alpha(0).setDuration(1000);
                }
            }
        });

        bind.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bind.header.openTwoLevel(true);
            }
        });

        bind.header.setOnTwoLevelListener(new OnTwoLevelListener() {
            @Override
            public boolean onTwoLevel(@NonNull RefreshLayout refreshLayout) {
                bind.secondFloorContent.animate().alpha(1).setDuration(2000);
                return true;//true 将会展开二楼状态 false 关闭刷新
            }
        });

        bind.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initRequest();
                refreshLayout.finishRefresh(1000);
            }
        });
    }

    private void initView() {
        bind.llChineseHerb.setOnClickListener(this);
        bind.llPrescription.setOnClickListener(this);
        bind.llChinesePatentMedicine.setOnClickListener(this);
        bind.llMedicatedDiet.setOnClickListener(this);
        bind.llTyphoidTheory.setOnClickListener(this);
        bind.llEighteenAnti.setOnClickListener(this);
    }

    private void initRequest() {
        RetrofitManager
                .getInstance()
                .getApiService()
                .getMethod("/homebanner/get")
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        List<String> list = JsonHelper.parserJson2List(
                                data, new TypeToken<List<String>>() {}.getType());
                        initData(list);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initData(List<String> list) {
        List<ImageViewerHelper.ImageInfo> images = new ArrayList<>();
        List<View> views = new ArrayList<>();
        for (String image : list) {
            images.add(new ImageViewerHelper.ImageInfo(image,"",0));
            views.add(bind.banner);
        }

        ImageBannerAdapter mAdapter = new ImageBannerAdapter(list);
        bind.banner.setAdapter(mAdapter)
                .setBannerGalleryEffect(20,20,15)
                .setIndicator(new CircleIndicator(getActivity()));
        bind.banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(Object data, int position) {
                ImageViewerHelper.showImages(getContext(), views, images, position, false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_chinese_herb:
                startActivity(ChineseHerbFlowCategoryActivity.class, "中药");
                break;
            case R.id.ll_prescription:
                startActivity(PrescriptionFlowCategoryActivity.class, "方剂");
                break;
            case R.id.ll_chinese_patent_medicine:
                startActivity(ListCategoryActivity.class, "中成药");
                break;
            case R.id.ll_medicated_diet:
                startActivity(ListCategoryActivity.class, "药膳");
                break;
            case R.id.ll_typhoid_theory:
                startActivity(TyphoidTheoryCategoryActivity.class, "伤寒论查阅");
                break;
            case R.id.ll_eighteen_anti:
                startActivity(EighteenAntiActivity.class, "十八反十九畏");
                break;
        }
    }

    private void startActivity(Class<?> cls, String title) {
        Intent intent = new Intent(getActivity(), cls);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        bind.banner.start();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            bind.banner.start();
        } else {
            bind.banner.stop();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        bind.banner.stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.banner.destroy();
        bind = null;
    }
}