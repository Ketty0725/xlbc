package com.ketty.chinesemedicine;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.component.AndroidBottomSoftBar;
import com.ketty.chinesemedicine.component.BottomNavigationBar;
import com.ketty.chinesemedicine.databinding.ActivityMainBinding;
import com.ketty.chinesemedicine.main.community.CommunityFragment;
import com.ketty.chinesemedicine.main.home.HomeFragment;
import com.ketty.chinesemedicine.main.mine.MineFragment;
import com.ketty.chinesemedicine.main.mine.setting.UpdateDialog;
import com.ketty.chinesemedicine.main.publish.PubActivity;
import com.ketty.chinesemedicine.main.store.StoreFragment;
import com.ketty.chinesemedicine.util.AppExecutors;
import com.ketty.chinesemedicine.util.DestroyActivityUtil;
import com.ketty.chinesemedicine.util.IPUtils;
import com.ketty.chinesemedicine.util.T;
import com.pgyersdk.update.DownloadFileListener;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.pgyersdk.update.javabean.AppBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding bind;
    private static final int TIME_EXIT = 2000;
    private long mBackPressed;
    private final HomeFragment mHomeFragment = HomeFragment.getInstance();
    private final CommunityFragment mCommunityFragment = CommunityFragment.getInstance();
    private final StoreFragment mStoreFragment = StoreFragment.getInstance();
    private final MineFragment mMineFragment = MineFragment.getInstance();
    private final FragmentManager fm = getSupportFragmentManager();
    private Fragment active = mHomeFragment;
    private List<Fragment> mFragmentList = new ArrayList<>();
    public static final String TAG_EXIT = "exit";
    private boolean isLoaded = false;
    private UpdateDialog dialog;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                this.finish();
            }
        }
    }

    @Override
    protected View initLayout() {
        DestroyActivityUtil.addDestoryActivityToMap(MainActivity.this, "MainActivity");
        bind = ActivityMainBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        if (!isLoaded) {
            updateIpAddress();
            isLoaded = true;
            checkUpdate();
        }
        DestroyActivityUtil.addDestoryActivityToMap(MainActivity.this, "MainActivity");
        AndroidBottomSoftBar.assistActivity(findViewById(android.R.id.content), this);
        verifyStoragePermissions(this);
        showFragment(mHomeFragment);

        bind.bottombar.setOnBottomNavClickListener(new BottomNavigationBar.OnBottomNavClickListener() {
            @Override
            public void onBottomNavClick(int checkedId) {
                switch (checkedId){
                    case 0:
                        showFragment(mHomeFragment);
                        break;
                    case 1:
                        showFragment(mCommunityFragment);
                        break;
                    case 2:
                        showFragment(mStoreFragment);
                        break;
                    case 3:
                        showFragment(mMineFragment);
                        break;
                    case 4:
                        PubActivity.show(MainActivity.this);
                        break;
                }
            }

            @Override
            public void onBottomDoubleClick(int checkedId) {
                switch (checkedId){

                }
            }
        });
    }

    private void checkUpdate() {
        new PgyUpdateManager.Builder()
                .setUserCanRetry(false)
                .setDeleteHistroyApk(false)
                .setUpdateManagerListener(new UpdateManagerListener() {
                    @Override
                    public void onNoUpdateAvailable() {

                    }
                    @Override
                    public void onUpdateAvailable(AppBean appBean) {
                        dialog = new UpdateDialog();
                        Bundle bundle = new Bundle();
                        bundle.putString("versionName", appBean.getVersionName());
                        bundle.putString("releaseNote", appBean.getReleaseNote());
                        dialog.setArguments(bundle);
                        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullScreen);
                        dialog.show(getSupportFragmentManager(), "UpdateDialog");
                        dialog.setStateListener(new UpdateDialog.StateListener() {
                            @Override
                            public void close() {
                                dialog.dismiss();
                            }

                            @Override
                            public void update() {
                                T.showShort("开始下载更新...");
                                PgyUpdateManager.downLoadApk(appBean.getDownloadURL());
                            }

                            @Override
                            public void install(File file) {
                                PgyUpdateManager.installApk(file);
                            }
                        });
                    }

                    @Override
                    public void checkUpdateFailed(Exception e) {

                    }
                })
                .setDownloadFileListener(new DownloadFileListener() {
                    @Override
                    public void downloadFailed() {
                        T.showShort("下载失败，请重试");
                        dialog.downloadFailed();
                    }

                    @Override
                    public void downloadSuccessful(File file) {
                        T.showShort("下载完成");
                        PgyUpdateManager.installApk(file);
                        dialog.downloadSuccessful(file);
                    }

                    @Override
                    public void onProgressUpdate(Integer... integers) {
                        for (Integer integer : integers) {
                            dialog.initDownload(integer);
                        }
                    }})
                .register();
    }

    private void updateIpAddress() {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                String ip = IPUtils.getOutNetIP();
                if (ip != null) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("uId", App.getMmkvUtil().getString("user","uId",null));
                    map.put("ip", ip);

                    RetrofitManager
                            .getInstance()
                            .getApiService()
                            .postMethod("/user/updateIPAddress", map)
                            .compose(RxHelper.observableIO2Main(MainActivity.this))
                            .subscribe(new BaseObserver() {
                                @Override
                                public void onSuccess(Map<String, Object> response) {

                                }

                                @Override
                                public void onFailure(Throwable e, String errorMsg) {

                                }
                            });
                }
            }
        });
    }

    private void showFragment(Fragment fragment) {
        /*判断该fragment是否已经被添加过  如果没有被添加  则添加*/
        if (!fragment.isAdded()) {
            fm.beginTransaction().add(R.id.fragment, fragment).commit();
            /*添加到 mFragmentList*/
            mFragmentList.add(fragment);
        }

        for (Fragment frag : mFragmentList) {
            if (frag != fragment) {
                /*先隐藏其他fragment*/
                fm.beginTransaction().hide(frag).commit();
            }
        }
        fm.beginTransaction().show(fragment).commit();

        active = fragment;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (active != mHomeFragment) {
                showFragment(mHomeFragment);
                bind.bottombar.initialize();
            } else {
                if (mBackPressed + TIME_EXIT > System.currentTimeMillis()) {
                    super.onBackPressed();
                    return true;
                } else {
                    T.showShort("再按一次返回退出程序");
                    mBackPressed = System.currentTimeMillis();
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragmentList = null;
        bind = null;
    }

    //动态申请sd卡读写权限
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}