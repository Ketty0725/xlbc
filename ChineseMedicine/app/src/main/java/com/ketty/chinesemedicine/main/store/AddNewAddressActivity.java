package com.ketty.chinesemedicine.main.store;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.component.XEditText;
import com.ketty.chinesemedicine.databinding.ActivityAddNewAddressBinding;
import com.ketty.chinesemedicine.entity.JsonBean;
import com.ketty.chinesemedicine.entity.Product;
import com.ketty.chinesemedicine.entity.Productshopcart;
import com.ketty.chinesemedicine.entity.Shoppingaddress;
import com.ketty.chinesemedicine.login.phone.CaptchaFragment;
import com.ketty.chinesemedicine.util.GetJsonDataUtil;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;
import com.ketty.chinesemedicine.util.MMKVUtil;
import com.ketty.chinesemedicine.util.T;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class AddNewAddressActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAddNewAddressBinding bind;
    private MMKVUtil mmkvUtil;
    private String uId;
    private int type;
    private Shoppingaddress data;
    private InputMethodManager imm;
    private static boolean isLoaded = false;
    private List<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;

    @Override
    protected View initLayout() {
        bind = ActivityAddNewAddressBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        mmkvUtil = App.getMmkvUtil();
        uId = mmkvUtil.getString("user","uId",null);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");
        data = (Shoppingaddress) bundle.getSerializable("data");
        if (type == 0) {
            bind.tvTitle.setText("创建新地址");
            bind.tvAddNewAddress.setText("添加新地址");
        } else {
            bind.tvTitle.setText("修改地址");
            bind.etName.setTextEx(data.getName());
            bind.etPhone.setTextEx(data.getPhone());
            bind.etArea.setTextEx(data.getArea());
            bind.etAddress.setTextEx(data.getAddress());
            if (data.getIsDefault() == 1) {
                bind.switchButton.setChecked(true);
            } else {
                bind.switchButton.setChecked(false);
            }
            bind.tvAddNewAddress.setText("保存地址");
        }

        bind.slAddNewAddress.setClickable(false);
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        initEditText();

        bind.etArea.setOnClickListener(this);
        bind.slAddNewAddress.setOnClickListener(this);
        bind.ivBack.setOnClickListener(this);

        TextChange textChange = new TextChange();
        bind.etName.setOnXTextChangeListener(textChange);
        bind.etPhone.setOnXTextChangeListener(textChange);
        bind.etArea.setOnXTextChangeListener(textChange);
        bind.etAddress.setOnXTextChangeListener(textChange);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.et_area:
                if (isLoaded) {
                    showPickerView();
                }
                break;
            case R.id.sl_add_new_address:
                String url = "";
                if (type == 0) {
                    url = "/shoppingaddress/add";
                } else {
                    url = "/shoppingaddress/updateData";
                }
                if (bind.etPhone.getTrimmedString().length() == 11) {
                    initRequest(url);
                } else {
                    T.showShort("手机号无效");
                }
                break;
        }
    }

    private void initRequest(String url) {
        String name = bind.etName.getTrimmedString();
        String phone = bind.etPhone.getTrimmedString();
        String area = bind.etArea.getTrimmedString();
        String address = bind.etAddress.getTrimmedString();
        int isDefault = bind.switchButton.isChecked() ? 1 : 0;
        HashMap<String, Object> map = new HashMap<>();
        if (type == 1) {
            map.put("id", data.getId());
        }
        map.put("userId", uId);
        map.put("name", name);
        map.put("phone", phone);
        map.put("area", area);
        map.put("address", address);
        map.put("isDefault", isDefault);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod(url, map)
                .compose(RxHelper.observableIO2Main(this))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        if (type == 0) {
                            T.showShort("添加成功");
                        } else {
                            T.showShort("修改成功");
                        }
                        setResult(RESULT_OK,new Intent());
                        finish();
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {

                    }
                });
    }

    private class TextChange implements XEditText.OnXTextChangeListener {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            initEditText();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void initEditText() {
        String name = bind.etName.getTrimmedString();
        String phone = bind.etPhone.getTrimmedString();
        String area = bind.etArea.getTrimmedString();
        String address = bind.etAddress.getTrimmedString();

        if (!"".equals(name) && !"".equals(phone) && !"".equals(area) && !"".equals(address)) {
            bind.slAddNewAddress.setClickable(true);
            if (phone.length() == 11) {
                bind.slAddNewAddress.setLayoutBackground(0xFFff2442);
            } else {
                bind.slAddNewAddress.setLayoutBackground(0xFFffa5a5);
            }
        } else {
            bind.slAddNewAddress.setLayoutBackground(0xFFffa5a5);
            bind.slAddNewAddress.setClickable(false);
        }
    }

    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
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
                bind.etArea.setText(area);
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

        pvOptions.setPicker(options1Items, options2Items, options3Items);
        pvOptions.show();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
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

    private void initJsonData() {
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");
        ArrayList<JsonBean> jsonBean = JsonHelper.parserJson2List(JsonData, new TypeToken<ArrayList<JsonBean>>() {}.getType());
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {
            ArrayList<String> cityList = new ArrayList<>();
            ArrayList<ArrayList<String>> province_AreaList = new ArrayList<>();

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {
                String cityName = jsonBean.get(i).getCityList().get(c).getName();
                cityList.add(cityName);
                ArrayList<String> city_AreaList = new ArrayList<>();
                city_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                province_AreaList.add(city_AreaList);
            }
            options2Items.add(cityList);
            options3Items.add(province_AreaList);
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
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