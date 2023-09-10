package com.ketty.chinesemedicine.util;

import static com.luck.picture.lib.thread.PictureThreadUtils.runOnUiThread;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.hw.videoprocessor.VideoProcessor;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.adapter.GridImageAdapter;
import com.ketty.chinesemedicine.component.CustomLoadingDialog;
import com.ketty.chinesemedicine.component.CustomPreviewFragment;
import com.ketty.chinesemedicine.component.GlideEngine;
import com.luck.lib.camerax.CameraImageEngine;
import com.luck.lib.camerax.SimpleCameraX;
import com.luck.lib.camerax.listener.OnSimpleXPermissionDeniedListener;
import com.luck.lib.camerax.listener.OnSimpleXPermissionDescriptionListener;
import com.luck.lib.camerax.permissions.SimpleXPermissionUtil;
import com.luck.picture.lib.PictureSelectorPreviewFragment;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.basic.FragmentInjectManager;
import com.luck.picture.lib.basic.PictureSelectionCameraModel;
import com.luck.picture.lib.basic.PictureSelectionModel;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.InjectResourceSource;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.PictureSelectionConfig;
import com.luck.picture.lib.config.SelectLimitType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.dialog.RemindDialog;
import com.luck.picture.lib.engine.CompressEngine;
import com.luck.picture.lib.engine.CompressFileEngine;
import com.luck.picture.lib.engine.CropEngine;
import com.luck.picture.lib.engine.CropFileEngine;
import com.luck.picture.lib.engine.ExtendLoaderEngine;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.engine.UriToFileTransformEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.LocalMediaFolder;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.interfaces.OnCallbackListener;
import com.luck.picture.lib.interfaces.OnCameraInterceptListener;
import com.luck.picture.lib.interfaces.OnCustomLoadingListener;
import com.luck.picture.lib.interfaces.OnExternalPreviewEventListener;
import com.luck.picture.lib.interfaces.OnGridItemSelectAnimListener;
import com.luck.picture.lib.interfaces.OnInjectActivityPreviewListener;
import com.luck.picture.lib.interfaces.OnInjectLayoutResourceListener;
import com.luck.picture.lib.interfaces.OnKeyValueResultCallbackListener;
import com.luck.picture.lib.interfaces.OnMediaEditInterceptListener;
import com.luck.picture.lib.interfaces.OnPermissionDeniedListener;
import com.luck.picture.lib.interfaces.OnPermissionDescriptionListener;
import com.luck.picture.lib.interfaces.OnPreviewInterceptListener;
import com.luck.picture.lib.interfaces.OnQueryAlbumListener;
import com.luck.picture.lib.interfaces.OnQueryAllAlbumListener;
import com.luck.picture.lib.interfaces.OnQueryDataResultListener;
import com.luck.picture.lib.interfaces.OnRecordAudioInterceptListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.interfaces.OnSelectAnimListener;
import com.luck.picture.lib.interfaces.OnSelectLimitTipsListener;
import com.luck.picture.lib.interfaces.OnVideoThumbnailEventListener;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.loader.SandboxFileLoader;
import com.luck.picture.lib.permissions.PermissionChecker;
import com.luck.picture.lib.permissions.PermissionConfig;
import com.luck.picture.lib.permissions.PermissionResultCallback;
import com.luck.picture.lib.permissions.PermissionUtil;
import com.luck.picture.lib.style.BottomNavBarStyle;
import com.luck.picture.lib.style.PictureSelectorStyle;
import com.luck.picture.lib.style.PictureWindowAnimationStyle;
import com.luck.picture.lib.style.SelectMainStyle;
import com.luck.picture.lib.style.TitleBarStyle;
import com.luck.picture.lib.utils.DateUtils;
import com.luck.picture.lib.utils.DensityUtil;
import com.luck.picture.lib.utils.MediaUtils;
import com.luck.picture.lib.utils.PictureFileUtils;
import com.luck.picture.lib.utils.SandboxTransformUtils;
import com.luck.picture.lib.utils.SdkVersionUtils;
import com.luck.picture.lib.utils.StyleUtils;
import com.luck.picture.lib.utils.ToastUtils;
import com.luck.picture.lib.widget.MediumBoldTextView;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropImageEngine;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnNewCompressListener;
import top.zibin.luban.OnRenameListener;

public class PictureUtil {
    private Context context;
    private final static String TAG = "PictureSelectorTag";
    private final static String TAG_EXPLAIN_VIEW = "TAG_EXPLAIN_VIEW";
    private final static int ACTIVITY_RESULT = 1;
    private final static int CALLBACK_RESULT = 2;
    private final static int LAUNCHER_RESULT = 3;
    private GridImageAdapter mAdapter;
    private int maxSelectNum = 9;
    private int maxSelectVideoNum = 1;
    private int maxSecond = 15;
    private int aspect_ratio_x = -1, aspect_ratio_y = -1;
    private int chooseMode = SelectMimeType.ofAll();
    private int language = LanguageConfig.UNKNOWN_LANGUAGE;
    private int animationMode = AnimationType.DEFAULT_ANIMATION;
    private PictureSelectorStyle selectorStyle = new PictureSelectorStyle();
    private ActivityResultLauncher<Intent> launcherResult;
    private int resultMode = LAUNCHER_RESULT;
    private ImageEngine imageEngine = GlideEngine.createGlideEngine();
    private WeakReference<Context> reference;

    public PictureUtil(Context context) {
        reference = new WeakReference<>(context);
        this.context = reference.get();
    }

    public PictureUtil(Context context, GridImageAdapter mAdapter, ActivityResultLauncher<Intent> launcherResult) {
        reference = new WeakReference<>(context);
        this.context = reference.get();
        this.mAdapter = mAdapter;
        this.launcherResult = launcherResult;
    }

