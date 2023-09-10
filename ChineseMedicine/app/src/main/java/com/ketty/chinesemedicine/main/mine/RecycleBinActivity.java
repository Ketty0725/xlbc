package com.ketty.chinesemedicine.main.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.RecycleBinAdapter;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.databinding.ActivityRecycleBinBinding;
import com.ketty.chinesemedicine.entity.Community;
import com.ketty.chinesemedicine.entity.Communityimage;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.main.community.details.DetailsActivity;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.T;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecycleBinActivity extends BaseActivity implements View.OnClickListener {
    private ActivityRecycleBinBinding bind;
    private RecycleBinAdapter mAdapter;
    private List<Community> communities;
    private List<Community> communitySelected = new ArrayList<>();
    private long userId;
    private boolean isManaged = false;

    @Override
    protected View initLayout() {
        bind = ActivityRecycleBinBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        userId = Long.parseLong(App.getMmkvUtil().getString("user", "uId", null));
        initRecyclerView();
        initRequest();

        bind.ivBack.setOnClickListener(this);
        bind.tvManage.setOnClickListener(this);
        bind.scb.setOnClickListener(this);
        bind.tvSelectAll.setOnClickListener(this);
        bind.slDelete.setOnClickListener(this);
        bind.slRecover.setOnClickListener(this);
    }

    private void initRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/community/getFromRecycled", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("communities"));
                        communities = JsonHelper.parserJson2List(data,
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
                        mAdapter.setData(communities, images, users);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initRecyclerView() {
        mAdapter = new RecycleBinAdapter();
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

        mAdapter.setOnItemClickListener(new RecycleBinAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(long id) {
                if (!isManaged) {
                    Intent intent = new Intent(RecycleBinActivity.this, DetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putLong("noteId", id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

            @Override
            public void onAllChecked(boolean checked) {
                bind.scb.setChecked(checked);
            }

            @Override
            public void onFinished(List<Community> communitySelected) {
                RecycleBinActivity.this.communitySelected = communitySelected;
                if (communitySelected.size() > 0) {
                    bind.slDelete.setClickable(true);
                    bind.slRecover.setClickable(true);
                    bind.slDelete.setLayoutBackground(0xFFff2442);
                    bind.slRecover.setLayoutBackground(0xFFffffff);
                    bind.slRecover.setStrokeColor(0xFF858585);
                } else {
                    bind.slRecover.setLayoutBackground(0xFFDCDCDC);
                    bind.slRecover.setStrokeColor(0xFFDCDCDC);
                    bind.slDelete.setLayoutBackground(0xFFffa5a5);
                    bind.slRecover.setClickable(false);
                    bind.slDelete.setClickable(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_manage:
                isManaged = !isManaged;
                if (isManaged) {
                    bind.tvManage.setText("完成");
                    bind.tvManage.setTextColor(0xFFff2442);
                    bind.motionLayout.transitionToEnd();
                } else {
                    bind.tvManage.setText("管理");
                    bind.tvManage.setTextColor(0xFF333333);
                    bind.motionLayout.transitionToStart();
                }
                mAdapter.changeState(isManaged);
                break;
            case R.id.scb:
            case R.id.tv_select_all:
                if (communities.size() > 0) {
                    if (bind.scb.isChecked()) {
                        bind.scb.setChecked(false);
                        mAdapter.revertSelected();
                    } else {
                        bind.scb.setChecked(true);
                        mAdapter.selectedAll();
                    }
                }
                break;
            case R.id.sl_recover:
                if (communitySelected.size() > 0) {
                    String title = "恢复";
                    String msg = "确认将这"+communitySelected.size()+"篇笔记还原？";
                    String success = "恢复成功";
                    String url = "/community/retrievalFromRecycled";
                    submit(title,msg,url,success);
                }
                break;
            case R.id.sl_delete:
                if (communitySelected.size() > 0) {
                    String title = "彻底删除";
                    String msg = "确认将这"+communitySelected.size()+"篇笔记彻底删除？";
                    String success = "删除成功";
                    String url = "/community/deleteOfCompletely";
                    submit(title,msg,url,success);
                }
                break;
        }
    }

    private void submit(String title, String msg, String url, String success) {
        new CommonAlertDialog(this).builder()
                .setTitle(title)
                .setMsg(msg)
                .setNegativeButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        update(url, success);
                    }
                }).setPositiveButton("再想想", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).setCancelable(false)
                .show();
    }

    private void update(String url, String success) {
        for (Community community : communitySelected) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", community.getId());

            RetrofitManager
                    .getInstance()
                    .getApiService()
                    .postMethod(url, map)
                    .compose(RxHelper.observableIO2Main(this))
                    .subscribe(new BaseObserver() {
                        @Override
                        public void onSuccess(Map<String, Object> response) {
                            mAdapter.removeItem(community);
                            T.showShort(success);
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg) {

                        }
                    });
        }
    }
}