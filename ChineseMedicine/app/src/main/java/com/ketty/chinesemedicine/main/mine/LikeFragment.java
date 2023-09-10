package com.ketty.chinesemedicine.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.CommunityAdapter;
import com.ketty.chinesemedicine.databinding.FragmentLikeBinding;
import com.ketty.chinesemedicine.entity.Community;
import com.ketty.chinesemedicine.entity.Communityimage;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.main.community.details.DetailsActivity;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LikeFragment extends RxFragment {
    private FragmentLikeBinding bind;
    private CommunityAdapter mAdapter;

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
        initRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentLikeBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        mAdapter = new CommunityAdapter();
        bind.recyclerView.setHasFixedSize(true);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE); //防止item交换位置
        bind.recyclerView.setLayoutManager(layoutManager);
        bind.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //防止第一行到顶部有空白区域
                layoutManager.invalidateSpanAssignments();
            }
        });
        bind.recyclerView.getItemAnimator().setChangeDuration(0);
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mAdapter.setOnItemClickListener(new CommunityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long id) {
                Intent intent = new Intent(getContext(), DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("noteId", id);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int pos, long id) {

            }
        });
    }

    private void initRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", App.getMmkvUtil().getString("user","uId",null));

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/community/getBeLikedList", map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("data"));
                        List<Community> communities = JsonHelper.parserJson2List(data,
                                new TypeToken<List<Community>>() {
                                }.getType());
                        data = JsonHelper.parserObject2Json(response.get("images"));
                        List<Communityimage> images = JsonHelper.parserJson2List(data,
                                new TypeToken<List<Communityimage>>() {
                                }.getType());
                        data = JsonHelper.parserObject2Json(response.get("users"));
                        List<User> users = JsonHelper.parserJson2List(data,
                                new TypeToken<List<User>>() {
                                }.getType());
                        mAdapter.clearData();
                        mAdapter.setData(communities, images, users, 0);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

}