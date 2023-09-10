package com.ketty.chinesemedicine.main.mine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.component.GlideEngine;
import com.ketty.chinesemedicine.databinding.ActivityUserInfoBinding;
import com.ketty.chinesemedicine.entity.JsonBean;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.util.GetJsonDataUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;
import com.ketty.chinesemedicine.util.MMKVUtil;
import com.ketty.chinesemedicine.util.PictureUtil;
import com.ketty.chinesemedicine.util.T;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.manager.PictureCacheManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    private ActivityUserInfoBinding bind;
    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static boolean isLoaded = false;
    private RequestOptions options = RequestOptions.circleCropTransform();
    private MMKVUtil mmkvUtil;
    private User user;
    private LocalMedia mHeadIconMedia = null;
    private LocalMedia mBackgroundMedia = null;

    @Override
    protected View initLayout() {
        bind = ActivityUserInfoBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        mmkvUtil = App.getMmkvUtil();

        bind.ivBack.setOnClickListener(this);
        bind.layoutHead.setOnClickListener(this);
        bind.shadowName.setOnClickListener(this);
        bind.shadowSex.setOnClickListener(this);
        bind.shadowBirthday.setOnClickListener(this);
        bind.shadowArea.setOnClickListener(this);
        bind.shadowAbout.setOnClickListener(this);
        bind.shadowBackground.setOnClickListener(this);
        bind.shadowSave.setOnClickListener(this);

        HashMap<String, Object> map = new HashMap<>();
        map.put("uId", App.getMmkvUtil().getString("user","uId",null));

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                setResult(RESULT_OK,new Intent());
                finish();
                clearCache();
                break;
            case R.id.layout_head:
                openGallery(0);
                break;
            case R.id.shadow_name:
                bind.etName.requestFocus();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                break;
            case R.id.shadow_sex:
                initSexPicker();
                break;
            case R.id.shadow_birthday:
                initTimePicker();
                break;
            case R.id.shadow_area:
                if (isLoaded) {
                    showPickerView();
                }
                break;
            case R.id.shadow_about:
                InputDialog mDialog = new InputDialog();
                if (bind.tvAbout.getText() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("about",bind.tvAbout.getText().toString());
                    mDialog.setArguments(bundle);
                }
                mDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullScreen);
                mDialog.show(getSupportFragmentManager(),"inputDialog");
                mDialog.setStateListener(new InputDialog.StateListener() {
                    @Override
                    public void close(String str) {
                        bind.tvAbout.setText(str);
                        mDialog.dismiss();
                    }
                });
                break;
            case R.id.shadow_save:
                update();
                break;
            case R.id.shadow_background:
                openGallery(1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_OK,new Intent());
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResult(RESULT_OK,new Intent());
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void update() {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (mHeadIconMedia != null) {
            String path = mHeadIconMedia.getCompressPath();
            File file = new File(path);
            String filename = path.substring(path.lastIndexOf("/") + 1);
            builder.addFormDataPart("headIconFile", filename, RequestBody.create(MediaType.parse("application/octet-stream"), file));
        }
        if (mBackgroundMedia != null) {
            String path = mBackgroundMedia.getCompressPath();
            File file = new File(path);
            String filename = path.substring(path.lastIndexOf("/") + 1);
            builder.addFormDataPart("backgroundFile", filename, RequestBody.create(MediaType.parse("application/octet-stream"), file));
        }
        builder.addFormDataPart("uId", mmkvUtil.getString("user","uId",null));
        builder.addFormDataPart("uName", bind.etName.getText().toString());
        builder.addFormDataPart("uSex", bind.tvSex.getText().toString());
        builder.addFormDataPart("uBirthday", bind.tvBirthday.getText().toString());
        builder.addFormDataPart("uAddress", bind.tvArea.getText().toString());
        builder.addFormDataPart("uAbout", bind.tvAbout.getText().toString());
        builder.addFormDataPart("uHeadicon", user.getuHeadicon());
        builder.addFormDataPart("backgroundImage", user.getBackgroundImage());

        RequestBody body = builder.build();

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/updateUserInfo", body)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        T.showShort("保存成功");
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        T.showShort("保存失败");
                    }
                });

    }

    private void initData() {
        Glide.with(this).load(user.getuHeadicon()).apply(options).into(bind.ivUseravator);
        bind.etName.setText(user.getuName());
        bind.tvSex.setText(user.getuSex());
        bind.tvBirthday.setText(user.getuBirthday() == null ? "" : user.getuBirthday());
        bind.tvArea.setText(user.getuAddress() == null ? "" : user.getuAddress());
        bind.tvAbout.setText(user.getuAbout() == null ? "" : user.getuAbout());
        Glide.with(this).load(user.getBackgroundImage()).into(bind.ivBackground);
    }

    public void openGallery(int type) {
        PictureUtil pictureUtil = new PictureUtil(this);
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .setSelectorUIStyle(pictureUtil.getSelectorStyle())
                .setImageEngine(GlideEngine.createGlideEngine())
                .setCropEngine(pictureUtil.getCropFileEngine()) //是否裁剪
                .setCompressEngine(pictureUtil.getCompressFileEngine())
                .setCustomLoadingListener(pictureUtil.getCustomLoadingListener())   //自定义加载框
                .setSelectionMode(SelectModeConfig.SINGLE)    //单选 or 多选
                .isDisplayCamera(true)  //是否显示相机入口
                .isPreviewFullScreenMode(true)  //全屏预览(点击)
                .isPreviewZoomEffect(true)  //预览缩放效果
                .isPreviewImage(true)   //是否预览图片
                .setRecyclerAnimationMode(AnimationType.DEFAULT_ANIMATION)    //相册列表动画效果
                .isFilterSizeDuration(true)   //是否过滤图片或音视频大小时长为0的资源
                .isGif(false)    //是否显示Gif图片
                .isCameraForegroundService(true)    //拍照时是否开启一个前台服务
                .isAutomaticTitleRecyclerTop(true)    //通过连续点击标题栏，RecyclerView 自动回滚到顶部
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        if (type == 0) {
                            mHeadIconMedia = result.get(0);
                            Glide.with(UserInfoActivity.this).load(mHeadIconMedia.getCompressPath()).apply(options).into(bind.ivUseravator);
                        } else if (type == 1) {
                            mBackgroundMedia = result.get(0);
                            Glide.with(UserInfoActivity.this).load(mBackgroundMedia.getCompressPath()).into(bind.ivBackground);
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    private void clearCache() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        PictureCacheManager.deleteAllCacheDirRefreshFile(this);
        LogUtils.i("clearCache","success!");
    }

    private void initSexPicker() {
        List<String> sexList = new ArrayList<>();
        sexList.add("男");
        sexList.add("女");
        OptionsPickerView pvOptions = new OptionsPickerBuilder(UserInfoActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                String sex = sexList.get(options1);
                bind.tvSex.setText(sex);
            }
        })
                .setTitleText("选择性别")
                .setTitleSize(16)
                .setTextColorCenter(0xFF333333)
                .setContentTextSize(18)
                .setSubCalSize(16)
                .setTitleColor(0xFF333333)
                .setSubmitColor(0xFFff2442)
                .setCancelColor(0xFF999999)
                .build();
        pvOptions.setPicker(sexList);
        pvOptions.show();
    }

    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);
        Calendar endDate = Calendar.getInstance();
        TimePickerView pvTime = new TimePickerBuilder(UserInfoActivity.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String dateStr = getTime(date);
                bind.tvBirthday.setText(dateStr);
            }
        })
                .setTitleText("选择生日")
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setTitleSize(16)
                .setTextColorCenter(0xFF333333)
                .setContentTextSize(18)
                .setSubCalSize(16)
                .setTitleColor(0xFF333333)
                .setSubmitColor(0xFFff2442)
                .setCancelColor(0xFF999999)
                .build();
        pvTime.show();
    }

    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void showPickerView() {// 弹出选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String opt1tx = options1Items.size() > 0 ?
                        options1Items.get(options1).getPickerViewText() : "";

                String opt2tx = options2Items.size() > 0
                        && options2Items.get(options1).size() > 0 ?
                        options2Items.get(options1).get(options2) : "";

                String opt3tx = options2Items.size() > 0
                        && options3Items.get(options1).size() > 0
                        && options3Items.get(options1).get(options2).size() > 0 ?
                        options3Items.get(options1).get(options2).get(options3) : "";

                String area = opt1tx + " " + opt2tx + " " + opt3tx;
                bind.tvArea.setText(area);
            }
        })
                .setTitleText("选择城市")
                .setTitleSize(16)
                .setTextColorCenter(0xFF333333)
                .setContentTextSize(18)
                .setSubCalSize(16)
                .setTitleColor(0xFF333333)
                .setSubmitColor(0xFFff2442)
                .setCancelColor(0xFF999999)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;
                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    break;
            }
        }
    };

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<JsonBean> jsonBean = JsonHelper.parserJson2List(JsonData, new TypeToken<ArrayList<JsonBean>>() {}.getType());//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);//添加城市
                ArrayList<String> city_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                /*if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    city_AreaList.add("");
                } else {
                    city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }*/
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(cityList);

            /**
             * 添加地区数据
             */
            options3Items.add(province_AreaList);
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }

    /**
     * 获取点击事件
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (isHideInput(view, ev)) {
                HideSoftInput(view.getWindowToken());
                view.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 判定是否需要隐藏
     */
    private boolean isHideInput(View v, MotionEvent ev) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏软键盘
     */
    private void HideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}