package com.ketty.chinesemedicine.main.home.typhoidtheory;

import static com.ketty.chinesemedicine.main.home.TextSign.FJend;
import static com.ketty.chinesemedicine.main.home.TextSign.FJstart;
import static com.ketty.chinesemedicine.main.home.TextSign.ZYend;
import static com.ketty.chinesemedicine.main.home.TextSign.ZYstart;
import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEHERB;
import static com.ketty.chinesemedicine.main.search.SearchType.PRESCRIPTION;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.PrescriptionWorkCitedAdapter;
import com.ketty.chinesemedicine.databinding.ActivityTyphoidTheoryPrescriptionBinding;
import com.ketty.chinesemedicine.entity.Typhoidtheoryprescription;
import com.ketty.chinesemedicine.entity.Typhoidtheoryprescriptionworkcited;
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

public class TyphoidTheoryPrescriptionActivity extends BaseActivity implements View.OnClickListener {
    private ActivityTyphoidTheoryPrescriptionBinding bind;
    private String title;

    @Override
    protected View initLayout() {
        bind = ActivityTyphoidTheoryPrescriptionBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        bind.ivBack.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        bind.tvTitle.setText(title);

        initRequest();
        initRecyclerView("伤寒论");
        initRecyclerView("金匮要略");
    }

    private void initRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", title);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/typhoidtheoryprescription/getByName", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        bind.progressBar.setVisibility(View.GONE);
                        bind.llError.setVisibility(View.GONE);
                        bind.nestedScrollView.setVisibility(View.VISIBLE);
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        Typhoidtheoryprescription entity = JsonHelper.parserJson2Object(
                                data, Typhoidtheoryprescription.class);
                        initData(entity);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        bind.progressBar.setVisibility(View.GONE);
                        bind.nestedScrollView.setVisibility(View.GONE);
                        bind.llError.setVisibility(View.VISIBLE);
                    }
                });

    }

    private void initRecyclerView(String typeName) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", title);
        map.put("typeName", typeName);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/typhoidtheoryprescriptionworkcited/getByNameAndType", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        List<Typhoidtheoryprescriptionworkcited> dataList = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Typhoidtheoryprescriptionworkcited>>() {}.getType());
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

                        PrescriptionWorkCitedAdapter mAdapter = new PrescriptionWorkCitedAdapter(dataList);
                        if (TextUtils.equals(typeName,"伤寒论")) {
                            bind.tvTyphoidTheory.setVisibility(View.VISIBLE);
                            bind.rvTyphoidTheory.setVisibility(View.VISIBLE);
                            bind.tvTyphoidTheory.setText(TextColorSizeHelper.getTextSpan(getContext(), text, list));
                            bind.rvTyphoidTheory.setLayoutManager(new LinearLayoutManager(getContext()));
                            bind.rvTyphoidTheory.setAdapter(mAdapter);
                            bind.rvTyphoidTheory.setOverScrollMode(View.OVER_SCROLL_NEVER);
                        } else {
                            bind.tvGoldenChamber.setVisibility(View.VISIBLE);
                            bind.rvGoldenChamber.setVisibility(View.VISIBLE);
                            bind.tvGoldenChamber.setText(TextColorSizeHelper.getTextSpan(getContext(), text, list));
                            bind.rvGoldenChamber.setLayoutManager(new LinearLayoutManager(getContext()));
                            bind.rvGoldenChamber.setAdapter(mAdapter);
                            bind.rvGoldenChamber.setOverScrollMode(View.OVER_SCROLL_NEVER);
                        }

                        mAdapter.setOnItemClickListener(new PrescriptionWorkCitedAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, String subText, int xAxisLeft, int yAxisTop, int type) {
                                openBubbleDialog(view,subText,xAxisLeft,yAxisTop,type);
                            }
                        });

                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
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

    private void initData(Typhoidtheoryprescription entity) {
        setText(bind.tvCompose,entity.getCompose());
        setText(bind.tvPreparationMethod,entity.getPreparationMethod());
    }

    private void setText(TextView textView, String str) {
        if (str == null) {
            str = "";
        }
        String text = str.replace("\\n", "\n");

        if (text.contains(ZYstart) || text.contains(FJstart)) {
            List<TextColorSizeHelper.SpanInfo> list = new ArrayList<TextColorSizeHelper.SpanInfo>();
            String showText = text.replace(ZYstart,"").replace(ZYend,"").replace(FJstart,"").replace(FJend,"");

            int begin = 0;
            int end = 0;
            int finalBegin = 0;
            int finalEnd = 0;
            while (text.indexOf("<", end) != -1) {
                begin = text.indexOf("<", end) + 4;
                end = text.indexOf("</", end) + 5;
                String subText = text.substring(begin, end - 5);

                finalBegin = showText.indexOf(subText, finalEnd);
                finalEnd = finalBegin + subText.length();

                String type = text.substring(begin-4,begin);
                TextColorSizeHelper.SpanInfo textSpanInfo = null;
                if (type.equals(ZYstart)) {
                    textSpanInfo = newSpanInfo(subText,textView,finalBegin,CHINESEHERB);
                } else if (type.equals(FJstart)) {
                    textSpanInfo = newSpanInfo(subText,textView,finalBegin,PRESCRIPTION);
                }
                list.add(textSpanInfo);
            }

            textView.setHighlightColor(Color.parseColor("#00000000"));
            textView.setText(TextColorSizeHelper.getTextSpan(this, showText, list));
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            textView.setText(text);
        }

    }

    private TextColorSizeHelper.SpanInfo newSpanInfo(String subText, TextView textView,
                                                     int position, int type) {
        return new TextColorSizeHelper.SpanInfo(
                subText,
                -1,
                Color.parseColor("#5ebdbf"),
                new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View widget) {
                        Layout layout = textView.getLayout();
                        Rect bound = new Rect();
                        int line = layout.getLineForOffset(position);
                        layout.getLineBounds(line, bound);
                        int xAxisLeft = (int) layout.getPrimaryHorizontal(position);
                        int yAxisTop = bound.top - DisplayUtil.dip2px(TyphoidTheoryPrescriptionActivity.this,10);
                        openBubbleDialog(widget,subText,xAxisLeft,yAxisTop,type);
                    }
                }, true,
                false
        );
    }

    private void openBubbleDialog(View v, String title, int xAxisLeft, int yAxisTop, int type) {
        TextPaint newPaint = new TextPaint();
        float textSize = getResources().getDisplayMetrics().scaledDensity * 14;
        newPaint.setTextSize(textSize);
        int textWidth = (int) newPaint.measureText(title);

        int[] mLocation = new int[2];
        v.getLocationOnScreen(mLocation);
        BubbleDialog bubbleDialog = new BubbleDialog(mLocation,textWidth,xAxisLeft,yAxisTop);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("type", type);
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

    private void startActivity(String title, Class<?> cls) {
        Intent intent = new Intent(TyphoidTheoryPrescriptionActivity.this, cls);
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
        return TyphoidTheoryPrescriptionActivity.this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}