    public void openPreview(int position, RecyclerView mRecyclerView) {
        // 预览图片、视频、音频
        PictureSelector.create(context)
                .openPreview()
                .setImageEngine(imageEngine)
                .setSelectorUIStyle(selectorStyle)
                .setLanguage(language)
                .isAutoVideoPlay(true)  //预览视频是否自动播放
                .isLoopAutoVideoPlay(true)  //预览视频是否循环播放
                .isPreviewFullScreenMode(true)  //预览点击全屏效果
                .isVideoPauseResumePlay(true)   //视频支持暂停与播放
                .setCustomLoadingListener(getCustomLoadingListener())
                .isPreviewZoomEffect(chooseMode != SelectMimeType.ofAudio(), mRecyclerView)
                /*.setAttachViewLifecycle(new IBridgeViewLifecycle() {
                    @Override
                    public void onViewCreated(Fragment fragment, View view, Bundle savedInstanceState) {
                                PictureSelectorPreviewFragment previewFragment = (PictureSelectorPreviewFragment) fragment;
                                MediumBoldTextView tvShare = view.findViewById(R.id.tv_share);
                                tvShare.setVisibility(View.VISIBLE);
                                previewFragment.addAminViews(tvShare);
                                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tvShare.getLayoutParams();
                                layoutParams.topMargin = DensityUtil.getStatusBarHeight(context);
                                tvShare.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        PicturePreviewAdapter previewAdapter = previewFragment.getAdapter();
                                        ViewPager2 viewPager2 = previewFragment.getViewPager2();
                                        LocalMedia media = previewAdapter.getItem(viewPager2.getCurrentItem());
                                        ToastUtils.showToast(fragment.getContext(), "自定义分享事件:" + viewPager2.getCurrentItem());
                                    }
                                });
                    }

                    @Override
                    public void onDestroy(Fragment fragment) {
                                    // 如果是全屏预览模式且是startFragmentPreview预览，回到自己的界面时需要恢复一下自己的沉浸式状态
                                    // 以下提供2种解决方案:
                                    // 1.通过ImmersiveManager.immersiveAboveAPI23重新设置一下沉浸式
                                    int statusBarColor = ContextCompat.getColor(context, R.color.ps_color_grey);
                                    int navigationBarColor = ContextCompat.getColor(context, R.color.ps_color_grey);
                                    ImmersiveManager.immersiveAboveAPI23((AppCompatActivity) context,
                                            true, true,
                                            statusBarColor, navigationBarColor, false);
                                    // 2.让自己的titleBar的高度加上一个状态栏高度且内容PaddingTop下沉一个状态栏的高度

                    }
                })*/
                .setInjectLayoutResourceListener(new OnInjectLayoutResourceListener() {
                    @Override
                    public int getLayoutResourceId(Context context, int resourceSource) {
                        return resourceSource == InjectResourceSource.PREVIEW_LAYOUT_RESOURCE
                                ? R.layout.ps_custom_fragment_preview
                                : InjectResourceSource.DEFAULT_LAYOUT_RESOURCE;
                    }
                })
                .setExternalPreviewEventListener(new MyExternalPreviewEventListener())
                .setInjectActivityPreviewFragment(new OnInjectActivityPreviewListener() {
                    @Override
                    public PictureSelectorPreviewFragment onInjectPreviewFragment() {
                        return CustomPreviewFragment.newInstance();
                    }
                })
                .startActivityPreview(position, true, mAdapter.getData());
    }

    public void openGallery(int chooseMode) {
        PictureSelectionModel selectionModel = PictureSelector.create(context)
                .openGallery(chooseMode)
                .setSelectorUIStyle(selectorStyle)
                .setImageEngine(imageEngine)
                //.setCropEngine(getCropFileEngine()) //是否裁剪
                .setCompressEngine(getCompressFileEngine())
                .setSandboxFileEngine(new MeSandboxFileEngine())
                //.setCameraInterceptListener(getCustomCameraEvent()) //是否使用自定义相机
                .setRecordAudioInterceptListener(new MeOnRecordAudioInterceptListener())
                .setSelectLimitTipsListener(new MeOnSelectLimitTipsListener())  //拦截选择限制事件，可实现自定义提示
                .setEditMediaInterceptListener(getCustomEditMediaEvent())   //拦截资源编辑事件，实现自定义编辑
                .setPermissionDescriptionListener(getPermissionDescriptionListener())
                .setPreviewInterceptListener(getPreviewInterceptListener())
                .setPermissionDeniedListener(getPermissionDeniedListener())
                //.setAddBitmapWatermarkListener(getAddBitmapWatermarkListener())
                .setVideoThumbnailListener(getVideoThumbnailEventListener())    //处理视频缩略图
                .isAutoVideoPlay(true)  //预览视频是否自动播放
                .isLoopAutoVideoPlay(true)  //预览视频是否循环播放
                .isPageSyncAlbumCount(true)  //分页模式下设置过滤条件后是否同步专辑下资源的数量
                .setCustomLoadingListener(getCustomLoadingListener())   //自定义加载框
                .setGridItemSelectAnimListener(new OnGridItemSelectAnimListener() {
                    @Override
                    public void onSelectItemAnim(View view, boolean isSelected) {
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(
                                ObjectAnimator.ofFloat(view, "scaleX", isSelected ? 1F : 1.12F, isSelected ? 1.12f : 1.0F),
                                ObjectAnimator.ofFloat(view, "scaleY", isSelected ? 1F : 1.12F, isSelected ? 1.12f : 1.0F)
                        );
                        set.setDuration(350);
                        set.start();
                    }
                })
                .setSelectAnimListener(new OnSelectAnimListener() {
                    @Override
                    public long onSelectAnim(View view) {
                        Animation animation = AnimationUtils.loadAnimation(context, com.luck.picture.lib.R.anim.ps_anim_modal_in);
                        view.startAnimation(animation);
                        return animation.getDuration();
                    }
                })   //自定义选中动画
                /*.setQueryFilterListener(new OnQueryFilterListener() {
                    @Override
                    public boolean onFilter(LocalMedia media) {
                        return false;
                    }
                })*/
                //.setExtendLoaderEngine(getExtendLoaderEngine())
                //.setInjectLayoutResourceListener(getInjectLayoutResource())   //是否注入自定义布局
                .setSelectionMode(SelectModeConfig.MULTIPLE)    //单选 or 多选
                .setLanguage(language)
                .isDisplayTimeAxis(true)
                .isOnlyObtainSandboxDir(false)  //是否只查询指定目录下的资源
                .isPageStrategy(true)   //是否开启分页模式
                .isOriginalControl(true)    //是否开启原图功能
                .isDisplayCamera(true)  //是否显示相机入口
                .isOpenClickSound(false)    //是否开启点击音效
                .setSkipCropMimeType(getNotSupportCrop())   //跳过不需要裁剪的类型
                .isFastSlidingSelect(true)  //快速滑动选择
                //.setOutputCameraImageFileName("luck.jpeg")
                //.setOutputCameraVideoFileName("luck.mp4")
                .isWithSelectVideoImage(true)  //图片视频同选
                .isPreviewFullScreenMode(true)  //全屏预览(点击)
                .isVideoPauseResumePlay(true)  //视频暂停与继续
                .isPreviewZoomEffect(true)  //预览缩放效果
                .isPreviewImage(true)   //是否预览图片
                .isPreviewVideo(true)   //是否预览视频
                .isPreviewAudio(true)   //是否预览音频
                //.setQueryOnlyMimeType(PictureMimeType.ofGIF())
                .isMaxSelectEnabledMask(true)  //是否显示蒙层(达到最大可选数量)
                //.isDirectReturnSingle(true)   //单选模式直接返回
                .setMaxSelectNum(maxSelectNum)  //图片最大选择数量
                .setMaxVideoSelectNum(maxSelectVideoNum)    //视频最大选择数量
                .setRecordVideoMaxSecond(maxSecond)   //视频录制最大时长
                .setSelectMaxDurationSecond(maxSecond)    //选择视频或音频支持的最大秒数
                .setSelectMaxFileSize(102400)   //最大可选文件大小 kb
                .setRecyclerAnimationMode(animationMode)    //相册列表动画效果
                .isFilterSizeDuration(true)   //是否过滤图片或音视频大小时长为0的资源
                .isGif(false)    //是否显示Gif图片
                .isCameraForegroundService(true)    //拍照时是否开启一个前台服务
                .isAutomaticTitleRecyclerTop(true)    //通过连续点击标题栏，RecyclerView 自动回滚到顶部
                .isOriginalSkipCompress(true)   //选择原图跳过压缩
                .setSelectedData(mAdapter.getData());   //相册已选数据
        forSelectResult(selectionModel);
    }

