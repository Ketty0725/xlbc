package com.ketty.chinesemedicine.main.home.typhoidtheory;

import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEHERB;
import static com.ketty.chinesemedicine.main.search.SearchType.PRESCRIPTION;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.TyphoidTheoryChineseHerbAdapter;
import com.ketty.chinesemedicine.databinding.ActivityTyphoidTheoryChineseHerbBinding;
import com.ketty.chinesemedicine.entity.Typhoidtheorychineseherbcontent;
import com.ketty.chinesemedicine.main.home.BubbleDialog;
import com.ketty.chinesemedicine.main.home.chineseherb.ChineseHerbActivity;
import com.ketty.chinesemedicine.main.home.prescription.PrescriptionActivity;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TyphoidTheoryChineseHerbActivity extends BaseActivity implements View.OnClickListener {
    private ActivityTyphoidTheoryChineseHerbBinding bind;
    private String title;
    private TyphoidTheoryChineseHerbAdapter mAdapter;

    @Override
    protected View initLayout() {
        bind = ActivityTyphoidTheoryChineseHerbBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        bind.ivBack.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        bind.tvTitle.setText(title);

        initRecyclerView("原文摘要");
        initRecyclerView("伤寒论");
        initRecyclerView("金匮要略");
    }

    private void initRecyclerView(String typeName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", title);
        map.put("typeName", typeName);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/typhoidtheorychineseherbcontent/getByNameAndType", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        bind.progressBar.setVisibility(View.GONE);
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        List<Typhoidtheorychineseherbcontent> dataList = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Typhoidtheorychineseherbcontent>>() {}.getType());
                        initData(dataList, typeName);
                        bind.llError.setVisibility(View.GONE);
                        bind.nestedScrollView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        bind.progressBar.setVisibility(View.GONE);
                        bind.nestedScrollView.setVisibility(View.GONE);
                        bind.llError.setVisibility(View.VISIBLE);
                        if (TextUtils.equals(typeName,"伤寒论")) {
                            bind.tvTyphoidTheory.setVisibility(View.GONE);
                            bind.rvTyphoidTheory.setVisibility(View.GONE);
                        } else {
                            bind.tvGoldenChamber.setVisibility(View.GONE);
                            bind.rvGoldenChamber.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void initData(List<Typhoidtheorychineseherbcontent> dataList, String typeName) {
        bind.tvContain.setText("含\"" + title + "\"经方:");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        if (dataList != null) {
            String text = typeName + "（" + dataList.size() + "条）";
            List<TextColorSizeHelper.SpanInfo> list = new ArrayList<TextColorSizeHelper.SpanInfo>();
            list.add(
                    new TextColorSizeHelper.SpanInfo(
                            text.substring(0,text.indexOf("（")),
                            DisplayUtil.dip2px(getContext(),14),
                            Color.parseColor("#333333"),
                            true
                    )
            );
            list.add(
                    new TextColorSizeHelper.SpanInfo(
                            text.substring(text.indexOf("（")),
                            DisplayUtil.dip2px(getContext(),12),
                            Color.parseColor("#999999"),
                            true
                    )
            );

            if (TextUtils.equals(typeName,"伤寒论")) {
                bind.tvTyphoidTheory.setVisibility(View.VISIBLE);
                bind.rvTyphoidTheory.setVisibility(View.VISIBLE);
                bind.lineTyphoidTheory.setVisibility(View.VISIBLE);
                bind.tvTyphoidTheory.setText(TextColorSizeHelper.getTextSpan(getContext(), text, list));
                bind.rvTyphoidTheory.setLayoutManager(layoutManager);
                mAdapter = new TyphoidTheoryChineseHerbAdapter(dataList,0);
                bind.rvTyphoidTheory.setAdapter(mAdapter);
                bind.rvTyphoidTheory.setOverScrollMode(View.OVER_SCROLL_NEVER);
            } else if (TextUtils.equals(typeName,"金匮要略")) {
                bind.tvGoldenChamber.setVisibility(View.VISIBLE);
                bind.rvGoldenChamber.setVisibility(View.VISIBLE);
                bind.tvGoldenChamber.setText(TextColorSizeHelper.getTextSpan(getContext(), text, list));
                bind.rvGoldenChamber.setLayoutManager(layoutManager);
                mAdapter = new TyphoidTheoryChineseHerbAdapter(dataList,1);
                bind.rvGoldenChamber.setAdapter(mAdapter);
                bind.rvGoldenChamber.setOverScrollMode(View.OVER_SCROLL_NEVER);
            }
        } else {
            mAdapter = new TyphoidTheoryChineseHerbAdapter(null,null);
            if (TextUtils.equals(typeName,"伤寒论")) {
                bind.tvTyphoidTheory.setVisibility(View.GONE);
                bind.rvTyphoidTheory.setVisibility(View.GONE);
                bind.lineTyphoidTheory.setVisibility(View.GONE);
            } else if (TextUtils.equals(typeName,"金匮要略")) {
                bind.tvGoldenChamber.setVisibility(View.GONE);
                bind.rvGoldenChamber.setVisibility(View.GONE);
            }
        }

        if (TextUtils.equals(typeName,"原文摘要")) {
            bind.rvOriginalAbstract.setVisibility(View.VISIBLE);
            bind.rvOriginalAbstract.setLayoutManager(layoutManager);
            mAdapter = new TyphoidTheoryChineseHerbAdapter(dataList,-1);
            bind.rvOriginalAbstract.setAdapter(mAdapter);
            bind.rvOriginalAbstract.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }

        mAdapter.setOnItemClickListener(new TyphoidTheoryChineseHerbAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String title, int xAxisLeft, int yAxisTop) {
                TextPaint newPaint = new TextPaint();
                float textSize = getResources().getDisplayMetrics().scaledDensity * 14;
                newPaint.setTextSize(textSize);
                int textWidth = (int) newPaint.measureText(title);

                int[] mLocation = new int[2];
                v.getLocationOnScreen(mLocation);
                BubbleDialog bubbleDialog = new BubbleDialog(mLocation,textWidth,xAxisLeft,yAxisTop);
                Bundle bundle = new Bundle();
                bundle.putString("title", title);
                bundle.putInt("type", PRESCRIPTION);
                bubbleDialog.setArguments(bundle);
                bubbleDialog.show(getSupportFragmentManager(), "BubbleDialog");
                bubbleDialog.setStateListener(new BubbleDialog.StateListener() {
                    @Override
                    public void onFirstItemClick(String name, int type) {
                        if (type == CHINESEHERB) {
                            startActivity(name, ChineseHerbActivity.class);
                        } else if (type == PRESCRIPTION) {
                            startActivity(name, PrescriptionActivity.class);
                        }
                    }

                    @Override
                    public void onSecondItemClick(String name) {
                        startActivity(name, TyphoidTheoryPrescriptionActivity.class);
                    }
                });
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private Context getContext() {
        return TyphoidTheoryChineseHerbActivity.this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}