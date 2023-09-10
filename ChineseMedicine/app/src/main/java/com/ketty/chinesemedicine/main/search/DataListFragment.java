package com.ketty.chinesemedicine.main.search;

import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEHERB;
import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEPATENTMEDICINE;
import static com.ketty.chinesemedicine.main.search.SearchType.MEDICATEDDIET;
import static com.ketty.chinesemedicine.main.search.SearchType.PRESCRIPTION;
import static com.ketty.chinesemedicine.main.search.SearchType.TYPHOIDTHEORY;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.SearchDataListAdapter;
import com.ketty.chinesemedicine.databinding.FragmentDataListBinding;
import com.ketty.chinesemedicine.entity.ChineseherbAndImages;
import com.ketty.chinesemedicine.entity.Medicateddiet;
import com.ketty.chinesemedicine.entity.NameAndType;
import com.ketty.chinesemedicine.entity.Prescription;
import com.ketty.chinesemedicine.main.home.ChinesePatentMedicineActivity;
import com.ketty.chinesemedicine.main.home.MedicatedDietActivity;
import com.ketty.chinesemedicine.main.home.chineseherb.ChineseHerbActivity;
import com.ketty.chinesemedicine.main.home.prescription.PrescriptionActivity;
import com.ketty.chinesemedicine.main.home.typhoidtheory.TyphoidTheoryChineseHerbActivity;
import com.ketty.chinesemedicine.main.home.typhoidtheory.TyphoidTheoryPrescriptionActivity;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataListFragment extends RxFragment {
    private FragmentDataListBinding bind;
    private static final String ARG_PARAM1 = "type";
    private static final String ARG_PARAM2 = "title";
    private int type;
    private String title;

    public static DataListFragment newInstance(int type, String title) {
        DataListFragment fragment = new DataListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, type);
        args.putString(ARG_PARAM2, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_PARAM1);
            title = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentDataListBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        LogUtils.i(String.valueOf(type));

        String path = "";
        switch (type) {
            case CHINESEHERB:
                path = "/chineseherb/selectLikeName";
                break;
            case PRESCRIPTION:
                path = "/prescription/selectLikeName";
                break;
            case CHINESEPATENTMEDICINE:
                path = "/chinesepatentmedicine/selectLikeName";
                break;
            case MEDICATEDDIET:
                path = "/medicateddiet/selectLikeName";
                break;
            case TYPHOIDTHEORY:
                path = "/typhoidtheorytype/selectLikeName";
                break;
        }
        initRequest(path);
    }

    private void initRecyclerView(List<?> list) {
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SearchDataListAdapter mAdapter = new SearchDataListAdapter(type,list);
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mAdapter.setOnItemClickListener(new SearchDataListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String title, int markType) {
                switch (type) {
                    case CHINESEHERB:
                        startActivity(ChineseHerbActivity.class, title);
                        break;
                    case PRESCRIPTION:
                        startActivity(PrescriptionActivity.class, title);
                        break;
                    case CHINESEPATENTMEDICINE:
                        startActivity(ChinesePatentMedicineActivity.class, title);
                        break;
                    case MEDICATEDDIET:
                        startActivity(MedicatedDietActivity.class, title);
                        break;
                    case TYPHOIDTHEORY:
                        LogUtils.i(title);
                        LogUtils.i(String.valueOf(markType));
                        if (markType == 0) {
                            startActivity(TyphoidTheoryPrescriptionActivity.class, title);
                        } else if (markType == 1) {
                            startActivity(TyphoidTheoryChineseHerbActivity.class, title);
                        }
                        break;
                }
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

    private void initRequest(String path) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", title);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod(path, map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        bind.progressBar.setVisibility(View.GONE);
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        List<?> list = new ArrayList<>();
                        switch (type) {
                            case CHINESEHERB:
                                list = JsonHelper.parserJson2List(
                                        data, new TypeToken<List<ChineseherbAndImages>>() {}.getType());
                                initRecyclerView(list);
                                break;
                            case PRESCRIPTION:
                                list = JsonHelper.parserJson2List(
                                        data, new TypeToken<List<Prescription>>() {}.getType());
                                initRecyclerView(list);
                                break;
                            case CHINESEPATENTMEDICINE:
                                list = JsonHelper.parserJson2List(
                                        data, new TypeToken<List<String>>() {}.getType());
                                initRecyclerView(list);
                                break;
                            case MEDICATEDDIET:
                                list = JsonHelper.parserJson2List(
                                        data, new TypeToken<List<Medicateddiet>>() {}.getType());
                                initRecyclerView(list);
                                break;
                            case TYPHOIDTHEORY:
                                list = JsonHelper.parserJson2List(
                                        data, new TypeToken<List<NameAndType>>() {}.getType());
                                initRecyclerView(list);
                                break;
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        bind.progressBar.setVisibility(View.GONE);
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }
}