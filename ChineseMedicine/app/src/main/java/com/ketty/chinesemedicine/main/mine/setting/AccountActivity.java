package com.ketty.chinesemedicine.main.mine.setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.component.CommonAlertDialog;
import com.ketty.chinesemedicine.databinding.ActivityAccountBinding;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.util.DestroyActivityUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LoginUtil;
import com.ketty.chinesemedicine.util.T;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAccountBinding bind;
    private User user;
    private String uId;
    private boolean hasPhone = false;
    private boolean hasPassword = false;
    private boolean hasQQ = false;

    @Override
    protected View initLayout() {
        DestroyActivityUtil.addDestoryActivityToMap(AccountActivity.this, "AccountActivity");
        bind = ActivityAccountBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        uId = App.getMmkvUtil().getString("user","uId",null);
        bind.ivBack.setOnClickListener(this);
        bind.shadowPhone.setOnClickListener(this);
        bind.shadowPassword.setOnClickListener(this);
        bind.shadowAccountQq.setOnClickListener(this);
        bind.shadowLogout.setOnClickListener(this);
        initRequest();
    }

    private void initRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uId", uId);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/getUserInfo", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String data = JsonHelper.parserObject2Json(response.get("result"));
                        user = JsonHelper.parserJson2Object(data, User.class);
                        initData();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void initData() {
        if (user.getuPhone() != null && !"".equals(user.getuPhone())) {
            hasPhone = true;
            String phone = user.getuPhone();
            phone = "+86 " + phone.substring(0,3) + "****" + phone.substring(7);
            bind.tvPhone.setText(phone);
        } else {
            hasPhone = false;
            bind.tvPhone.setText("未绑定");
        }

        if (user.getuPassword() != null && !"".equals(user.getuPassword())) {
            hasPassword = true;
            bind.tvPassword.setText("修改");
        } else {
            hasPassword = false;
            bind.tvPassword.setText("未设置");
        }

        if (user.getuQqId() != null && !"".equals(user.getuQqId())) {
            hasQQ = true;
            bind.tvQq.setText(user.getuQqName());
        } else {
            hasQQ = false;
            bind.tvQq.setText("未绑定");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.shadow_phone:
                if (hasPhone) {
                    String phone = user.getuPhone();
                    String encryptPhone = "+86 " + phone.substring(0,3) + "****" + phone.substring(7);
                    new CommonAlertDialog(this).builder()
                            .setTitle("更换绑定的手机号？")
                            .setMsg("当前绑定的手机号为\n" + encryptPhone)
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(AccountActivity.this,BindingPhoneActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("phone", phone);
                                    bundle.putInt("type", 1);
                                    intent.putExtras(bundle);
                                    mActivityLauncher.launch(intent);
                                }
                            }).setCancelable(false)
                            .show();
                } else {
                    Intent intent = new Intent(this,BindingPhoneActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", "");
                    bundle.putInt("type", 0);
                    intent.putExtras(bundle);
                    mActivityLauncher.launch(intent);
                }
                break;
            case R.id.shadow_password:
                if (hasPhone) {
                    if (hasPassword) {
                        Intent intent = new Intent(this,AuthenticationActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("phone", user.getuPhone());
                        intent.putExtras(bundle);
                        mActivityLauncher.launch(intent);
                    } else {
                        Intent intent = new Intent(this,SetNewPasswordActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", 0);
                        intent.putExtras(bundle);
                        mActivityLauncher.launch(intent);
                    }
                } else {
                    T.showShort("请先绑定手机号");
                }
                break;
            case R.id.shadow_account_qq:
                if (hasQQ) {
                    new CommonAlertDialog(this).builder()
                            .setTitle("确认解绑")
                            .setMsg("解绑QQ账号后将无法继续使用它登录该账号")
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            }).setPositiveButton("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    removeQQAcount();
                                }
                            }).setCancelable(false)
                            .show();
                } else {
                    LoginUtil mLoginUtil = new LoginUtil(handler, 1);
                    mLoginUtil.login("qq");
                }
                break;
            case R.id.shadow_logout:
                Intent intent = new Intent(AccountActivity.this,BindingPhoneActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("phone", user.getuPhone());
                bundle.putInt("type", 2);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private void removeQQAcount() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uId", uId);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/removeQQAcount", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        T.showShort("解绑成功");
                        hasQQ = false;
                        bind.tvQq.setText("未绑定");
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            String[] data = (String[]) msg.obj;
            switch (msg.what) {
                case 1:
                    if (data.length > 0) {
                        verify(data);
                    }
                    break;
            }
        }
    };

    private void verify(String[] data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uId", uId);
        map.put("uQqId", data[0]);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/selectQQUsed", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        String result = JsonHelper.parserObject2Json(response.get("result"));
                        User user = JsonHelper.parserJson2Object(result, User.class);
                        if (user != null) {
                            new CommonAlertDialog(AccountActivity.this).builder()
                                    .setTitle("更换绑定")
                                    .setMsg("该QQ当前绑定的账号为\n" + user.getuName() + "\n是否解绑并绑定至当前帐号？")
                                    .setNegativeButton("取消", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                        }
                                    }).setPositiveButton("确定", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            update(data);
                                        }
                                    }).setCancelable(false)
                                    .show();
                        } else {
                            update(data);
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private void update(String[] data) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("uId", uId);
        map.put("uQqId", data[0]);
        map.put("uQqName", data[1]);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/user/updateQQ", map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        T.showShort("绑定成功");
                        hasQQ = true;
                        bind.tvQq.setText(data[1]);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}