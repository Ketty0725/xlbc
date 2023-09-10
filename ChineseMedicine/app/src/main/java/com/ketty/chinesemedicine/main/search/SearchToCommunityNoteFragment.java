package com.ketty.chinesemedicine.main.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.CommunityAdapter;
import com.ketty.chinesemedicine.databinding.FragmentCClassifyBinding;
import com.ketty.chinesemedicine.entity.Community;
import com.ketty.chinesemedicine.entity.Communityimage;
import com.ketty.chinesemedicine.entity.PageEnable;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.main.community.details.DetailsActivity;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchToCommunityNoteFragment extends RxFragment {
    private FragmentCClassifyBinding bind;
    private static final String ARG_PARAM1 = "title";
    private String title;
    private CommunityAdapter mAdapter;
    private long current;

    public static SearchToCommunityNoteFragment newInstance(String title) {
        SearchToCommunityNoteFragment fragment = new SearchToCommunityNoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentCClassifyBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        current = 1;
        initView();
        initRequest();
        initRefresh();
    }

    private void initRefresh() {
        bind.refreshLayout.setEnableRefresh(false);
        bind.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                initRequest();
            }
        });
    }

    private void initView() {
        mAdapter = new CommunityAdapter();
        bind.recyclerview.setHasFixedSize(true);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE); //防止item交换位置
        bind.recyclerview.setLayoutManager(layoutManager);
        bind.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //防止第一行到顶部有空白区域
                layoutManager.invalidateSpanAssignments();
            }
        });
        bind.recyclerview.getItemAnimator().setChangeDuration(0);
        bind.recyclerview.setAdapter(mAdapter);
        bind.recyclerview.setOverScrollMode(View.OVER_SCROLL_NEVER);

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
        if (current == 0) {
            bind.refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("current", current);
            map.put("title", title);

            RetrofitManager
                    .getInstance()
                    .getApiService()
                    .postMethod("/community/selectPageByTitle", map)
                    .compose(RxHelper.observableIO2Main(getContext()))
                    .subscribe(new BaseObserver() {
                        @Override
                        public void onSuccess(Map<String, Object> response) {
                            bind.progressBar.setVisibility(View.GONE);
                            String data = JsonHelper.parserObject2Json(response.get("data"));
                            List<Community> communities = JsonHelper.parserJson2List(data,
                                    new TypeToken<List<Community>>(){}.getType());
                            data = JsonHelper.parserObject2Json(response.get("images"));
                            List<Communityimage> images = JsonHelper.parserJson2List(data,
                                    new TypeToken<List<Communityimage>>(){}.getType());
                            data = JsonHelper.parserObject2Json(response.get("users"));
                            List<User> users = JsonHelper.parserJson2List(data,
                                    new TypeToken<List<User>>(){}.getType());
                            data = JsonHelper.parserObject2Json(response.get("page"));
                            PageEnable page = JsonHelper.parserJson2Object(data, PageEnable.class);
                            if (current == 1) {
                                mAdapter.clearData();
                            }
                            mAdapter.setData(communities, images, users, (int) ((current - 1) * 10));
                            bind.refreshLayout.finishLoadMore(1000, true, false);
                            current = page.getNextPage();
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg) {
                            bind.progressBar.setVisibility(View.GONE);
                            bind.refreshLayout.finishLoadMore(false);
                        }
                    });
        }
    }
}