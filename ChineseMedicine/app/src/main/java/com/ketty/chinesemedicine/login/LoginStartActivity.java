package com.ketty.chinesemedicine.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.MainActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.databinding.ActivityLoginStartBinding;
import com.ketty.chinesemedicine.login.phone.LoginForPhoneActivity;
import com.ketty.chinesemedicine.util.DestroyActivityUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LoginUtil;
import com.ketty.chinesemedicine.util.MMKVUtil;
import com.ketty.chinesemedicine.util.NetworkUtils;
import com.ketty.chinesemedicine.util.T;

import java.util.HashMap;
import java.util.Map;

public class LoginStartActivity extends BaseActivity implements View.OnClickListener {
    private ActivityLoginStartBinding bind;
    private LoginUtil mLoginUtil;

    @Override
    protected View initLayout() {
        DestroyActivityUtil.addDestoryActivityToMap(LoginStartActivity.this, "LoginStartActivity");
        bind = ActivityLoginStartBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        if (App.getMmkvUtil().getString("user","uId",null) != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        bind.loginQq.setOnClickListener(this);
        bind.loginOther.setOnClickListener(this);
        bind.scb.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_qq:
                if (!bind.scb.isChecked()) {
                    AgreeAndContinueDialog mAgreeAndContinueDialog = new AgreeAndContinueDialog();
                    mAgreeAndContinueDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullScreen);
                    mAgreeAndContinueDialog.show(getSupportFragmentManager(), "AgreeAndContinueDialog");
                    mAgreeAndContinueDialog.setStateListener(new AgreeAndContinueDialog.StateListener() {
                        @Override
                        public void close() {
                            mAgreeAndContinueDialog.dismiss();
                        }

                        @Override
                        public void agree() {
                            loginForQQ();
                            mAgreeAndContinueDialog.dismiss();
                        }
                    });
                } else {
                    loginForQQ();
                }
                break;
            case R.id.login_other:
                LoginOtherDialog mLoginOtherDialog = new LoginOtherDialog();
                mLoginOtherDialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogFullScreen);
                mLoginOtherDialog.show(getSupportFragmentManager(), "LoginOtherDialog");
                mLoginOtherDialog.setStateListener(new LoginOtherDialog.StateListener() {
                    @Override
                    public void close() {
                        mLoginOtherDialog.dismiss();
                    }

                    /*@Override
                    public void go2qq() {
                        if (NetUtil.getNetWorkState(LoginStartActivity.this) == -1) {
                            T.showShort("网络连接断开，请检查网络设置");
                        } else {
                            mLoginUtil = new LoginUtil(LoginStartActivity.this, myHandler);
                            mLoginUtil.login("qq");
                        }
                    }*/

                    @Override
                    public void go2phone() {
                        startActivity(new Intent(LoginStartActivity.this, LoginForPhoneActivity.class));
                    }
                });
                break;
            case R.id.scb:
                bind.scb.toggle();
                break;
        }
    }

    private void loginForQQ() {
        if (!NetworkUtils.isAvailableByPing()) {
            T.showShort("当前网络无法访问互联网，请检查后重试");
        } else {
            mLoginUtil = new LoginUtil(handler, 1);
            mLoginUtil.login("qq");
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String[] data = (String[]) msg.obj;
            switch (msg.what) {
                case 1:
                    if (data.length > 0) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("uQqId", data[0]);
                        map.put("uQqName", data[1]);
                        map.put("uHeadicon", data[2]);

                        RetrofitManager
                                .getInstance()
                                .getApiService()
                                .postMethod("/user/loginForQQ", map)
                                .compose(RxHelper.observableIO2Main(LoginStartActivity.this))
                                .subscribe(new BaseObserver() {
                                    @Override
                                    public void onSuccess(Map<String, Object> response) {
                                        MMKVUtil mmkvUtil = App.getMmkvUtil();
                                        String data = JsonHelper.parserObject2Json(response.get("result"));
                                        Long uId = JsonHelper.parserJson2Object(
                                                data, Long.class);
                                        mmkvUtil.putString("user", "uId", String.valueOf(uId));
                                        startActivity(new Intent(LoginStartActivity.this, MainActivity.class));
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Throwable e, String errorMsg) {

                                    }
                                });
                    }
                    break;
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}