package com.ketty.chinesemedicine.main.mine;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.draggable.library.extension.ImageViewerHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.databinding.ActivityUserHomeBinding;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.entity.enums.ConcernEnum;
import com.ketty.chinesemedicine.main.mine.concern.ConcernListActivity;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.ScreenUtils;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;

import java.util.HashMap;
import java.util.Map;

public class UserHomeActivity extends BaseActivity implements View.OnClickListener {
    private ActivityUserHomeBinding bind;
    private RequestOptions options = RequestOptions.circleCropTransform();
    private int mOffset = 0;
    private int mScrollY = 0;
    private User user;
    private int type;
    private long userId;
    private boolean isConcerned = false;

    @Override
    protected View initLayout() {
        bind = ActivityUserHomeBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");
        userId = bundle.getLong("userId");

        if (type == 0) {
            bind.slButton.setPadding(DisplayUtil.dip2px(this,15),
                    DisplayUtil.dip2px(this,6),
                    DisplayUtil.dip2px(this,15),
                    DisplayUtil.dip2px(this,6));
            bind.slButton.setLayoutBackground(0x4D696e74);
            bind.slButton.setStrokeColor(0xFFAFAFAF);
            bind.tvButton.setText("编辑资料");
        } else {
            bind.slButton.setPadding(DisplayUtil.dip2px(this,20),
                    DisplayUtil.dip2px(this,5),
                    DisplayUtil.dip2px(this,20),
                    DisplayUtil.dip2px(this,5));
            bind.slButton.setLayoutBackground(0xFFff2442);
            bind.slButton.setStrokeColor(0xFFff2442);
            bind.tvButton.setText("关注");
            initIsConcerned();
        }

        bind.refreshLayout.setEnableLoadMore(false);
        bind.refreshLayout.setEnablePureScrollMode(true);
        bind.refreshLayout.setPrimaryColorsId(android.R.color.transparent, android.R.color.white);
        bind.refreshLayout.setOnMultiListener(new SimpleMultiListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                mOffset = offset / 2;
                bind.parallax.setTranslationY(mOffset - mScrollY);
            }
        });

        bind.ivUseravator.setOnClickListener(this);
        bind.slButton.setOnClickListener(this);
        bind.slConcern.setOnClickListener(this);
        bind.ivBack.setOnClickListener(this);
        bind.tvConcernNumber.setOnClickListener(this);
        bind.tgConcern.setOnClickListener(this);
        bind.tvFansNumber.setOnClickListener(this);
        bind.tgFans.setOnClickListener(this);

        bind.toolbar.setPadding(0, ScreenUtils.getStatusHeight(UserHomeActivity.this),0,0);
        bind.toolbar.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                bind.toolbar.removeOnLayoutChangeListener(this);
                int height = bind.toolbar.getHeight();
                ViewGroup.LayoutParams para = bind.viewTop.getLayoutParams();
                para.height = height;
                bind.viewTop.setLayoutParams(para);
            }
        });
        bind.toolbar.setTitleTextColor(Color.WHITE);
        bind.ctlToolbar.setTitleEnabled(false);
        bind.ctlToolbar.setExpandedTitleGravity(Gravity.CENTER);
        bind.ctlToolbar.setCollapsedTitleGravity(Gravity.CENTER);
        bind.ctlToolbar.setExpandedTitleColor(Color.WHITE);
        bind.ctlToolbar.setCollapsedTitleTextColor(Color.BLACK);

        bind.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int Offset = Math.abs(verticalOffset);
                bind.toolbar.setBackgroundColor(changeAlpha(getResources().getColor
                        (R.color.white),Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));

                if (Offset < appBarLayout.getTotalScrollRange() / 2) {
                    bind.toolbar.setTitle("");
                    if (type == 0) {
                        bind.title.setVisibility(View.VISIBLE);
                        bind.title.setAlpha((appBarLayout.getTotalScrollRange() / 2 - Offset * 1.0f) / (appBarLayout.getTotalScrollRange() / 2));
                        bind.title.setTextColor(Color.WHITE);
                        bind.slConcern.setVisibility(View.GONE);
                    } else {
                        bind.title.setVisibility(View.GONE);
                        bind.slConcern.setVisibility(View.INVISIBLE);
                    }
                    bind.ivBack.setAlpha((appBarLayout.getTotalScrollRange() / 2 - Offset * 1.0f) / (appBarLayout.getTotalScrollRange() / 2));
                    Drawable drawable = ContextCompat.getDrawable(UserHomeActivity.this, R.drawable.ic_back).mutate();
                    Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
                    wrappedDrawable.setTint(0xFFffffff);
                    bind.ivBack.setImageDrawable(wrappedDrawable);
                    bind.ivUseravatorHead.setVisibility(View.INVISIBLE);
                    bind.parallax.setTranslationY(mOffset - Offset);
                } else if (Offset > appBarLayout.getTotalScrollRange() / 2) {
                    float floate = (Offset - appBarLayout.getTotalScrollRange() / 2) * 1.0f / (appBarLayout.getTotalScrollRange() / 2);
                    bind.ivBack.setAlpha(floate);
                    Drawable drawable = ContextCompat.getDrawable(UserHomeActivity.this, R.drawable.ic_back).mutate();
                    Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
                    wrappedDrawable.setTint(0xFF000000);
                    bind.ivBack.setImageDrawable(wrappedDrawable);
                    if (type == 0) {
                        bind.title.setVisibility(View.INVISIBLE);
                        bind.slConcern.setVisibility(View.GONE);
                    } else {
                        bind.title.setVisibility(View.GONE);
                        bind.slConcern.setVisibility(View.VISIBLE);
                        bind.slConcern.setAlpha(floate);
                    }
                    bind.ivUseravatorHead.setVisibility(View.VISIBLE);
                    bind.ivUseravatorHead.setAlpha(floate);
                    bind.parallax.setTranslationY(mOffset - Offset);
                }
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, NoteFragment.newInstance(userId, type)).commit();

        initRequest();
        initConcern();
    }

    private void initRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uId", userId);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/getUserInfo", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        user = JsonHelper.parserJson2Object(
                                data, User.class);
                        initData();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initData() {
        if ("男".equals(user.getuSex())) {
            bind.ivSex.setImageResource(R.drawable.ic_male);
        } else {
            bind.ivSex.setImageResource(R.drawable.ic_female);
        }

        Glide.with(this).load(user.getuHeadicon()).apply(options).into(bind.ivUseravator);
        Glide.with(this).load(user.getuHeadicon()).apply(options).into(bind.ivUseravatorHead);
        Glide.with(this).load(user.getBackgroundImage()).into(bind.parallax);

        bind.tvUsername.setText(user.getuName());
        bind.tvUserid.setText("小杏林号："+user.getuId());

        String ip = user.getIpAddress();
        if (ip.contains("-")) {
            String[] ipAddress = ip.split("-");
            if ("中国".equals(ipAddress[0]) && !"".equals(ipAddress[1])) {
                ip = ipAddress[1];
            } else {
                ip = ipAddress[0];
            }
        }
        bind.tvIpAddress.setText("IP属地：" + ip);

        if (user.getuAbout() != null && !"".equals(user.getuAbout())) {
            bind.personAbout.setText(user.getuAbout());
        } else {
            bind.personAbout.setText("暂时还没有简介");
        }
    }

    private void initConcern() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/concern/getConcernCount", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("concern"));
                        long count = JsonHelper.parserJson2Object(data,Long.class);
                        bind.tvConcernNumber.setText(String.valueOf(count));

                        data = JsonHelper.parserObject2Json(response.get("beConcerned"));
                        count = JsonHelper.parserJson2Object(data,Long.class);
                        bind.tvFansNumber.setText(String.valueOf(count));
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initIsConcerned() {
        if (type == 1) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("concernId", App.getMmkvUtil().getString("user","uId",null));
            map.put("beConcernedId", userId);
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
                                bind.slButton.setLayoutBackground(0xFFff2442);
                                bind.slButton.setStrokeColor(0xFFff2442);
                                bind.slConcern.setLayoutBackground(0xFFff2442);
                                bind.slConcern.setStrokeColor(0xFFff2442);
                                bind.tvConcern.setTextColor(0xFFffffff);
                                if (state == ConcernEnum.NONE.getCode()) {
                                    bind.tvButton.setText("关注");
                                    bind.tvConcern.setText("关注");
                                } else if (state == ConcernEnum.OPPOSITE.getCode()) {
                                    bind.tvButton.setText("回粉");
                                    bind.tvConcern.setText("回粉");
                                }
                            } else if (state == ConcernEnum.ALIKE.getCode() || state == ConcernEnum.ALL.getCode()) {
                                isConcerned = true;
                                bind.slButton.setLayoutBackground(0x4D696e74);
                                bind.slButton.setStrokeColor(0xFFAFAFAF);
                                bind.slConcern.setLayoutBackground(0x00000000);
                                bind.slConcern.setStrokeColor(0xFFcccccc);
                                bind.tvConcern.setTextColor(0xFF999999);
                                if (state == ConcernEnum.ALIKE.getCode()) {
                                    bind.tvButton.setText("已关注");
                                    bind.tvConcern.setText("已关注");
                                } else if (state == ConcernEnum.ALL.getCode()) {
                                    bind.tvButton.setText("互相关注");
                                    bind.tvConcern.setText("互相关注");
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg) {

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
            case R.id.iv_useravator:
                ImageViewerHelper.showSimpleImage(this, user.getuHeadicon(), "", bind.ivUseravator, false);
                break;
            case R.id.sl_concern:
            case R.id.sl_button:
                if (type == 1) {
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
                } else {
                    Intent intent = new Intent(this, UserInfoActivity.class);
                    mActivityLauncher.launch(intent);
                }
                break;
            case R.id.tv_concern_number:
            case R.id.tg_concern:
                startActivity(ConcernListActivity.class, 0);
                break;
            case R.id.tv_fans_number:
            case R.id.tg_fans:
                startActivity(ConcernListActivity.class, 1);
                break;
        }
    }

    private void startActivity(Class<?> cls, int type) {
        Intent intent = new Intent(this,cls);
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        bundle.putLong("userId",user.getuId());
        bundle.putString("userName",user.getuName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void changeState() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("concernId", App.getMmkvUtil().getString("user","uId",null));
        map.put("beConcernedId", userId);
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

    private final ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                initRequest();
            }
        }
    });

    private int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}