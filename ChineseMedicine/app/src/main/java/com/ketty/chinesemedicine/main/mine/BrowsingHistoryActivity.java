package com.ketty.chinesemedicine.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.CommunityAdapter;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.databinding.ActivityBrowsingHistoryBinding;
import com.ketty.chinesemedicine.entity.Community;
import com.ketty.chinesemedicine.entity.Communityimage;
import com.ketty.chinesemedicine.entity.PageEnable;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.main.community.details.DetailsActivity;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrowsingHistoryActivity extends BaseActivity implements View.OnClickListener {
    private ActivityBrowsingHistoryBinding bind;
    private CommunityAdapter mAdapter;
    private long current;

    @Override
    protected View initLayout() {
        bind = ActivityBrowsingHistoryBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        current = 1;
        initRecyclerView();
        initData();
        initRefresh();

        bind.ivBack.setOnClickListener(this);
        bind.ivClear.setOnClickListener(this);
    }

    private void initRefresh() {
        bind.include.refreshLayout.setEnableRefresh(false);
        bind.include.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
    }

    private void initRecyclerView() {
        mAdapter = new CommunityAdapter();
        bind.include.recyclerview.setHasFixedSize(true);
        final StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE); //防止item交换位置
        bind.include.recyclerview.setLayoutManager(layoutManager);
        bind.include.recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //防止第一行到顶部有空白区域
                layoutManager.invalidateSpanAssignments();
            }
        });
        bind.include.recyclerview.getItemAnimator().setChangeDuration(0);
        bind.include.recyclerview.setAdapter(mAdapter);
        bind.include.recyclerview.setOverScrollMode(View.OVER_SCROLL_NEVER);

        mAdapter.setOnItemClickListener(new CommunityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long id) {
                Intent intent = new Intent(BrowsingHistoryActivity.this, DetailsActivity.class);
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

    private void initData() {
        if (current == 0) {
            bind.include.refreshLayout.finishLoadMoreWithNoMoreData();
        } else {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", App.getMmkvUtil().getString("user","uId",null));
            map.put("currentPage", current);

            RetrofitManager
                    .getInstance()
                    .getApiService()
                    .postMethod("/browsinghistory/getByUserId", map)
                    .compose(RxHelper.observableIO2Main(this))
                    .subscribe(new BaseObserver() {
                        @Override
                        public void onSuccess(Map<String, Object> response) {
                            bind.include.progressBar.setVisibility(View.GONE);
                            String data = JsonHelper.parserObject2Json(response.get("communities"));
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

                            mAdapter.setData(communities, images, users, (int) ((current - 1) * 10));
                            bind.include.refreshLayout.finishLoadMore(1000, true, false);
                            current = page.getNextPage();
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg) {
                            bind.include.progressBar.setVisibility(View.GONE);
                            bind.include.refreshLayout.finishLoadMore(false);
                        }
                    });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear:
                clearData();
                break;
        }
    }

    private void clearData() {
        new CommonAlertDialog(this).builder()
                .setTitle("确认清空所有浏览记录？")
                .setNegativeButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("userId", App.getMmkvUtil().getString("user","uId",null));

                        RetrofitManager
                                .getInstance()
                                .getApiService()
                                .postMethod("/browsinghistory/deleteAll", map)
                                .compose(RxHelper.observableIO2Main(BrowsingHistoryActivity.this))
                                .subscribe(new BaseObserver() {
                                    @Override
                                    public void onSuccess(Map<String, Object> response) {
                                        mAdapter.clearData();
                                    }

                                    @Override
                                    public void onFailure(Throwable e, String errorMsg) {

                                    }
                                });
                    }
                }).setPositiveButton("再想想", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}