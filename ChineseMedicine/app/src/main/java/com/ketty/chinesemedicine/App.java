package com.ketty.chinesemedicine;

import static android.os.Build.VERSION.SDK_INT;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.camera.camera2.Camera2Config;
import androidx.camera.core.CameraXConfig;

import com.ketty.chinesemedicine.component.PictureSelectorEngineImp;
import com.ketty.chinesemedicine.util.DynamicTimeFormat;
import com.ketty.chinesemedicine.util.MMKVUtil;
import com.ketty.chinesemedicine.util.T;
import com.ketty.chinesemedicine.util.Utils;
import com.luck.picture.lib.app.IApp;
import com.luck.picture.lib.app.PictureAppMaster;
import com.luck.picture.lib.engine.PictureSelectorEngine;
import com.mob.MobSDK;
import com.pgyersdk.Pgyer;
import com.pgyersdk.PgyerActivityManager;
import com.pgyersdk.crash.PgyCrashManager;
import com.pgyersdk.crash.PgyerCrashObservable;
import com.pgyersdk.crash.PgyerObserver;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshInitializer;

import java.io.File;

import coil.ComponentRegistry;
import coil.ImageLoader;
import coil.ImageLoaderFactory;
import coil.decode.GifDecoder;
import coil.decode.ImageDecoderDecoder;
import coil.decode.VideoFrameDecoder;
import coil.disk.DiskCache;
import coil.memory.MemoryCache;

public class App extends Application implements IApp, CameraXConfig.Provider, ImageLoaderFactory {
    private static final String TAG = App.class.getSimpleName();
    private static final String cryptKey = "ORIqzZ&SzDwH6fE%";
    private static MMKVUtil mmkvUtil = null;
    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        T.init(this);
        Utils.init(this);
        instance = this;
        PictureAppMaster.getInstance().setApp(this);
        MobSDK.init(this, "372b21312c118","6eaaf9738778c6b4f97d7202640556ce");
        MobSDK.submitPolicyGrantResult(true, null);
        //mmkv
        mmkvUtil = MMKVUtil.getInstance();
        mmkvUtil.init(this);
        mmkvUtil.setEncrypt(true, cryptKey);

        PgyCrashManager.register();
        PgyerCrashObservable.get().attach(new PgyerObserver() {
            @Override
            public void receivedCrash(Thread thread, Throwable throwable) {
            }
        });
        PgyerActivityManager.set(this);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        Pgyer.setAppId("5a7c92f8e489273c62fc949f8b372132");
    }

    public static MMKVUtil getMmkvUtil() {
        return mmkvUtil;
    }

    @Override
    public Context getAppContext() {
        return this;
    }

    public static App getInstance() {
        return instance;
    }

    @Override
    public PictureSelectorEngine getPictureSelectorEngine() {
        return new PictureSelectorEngineImp();
    }

    @NonNull
    @Override
    public CameraXConfig getCameraXConfig() {
        return CameraXConfig.Builder.fromConfig(Camera2Config.defaultConfig())
                .setMinimumLoggingLevel(Log.ERROR).build();
    }

    @NonNull
    @Override
    public ImageLoader newImageLoader() {
        ImageLoader.Builder imageLoader = new ImageLoader.Builder(getAppContext());
        ComponentRegistry.Builder newBuilder = new ComponentRegistry().newBuilder();
        newBuilder.add(new VideoFrameDecoder.Factory());
        if (SDK_INT >= 28) {
            newBuilder.add(new ImageDecoderDecoder.Factory());
        } else {
            newBuilder.add(new GifDecoder.Factory());
        }
        ComponentRegistry componentRegistry = newBuilder.build();
        imageLoader.components(componentRegistry);
        imageLoader.memoryCache(new MemoryCache.Builder(getAppContext())
                .maxSizePercent(0.25).build());
        imageLoader.diskCache(new DiskCache.Builder()
                .directory(new File(getCacheDir(), "image_cache"))
                .maxSizePercent(0.02)
                .build());
        return imageLoader.build();
    }

    //static 代码段可以防止内存泄露
    static {
        //启用矢量图兼容
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置（优先级最低）
                layout.setEnableAutoLoadMore(true);
                layout.setEnableOverScrollDrag(false);
                layout.setEnableOverScrollBounce(true);
                layout.setEnableLoadMoreWhenContentNotFull(true);
                layout.setEnableScrollContentWhenRefreshed(true);
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
                layout.setFooterMaxDragRate(4.0F);
                layout.setFooterHeight(45);
            }
        });
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置主题颜色（优先级第二低，可以覆盖 DefaultRefreshInitializer 的配置，与下面的ClassicsHeader绑定）
                layout.setEnableHeaderTranslationContent(true);
                return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context);
            }
        });
    }
}