    public void openCamera(int chooseMode) {
        // 单独拍照
        PictureSelectionCameraModel cameraModel = PictureSelector.create(context)
                .openCamera(chooseMode)
                //.setCameraInterceptListener(getCustomCameraEvent())    //自定义相机
                .setRecordAudioInterceptListener(new MeOnRecordAudioInterceptListener())
                .setCropEngine(getCropFileEngine())
                .setCompressEngine(getCompressFileEngine())
                //.setAddBitmapWatermarkListener(getAddBitmapWatermarkListener())   //添加水印
                .setVideoThumbnailListener(getVideoThumbnailEventListener())
                .setCustomLoadingListener(getCustomLoadingListener())
                .setLanguage(language)
                .setSandboxFileEngine(new MeSandboxFileEngine())
                .isOriginalControl(false) //是否开启原图功能
                .setPermissionDescriptionListener(getPermissionDescriptionListener())
                .isCameraForegroundService(true)    //拍照时是否开启一个前台服务
                .setRecordVideoMaxSecond(maxSecond)   //视频录制最大时长
                .setSelectedData(mAdapter.getData());
        forOnlyCameraResult(cameraModel);
    }

    private void forSelectResult(PictureSelectionModel model) {
        switch (resultMode) {
            case ACTIVITY_RESULT:
                model.forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case CALLBACK_RESULT:
                model.forResult(new MeOnResultCallbackListener());
                break;
            default:
                model.forResult(launcherResult);
                break;
        }
    }

    private void forOnlyCameraResult(PictureSelectionCameraModel model) {
        if (resultMode == CALLBACK_RESULT) {
            model.forResult(new MeOnResultCallbackListener());
        } else {
            model.forResult();
        }
    }

    /**
     * 外部预览监听事件
     */
    private class MyExternalPreviewEventListener implements OnExternalPreviewEventListener {

        @Override
        public void onPreviewDelete(int position) {
            mAdapter.remove(position);
            mAdapter.notifyItemRemoved(position);
        }

        @Override
        public boolean onLongPressDownload(LocalMedia media) {
            return false;
        }
    }

