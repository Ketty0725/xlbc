package com.ketty.chinesemedicine.main.mine.concern;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.ConcernListAdapter;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.databinding.FragmentConcernListBinding;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.entity.enums.ConcernEnum;
import com.ketty.chinesemedicine.main.mine.UserHomeActivity;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConcernListFragment extends RxFragment {
    private FragmentConcernListBinding bind;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int type;
    private long userId;
    private ConcernListAdapter mAdapter;
    private boolean isMine = false;
    private long mineId;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isResumed()){
                resume();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()){
            resume();
        }
    }

    private void resume() {
        setRequestData();
    }

    public static ConcernListFragment newInstance(int type, long userId) {
        ConcernListFragment fragment = new ConcernListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, type);
        args.putLong(ARG_PARAM2, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_PARAM1);
            userId = getArguments().getLong(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentConcernListBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRecyclerView();
        initView();
    }

    private void initView() {
        mineId = Long.parseLong(App.getMmkvUtil().getString("user","uId",null));
        if (mineId == userId) {
            isMine = true;
        } else {
            isMine = false;
        }
    }

    private void setRequestData() {
        String url = "";
        if (type == 0) {
            url = "/concern/selectConcernList";
            initData(url);
        } else if (type == 1) {
            url = "/concern/selectBeConcernedList";
            initData(url);
        }
    }

    private void initData(String url) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("mineId", App.getMmkvUtil().getString("user","uId",null));

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod(url, map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("users"));
                        List<User> users = JsonHelper.parserJson2List(
                                data, new TypeToken<List<User>>() {}.getType());

                        data = JsonHelper.parserObject2Json(response.get("states"));
                        List<Integer> states = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Integer>>() {}.getType());
                        mAdapter.setData(users, states);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initRecyclerView() {
        bind.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ConcernListAdapter();
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mAdapter.setOnItemClickListener(new ConcernListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Long userId) {
                Intent intent = new Intent(getContext(), UserHomeActivity.class);
                Bundle bundle2 = new Bundle();
                if (userId.equals(mineId)) {
                    bundle2.putInt("type",0);
                    bundle2.putLong("userId", mineId);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                } else {
                    bundle2.putInt("type",1);
                    bundle2.putLong("userId", userId);
                    intent.putExtras(bundle2);
                    startActivity(intent);
                }
            }

            @Override
            public void onConcernClick(Long userId, int state, int position) {
                if (state == ConcernEnum.ALIKE.getCode() || state == ConcernEnum.ALL.getCode()) {
                    new CommonAlertDialog(getContext()).builder()
                            .setTitle("确定不再关注对方？")
                            .setNegativeButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    changeState(userId, state, position);
                                }
                            }).setPositiveButton("再想想", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).setCancelable(false)
                            .show();
                } else if (state == ConcernEnum.NONE.getCode() || state == ConcernEnum.OPPOSITE.getCode()) {
                    changeState(userId, state, position);
                }
            }
        });
    }

    private void changeState(Long userId, int state, int position) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("concernId", App.getMmkvUtil().getString("user","uId",null));
        map.put("beConcernedId", userId);
        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/concern/addOrDelete", map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        int newState = 0;
                        if (isMine && type == 0) {
                            mAdapter.removeItem(position);
                        } else {
                            if (state == ConcernEnum.NONE.getCode()) {
                                newState = ConcernEnum.ALIKE.getCode();
                            } else if (state == ConcernEnum.ALIKE.getCode()) {
                                newState = ConcernEnum.NONE.getCode();
                            } else if (state == ConcernEnum.OPPOSITE.getCode()) {
                                newState = ConcernEnum.ALL.getCode();
                            } else if (state == ConcernEnum.ALL.getCode()) {
                                newState = ConcernEnum.OPPOSITE.getCode();
                            }
                            mAdapter.setConcerned(newState, position);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

}