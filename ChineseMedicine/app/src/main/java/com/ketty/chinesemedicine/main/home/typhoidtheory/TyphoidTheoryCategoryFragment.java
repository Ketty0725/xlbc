package com.ketty.chinesemedicine.main.home.typhoidtheory;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.databinding.FragmentCaptchaBinding;
import com.ketty.chinesemedicine.databinding.FragmentTyphoidTheoryCategoryBinding;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;
import com.lihang.ShadowLayout;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TyphoidTheoryCategoryFragment extends RxFragment {
    private FragmentTyphoidTheoryCategoryBinding bind;
    private static final String ARG_PARAM = "title";
    private String title;

    public static TyphoidTheoryCategoryFragment newInstance(String title) {
        TyphoidTheoryCategoryFragment fragment = new TyphoidTheoryCategoryFragment();
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
        bind = FragmentTyphoidTheoryCategoryBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        bind.llPrescription.setVisibility(View.GONE);
        bind.llChineseHerb.setVisibility(View.GONE);

        String url = "";
        if (title.equals("经方")) {
            url = "/typhoidtheoryprescription/selectNameList";
            initRequest("伤寒论",url,bind.tvTyphoidTheory,bind.flexboxTyphoidTheory);
            initRequest("金匮要略",url,bind.tvGoldenChamber,bind.flexboxGoldenChamber);
        } else if (title.equals("中药")) {
            url = "/typhoidtheorychineseherb/getAll";
            initRequest("中药",url,bind.tvChineseHerb,bind.flexboxChineseHerb);
        }
    }

    private void initRequest(String typeName, String url, TextView tv, FlexboxLayout fl) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("typeName", typeName);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod(url, map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        LogUtils.e("请求成功");
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        List<String> dataList = JsonHelper.parserJson2List(
                                data, new TypeToken<List<String>>() {}.getType());
                        initData(dataList,typeName,tv,fl);

                        bind.progressBar.setVisibility(View.GONE);
                        if (title.equals("经方")) {
                            bind.llPrescription.setVisibility(View.VISIBLE);
                        } else if (title.equals("中药")) {
                            bind.llChineseHerb.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        LogUtils.e("请求失败");
                    }
                });

    }

    private void initData(List<String> dataList, String typeName, TextView tv, FlexboxLayout fl) {
        if (!TextUtils.equals(typeName, "中药")) {
            String text = typeName + "（含" + dataList.size() + "方）";
            List<TextColorSizeHelper.SpanInfo> list = new ArrayList<TextColorSizeHelper.SpanInfo>();
            list.add(
                    new TextColorSizeHelper.SpanInfo(
                            text.substring(0,text.indexOf("（")),
                            DisplayUtil.dip2px(getContext(),15),
                            Color.parseColor("#58bec0"),
                            true
                    )
            );
            list.add(
                    new TextColorSizeHelper.SpanInfo(
                            text.substring(text.indexOf("（")),
                            DisplayUtil.dip2px(getContext(),13),
                            Color.parseColor("#58bec0"),
                            true
                    )
            );
            tv.setText(TextColorSizeHelper.getTextSpan(getContext(), text, list));
        } else {
            String text = "共收录" + dataList.size() + "味中药";
            tv.setText(text);
        }

        fl.removeAllViews();
        // flexbox布局动态添加标签
        for (int i = 0; i < dataList.size(); i++) {
            String temp = dataList.get(i);
            View tagView = LayoutInflater.from(getContext()).inflate(R.layout.item_tag_cell, null, false);
            TextView tag = tagView.findViewById(R.id.tv_tag);
            tag.setText(temp);
            // 设置标签点击事件
            ShadowLayout slTag = tagView.findViewById(R.id.sl_tag);
            slTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.equals(title,"经方")) {
                        startActivity(temp, TyphoidTheoryPrescriptionActivity.class);
                    } else {
                        startActivity(temp, TyphoidTheoryChineseHerbActivity.class);
                    }
                }
            });
            fl.addView(tagView);
        }

    }

    private void startActivity(String title, Class<?> cls) {
        Intent intent = new Intent(getContext(), cls);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }
}