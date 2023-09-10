package com.ketty.chinesemedicine.main.home.chineseherb;

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
import com.ketty.chinesemedicine.databinding.ActivityChineseHerbCategoryBinding;
import com.ketty.chinesemedicine.entity.Chineseherbcategoryintroduce;
import com.ketty.chinesemedicine.main.home.BubbleDialog;
import com.ketty.chinesemedicine.main.home.CategoryDialog;
import com.ketty.chinesemedicine.main.home.prescription.PrescriptionActivity;
import com.ketty.chinesemedicine.main.home.typhoidtheory.TyphoidTheoryPrescriptionActivity;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChineseHerbCategoryActivity extends BaseActivity implements View.OnClickListener {
    private ActivityChineseHerbCategoryBinding bind;
    private List<String> mTitleList;
    private int nestedScrollViewTop;
    private List<View> mViewList;

    @Override
    protected View initLayout() {
        bind = ActivityChineseHerbCategoryBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        bind.shadowUnfold.setOnClickListener(this);
        bind.ivBack.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        bind.tvTitle.setText(title);
        initRequest(title);
    }

    private void initData(Chineseherbcategoryintroduce bean) {
        mViewList = new ArrayList<>();
        mViewList.add(bind.slParaphrase);
        mViewList.add(bind.slClassify);
        mViewList.add(bind.slEfficacy);
        mViewList.add(bind.slMatchingFeatures);
        mViewList.add(bind.slMedicationAttention);
        mViewList.add(bind.slModernResearch);

        mTitleList = new ArrayList<>();
        mTitleList.add("释义");
        mTitleList.add("分类");
        mTitleList.add("功用");
        mTitleList.add("配伍");
        mTitleList.add("用药");
        mTitleList.add("研究");

        setText(bind.tvParaphrase,bean.getParaphrase());
        setText(bind.tvClassify,bean.getClassify());
        setText(bind.tvEfficacy,bean.getEfficacy());
        setText(bind.tvMatchingFeatures,bean.getMatchingFeatures());
        setText(bind.tvMedicationAttention,bean.getMedicationAttention());
        setText(bind.tvModernResearch,bean.getModernResearch());
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
                        int yAxisTop = bound.top - DisplayUtil.dip2px(ChineseHerbCategoryActivity.this,10);
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
        Intent intent = new Intent(ChineseHerbCategoryActivity.this, cls);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void initRequest(String title) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", title);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/chineseherbcategoryintroduce/getByTitle", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        bind.progressBar.setVisibility(View.GONE);
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        Chineseherbcategoryintroduce bean = JsonHelper.parserJson2Object(
                                data, Chineseherbcategoryintroduce.class);
                        initData(bean);
                        bind.shadowUnfold.setClickable(true);
                        bind.nestedScrollView.setVisibility(View.VISIBLE);
                        bind.llError.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        bind.progressBar.setVisibility(View.GONE);
                        bind.nestedScrollView.setVisibility(View.GONE);
                        bind.shadowUnfold.setClickable(false);
                        bind.llError.setVisibility(View.VISIBLE);
                    }
                });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.shadow_unfold:
                int position = getVisibleViewTop(mViewList);
                CategoryDialog categoryDialog = new CategoryDialog(mTitleList,0);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                categoryDialog.setArguments(bundle);
                categoryDialog.show(getSupportFragmentManager(), "CategoryDialog");
                categoryDialog.setStateListener(new CategoryDialog.StateListener() {
                    @Override
                    public void onClick(int position) {
                        int[] intArray = new int[2];
                        mViewList.get(position).getLocationOnScreen(intArray);//测量某View相对于屏幕的距离
                        scrollByDistance(intArray[1]);
                        categoryDialog.dismiss();
                    }
                });
                break;
        }
    }

    private int getVisibleViewTop(List<View> list) {
        int num = 0;
        int position = 0;
        for (int i = 0; i < list.size(); i++) {
            if (num == 1) {
                break;
            } else {
                if (isVisibleLocal(list.get(i),false)) {
                    position = i;
                    num++;
                }
            }
        }
        return position;
    }

    private boolean isVisibleLocal(View target, boolean judgeAll) {
        Rect rect = new Rect();
        target.getLocalVisibleRect(rect);
        if (judgeAll) {
            return rect.top == 0;
        } else {
            return rect.top >= 0;
        }
    }

    private void scrollByDistance(int dy) {
        if (nestedScrollViewTop == 0) {
            int[] intArray = new int[2];
            bind.nestedScrollView.getLocationOnScreen(intArray);
            nestedScrollViewTop = intArray[1];
        }
        int distance = dy - nestedScrollViewTop;    //必须算上nestedScrollView本身与屏幕的距离
        bind.nestedScrollView.fling(distance);   //添加上这句滑动才有效
        bind.nestedScrollView.smoothScrollBy(0, distance,0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }

}