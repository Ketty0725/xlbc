package com.ketty.chinesemedicine.main.publish;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.adapter.GridImageAdapter;
import com.ketty.chinesemedicine.component.ActionSheetDialog;
import com.ketty.chinesemedicine.component.FullyGridLayoutManager;
import com.ketty.chinesemedicine.component.view.TipsAlertDialog;
import com.ketty.chinesemedicine.databinding.ActivityPublishBinding;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;
import com.ketty.chinesemedicine.util.PictureUtil;
import com.ketty.chinesemedicine.util.T;
import com.ketty.gifloadinglibrary.GifLoadingView;
import com.luck.picture.lib.basic.IBridgePictureBehavior;
import com.luck.picture.lib.basic.PictureCommonFragment;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.manager.PictureCacheManager;
import com.luck.picture.lib.utils.DensityUtil;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PublishActivity extends BaseActivity implements IBridgePictureBehavior, View.OnClickListener {
    private ActivityPublishBinding bind;
    private int maxSelectNum = 9;
    private final List<LocalMedia> mData = new ArrayList<>();
    private ActivityResultLauncher<Intent> launcherResult;
    private PictureUtil pictureUtil;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter mAdapter;
    private ContentResolver resolver;
    private Uri uri;
    private ContentValues values;
    private GifLoadingView mGifLoadingView;
    private String ip;
    private InputMethodManager imm;

    @Override
    protected View initLayout() {
        bind = ActivityPublishBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mGifLoadingView = new GifLoadingView();

        bind.ivBack.setOnClickListener(this);
        bind.slPublish.setOnClickListener(this);
        bind.ivTag.setOnClickListener(this);

        launcherResult = createActivityResultLauncher();
        initData();
    }

    private void initData() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uId", App.getMmkvUtil().getString("user","uId",null));

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/getUserIP", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        ip = JsonHelper.parserJson2Object(data, String.class);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this,
                3, GridLayoutManager.VERTICAL, false);
        bind.recyclerView.setLayoutManager(manager);
        RecyclerView.ItemAnimator itemAnimator = bind.recyclerView.getItemAnimator();
        if (itemAnimator != null) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        bind.recyclerView.addItemDecoration(new GridSpacingItemDecoration(3,
                DensityUtil.dip2px(this, 3), false));
        mAdapter = new GridImageAdapter(getContext(), mData);
        mAdapter.setSelectMax(maxSelectNum);
        bind.recyclerView.setAdapter(mAdapter);

        pictureUtil = new PictureUtil(getContext(),mAdapter,launcherResult);

        mAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // 预览图片、视频、音频
                pictureUtil.openPreview(position,bind.recyclerView);
            }

            @Override
            public void openPicture() {
                if (imm.isActive() && PublishActivity.this.getCurrentFocus() != null) {
                    if (PublishActivity.this.getCurrentFocus().getWindowToken() != null) {
                        HideSoftInput(PublishActivity.this.getCurrentFocus().getWindowToken());
                    }
                }
                new ActionSheetDialog(PublishActivity.this)
                        .builder()
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(true)
                        .addSheetItem("拍照上传", null,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        pictureUtil.openCamera(SelectMimeType.ofImage());
                                    }
                                })
                        .addSheetItem("从相册中选择", null,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        pictureUtil.openGallery(SelectMimeType.ofImage());
                                    }
                                }).show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finishAfterTransition();
                clearCache();
                break;
            case R.id.iv_tag:
                String msg = "杏林本草鼓励向上、真实、原创的内容，请勿发布含以下内容的笔记：\n" +
                        "1. 含有不文明语言、过度性感图片；\n" +
                        "2. 含有网址链接、联系方式、二维码或售卖语言；\n" +
                        "3. 冒充他人身份或搬运他人作品；\n" +
                        "4. 通过有奖方式诱导他人点赞、评论、收藏、转发、关注；\n" +
                        "5. 为刻意博取眼球，在标题、封面等处使用夸张表达。";
                new TipsAlertDialog(getContext()).builder()
                        .setTitle("发布小贴士")
                        .setMsg(msg)
                        .setSureButton("我知道了", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).setCancelable(true)
                        .show();
                break;
            case R.id.sl_publish:
                if (!mAdapter.getData().isEmpty() && !TextUtils.isEmpty(bind.etTitle.getText())) {
                    mGifLoadingView.setImageResource(R.mipmap.num19);
                    mGifLoadingView.show(getFragmentManager());

                    String title = bind.etTitle.getText().toString();
                    String content = bind.etContent.getText().toString();
                    String uId = App.getMmkvUtil().getString("user","uId", null);

                    selectList = mAdapter.getData();
                    File file = null;
                    String filename = null;
                    String path = null;

                    MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    for (LocalMedia photoOutputUri : selectList) {
                        path = PictureUtil.getPath(photoOutputUri,getContext());
                        file = new File(path);
                        filename = path.substring(path.lastIndexOf("/") + 1);
                        builder.addFormDataPart("upload", filename, RequestBody.create(MediaType.parse("application/octet-stream"), file));
                    }
                    builder.addFormDataPart("title", title);
                    builder.addFormDataPart("content", content);
                    builder.addFormDataPart("time", String.valueOf(System.currentTimeMillis()));
                    builder.addFormDataPart("ipAddress", ip);
                    builder.addFormDataPart("userId", uId);

                    RequestBody body = builder.build();

                    RetrofitManager
                            .getInstance()
                            .getApiService()
                            .postMethod("/community/add", body)
                            .compose(RxHelper.observableIO2Main(this))
                            .subscribe(new BaseObserver() {
                                @Override
                                public void onSuccess(Map<String, Object> response) {
                                    resolver = getContentResolver();
                                    uri = Uri.parse("content://com.ketty.chinesemedicine.provider.cover/coverInfo");
                                    Cursor cursor = resolver.query(uri, new String[]{"_id", "tag"},null,null,null);
                                    values = new ContentValues();
                                    String uId = App.getMmkvUtil().getString("user","uId", null);
                                    if (cursor.getCount() > 0) {
                                        values.put("tag", String.valueOf(System.currentTimeMillis()));
                                        LogUtils.i("values", String.valueOf(values));
                                        resolver.update(uri, values, "_id=?",
                                                new String[]{uId});
                                    } else {
                                        values.put("_id", uId);
                                        values.put("tag", String.valueOf(System.currentTimeMillis()));
                                        resolver.insert(uri, values);
                                    }

                                    cursor.close();

                                    // 清除缓存
                                    clearCache();

                                    mGifLoadingView.setDimming(false);
                                    finishAfterTransition();
                                    T.showShort("发布成功");
                                }

                                @Override
                                public void onFailure(Throwable e, String errorMsg) {
                                    T.showShort("发布失败");
                                }
                            });
                } else {
                    T.showShort("请填写完整");
                }
                break;
        }
    }

    /**
     * 清空缓存包括裁剪、压缩、AndroidQToPath所生成的文件，注意调用时机必须是处理完本身的业务逻辑后调用；非强制性
     */
    private void clearCache() {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        PictureCacheManager.deleteAllCacheDirRefreshFile(getContext());
        LogUtils.i("clearCache","success!");
        /*if (PermissionChecker.checkSelfPermission(getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            //PictureCacheManager.deleteCacheDirFile(this, PictureMimeType.ofImage());
        }*/
    }

    /**
     * 创建一个ActivityResultLauncher
     *
     * @return
     */
    private ActivityResultLauncher<Intent> createActivityResultLauncher() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        if (resultCode == RESULT_OK) {
                            ArrayList<LocalMedia> selectList = PictureSelector.obtainSelectorList(result.getData());
                            pictureUtil.analyticalSelectResults(selectList);
                        } else if (resultCode == RESULT_CANCELED) {
                            LogUtils.i("onActivityResult PictureSelector Cancel");
                        }
                    }
                });
    }

    @Override
    public void onSelectFinish(@Nullable PictureCommonFragment.SelectorResult result) {
        if (result == null) {
            return;
        }
        if (result.mResultCode == RESULT_OK) {
            ArrayList<LocalMedia> selectorResult = PictureSelector.obtainSelectorList(result.mResultData);
            pictureUtil.analyticalSelectResults(selectorResult);
        } else if (result.mResultCode == RESULT_CANCELED) {
            LogUtils.i("onSelectFinish PictureSelector Cancel");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST || requestCode == PictureConfig.REQUEST_CAMERA) {
                ArrayList<LocalMedia> result = PictureSelector.obtainSelectorList(data);
                pictureUtil.analyticalSelectResults(result);
            }
        } else if (resultCode == RESULT_CANCELED) {
            LogUtils.i("onActivityResult PictureSelector Cancel");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mAdapter != null && mAdapter.getData() != null && mAdapter.getData().size() > 0) {
            outState.putParcelableArrayList("selectorList",
                    mAdapter.getData());
        }
    }

    public Context getContext() {
        return this;
    }

    public void showSoftInput(View view) {
        if (view != null && imm != null){
            Timer timer = new Timer();
            timer.schedule(new TimerTask(){
                @Override
                public void run() {
                    imm.showSoftInput(view, 0);
                }
            }, 300);
        }
    }

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

    private void HideSoftInput(IBinder token) {
        if (token != null) {
            imm.hideSoftInputFromWindow(token, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}