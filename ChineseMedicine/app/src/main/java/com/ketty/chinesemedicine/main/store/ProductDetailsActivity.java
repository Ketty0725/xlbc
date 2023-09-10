package com.ketty.chinesemedicine.main.store;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.databinding.ActivityProductDetailsBinding;
import com.ketty.chinesemedicine.entity.Product;
import com.ketty.chinesemedicine.entity.Productdoctorqa;
import com.ketty.chinesemedicine.entity.Productshopcart;
import com.ketty.chinesemedicine.util.ButtonClickUtils;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.MMKVUtil;
import com.ketty.chinesemedicine.util.ScreenUtils;
import com.ketty.chinesemedicine.util.T;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;
import com.yuruiyin.appbarlayoutbehavior.AppBarLayoutBehavior;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class ProductDetailsActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener, View.OnClickListener {
    private ActivityProductDetailsBinding bind;
    float comments_height;
    private boolean isScrolling = false;
    private boolean isCommentClick = false;
    private ArrayList<TextView> textViews = new ArrayList<>();
    //关于下方fragment的切换
    private static final int HOME_ONE = 0;
    private static final int HOME_TWO = 1;
    private static final int HOME_THREE = 2;
    private int index;
    private int currentTabIndex = 0;
    ProductBasicInfoFragment fragment_one;
    ProductExplainFragment fragment_two;
    ProductExplainFragment fragment_three;
    private TextView[] mTabs;
    private TextView[] mTabs_second;
    private FragmentManager manager;
    private ArrayList<Fragment> list_fragment = new ArrayList<Fragment>();
    private Long id;
    private MMKVUtil mmkvUtil;
    private String uId;
    private Product product;

    @Override
    protected View initLayout() {
        bind = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        mmkvUtil = App.getMmkvUtil();
        uId = mmkvUtil.getString("user","uId",null);
        initRequest();
        isCollect();
        setListener();
        initFragment();
        initArray();
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) bind.viewStatus.getLayoutParams();
        layoutParams.height = ScreenUtils.getStatusHeight(this);
        initProduct();
    }

    private void initRequest() {
        Bundle bundle = getIntent().getExtras();
        id = bundle.getLong("id");

        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("id", id);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/product/getById", map1)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        product = JsonHelper.parserJson2Object(
                                data, Product.class);
                        initData();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });

        HashMap<String, Object> map2 = new HashMap<>();
        map2.put("productId", id);
        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/productdoctorqa/countByProductId", map2)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        Long count = JsonHelper.parserJson2Object(
                                data, Long.class);
                        bind.tvMoreQuestion.setText("查看全部"+count+"个问题");
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });

        HashMap<String, Object> map3 = new HashMap<>();
        map3.put("productId", id);
        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/productdoctorqa/getFirstTwo", map3)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        List<Productdoctorqa> list = JsonHelper.parserJson2List(
                                data, new TypeToken<List<Productdoctorqa>>() {}.getType());

                        int width = DisplayUtil.dip2px(ProductDetailsActivity.this,20);
                        int height = DisplayUtil.dip2px(ProductDetailsActivity.this,20);
                        ViewGroup.LayoutParams layout;
                        if (list.size() > 0) {
                            bind.include1.tvAsk.setTextSize(14);
                            bind.include1.tvAsk.setText(list.get(0).getAsk());
                            bind.include1.tvAnswer.setMaxLines(1);
                            bind.include1.tvAnswer.setEllipsize(TextUtils.TruncateAt.END);
                            bind.include1.tvAnswer.setTextSize(13);
                            bind.include1.tvAnswer.setText(list.get(0).getAnswer());
                            layout = bind.include1.icAsk.getLayoutParams();
                            layout.width = width;
                            layout.height = height;
                            bind.include1.icAsk.setLayoutParams(layout);
                            layout = bind.include1.icAnswer.getLayoutParams();
                            layout.width = width;
                            layout.height = height;
                            bind.include1.icAnswer.setLayoutParams(layout);
                            if (list.size() > 1) {
                                bind.include2.tvAsk.setTextSize(14);
                                bind.include2.tvAsk.setText(list.get(1).getAsk());
                                bind.include2.tvAnswer.setMaxLines(1);
                                bind.include2.tvAnswer.setEllipsize(TextUtils.TruncateAt.END);
                                bind.include2.tvAnswer.setTextSize(13);
                                bind.include2.tvAnswer.setText(list.get(1).getAnswer());
                                layout = bind.include2.icAsk.getLayoutParams();
                                layout.width = width;
                                layout.height = height;
                                bind.include2.icAsk.setLayoutParams(layout);
                                layout = bind.include2.icAnswer.getLayoutParams();
                                layout.width = width;
                                layout.height = height;
                                bind.include2.icAnswer.setLayoutParams(layout);
                            } else {
                                bind.include2.rootLayout.setVisibility(View.GONE);
                            }
                        } else {
                            bind.include1.rootLayout.setVisibility(View.GONE);
                            bind.include2.rootLayout.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });

        HashMap<String, Object> map4 = new HashMap<>();
        map4.put("userId", uId);
        map4.put("productId", id);
        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/productshopcart/isExistsFromAll", map4)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        bind.slAddShopCart.setLayoutBackground(0xFFC8C8C8);
                        bind.slAddShopCart.setClickable(false);
                        bind.tvAddShopCart.setText("已加购");
                        bind.tvAddShopCart.setTextColor(0xFF666666);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initData() {
        if (product.getImage() != null && !"".equals(product.getImage())) {
            Glide.with(this).load(product.getImage()).into(bind.ivBasic);
        }
        if (product.getName() != null && !"".equals(product.getName())) {
            bind.tvName.setText(product.getName());
        }
        if (product.getInfo() != null && !"".equals(product.getInfo())) {
            bind.tvInfo.setText(product.getInfo());
        }
        if (product.getPrice() != null) {
            String text = String.valueOf(product.getPrice());
            if (text.contains(".")) {
                List<TextColorSizeHelper.SpanInfo> spanInfos = new ArrayList<TextColorSizeHelper.SpanInfo>();
                spanInfos.add(
                        new TextColorSizeHelper.SpanInfo(
                                text.substring(0, text.indexOf(".")),
                                DisplayUtil.dip2px(this, 22),
                                Color.parseColor("#ec5d29"),
                                false
                        )
                );
                spanInfos.add(
                        new TextColorSizeHelper.SpanInfo(
                                text.substring(text.indexOf(".")),
                                DisplayUtil.dip2px(this, 18),
                                Color.parseColor("#ec5d29"),
                                false
                        )
                );
                bind.tvPrice.setText(TextColorSizeHelper.getTextSpan(this, text, spanInfos));
            } else {
                bind.tvPrice.setText(text);
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        bind.btnBasicInfo.setOnClickListener(this);
        bind.btnInstructionBook.setOnClickListener(this);
        bind.btnServiceAssurance.setOnClickListener(this);
        bind.tvBasicInfo.setOnClickListener(this);
        bind.tvInstructionBook.setOnClickListener(this);
        bind.tvServiceAssurance.setOnClickListener(this);
        bind.llMoreQuestion.setOnClickListener(this);
        bind.include1.rootLayout.setOnClickListener(this);
        bind.include2.rootLayout.setOnClickListener(this);
        bind.slAddShopCart.setOnClickListener(this);
        bind.rlAddShopCart.setOnClickListener(this);
        bind.slInstantlyBuy.setOnClickListener(this);
        bind.rlCollect.setOnClickListener(this);
        bind.rlCustomer.setOnClickListener(this);

        bind.productTitleBar.setOnClickListener(new ProductTitleBar.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bind.appbar.addOnOffsetChangedListener(this);
        //评论的点击
        bind.txtComment.setOnTouchListener((View v, MotionEvent ev) -> {
                    switch (ev.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            if (bind.rlTitle.getVisibility() == View.VISIBLE && !isScrolling && !bind.txtComment.isSelected()) {
                                bind.include1.rootLayout.setClickable(false);
                                bind.include2.rootLayout.setClickable(false);
                                isCommentClick = true;
                                bind.nestedScrollView.scrollTo(0, 0);
                                bind.nestedScrollView.smoothScrollTo(0, 0);
                                CoordinatorLayout.Behavior behavior =
                                        ((CoordinatorLayout.LayoutParams) bind.appbar.getLayoutParams()).getBehavior();
                                AppBarLayoutBehavior appBarLayoutBehavior = (AppBarLayoutBehavior) behavior;
                                appBarLayoutBehavior.onInterceptTouchEvent(bind.coordinator, bind.appbar, ev);

                                setAppBarLayoutOffset(bind.appbar, (int) -comments_height);
                                bind.linearTherother.setVisibility(View.GONE);
                                selectTitle(bind.txtComment);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            bind.include1.rootLayout.setClickable(true);
                            bind.include2.rootLayout.setClickable(true);
                            break;
                    }
                    return true;
                }
        );

        //商品的点击
        bind.txtProduct.setOnTouchListener((View v, MotionEvent ev) -> {
                    switch (ev.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            if (!bind.txtProduct.isSelected()) {
                                bind.include1.rootLayout.setClickable(false);
                                bind.include2.rootLayout.setClickable(false);
                                isScrolling = true;
                                bind.nestedScrollView.scrollTo(0, 0);
                                bind.nestedScrollView.smoothScrollTo(0, 0);
                                CoordinatorLayout.Behavior behavior =
                                        ((CoordinatorLayout.LayoutParams) bind.appbar.getLayoutParams()).getBehavior();
                                AppBarLayoutBehavior appBarLayoutBehavior = (AppBarLayoutBehavior) behavior;
                                appBarLayoutBehavior.onInterceptTouchEvent(bind.coordinator, bind.appbar, ev);
                                bind.appbar.setExpanded(true, true);
                                selectTitle(bind.txtProduct);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            bind.include1.rootLayout.setClickable(true);
                            bind.include2.rootLayout.setClickable(true);
                            break;
                    }
                    return true;
                }
        );

        //详情的点击
        bind.txtDetail.setOnTouchListener((View v, MotionEvent ev) -> {
                    switch (ev.getActionMasked()) {
                        case MotionEvent.ACTION_DOWN:
                            if (bind.rlTitle.getVisibility() == View.VISIBLE && !isScrolling && !bind.txtDetail.isSelected()) {
                                bind.include1.rootLayout.setClickable(false);
                                bind.include2.rootLayout.setClickable(false);
                                bind.nestedScrollView.scrollTo(0, 0);
                                bind.nestedScrollView.smoothScrollTo(0, 0);
                                CoordinatorLayout.Behavior behavior =
                                        ((CoordinatorLayout.LayoutParams) bind.appbar.getLayoutParams()).getBehavior();
                                AppBarLayoutBehavior appBarLayoutBehavior = (AppBarLayoutBehavior) behavior;
                                appBarLayoutBehavior.onInterceptTouchEvent(bind.coordinator, bind.appbar, ev);
                                setAppBarLayoutOffset(bind.appbar,
                                        -(int) (bind.appbar.getTotalScrollRange() - DisplayUtil.dip2px(this,50) - ScreenUtils.getStatusHeight(this)));
                                selectTitle(bind.txtDetail);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            bind.include1.rootLayout.setClickable(true);
                            bind.include2.rootLayout.setClickable(true);
                            break;
                    }
                    return true;
                }
        );

        bind.nestedScrollView.setFadingEdgeLength(0);
    }

    private void initProduct() {

        //这个方法是在获取商品详情接口后调用的。目的是填充数据，且测量评论区所占高度
        Observable.timer(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
            measure(bind.appbar.getTotalScrollRange());
        });
    }

    private void initFragment() {
        manager = getSupportFragmentManager();
        mTabs = new TextView[3];
        mTabs[0] = bind.tvBasicInfo;
        mTabs[1] = bind.tvInstructionBook;
        mTabs[2] = bind.tvServiceAssurance;

        mTabs_second = new TextView[3];
        mTabs_second[0] = bind.btnBasicInfo;
        mTabs_second[1] = bind.btnInstructionBook;
        mTabs_second[2] = bind.btnServiceAssurance;

        fragment_one = ProductBasicInfoFragment.newInstance(id);
        fragment_two = ProductExplainFragment.newInstance(1,id);
        fragment_three = ProductExplainFragment.newInstance(2,id);

        list_fragment.add(fragment_one);
        list_fragment.add(fragment_two);
        list_fragment.add(fragment_three);
        switchFragment(R.id.tv_basic_info);
    }

    private void initArray() {
        textViews.add(bind.txtProduct);
        textViews.add(bind.txtDetail);
        textViews.add(bind.txtComment);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(verticalOffset) >= 10) {
            if (bind.rlTitle.getVisibility() == View.GONE) {
                bind.rlTitle.setVisibility(View.VISIBLE);
                bind.viewStatus.setVisibility(View.VISIBLE);

                Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_detail_come);
                bind.rlTitle.setAnimation(animation);
                bind.viewStatus.setAnimation(animation);
                animation.start();
            }
        } else {
            if (bind.rlTitle.getVisibility() == View.VISIBLE) {
                isScrolling = false;
                bind.rlTitle.setVisibility(View.GONE);
                bind.viewStatus.setVisibility(View.INVISIBLE);
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.alpha_detail_go);
                bind.rlTitle.setAnimation(animation);
                bind.viewStatus.setAnimation(animation);
                animation.start();
            }
        }


        if (comments_height != 0 && Math.abs(verticalOffset) >= comments_height && Math.abs(verticalOffset) < appBarLayout.getTotalScrollRange() - DisplayUtil.dip2px(this,50) - ScreenUtils.getStatusHeight(this)) {
            //选中评论
            if (!bind.txtComment.isSelected()) {
                selectTitle(bind.txtComment);
            }
            bind.linearTherother.setVisibility(View.GONE);
            //选中
        } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange() - DisplayUtil.dip2px(this,50) - ScreenUtils.getStatusHeight(this)) {
            //选中详情
            if (!isCommentClick) {
                if (!bind.txtDetail.isSelected()) {
                    selectTitle(bind.txtDetail);
                }
                bind.linearTherother.setVisibility(View.VISIBLE);
            }
            isCommentClick = false;


        } else {
            if (!bind.txtProduct.isSelected()) {
                selectTitle(bind.txtProduct);
            }
            bind.linearTherother.setVisibility(View.GONE);
        }
    }

    public void measure(int total) {
        if (comments_height == 0) {
            comments_height = total - DisplayUtil.dip2px(this,100) - ScreenUtils.getStatusHeight(this) - bind.include1.rootLayout.getHeight() - bind.include2.rootLayout.getHeight();
        }
    }

    public void switchFragment(int id) {
        FragmentTransaction ft = manager.beginTransaction();
        TextView relativeLayout = (TextView) findViewById(id);
        String tag = (String) relativeLayout.getTag();
        Fragment f = manager.findFragmentByTag(tag);
        if (f == null) {
            int num = Integer.parseInt(tag);
            ft.add(R.id.frame_layout, list_fragment.get(num), tag);
        }

        for (int i = 0; i < list_fragment.size(); i++) {
            Fragment fragment = list_fragment.get(i);
            if (fragment.getTag() != null) {
                if (fragment.getTag().equals(tag)) {
                    ft.show(fragment);
                } else {
                    ft.hide(fragment);
                }
            }
        }
        ft.commitAllowingStateLoss();
        switch (id) {
            case R.id.tv_basic_info://首页
                index = HOME_ONE;
                break;
            case R.id.tv_instruction_book:
                index = HOME_TWO;
                break;
            case R.id.tv_service_assurance:
                index = HOME_THREE;
                break;
        }
        mTabs[currentTabIndex].setSelected(false);
        mTabs_second[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        mTabs[index].setSelected(true);
        mTabs_second[index].setSelected(true);
        currentTabIndex = index;
    }

    public void selectTitle(TextView textView) {
        for (int i = 0; i < textViews.size(); i++) {
            if (textView == textViews.get(i)) {
                if (!textViews.get(i).isSelected()) {
                    textViews.get(i).setSelected(true);
                    textViews.get(i).setScaleX(1.3f);
                    textViews.get(i).setScaleY(1.3f);
                }
            } else {
                if (textViews.get(i).isSelected()) {
                    textViews.get(i).setSelected(false);
                    textViews.get(i).setScaleX(1.0f);
                    textViews.get(i).setScaleY(1.0f);
                }
            }

        }
    }

    /**
     * 设置appbar偏移量
     *
     * @param appBar
     * @param offset
     */
    public void setAppBarLayoutOffset(AppBarLayout appBar, int offset) {
        CoordinatorLayout.Behavior behavior =
                ((CoordinatorLayout.LayoutParams) appBar.getLayoutParams()).getBehavior();
        if (behavior instanceof AppBarLayout.Behavior) {

            AppBarLayout.Behavior appBarLayoutBehavior = (AppBarLayout.Behavior) behavior;
            int topAndBottomOffset = appBarLayoutBehavior.getTopAndBottomOffset();

            if (topAndBottomOffset != offset) {
                ValueAnimator valueAnimator = ValueAnimator.ofInt(appBarLayoutBehavior.getTopAndBottomOffset(), offset);
                valueAnimator.setDuration(500);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int offetOther = (int) animation.getAnimatedValue();
                        appBarLayoutBehavior.setTopAndBottomOffset(offetOther);
                        if (bind.rlTitle.getVisibility() == View.GONE) {
                            bind.rlTitle.setVisibility(View.VISIBLE);
                            bind.viewStatus.setVisibility(View.VISIBLE);

                            Animation animation_appBarScroll = AnimationUtils.loadAnimation(ProductDetailsActivity.this, R.anim.alpha_detail_come);
                            bind.rlTitle.setAnimation(animation_appBarScroll);
                            bind.viewStatus.setAnimation(animation_appBarScroll);
                            animation_appBarScroll.start();
                        }

//                        if (Math.abs(offetOther) >= linearScroll_height - getResources().getDimension(R.dimen.dp_50) - DensityUtils.getStatusBarHeight()) {
//                            binding.linearTherother.setVisibility(View.VISIBLE);
//                        } else {
//                            binding.linearTherother.setVisibility(View.GONE);
//                        }

                    }
                });
                valueAnimator.start();
            }
        }
    }

    @Override
    public void onClick(View v) {
        //防止快速点击
        if (ButtonClickUtils.isFastClick()) {
            return;
        }

        switch (v.getId()) {
            case R.id.btn_basic_info:
            case R.id.tv_basic_info:
                switchFragment(R.id.tv_basic_info);
                break;

            case R.id.btn_instruction_book:
            case R.id.tv_instruction_book:
                switchFragment(R.id.tv_instruction_book);
                break;

            case R.id.btn_service_assurance:
            case R.id.tv_service_assurance:
                switchFragment(R.id.tv_service_assurance);
                break;

            case R.id.ll_more_question:
            case R.id.root_layout:
                startActivity(id, DoctorQAActivity.class);
                break;

            case R.id.rl_customer:
                T.showShort("正在开发中，敬请期待");
                break;

            case R.id.sl_add_shop_cart:
                addShopCart();
                break;
            case R.id.rl_add_shop_cart:
                startActivity(id, ShoppingCartActivity.class);
                break;
            case R.id.rl_collect:
                HashMap<String, Object> map = new HashMap<>();
                map.put("userId", uId);
                map.put("beCollectId", product.getId());

                RetrofitManager
                        .getInstance()
                        .getApiService()
                        .postMethod("/product/updateCollectState", map)
                        .compose(RxHelper.observableIO2Main(this))
                        .subscribe(new BaseObserver() {
                            @Override
                            public void onSuccess(Map<String, Object> response) {
                                isCollect();
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg) {

                            }
                        });
                break;
            case R.id.sl_instantly_buy:
                ShoppingBuyDialog dialog = new ShoppingBuyDialog();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) product);
                dialog.setArguments(bundle);
                dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullScreen);
                dialog.show(getSupportFragmentManager(), "ShoppingBuyDialog");
                dialog.setStateListener(new ShoppingBuyDialog.StateListener() {
                    @Override
                    public void close() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onBottomClick(Productshopcart productshopcart, float price) {
                        if (product.getInventory() > 0) {
                            List<Productshopcart> list = new ArrayList<>();
                            list.add(productshopcart);
                            Intent intent = new Intent(ProductDetailsActivity.this, ProductSettlementActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("price", "￥" + price);
                            bundle.putSerializable("list", (Serializable) list);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else {
                            T.showShort("库存不足");
                        }
                    }
                });
                break;
        }
    }

    private void isCollect() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", uId);
        map.put("id", id);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/product/getIsCollectByUserId", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        int isCollect = JsonHelper.parserJson2Object(
                                data, Integer.class);
                        if (isCollect == 1) {
                            bind.ivCollect.setImageResource(R.drawable.ic_collection_selected);
                            T.showShort("收藏成功");
                        } else {
                            bind.ivCollect.setImageResource(R.drawable.ic_collection_normal);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void addShopCart() {
        if (product.getInventory() > 0) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("userId", uId);
            map.put("productId", id);

            RetrofitManager
                    .getInstance()
                    .getApiService()
                    .postMethod("/productshopcart/addOrIncrementToRedis", map)
                    .compose(RxHelper.observableIO2Main(this))
                    .subscribe(new BaseObserver() {
                        @Override
                        public void onSuccess(Map<String, Object> response) {
                            T.showShort("添加成功");
                            bind.slAddShopCart.setLayoutBackground(0xFFDCDCDC);
                            bind.slAddShopCart.setClickable(false);
                            bind.tvAddShopCart.setText("已加购");
                            bind.tvAddShopCart.setTextColor(0xFF666666);
                        }

                        @Override
                        public void onFailure(Throwable e, String errorMsg) {

                        }
                    });
        } else {
            T.showShort("库存不足");
        }
    }

    private void startActivity(long id, Class<?> cls) {
        Intent intent = new Intent(this, cls);
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}