    /**
     * 选择结果
     */
    private class MeOnResultCallbackListener implements OnResultCallbackListener<LocalMedia> {
        @Override
        public void onResult(ArrayList<LocalMedia> result) {
            analyticalSelectResults(result);
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "PictureSelector Cancel");
        }
    }

    /**
     * 压缩引擎
     *
     * @return
     */
    public ImageFileCompressEngine getCompressFileEngine() {
        return new ImageFileCompressEngine();
    }

    /**
     * 压缩引擎
     *
     * @return
     */
    @Deprecated
    private ImageCompressEngine getCompressEngine() {
        return new ImageCompressEngine();
    }

    /**
     * 裁剪引擎
     *
     * @return
     */
    public ImageFileCropEngine getCropFileEngine() {
        return new ImageFileCropEngine();
    }

    /**
     * 裁剪引擎
     *
     * @return
     */
    private ImageCropEngine getCropEngine() {
        return new ImageCropEngine();
    }

    /**
     * 自定义相机事件
     *
     * @return
     */
    private OnCameraInterceptListener getCustomCameraEvent() {
        return new MeOnCameraInterceptListener();
    }

    /**
     * 自定义拍照
     */
    private class MeOnCameraInterceptListener implements OnCameraInterceptListener {

        @Override
        public void openCamera(Fragment fragment, int cameraMode, int requestCode) {
            SimpleCameraX camera = SimpleCameraX.of();
            camera.isAutoRotation(true);
            camera.setCameraMode(cameraMode);
            camera.setVideoFrameRate(25);
            camera.setVideoBitRate(3 * 1024 * 1024);
            camera.isDisplayRecordChangeTime(true);
            camera.isManualFocusCameraPreview(true);
            camera.isZoomCameraPreview(true);
            camera.setPermissionDeniedListener(getSimpleXPermissionDeniedListener());
            camera.setPermissionDescriptionListener(getSimpleXPermissionDescriptionListener());
            camera.setImageEngine(new CameraImageEngine() {
                @Override
                public void loadImage(Context context, String url, ImageView imageView) {
                    Glide.with(context).load(url).into(imageView);
                }
            });
            camera.start(fragment.requireActivity(), fragment, requestCode);
        }
    }


    /**
     * 自定义数据加载器
     *
     * @return
     */
    private ExtendLoaderEngine getExtendLoaderEngine() {
        return new MeExtendLoaderEngine();
    }


    /**
     * 注入自定义布局
     *
     * @return
     */
    private OnInjectLayoutResourceListener getInjectLayoutResource() {
        return new MeOnInjectLayoutResourceListener();
    }


    /**
     * 处理视频缩略图
     */
    private OnVideoThumbnailEventListener getVideoThumbnailEventListener() {
        return new MeOnVideoThumbnailEventListener(getVideoThumbnailDir());
    }

    /**
     * 处理视频缩略图
     */
    private static class MeOnVideoThumbnailEventListener implements OnVideoThumbnailEventListener {
        private final String targetPath;

        public MeOnVideoThumbnailEventListener(String targetPath) {
            this.targetPath = targetPath;
        }

        @Override
        public void onVideoThumbnail(Context context, String videoPath, OnKeyValueResultCallbackListener call) {
            Glide.with(context).asBitmap().sizeMultiplier(0.6F).load(videoPath).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    resource.compress(Bitmap.CompressFormat.JPEG, 60, stream);
                    FileOutputStream fos = null;
                    String result = null;
                    try {
                        File targetFile = new File(targetPath, "thumbnails_" + System.currentTimeMillis() + ".jpg");
                        fos = new FileOutputStream(targetFile);
                        fos.write(stream.toByteArray());
                        fos.flush();
                        result = targetFile.getAbsolutePath();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        PictureFileUtils.close(fos);
                        PictureFileUtils.close(stream);
                    }
                    if (call != null) {
                        call.onCallback(videoPath, result);
                    }
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {
                    if (call != null) {
                        call.onCallback(videoPath, "");
                    }
                }
            });
        }
    }

    /**
     * 自定义loading
     *
     * @return
     */
    public OnCustomLoadingListener getCustomLoadingListener() {
        return new OnCustomLoadingListener() {
            @Override
            public Dialog create(Context context) {
                return new CustomLoadingDialog(context);
            }
        };
    }

    /**
     * 权限拒绝后回调
     *
     * @return
     */
    private OnPermissionDeniedListener getPermissionDeniedListener() {
        return new MeOnPermissionDeniedListener();
    }


    /**
     * 权限拒绝后回调
     */
    private static class MeOnPermissionDeniedListener implements OnPermissionDeniedListener {

        @Override
        public void onDenied(Fragment fragment, String[] permissionArray,
                             int requestCode, OnCallbackListener<Boolean> call) {
            String tips;
            if (TextUtils.equals(permissionArray[0], PermissionConfig.CAMERA[0])) {
                tips = "缺少相机权限\n可能会导致不能使用摄像头功能";
            } else if (TextUtils.equals(permissionArray[0], Manifest.permission.RECORD_AUDIO)) {
                tips = "缺少录音权限\n访问您设备上的音频、媒体内容和文件";
            } else {
                tips = "缺少存储权限\n访问您设备上的照片、媒体内容和文件";
            }
            RemindDialog dialog = RemindDialog.buildDialog(fragment.getContext(), tips);
            dialog.setButtonText("去设置");
            dialog.setButtonTextColor(0xFF7D7DFF);
            dialog.setContentTextColor(0xFF333333);
            dialog.setOnDialogClickListener(new RemindDialog.OnDialogClickListener() {
                @Override
                public void onClick(View view) {
                    PermissionUtil.goIntentSetting(fragment, requestCode);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    /**
     * SimpleCameraX权限拒绝后回调
     *
     * @return
     */
    private OnSimpleXPermissionDeniedListener getSimpleXPermissionDeniedListener() {
        return new MeOnSimpleXPermissionDeniedListener();
    }

    /**
     * SimpleCameraX添加权限说明
     */
    private static class MeOnSimpleXPermissionDeniedListener implements OnSimpleXPermissionDeniedListener {

        @Override
        public void onDenied(Context context, String permission, int requestCode) {
            String tips;
            if (TextUtils.equals(permission, Manifest.permission.RECORD_AUDIO)) {
                tips = "缺少麦克风权限\n可能会导致录视频无法采集声音";
            } else {
                tips = "缺少相机权限\n可能会导致不能使用摄像头功能";
            }
            RemindDialog dialog = RemindDialog.buildDialog(context, tips);
            dialog.setButtonText("去设置");
            dialog.setButtonTextColor(0xFF7D7DFF);
            dialog.setContentTextColor(0xFF333333);
            dialog.setOnDialogClickListener(new RemindDialog.OnDialogClickListener() {
                @Override
                public void onClick(View view) {
                    SimpleXPermissionUtil.goIntentSetting((Activity) context, requestCode);
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    /**
     * SimpleCameraX权限说明
     *
     * @return
     */
    private OnSimpleXPermissionDescriptionListener getSimpleXPermissionDescriptionListener() {
        return new MeOnSimpleXPermissionDescriptionListener();
    }

    /**
     * SimpleCameraX添加权限说明
     */
    private static class MeOnSimpleXPermissionDescriptionListener implements OnSimpleXPermissionDescriptionListener {

        @Override
        public void onPermissionDescription(Context context, ViewGroup viewGroup, String permission) {
            addPermissionDescription(true, viewGroup, new String[]{permission});
        }

        @Override
        public void onDismiss(ViewGroup viewGroup) {
            removePermissionDescription(viewGroup);
        }
    }


    /**
     * 权限说明
     *
     * @return
     */
    private OnPermissionDescriptionListener getPermissionDescriptionListener() {
        return new MeOnPermissionDescriptionListener();
    }

    /**
     * 添加权限说明
     */
    private static class MeOnPermissionDescriptionListener implements OnPermissionDescriptionListener {

        @Override
        public void onPermissionDescription(Fragment fragment, String[] permissionArray) {
            View rootView = fragment.requireView();
            if (rootView instanceof ViewGroup) {
                addPermissionDescription(false, (ViewGroup) rootView, permissionArray);
            }
        }

        @Override
        public void onDismiss(Fragment fragment) {
            removePermissionDescription((ViewGroup) fragment.requireView());
        }
    }

    /**
     * 添加权限说明
     *
     * @param viewGroup
     * @param permissionArray
     */
    private static void addPermissionDescription(boolean isHasSimpleXCamera, ViewGroup viewGroup, String[] permissionArray) {
        int dp10 = DensityUtil.dip2px(viewGroup.getContext(), 10);
        int dp15 = DensityUtil.dip2px(viewGroup.getContext(), 15);
        MediumBoldTextView view = new MediumBoldTextView(viewGroup.getContext());
        view.setTag(TAG_EXPLAIN_VIEW);
        view.setTextSize(14);
        view.setTextColor(Color.parseColor("#333333"));
        view.setPadding(dp10, dp15, dp10, dp15);

        String title;
        String explain;

        if (TextUtils.equals(permissionArray[0], PermissionConfig.CAMERA[0])) {
            title = "相机权限使用说明";
            explain = "相机权限使用说明\n用户app用于拍照/录视频";
        } else if (TextUtils.equals(permissionArray[0], Manifest.permission.RECORD_AUDIO)) {
            if (isHasSimpleXCamera) {
                title = "麦克风权限使用说明";
                explain = "麦克风权限使用说明\n用户app用于录视频时采集声音";
            } else {
                title = "录音权限使用说明";
                explain = "录音权限使用说明\n用户app用于采集声音";
            }
        } else {
            title = "存储权限使用说明";
            explain = "存储权限使用说明\n用户app写入/下载/保存/读取/修改/删除图片、视频、文件等信息";
        }
        int startIndex = 0;
        int endOf = startIndex + title.length();
        SpannableStringBuilder builder = new SpannableStringBuilder(explain);
        builder.setSpan(new AbsoluteSizeSpan(DensityUtil.dip2px(viewGroup.getContext(), 16)), startIndex, endOf, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(0xFF333333), startIndex, endOf, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        view.setText(builder);
        view.setBackground(ContextCompat.getDrawable(viewGroup.getContext(), R.drawable.ps_demo_permission_desc_bg));

        if (isHasSimpleXCamera) {
            RelativeLayout.LayoutParams layoutParams =
                    new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = DensityUtil.getStatusBarHeight(viewGroup.getContext());
            layoutParams.leftMargin = dp10;
            layoutParams.rightMargin = dp10;
            viewGroup.addView(view, layoutParams);
        } else {
            ConstraintLayout.LayoutParams layoutParams =
                    new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.topToBottom = R.id.title_bar;
            layoutParams.leftToLeft = ConstraintSet.PARENT_ID;
            layoutParams.leftMargin = dp10;
            layoutParams.rightMargin = dp10;
            viewGroup.addView(view, layoutParams);
        }
    }

    /**
     * 移除权限说明
     *
     * @param viewGroup
     */
    private static void removePermissionDescription(ViewGroup viewGroup) {
        View tagExplainView = viewGroup.findViewWithTag(TAG_EXPLAIN_VIEW);
        viewGroup.removeView(tagExplainView);
    }



    /**
     * 自定义预览
     *
     * @return
     */
    private OnPreviewInterceptListener getPreviewInterceptListener() {
        return new MeOnPreviewInterceptListener();
    }

    /**
     * 自定义预览
     *
     * @return
     */
    private static class MeOnPreviewInterceptListener implements OnPreviewInterceptListener {

        @Override
        public void onPreview(Context context, int position, int totalNum, int page, long currentBucketId, String currentAlbumName, boolean isShowCamera, ArrayList<LocalMedia> data, boolean isBottomPreview) {
            CustomPreviewFragment previewFragment = CustomPreviewFragment.newInstance();
            previewFragment.setInternalPreviewData(isBottomPreview, currentAlbumName, isShowCamera,
                    position, totalNum, page, currentBucketId, data);
            FragmentInjectManager.injectFragment((FragmentActivity) context, CustomPreviewFragment.TAG, previewFragment);
        }
    }

    /**
     * 拦截自定义提示
     */
    private static class MeOnSelectLimitTipsListener implements OnSelectLimitTipsListener {

        @Override
        public boolean onSelectLimitTips(Context context, PictureSelectionConfig config, int limitType) {
            if (limitType == SelectLimitType.SELECT_NOT_SUPPORT_SELECT_LIMIT) {
                ToastUtils.showToast(context, "暂不支持的选择类型");
                return true;
            }
            return false;
        }
    }

    /**
     * 注入自定义布局UI，前提是布局View id 和 根目录Layout必须一致
     */
    private static class MeOnInjectLayoutResourceListener implements OnInjectLayoutResourceListener {

        @Override
        public int getLayoutResourceId(Context context, int resourceSource) {
            switch (resourceSource) {
                case InjectResourceSource.MAIN_SELECTOR_LAYOUT_RESOURCE:
                    return R.layout.ps_custom_fragment_selector;
                case InjectResourceSource.PREVIEW_LAYOUT_RESOURCE:
                    return R.layout.ps_custom_fragment_preview;
                case InjectResourceSource.MAIN_ITEM_IMAGE_LAYOUT_RESOURCE:
                    return R.layout.ps_custom_item_grid_image;
                case InjectResourceSource.MAIN_ITEM_VIDEO_LAYOUT_RESOURCE:
                    return R.layout.ps_custom_item_grid_video;
                case InjectResourceSource.MAIN_ITEM_AUDIO_LAYOUT_RESOURCE:
                    return R.layout.ps_custom_item_grid_audio;
                case InjectResourceSource.ALBUM_ITEM_LAYOUT_RESOURCE:
                    return R.layout.ps_custom_album_folder_item;
                case InjectResourceSource.PREVIEW_ITEM_IMAGE_LAYOUT_RESOURCE:
                    return R.layout.ps_custom_preview_image;
                case InjectResourceSource.PREVIEW_ITEM_VIDEO_LAYOUT_RESOURCE:
                    return R.layout.ps_custom_preview_video;
                case InjectResourceSource.PREVIEW_GALLERY_ITEM_LAYOUT_RESOURCE:
                    return R.layout.ps_custom_preview_gallery_item;
                default:
                    return 0;
            }
        }
    }

    /**
     * 自定义数据加载器
     */
    private class MeExtendLoaderEngine implements ExtendLoaderEngine {

        @Override
        public void loadAllAlbumData(Context context,
                                     OnQueryAllAlbumListener<LocalMediaFolder> query) {
            LocalMediaFolder folder = SandboxFileLoader
                    .loadInAppSandboxFolderFile(context, getSandboxPath());
            List<LocalMediaFolder> folders = new ArrayList<>();
            folders.add(folder);
            query.onComplete(folders);
        }

        @Override
        public void loadOnlyInAppDirAllMediaData(Context context,
                                                 OnQueryAlbumListener<LocalMediaFolder> query) {
            LocalMediaFolder folder = SandboxFileLoader
                    .loadInAppSandboxFolderFile(context, getSandboxPath());
            query.onComplete(folder);
        }

        @Override
        public void loadFirstPageMediaData(Context context, long bucketId, int page, int pageSize, OnQueryDataResultListener<LocalMedia> query) {
            LocalMediaFolder folder = SandboxFileLoader
                    .loadInAppSandboxFolderFile(context, getSandboxPath());
            query.onComplete(folder.getData(), false);
        }

        @Override
        public void loadMoreMediaData(Context context, long bucketId, int page, int limit, int pageSize, OnQueryDataResultListener<LocalMedia> query) {

        }
    }

    /**
     * 自定义编辑事件
     *
     * @return
     */
    private OnMediaEditInterceptListener getCustomEditMediaEvent() {
        return new MeOnMediaEditInterceptListener(getSandboxPath(), buildOptions());
    }


    /**
     * 自定义编辑
     */
    private static class MeOnMediaEditInterceptListener implements OnMediaEditInterceptListener {
        private final String outputCropPath;
        private final UCrop.Options options;

        public MeOnMediaEditInterceptListener(String outputCropPath, UCrop.Options options) {
            this.outputCropPath = outputCropPath;
            this.options = options;
        }

        @Override
        public void onStartMediaEdit(Fragment fragment, LocalMedia currentLocalMedia, int requestCode) {
            String currentEditPath = currentLocalMedia.getAvailablePath();
            Uri inputUri = PictureMimeType.isContent(currentEditPath)
                    ? Uri.parse(currentEditPath) : Uri.fromFile(new File(currentEditPath));
            Uri destinationUri = Uri.fromFile(
                    new File(outputCropPath, DateUtils.getCreateFileName("CROP_") + ".jpeg"));
            UCrop uCrop = UCrop.of(inputUri, destinationUri);
            options.setHideBottomControls(false);
            uCrop.withOptions(options);
            uCrop.setImageEngine(new UCropImageEngine() {
                @Override
                public void loadImage(Context context, String url, ImageView imageView) {
                    if (!ImageLoaderUtils.assertValidRequest(context)) {
                        return;
                    }
                    Glide.with(context).load(url).override(180, 180).into(imageView);
                }

                @Override
                public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
                    Glide.with(context).asBitmap().load(url).override(maxWidth, maxHeight).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (call != null) {
                                call.onCall(resource);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            if (call != null) {
                                call.onCall(null);
                            }
                        }
                    });
                }
            });
            uCrop.startEdit(fragment.requireActivity(), fragment, requestCode);
        }
    }

    /**
     * 录音回调事件
     */
    private static class MeOnRecordAudioInterceptListener implements OnRecordAudioInterceptListener {

        @Override
        public void onRecordAudio(Fragment fragment, int requestCode) {
            String[] recordAudio = {Manifest.permission.RECORD_AUDIO};
            if (PermissionChecker.isCheckSelfPermission(fragment.getContext(), recordAudio)) {
                startRecordSoundAction(fragment, requestCode);
            } else {
                addPermissionDescription(false, (ViewGroup) fragment.requireView(), recordAudio);
                PermissionChecker.getInstance().requestPermissions(fragment,
                        new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionResultCallback() {
                            @Override
                            public void onGranted() {
                                removePermissionDescription((ViewGroup) fragment.requireView());
                                startRecordSoundAction(fragment, requestCode);
                            }

                            @Override
                            public void onDenied() {
                                removePermissionDescription((ViewGroup) fragment.requireView());
                            }
                        });
            }
        }
    }

    /**
     * 启动录音意图
     *
     * @param fragment
     * @param requestCode
     */
    private static void startRecordSoundAction(Fragment fragment, int requestCode) {
        Intent recordAudioIntent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (recordAudioIntent.resolveActivity(fragment.requireActivity().getPackageManager()) != null) {
            fragment.startActivityForResult(recordAudioIntent, requestCode);
        } else {
            ToastUtils.showToast(fragment.getContext(), "The system is missing a recording component");
        }
    }

    /**
     * 自定义沙盒文件处理
     */
    private static class MeSandboxFileEngine implements UriToFileTransformEngine {

        @Override
        public void onUriToFileAsyncTransform(Context context, String srcPath, String mineType, OnKeyValueResultCallbackListener call) {
            if (call != null) {
                call.onCallback(srcPath, SandboxTransformUtils.copyPathToSandbox(context, srcPath, mineType));
            }
        }
    }

    /**
     * 自定义裁剪
     */
    private class ImageFileCropEngine implements CropFileEngine {

        @Override
        public void onStartCrop(Fragment fragment, Uri srcUri, Uri destinationUri, ArrayList<String> dataSource, int requestCode) {
            UCrop.Options options = buildOptions();
            UCrop uCrop = UCrop.of(srcUri, destinationUri, dataSource);
            uCrop.withOptions(options);
            uCrop.setImageEngine(new UCropImageEngine() {
                @Override
                public void loadImage(Context context, String url, ImageView imageView) {
                    if (!ImageLoaderUtils.assertValidRequest(context)) {
                        return;
                    }
                    Glide.with(context).load(url).override(180, 180).into(imageView);
                }

                @Override
                public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
                    Glide.with(context).asBitmap().load(url).override(maxWidth, maxHeight).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (call != null) {
                                call.onCall(resource);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            if (call != null) {
                                call.onCall(null);
                            }
                        }
                    });
                }
            });
            uCrop.start(fragment.requireActivity(), fragment, requestCode);
        }
    }

    /**
     * 自定义裁剪
     */
    private class ImageCropEngine implements CropEngine {

        @Override
        public void onStartCrop(Fragment fragment, LocalMedia currentLocalMedia,
                                ArrayList<LocalMedia> dataSource, int requestCode) {
            String currentCropPath = currentLocalMedia.getAvailablePath();
            Uri inputUri;
            if (PictureMimeType.isContent(currentCropPath) || PictureMimeType.isHasHttp(currentCropPath)) {
                inputUri = Uri.parse(currentCropPath);
            } else {
                inputUri = Uri.fromFile(new File(currentCropPath));
            }
            String fileName = DateUtils.getCreateFileName("CROP_") + ".jpg";
            Uri destinationUri = Uri.fromFile(new File(getSandboxPath(), fileName));
            UCrop.Options options = buildOptions();
            ArrayList<String> dataCropSource = new ArrayList<>();
            for (int i = 0; i < dataSource.size(); i++) {
                LocalMedia media = dataSource.get(i);
                dataCropSource.add(media.getAvailablePath());
            }
            UCrop uCrop = UCrop.of(inputUri, destinationUri, dataCropSource);
            //options.setMultipleCropAspectRatio(buildAspectRatios(dataSource.size()));
            uCrop.withOptions(options);
            uCrop.setImageEngine(new UCropImageEngine() {
                @Override
                public void loadImage(Context context, String url, ImageView imageView) {
                    if (!ImageLoaderUtils.assertValidRequest(context)) {
                        return;
                    }
                    Glide.with(context).load(url).override(180, 180).into(imageView);
                }

                @Override
                public void loadImage(Context context, Uri url, int maxWidth, int maxHeight, OnCallbackListener<Bitmap> call) {
                }
            });
            uCrop.start(fragment.requireActivity(), fragment, requestCode);
        }
    }

    /**
     * 多图裁剪时每张对应的裁剪比例
     *
     * @param dataSourceCount
     * @return
     */
    private AspectRatio[] buildAspectRatios(int dataSourceCount) {
        AspectRatio[] aspectRatios = new AspectRatio[dataSourceCount];
        for (int i = 0; i < dataSourceCount; i++) {
            if (i == 0) {
                aspectRatios[i] = new AspectRatio("16:9", 16, 9);
            } else if (i == 1) {
                aspectRatios[i] = new AspectRatio("3:2", 3, 2);
            } else {
                aspectRatios[i] = new AspectRatio("原始比例", 0, 0);
            }
        }
        return aspectRatios;
    }

    /**
     * 自定义压缩
     */
    private static class ImageFileCompressEngine implements CompressFileEngine {

        @Override
        public void onStartCompress(Context context, ArrayList<Uri> source, OnKeyValueResultCallbackListener call) {
            Luban.with(context).load(source).ignoreBy(100).setRenameListener(new OnRenameListener() {
                @Override
                public String rename(String filePath) {
                    int indexOf = filePath.lastIndexOf(".");
                    String postfix = indexOf != -1 ? filePath.substring(indexOf) : ".jpg";
                    return DateUtils.getCreateFileName("CMP_") + postfix;
                }
            }).setCompressListener(new OnNewCompressListener() {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(String source, File compressFile) {
                    if (call != null) {
                        call.onCallback(source, compressFile.getAbsolutePath());
                    }
                }

                @Override
                public void onError(String source, Throwable e) {
                    if (call != null) {
                        call.onCallback(source, null);
                    }
                }
            }).launch();
        }
    }

    /**
     * 自定义压缩
     */
    @Deprecated
    private static class ImageCompressEngine implements CompressEngine {

        @Override
        public void onStartCompress(Context context, ArrayList<LocalMedia> list,
                                    OnCallbackListener<ArrayList<LocalMedia>> listener) {
            // 自定义压缩
            List<Uri> compress = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                LocalMedia media = list.get(i);
                String availablePath = media.getAvailablePath();
                Uri uri = PictureMimeType.isContent(availablePath) || PictureMimeType.isHasHttp(availablePath)
                        ? Uri.parse(availablePath)
                        : Uri.fromFile(new File(availablePath));
                compress.add(uri);
            }
            if (compress.size() == 0) {
                listener.onCall(list);
                return;
            }
            Luban.with(context)
                    .load(compress)
                    .ignoreBy(100)
                    .filter(new CompressionPredicate() {
                        @Override
                        public boolean apply(String path) {
                            return PictureMimeType.isUrlHasImage(path) && !PictureMimeType.isHasHttp(path);
                        }
                    })
                    .setRenameListener(new OnRenameListener() {
                        @Override
                        public String rename(String filePath) {
                            int indexOf = filePath.lastIndexOf(".");
                            String postfix = indexOf != -1 ? filePath.substring(indexOf) : ".jpg";
                            return DateUtils.getCreateFileName("CMP_") + postfix;
                        }
                    })
                    .setCompressListener(new OnCompressListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onSuccess(int index, File compressFile) {
                            LocalMedia media = list.get(index);
                            if (compressFile.exists() && !TextUtils.isEmpty(compressFile.getAbsolutePath())) {
                                media.setCompressed(true);
                                media.setCompressPath(compressFile.getAbsolutePath());
                                media.setSandboxPath(SdkVersionUtils.isQ() ? media.getCompressPath() : null);
                            }
                            if (index == list.size() - 1) {
                                listener.onCall(list);
                            }
                        }

                        @Override
                        public void onError(int index, Throwable e) {
                            if (index != -1) {
                                LocalMedia media = list.get(index);
                                media.setCompressed(false);
                                media.setCompressPath(null);
                                media.setSandboxPath(null);
                                if (index == list.size() - 1) {
                                    listener.onCall(list);
                                }
                            }
                        }
                    }).launch();
        }

    }

    /**
     * 创建自定义输出目录
     *
     * @return
     */
    private String getSandboxPath() {
        File externalFilesDir = context.getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Sandbox");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }

    /**
     * 创建自定义输出目录
     *
     * @return
     */
    private String getSandboxMarkDir() {
        File externalFilesDir = context.getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Mark");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }

    /**
     * 创建自定义输出目录
     *
     * @return
     */
    private String getVideoThumbnailDir() {
        File externalFilesDir = context.getExternalFilesDir("");
        File customFile = new File(externalFilesDir.getAbsolutePath(), "Thumbnail");
        if (!customFile.exists()) {
            customFile.mkdirs();
        }
        return customFile.getAbsolutePath() + File.separator;
    }


    /**
     * 处理选择结果
     *
     * @param result
     */
    public void analyticalSelectResults(ArrayList<LocalMedia> result) {
        for (LocalMedia media : result) {
            if (media.getWidth() == 0 || media.getHeight() == 0) {
                if (PictureMimeType.isHasImage(media.getMimeType())) {
                    MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(context, media.getPath());
                    media.setWidth(imageExtraInfo.getWidth());
                    media.setHeight(imageExtraInfo.getHeight());
                } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                    MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(context, media.getPath());
                    media.setWidth(videoExtraInfo.getWidth());
                    media.setHeight(videoExtraInfo.getHeight());
                }
            }
            Log.i(TAG, "文件名: " + media.getFileName());
            Log.i(TAG, "是否压缩:" + media.isCompressed());
            Log.i(TAG, "压缩:" + media.getCompressPath());
            Log.i(TAG, "初始路径:" + media.getPath());
            Log.i(TAG, "绝对路径:" + media.getRealPath());
            Log.i(TAG, "是否裁剪:" + media.isCut());
            Log.i(TAG, "裁剪路径:" + media.getCutPath());
            Log.i(TAG, "是否开启原图:" + media.isOriginal());
            Log.i(TAG, "原图路径:" + media.getOriginalPath());
            Log.i(TAG, "沙盒路径:" + media.getSandboxPath());
            Log.i(TAG, "水印路径:" + media.getWatermarkPath());
            Log.i(TAG, "视频缩略图:" + media.getVideoThumbnailPath());
            Log.i(TAG, "原始宽高: " + media.getWidth() + "x" + media.getHeight());
            Log.i(TAG, "裁剪宽高: " + media.getCropImageWidth() + "x" + media.getCropImageHeight());
            Log.i(TAG, "文件大小: " + PictureFileUtils.formatAccurateUnitFileSize(media.getSize()));
            Log.i(TAG, "文件时长: " + media.getDuration());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isMaxSize = result.size() == mAdapter.getSelectMax();
                int oldSize = mAdapter.getData().size();
                mAdapter.notifyItemRangeRemoved(0, isMaxSize ? oldSize + 1 : oldSize);
                mAdapter.getData().clear();

                mAdapter.getData().addAll(result);
                mAdapter.notifyItemRangeInserted(0, result.size());
            }
        });
    }


    private String[] getNotSupportCrop() {
        //跳过裁剪gif
        return new String[]{PictureMimeType.ofGIF(), PictureMimeType.ofWEBP()};
    }

    /**
     * 配制UCrop，可根据需求自我扩展
     *
     * @return
     */
    private UCrop.Options buildOptions() {
        selectorStyle = getSelectorStyle();
        UCrop.Options options = new UCrop.Options();
        options.setHideBottomControls(false);   //是否隐藏裁剪菜单栏
        options.setFreeStyleCropEnabled(true);  //裁剪框or图片拖动
        options.setShowCropFrame(true); //是否显示裁剪边框
        options.setShowCropGrid(true);   //是否显示裁剪框网格
        options.setCircleDimmedLayer(false); //圆形头像裁剪模式
        options.withAspectRatio(aspect_ratio_x, aspect_ratio_y);
        options.setCropOutputPathDir(getSandboxPath());
        options.isCropDragSmoothToCenter(false);
        options.setSkipCropMimeType(getNotSupportCrop());
        options.isForbidCropGifWebp(true);    //禁止裁剪gif
        options.isForbidSkipMultipleCrop(false);
        options.setMaxScaleMultiplier(100);
        if (selectorStyle != null && selectorStyle.getSelectMainStyle().getStatusBarColor() != 0) {
            SelectMainStyle mainStyle = selectorStyle.getSelectMainStyle();
            boolean isDarkStatusBarBlack = mainStyle.isDarkStatusBarBlack();
            int statusBarColor = mainStyle.getStatusBarColor();
            options.isDarkStatusBarBlack(isDarkStatusBarBlack);
            if (StyleUtils.checkStyleValidity(statusBarColor)) {
                options.setStatusBarColor(statusBarColor);
                options.setToolbarColor(statusBarColor);
            } else {
                options.setStatusBarColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_grey));
                options.setToolbarColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_grey));
            }
            TitleBarStyle titleBarStyle = selectorStyle.getTitleBarStyle();
            if (StyleUtils.checkStyleValidity(titleBarStyle.getTitleTextColor())) {
                options.setToolbarWidgetColor(titleBarStyle.getTitleTextColor());
            } else {
                options.setToolbarWidgetColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_white));
            }
        } else {
            options.setStatusBarColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_grey));
            options.setToolbarColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_grey));
            options.setToolbarWidgetColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_white));
        }
        return options;
    }

    public PictureSelectorStyle getSelectorStyle() {
        // 主体风格
        SelectMainStyle numberSelectMainStyle = new SelectMainStyle();
        numberSelectMainStyle.setSelectNumberStyle(true);
        numberSelectMainStyle.setPreviewSelectNumberStyle(false);
        numberSelectMainStyle.setPreviewDisplaySelectGallery(true);
        numberSelectMainStyle.setSelectBackground(com.luck.picture.lib.R.drawable.ps_default_num_selector);
        numberSelectMainStyle.setPreviewSelectBackground(com.luck.picture.lib.R.drawable.ps_preview_checkbox_selector);
        numberSelectMainStyle.setSelectNormalBackgroundResources(com.luck.picture.lib.R.drawable.ps_select_complete_normal_bg);
        numberSelectMainStyle.setSelectNormalTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_53575e));
        numberSelectMainStyle.setSelectNormalText("完成");
        numberSelectMainStyle.setAdapterPreviewGalleryBackgroundResource(com.luck.picture.lib.R.drawable.ps_preview_gallery_bg);
        numberSelectMainStyle.setAdapterPreviewGalleryItemSize(DensityUtil.dip2px(context, 52));
        numberSelectMainStyle.setPreviewSelectText("选择");
        numberSelectMainStyle.setPreviewSelectTextSize(14);
        numberSelectMainStyle.setPreviewSelectTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_white));
        numberSelectMainStyle.setPreviewSelectMarginRight(DensityUtil.dip2px(context, 6));
        numberSelectMainStyle.setSelectBackgroundResources(com.luck.picture.lib.R.drawable.ps_select_complete_bg);
        numberSelectMainStyle.setSelectText("完成(%1$d/%2$d)");
        numberSelectMainStyle.setSelectTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_white));
        numberSelectMainStyle.setMainListBackgroundColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_black));
        numberSelectMainStyle.setCompleteSelectRelativeTop(true);
        numberSelectMainStyle.setPreviewSelectRelativeBottom(true);
        numberSelectMainStyle.setAdapterItemIncludeEdge(false);

        // 头部TitleBar 风格
        TitleBarStyle numberTitleBarStyle = new TitleBarStyle();
        numberTitleBarStyle.setHideCancelButton(true);
        numberTitleBarStyle.setAlbumTitleRelativeLeft(true);
        numberTitleBarStyle.setTitleAlbumBackgroundResource(com.luck.picture.lib.R.drawable.ps_album_bg);
        numberTitleBarStyle.setTitleDrawableRightResource(com.luck.picture.lib.R.drawable.ps_ic_grey_arrow);
        numberTitleBarStyle.setPreviewTitleLeftBackResource(com.luck.picture.lib.R.drawable.ps_ic_normal_back);

        // 底部NavBar 风格
        BottomNavBarStyle numberBottomNavBarStyle = new BottomNavBarStyle();
        numberBottomNavBarStyle.setBottomPreviewNarBarBackgroundColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_half_grey));
        numberBottomNavBarStyle.setBottomPreviewNormalText("预览");
        numberBottomNavBarStyle.setBottomPreviewNormalTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_9b));
        numberBottomNavBarStyle.setBottomPreviewNormalTextSize(16);
        numberBottomNavBarStyle.setCompleteCountTips(false);
        numberBottomNavBarStyle.setBottomPreviewSelectText("预览(%1$d)");
        numberBottomNavBarStyle.setBottomPreviewSelectTextColor(ContextCompat.getColor(context, com.luck.picture.lib.R.color.ps_color_white));

        selectorStyle.setTitleBarStyle(numberTitleBarStyle);
        selectorStyle.setBottomBarStyle(numberBottomNavBarStyle);
        selectorStyle.setSelectMainStyle(numberSelectMainStyle);

        PictureWindowAnimationStyle animationStyle = new PictureWindowAnimationStyle();
        animationStyle.setActivityEnterAnimation(com.luck.picture.lib.R.anim.ps_anim_up_in);
        animationStyle.setActivityExitAnimation(com.luck.picture.lib.R.anim.ps_anim_down_out);
        selectorStyle.setWindowAnimationStyle(animationStyle);
        return selectorStyle;
    }

    public static String getPath(LocalMedia photoOutputUri, Context context) {
        String path = null;
        String pictureType = photoOutputUri.getMimeType().substring(0,photoOutputUri.getMimeType().indexOf("/"));
        Log.i("update 文件类型::", pictureType);
        if (photoOutputUri.isOriginal()) {
            path = photoOutputUri.getOriginalPath();
            Log.i("update 开启原图功能后地址::", path);
            Log.i("update 开启原图功能后文件大小::", new File(path).length() / 1024 + "kb");
        } else {
            if (TextUtils.equals(pictureType,"video")) {
                //String outputVideoPath = getExternalFilesDir("").getAbsolutePath();
                Uri uri = Uri.parse(photoOutputUri.getPath());
                path = PathUtil.getPath(context,uri);

                File movie = new File(context.getCacheDir(), "movie");
                movie.mkdirs();
                String filePrefix = "scale_video";
                String fileExtn = ".mp4";
                File dest = new File(movie, filePrefix + fileExtn);
                int fileNo = 0;
                while (dest.exists()) {
                    fileNo++;
                    dest = new File(movie, filePrefix + fileNo + fileExtn);
                }
                String filePath = dest.getAbsolutePath();
                synchronized(context) {
                    try {
                        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                        retriever.setDataSource(path);
                        int originWidth = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                        int originHeight = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                        int bitrate = Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));

                        VideoProcessor.processor(context)
                                .input(path)
                                .output(filePath)
                                .outWidth(originWidth/3)
                                .outHeight(originHeight/3)
                                .bitrate(bitrate/3)
                                .process();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                path = filePath;
                Log.i("update 视频压缩后地址::", path);
                Log.i("update 视频压缩后文件大小::", new File(path).length() / 1024 + "kb");
            } else {
                if (photoOutputUri.isCut() && !photoOutputUri.isCompressed()) {
                    // 裁剪过
                    path = photoOutputUri.getCutPath();
                    Log.i("update 裁剪后地址::", path);
                    Log.i("update 裁剪后文件大小::", new File(path).length() / 1024 + "kb");
                } else if (photoOutputUri.isCut() || photoOutputUri.isCompressed()) {
                    // 压缩过,或者裁剪同时压缩过,以最终压缩过图片为准
                    path = photoOutputUri.getCompressPath();
                    Log.i("update 压缩后地址::", path);
                    Log.i("update 压缩后文件大小::", new File(path).length() / 1024 + "kb");
                }
            }
        }
        return path;
    }
}
