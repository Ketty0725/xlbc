package com.ketty.chinesemedicine.main.store;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.databinding.FragmentProductExplainBinding;
import com.ketty.chinesemedicine.util.JsonHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductExplainFragment extends Fragment {
    private FragmentProductExplainBinding bind;
    private static final String ARG_PARAM1 = "type";
    private static final String ARG_PARAM2 = "id";
    private Long id;
    private int type;
    private List<Integer> starts = new ArrayList<>();
    private List<Integer> ends = new ArrayList<>();

    public static ProductExplainFragment newInstance(int type, Long id) {
        ProductExplainFragment fragment = new ProductExplainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, type);
        args.putLong(ARG_PARAM2, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_PARAM1);
            id = getArguments().getLong(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentProductExplainBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        String url = "";
        if (type == 1) {
            url = "/productbasicinfo/getInstructionBook";
            initRequest(url);
        }
    }

    private void initRequest(String url) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("productId", id);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod(url, map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        data = JsonHelper.parserJson2Object(
                                data, String.class);
                        initData(data);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initData(String data) {
        if (data == null) {
            data = "";
        }
        if (type == 1) {
            data = data.replaceAll(" ", "").replace("\\n", "\n");
            SpannableString string = new SpannableString(data);
            for (int i = 0; i < data.length(); i++) {
                String subStr = data.substring(i, i + 1);
                if (subStr.equals("【")) {
                    starts.add(i);
                } else if (subStr.equals("】")) {
                    ends.add(i);
                }
            }

            if (starts.size() == ends.size()) {
                for (int i = 0; i < starts.size(); i++) {
                    StyleSpan span = new StyleSpan(Typeface.BOLD);
                    string.setSpan(span, starts.get(i), ends.get(i) + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    string.setSpan(new ForegroundColorSpan(0xFF5B5B5B), starts.get(i), ends.get(i) + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }

            bind.tvContent.setText(string);
        }
    }
}