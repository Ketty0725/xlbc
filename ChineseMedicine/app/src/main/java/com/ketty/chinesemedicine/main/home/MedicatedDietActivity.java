package com.ketty.chinesemedicine.main.home;

import static com.ketty.chinesemedicine.main.home.TextSign.FJend;
import static com.ketty.chinesemedicine.main.home.TextSign.FJstart;
import static com.ketty.chinesemedicine.main.home.TextSign.ZYend;
import static com.ketty.chinesemedicine.main.home.TextSign.ZYstart;
import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEHERB;
import static com.ketty.chinesemedicine.main.search.SearchType.PRESCRIPTION;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.databinding.ActivityMedicatedDietBinding;
import com.ketty.chinesemedicine.entity.Medicateddiet;
import com.ketty.chinesemedicine.main.home.chineseherb.ChineseHerbActivity;
import com.ketty.chinesemedicine.main.home.prescription.PrescriptionActivity;
import com.ketty.chinesemedicine.main.home.typhoidtheory.TyphoidTheoryPrescriptionActivity;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicatedDietActivity extends BaseActivity implements View.OnClickListener {
    private ActivityMedicatedDietBinding bind;
    private String title;

    @Override
    protected View initLayout() {
        bind = ActivityMedicatedDietBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        bind.ivBack.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        bind.tvTitle.setText(title);

        initRequest(title);
    }

    private void initData(Medicateddiet medicateddiet) {
        setText(bind.tvDerivation,medicateddiet.getDerivation());
        setText(bind.tvFoodMaterial,medicateddiet.getFoodMaterial());
        setText(bind.tvPreparationMethod,medicateddiet.getPreparationMethod());
        setText(bind.tvEfficacy,medicateddiet.getEfficacy());
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
                        int yAxisTop = bound.top - DisplayUtil.dip2px(MedicatedDietActivity.this,10);
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
        Intent intent = new Intent(MedicatedDietActivity.this, cls);
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

    private void initRequest(String title) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", title);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/medicateddiet/getByName", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        LogUtils.e("请求成功");
                        bind.progressBar.setVisibility(View.GONE);
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        Medicateddiet medicateddiet = JsonHelper.parserJson2Object(
                                data, Medicateddiet.class);
                        initData(medicateddiet);
                        bind.nestedScrollView.setVisibility(View.VISIBLE);
                        bind.llError.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        LogUtils.e("请求失败");
                        bind.progressBar.setVisibility(View.GONE);
                        bind.nestedScrollView.setVisibility(View.GONE);
                        bind.llError.setVisibility(View.VISIBLE);
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}