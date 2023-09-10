package com.ketty.chinesemedicine.main.mine;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.draggable.library.extension.ImageViewerHelper;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.MainActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.FragmentViewPagerAdapter;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.component.view.TipsAlertDialog;
import com.ketty.chinesemedicine.databinding.FragmentMineBinding;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.main.mine.collect.CollectFragment;
import com.ketty.chinesemedicine.main.mine.concern.ConcernListActivity;
import com.ketty.chinesemedicine.main.mine.setting.SettingActivity;
import com.ketty.chinesemedicine.main.store.ShippingAddressActivity;
import com.ketty.chinesemedicine.main.store.ShoppingCartActivity;
import com.ketty.chinesemedicine.main.store.orderform.OrderFormActivity;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.DynamicTimeFormat;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.ScreenUtils;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MineFragment extends Fragment implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
    private FragmentMineBinding bind;
    private List<String> mTitleList;
    private FragmentViewPagerAdapter mViewPagerAdapter;
    private List<Fragment> mFragmentList;
    private RequestOptions options = RequestOptions.circleCropTransform();
    private int mOffset = 0;
    private int mScrollY = 0;
    private final NoteFragment noteFragment = NoteFragment.newInstance(
            Long.parseLong(App.getMmkvUtil().getString("user","uId",null)),0);
    private final LikeFragment likeFragment = new LikeFragment();
    private final CollectFragment collectFragment = new CollectFragment();
    private User user;

    private static volatile MineFragment obj = null;

    public static MineFragment getInstance() {
        if (obj == null) {
            synchronized(MineFragment.class) {
                if (obj == null) {
                    obj = new MineFragment();
                }
            }
        }
        return obj;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentMineBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initViewPager();
        initRequest();
    }

    private void initView() {
        bind.refreshLayout.setEnableLoadMore(false);
        bind.refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()).setTimeFormat(new DynamicTimeFormat("更新于 %s")));
        bind.refreshLayout.setPrimaryColorsId(android.R.color.transparent, android.R.color.white);
        bind.refreshLayout.setOnMultiListener(new SimpleMultiListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initRequest();
                initConcern();
                refreshLayout.finishRefresh(1000);
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

        bind.shadowSetting.setOnClickListener(this);
        bind.ivSetting.setOnClickListener(this);
        bind.shadowEdit.setOnClickListener(this);
        bind.ivUseravator.setOnClickListener(this);
        bind.llSetting.setOnClickListener(this);
        bind.llExit.setOnClickListener(this);
        bind.tvConcern.setOnClickListener(this);
        bind.tgConcern.setOnClickListener(this);
        bind.tvFans.setOnClickListener(this);
        bind.tgFans.setOnClickListener(this);
        bind.ivTag.setOnClickListener(this);

        bind.personDrawer.setNavigationItemSelectedListener(this);
        bind.personDrawer.setItemIconTintList(null);

        int width = ScreenUtils.getScreenWidth(getContext());
        int height = ScreenUtils.getScreenHeight(getContext());
        ViewGroup.LayoutParams para = bind.personDrawer.getLayoutParams();
        para.width = width / 3 * 2;
        para.height = height - DisplayUtil.dip2px(getContext(),53);
        bind.personDrawer.setLayoutParams(para);
        bind.personDrawer.setPadding(0,ScreenUtils.getStatusHeight(getContext()),0,0);
        bind.personDrawer.setOverScrollMode(View.OVER_SCROLL_NEVER);

        bind.toolbar.setPadding(0,ScreenUtils.getStatusHeight(getContext()),0,0);

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
        bind.ctlToolbar.setExpandedTitleGravity(Gravity.CENTER);//设置展开后标题的位置
        bind.ctlToolbar.setCollapsedTitleGravity(Gravity.CENTER);//设置收缩后标题的位置
        bind.ctlToolbar.setExpandedTitleColor(Color.WHITE);//设置展开后标题的颜色
        bind.ctlToolbar.setCollapsedTitleTextColor(Color.BLACK);//设置收缩后标题的颜色

        bind.appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int Offset = Math.abs(verticalOffset);
                bind.toolbar.setBackgroundColor(changeAlpha(getResources().getColor
                        (R.color.white),Math.abs(verticalOffset * 1.0f) / appBarLayout.getTotalScrollRange()));

                /**
                 * 当前最大高度便宜值除以2 在减去已偏移值 获取浮动 先显示在隐藏
                 */
                if (Offset < appBarLayout.getTotalScrollRange() / 2) {
                    bind.toolbar.setTitle("");
                    bind.ivSetting.setAlpha((appBarLayout.getTotalScrollRange() / 2 - Offset * 1.0f) / (appBarLayout.getTotalScrollRange() / 2));
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_menu).mutate();
                    Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
                    wrappedDrawable.setTint(0xFFffffff);
                    bind.ivSetting.setImageDrawable(wrappedDrawable);
                    bind.title.setVisibility(View.VISIBLE);
                    bind.title.setAlpha((appBarLayout.getTotalScrollRange() / 2 - Offset * 1.0f) / (appBarLayout.getTotalScrollRange() / 2));
                    bind.title.setTextColor(Color.WHITE);
                    bind.ivUseravatorHead.setVisibility(View.INVISIBLE);
                    bind.parallax.setTranslationY(mOffset - Offset);

                    /**
                     * 从最低浮动开始渐显 当前 Offset就是  appBarLayout.getTotalScrollRange() / 2
                     * 所以 Offset - appBarLayout.getTotalScrollRange() / 2
                     */
                } else if (Offset > appBarLayout.getTotalScrollRange() / 2) {
                    float floate = (Offset - appBarLayout.getTotalScrollRange() / 2) * 1.0f / (appBarLayout.getTotalScrollRange() / 2);
                    bind.ivSetting.setAlpha(floate);
                    Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_menu).mutate();
                    Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
                    wrappedDrawable.setTint(0xFF000000);
                    bind.ivSetting.setImageDrawable(wrappedDrawable);
                    bind.ivUseravatorHead.setVisibility(View.VISIBLE);
                    bind.ivUseravatorHead.setAlpha(floate);
                    bind.title.setVisibility(View.INVISIBLE);
                    bind.parallax.setTranslationY(mOffset - Offset);
                }
            }
        });

        initConcern();
    }

    private void initConcern() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", App.getMmkvUtil().getString("user","uId",null));

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/concern/getConcernCount", map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("concern"));
                        long count = JsonHelper.parserJson2Object(data,Long.class);
                        bind.tvConcern.setText(String.valueOf(count));

                        data = JsonHelper.parserObject2Json(response.get("beConcerned"));
                        count = JsonHelper.parserJson2Object(data,Long.class);
                        bind.tvFans.setText(String.valueOf(count));
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
                bind.drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.shadow_setting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.shadow_edit:
                Intent intent = new Intent(getContext(), UserInfoActivity.class);
                mActivityLauncher.launch(intent);
                break;
            case R.id.iv_useravator:
                ImageViewerHelper.showSimpleImage(getContext(), user.getuHeadicon(), "", bind.ivUseravator, false);
                break;
            case R.id.ll_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                bind.drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.ll_exit:
                new CommonAlertDialog(getContext()).builder()
                        .setTitle("退出")
                        .setMsg("确定退出APP？")
                        .setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setPositiveButton("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getContext(), MainActivity.class);
                                intent.putExtra(MainActivity.TAG_EXIT, true);
                                startActivity(intent);
                                bind.drawerLayout.closeDrawer(GravityCompat.START);
                            }
                        }).setCancelable(false)
                        .show();
                break;
            case R.id.tv_concern:
            case R.id.tg_concern:
                startActivity(ConcernListActivity.class, 0);
                break;
            case R.id.tv_fans:
            case R.id.tg_fans:
                startActivity(ConcernListActivity.class, 1);
                break;
            case R.id.iv_tag:
                String msg = "为维护网络安全、保障良好生态和社区的真实性，根据网络运营商数据，展示用户IP属地信息。";
                new TipsAlertDialog(getContext()).builder()
                        .setTitle("IP属地说明")
                        .setMsg(msg)
                        .setSureButton("我知道了", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setCancelable(true)
                        .show();
                break;
        }
    }

    private void startActivity(Class<?> cls, int type) {
        Intent intent = new Intent(getContext(),cls);
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        bundle.putLong("userId",user.getuId());
        bundle.putString("userName",user.getuName());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private final ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                initRequest();
            }
        }
    });

    private void initViewPager() {
        mTitleList = new ArrayList<>();
        mTitleList.add("笔记");
        mTitleList.add("收藏");
        mTitleList.add("赞过");

        mFragmentList = new ArrayList<>();
        mFragmentList.add(noteFragment);
        mFragmentList.add(collectFragment);
        mFragmentList.add(likeFragment);

        mViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(),getLifecycle(),mFragmentList);
        bind.viewpager.setAdapter(mViewPagerAdapter);
        bind.viewpager.setSaveEnabled(false);

        new TabLayoutMediator(bind.tabLayout, bind.viewpager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(mTitleList.get(position));
            }
        }).attach();
    }

    private void initRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uId", App.getMmkvUtil().getString("user","uId",null));

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/getUserInfo", map)
                .compose(RxHelper.observableIO2Main(getContext()))
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

        Glide.with(getContext()).load(user.getuHeadicon()).apply(options).into(bind.ivUseravator);
        Glide.with(getContext()).load(user.getuHeadicon()).apply(options).into(bind.ivUseravatorHead);
        Glide.with(getContext()).load(user.getBackgroundImage()).into(bind.parallax);

        bind.tvUsername.setText(user.getuName());
        bind.tvUserid.setText("小杏林号：" + user.getuId());

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

    /**
     * 根据百分比改变颜色透明度
     */
    private int changeAlpha(int color, float fraction) {
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        int alpha = (int) (Color.alpha(color) * fraction);
        return Color.argb(alpha, red, green, blue);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_concern:
                startActivity(ConcernListActivity.class, 0);
                break;
            case R.id.item_fans:
                startActivity(ConcernListActivity.class, 1);
                break;
            case R.id.item_browsing_history:
                startActivity(new Intent(getContext(), BrowsingHistoryActivity.class));
                break;
            case R.id.item_recycle_bin:
                startActivity(new Intent(getContext(), RecycleBinActivity.class));
                break;
            case R.id.item_address:
                Intent intent = new Intent(getContext(), ShippingAddressActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type",1);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.item_order_form:
                startActivity(new Intent(getContext(), OrderFormActivity.class));
                break;
            case R.id.item_shop_cart:
                startActivity(new Intent(getContext(), ShoppingCartActivity.class));
                break;
        }
        bind.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }

}