package com.ketty.chinesemedicine.main.home.prescription;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.databinding.ActivityPrescriptionCategoryBinding;
import com.ketty.chinesemedicine.entity.Prescriptioncategoryintroduce;
import com.ketty.chinesemedicine.main.home.CategoryDialog;
import com.ketty.chinesemedicine.util.JsonHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrescriptionCategoryActivity extends BaseActivity implements View.OnClickListener {
    private ActivityPrescriptionCategoryBinding bind;
    private List<String> mTitleList;
    private int nestedScrollViewTop;
    private List<View> mViewList;

    @Override
    protected View initLayout() {
        bind = ActivityPrescriptionCategoryBinding.inflate(getLayoutInflater());
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

    private void initData(Prescriptioncategoryintroduce bean) {
        mViewList = new ArrayList<>();
        mViewList.add(bind.slParaphrase);
        mViewList.add(bind.slIndications);
        mViewList.add(bind.slNotes);

        mTitleList = new ArrayList<>();
        mTitleList.add("释义");
        mTitleList.add("适应");
        mTitleList.add("注意");

        bind.tvParaphrase.setText(bean.getParaphrase() == null ? "" : bean.getParaphrase().replace("\\n", "\n"));
        bind.tvIndications.setText(bean.getIndications() == null ? "" : bean.getIndications().replace("\\n", "\n"));
        bind.tvNotes.setText(bean.getNotes() == null ? "" : bean.getNotes().replace("\\n", "\n"));
    }

    private void initRequest(String title) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", title);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/prescriptioncategoryintroduce/getByTitle", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        bind.progressBar.setVisibility(View.GONE);
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        Prescriptioncategoryintroduce bean = JsonHelper.parserJson2Object(
                                data, Prescriptioncategoryintroduce.class);
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