package com.ketty.chinesemedicine.main.community.details;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.draggable.library.extension.ImageViewerHelper;
import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.ImageViewAdapter;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.databinding.ActivityDetailsBinding;
import com.ketty.chinesemedicine.entity.Community;
import com.ketty.chinesemedicine.entity.Communityimage;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.entity.enums.ConcernEnum;
import com.ketty.chinesemedicine.main.community.comment.CommentBottomDialog;
import com.ketty.chinesemedicine.main.mine.UserHomeActivity;
import com.ketty.chinesemedicine.util.AppExecutors;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;
import com.ketty.chinesemedicine.util.MMKVUtil;
import com.ketty.chinesemedicine.util.TimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DetailsActivity extends BaseActivity implements View.OnClickListener {
    private ActivityDetailsBinding bind;
    private ImageViewAdapter mImageViewAdapter;
    private int lastPosition;
    private RequestOptions options = RequestOptions.circleCropTransform();
    private MMKVUtil mmkvUtil;
    private String uId;
    private ScheduledFuture<?> scheduledFuture;
    private boolean isConcerned = false;
    private Community community;
    private List<Communityimage> images;
    private User user;

    @Override
    protected View initLayout() {
        bind = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        mmkvUtil = App.getMmkvUtil();
        uId = mmkvUtil.getString("user","uId",null);

        mImageViewAdapter = new ImageViewAdapter();
        bind.viewpager2.setAdapter(mImageViewAdapter);

        bind.ivDetailsBack.setOnClickListener(this);
        bind.llComment.setOnClickListener(this);
        bind.ivLove.setOnClickListener(this);
        bind.ivCollect.setOnClickListener(this);
        bind.headIcon.setOnClickListener(this);
        bind.userName.setOnClickListener(this);
        bind.slConcern.setOnClickListener(this);

        bind.nestedScroll.setVisibility(View.INVISIBLE);

        initRequest();
    }

    private void initRequest() {
        Bundle bundle = getIntent().getExtras();
        long noteId = bundle.getLong("noteId");

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", noteId);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/community/getById", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        bind.progressBar.setVisibility(View.GONE);
                        bind.nestedScroll.setVisibility(View.VISIBLE);
                        String data = JsonHelper.parserObject2Json(response.get("data"));
                        community = JsonHelper.parserJson2Object(data, Community.class);
                        data = JsonHelper.parserObject2Json(response.get("images"));
                        images = JsonHelper.parserJson2List(data,
                                new TypeToken<List<Communityimage>>() {}.getType());
                        data = JsonHelper.parserObject2Json(response.get("user"));
                        user = JsonHelper.parserJson2Object(data, User.class);
                        initData();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", uId);
        map.put("noteId", community.getId());
        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/community/getIsLikedAndCollectByUserId", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("Liked"));
                        Integer isLiked = JsonHelper.parserJson2Object(
                                data, Integer.class);
                        if (isLiked == 1) {
                            bind.ivLove.setCheckedWithoutAnimator(true);
                        } else {
                            bind.ivLove.setCheckedWithoutAnimator(false);
                        }

                        data = JsonHelper.parserObject2Json(response.get("Collect"));
                        Integer isCollect = JsonHelper.parserJson2Object(
                                data, Integer.class);
                        if (isCollect == 1) {
                            bind.ivCollect.setCheckedWithoutAnimator(true);
                        } else {
                            bind.ivCollect.setCheckedWithoutAnimator(false);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("noteId", community.getId());
        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/comment/getTotalCount", map2)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        Long totalCount = JsonHelper.parserJson2Object(
                                data, Long.class);
                        bind.tvCommentCount.setText(String.valueOf(totalCount));
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });

        if (community.getUserId().equals(Long.valueOf(uId))) {
            bind.slConcern.setVisibility(View.GONE);
        } else {
            bind.slConcern.setVisibility(View.VISIBLE);
            initIsConcerned();
        }

        if (community.getLikeCount() > 99) {
            bind.tvLoveCount.setText("99+");
        } else {
            bind.tvLoveCount.setText(String.valueOf(community.getLikeCount()));
        }
        if (community.getCollectCount() > 99) {
            bind.tvCollectCount.setText("99+");
        } else {
            bind.tvCollectCount.setText(String.valueOf(community.getCollectCount()));
        }

        bind.tvCurrent.setText("1");
        bind.tvTotal.setText(String.valueOf(images.size()));

        Glide.with(DetailsActivity.this).load(user.getuHeadicon()).apply(options).into(bind.headIcon);

        mImageViewAdapter.setData(images);
        mImageViewAdapter.notifyDataSetChanged();

        bind.userName.setText(user.getuName());
        bind.tvTitle.setText(community.getTitle());
        bind.tvContent.setText(community.getContent());
        bind.tvDate.setText(TimeUtils.getRecentTimeSpanByNow(community.getTime()));
        String ip = community.getIpAddress();
        if (ip.contains("-")) {
            String[] ipAddress = ip.split("-");
            if ("中国".equals(ipAddress[0]) && !"".equals(ipAddress[1])) {
                ip = ipAddress[1];
            } else {
                ip = ipAddress[0];
            }
        }
        bind.tvIpAddress.setText(ip);

        lastPosition = images.size()-1;
        LogUtils.i("lastPosition", String.valueOf(lastPosition));

        if (Integer.parseInt(bind.tvTotal.getText().toString()) > 1) {
            initIndicatorDots();

            bind.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    bind.layoutTab.setVisibility(View.VISIBLE);
                    //轮播时，改变指示点
                    int current = position % images.size();
                    int last = lastPosition % images.size();
                    bind.containerIndicator.getChildAt(current).setBackgroundResource(R.drawable.shape_dot_selected);
                    bind.containerIndicator.getChildAt(last).setBackgroundResource(R.drawable.shape_dot);
                    lastPosition = position;
                    bind.tvCurrent.setText(String.valueOf(position+1));
                }
            });
        }

        List<ImageViewerHelper.ImageInfo> imageInfos = new ArrayList<>();
        List<View> views = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            imageInfos.add(new ImageViewerHelper.ImageInfo(images.get(i).getImageUrl(),"",0));
            views.add(bind.viewpager2);
        }
        mImageViewAdapter.setOnItemClickListener(new ImageViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ImageViewerHelper.showImages(DetailsActivity.this,views,imageInfos,position,true);
            }
        });

        scheduledFuture = AppExecutors.getInstance().scheduledExecutor().schedule(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> map = new HashMap<>();
                map.put("userId", uId);
                map.put("beBrowseId", community.getId());
                RetrofitManager
                        .getInstance()
                        .getApiService()
                        .postMethod("/browsinghistory/addOrUpdateToRedis", map)
                        .compose(RxHelper.observableIO2Main(DetailsActivity.this))
                        .subscribe(new BaseObserver() {
                            @Override
                            public void onSuccess(Map<String, Object> response) {

                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg) {

                            }
                        });
            }
        }, 5, TimeUnit.SECONDS);
    }

    private void initIndicatorDots() {
        for (int i = 0; i < images.size(); i++) {
            ImageView imageView = new ImageView(this);
            if (i == 0) imageView.setBackgroundResource(R.drawable.shape_dot_selected);
            else imageView.setBackgroundResource(R.drawable.shape_dot);
            //为指示点添加间距
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMarginEnd(8);
            imageView.setLayoutParams(layoutParams);
            //将指示点添加进容器
            bind.containerIndicator.addView(imageView);
        }
    }

    private void initIsConcerned() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("concernId", uId);
        map.put("beConcernedId", user.getuId());
        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/concern/selectIsConcerned", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        int state = JsonHelper.parserJson2Object(data,Integer.class);
                        if (state == ConcernEnum.NONE.getCode() || state == ConcernEnum.OPPOSITE.getCode()) {
                            isConcerned = false;
                            bind.slConcern.setStrokeColor(0xFFff2442);
                            bind.tvConcern.setTextColor(0xFFff2442);
                            if (state == ConcernEnum.NONE.getCode()) {
                                bind.tvConcern.setText("关注");
                            } else if (state == ConcernEnum.OPPOSITE.getCode()) {
                                bind.tvConcern.setText("回粉");
                            }
                        } else if (state == ConcernEnum.ALIKE.getCode() || state == ConcernEnum.ALL.getCode()) {
                            isConcerned = true;
                            bind.slConcern.setStrokeColor(0xFFcccccc);
                            bind.tvConcern.setTextColor(0xFF999999);
                            if (state == ConcernEnum.ALIKE.getCode()) {
                                bind.tvConcern.setText("已关注");
                            } else if (state == ConcernEnum.ALL.getCode()) {
                                bind.tvConcern.setText("互相关注");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void changeState() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("concernId", uId);
        map.put("beConcernedId", user.getuId());
        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/concern/addOrDelete", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        initIsConcerned();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_details_back:
                finish();
                break;
            case R.id.ll_comment:
                CommentBottomDialog commentBottomDialog = new CommentBottomDialog();
                Bundle bundle = new Bundle();
                bundle.putLong("noteId", community.getId());
                commentBottomDialog.setArguments(bundle);
                commentBottomDialog.show(getSupportFragmentManager(), "dialog");
                commentBottomDialog.setStateListener(new CommentBottomDialog.StateListener() {
                    @Override
                    public void close() {
                        commentBottomDialog.dismiss();
                    }
                });
                break;
            case R.id.iv_love:
                HashMap<String, Object> map = new HashMap<>();
                map.put("userId", uId);
                map.put("beLikedId", community.getId());

                RetrofitManager
                        .getInstance()
                        .getApiService()
                        .postMethod("/community/updateLikedState", map)
                        .compose(RxHelper.observableIO2Main(this))
                        .subscribe(new BaseObserver() {
                            @Override
                            public void onSuccess(Map<String, Object> response) {
                                bind.ivLove.toggle();
                                int loveCount = Integer.parseInt(bind.tvLoveCount.getText().toString());
                                if (bind.ivLove.isChecked()) {
                                    loveCount++;
                                } else {
                                    loveCount--;
                                }
                                bind.tvLoveCount.setText(String.valueOf(loveCount));
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg) {

                            }
                        });
                break;
            case R.id.iv_collect:
                HashMap<String, Object> map2 = new HashMap<>();
                map2.put("userId", uId);
                map2.put("beCollectId", community.getId());

                RetrofitManager
                        .getInstance()
                        .getApiService()
                        .postMethod("/community/updateCollectState", map2)
                        .compose(RxHelper.observableIO2Main(this))
                        .subscribe(new BaseObserver() {
                            @Override
                            public void onSuccess(Map<String, Object> response) {
                                bind.ivCollect.toggle();
                                int collectCount = Integer.parseInt(bind.tvCollectCount.getText().toString());
                                if (bind.ivCollect.isChecked()) {
                                    collectCount++;
                                } else {
                                    collectCount--;
                                }
                                bind.tvCollectCount.setText(String.valueOf(collectCount));
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg) {

                            }
                        });
                break;
            case R.id.head_icon:
            case R.id.user_name:
                Intent intent = new Intent(this, UserHomeActivity.class);
                Bundle bundle2 = new Bundle();
                if (uId.equals(user.getuId().toString())) {
                    bundle2.putInt("type",0);
                    bundle2.putLong("userId", Long.parseLong(uId));
                    intent.putExtras(bundle2);
                    startActivity(intent);
                } else {
                    bundle2.putInt("type",1);
                    bundle2.putLong("userId",user.getuId());
                    intent.putExtras(bundle2);
                    startActivity(intent);
                }
                break;
            case R.id.sl_concern:
                if (isConcerned) {
                    new CommonAlertDialog(this).builder()
                            .setTitle("确定不再关注对方？")
                            .setNegativeButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    changeState();
                                }
                            }).setPositiveButton("再想想", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).setCancelable(false)
                            .show();
                } else {
                    changeState();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            if (scheduledFuture != null) {
                scheduledFuture.cancel(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        bind = null;
    }
}