package com.ketty.chinesemedicine.main.community;

import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.R;
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
import com.ketty.chinesemedicine.util.DynamicTimeFormat;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cClassifyFragment extends RxFragment {
    private FragmentCClassifyBinding bind;
    private static final String ARG_PARAM = "type";
    private int type;
    private CommunityAdapter mAdapter;
    private Uri uri;
    private long current = 1;

    public static cClassifyFragment newInstance(int type) {
        cClassifyFragment fragment = new cClassifyFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(ARG_PARAM);
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
        initRecyclerView();
        initRequest();
        initRefresh();
        uri = Uri.parse("content://com.ketty.chinesemedicine.provider.cover/coverInfo");
        getActivity().getContentResolver().registerContentObserver(uri,true,
                new MyObserver(new Handler()));
    }

    private void initRefresh() {
        bind.refreshLayout.setPrimaryColorsId(R.color.white, R.color.app_color_9b);
        bind.refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()).setTimeFormat(new DynamicTimeFormat("更新于 %s")));
        bind.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                current = 1;
                initRequest();
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                initRequest();
            }
        });
    }

    private void initRecyclerView() {
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
        String url = "";
        if (type == 1) {
            url = "/community/selectPageByIp";
        } else if (type == 2) {
            url = "/community/selectAllPage";
        } else if (type == 3) {
            url = "/community/selectPageByConcern";
        }
        initData(url);
    }

    private void initData(String url) {
        if (current == 0) {
            bind.refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("current", current);
            if (type != 2) {
                map.put("userId", App.getMmkvUtil().getString("user","uId",null));
            }

            RetrofitManager
                    .getInstance()
                    .getApiService()
                    .postMethod(url, map)
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
                                bind.refreshLayout.finishRefresh(1000,true,false);
                            }
                            mAdapter.setData(communities, images, users, (int) ((current - 1) * 10));
                            bind.refreshLayout.finishLoadMore(1000, true, false);
                            current = page.getNextPage();
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg) {
                            bind.progressBar.setVisibility(View.GONE);
                            bind.refreshLayout.finishRefresh(false);
                            bind.refreshLayout.finishLoadMore(false);
                        }
                    });
        }

    }

    private class MyObserver extends ContentObserver {

        public MyObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (type == 2) {
                current = 1;
                initRequest();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getContext().getContentResolver().unregisterContentObserver(new MyObserver(new Handler()));
        bind = null;
    }

}