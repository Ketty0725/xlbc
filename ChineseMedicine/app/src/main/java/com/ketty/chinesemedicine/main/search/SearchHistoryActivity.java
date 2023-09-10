package com.ketty.chinesemedicine.main.search;

import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEHERB;
import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEPATENTMEDICINE;
import static com.ketty.chinesemedicine.main.search.SearchType.COMMUNITY;
import static com.ketty.chinesemedicine.main.search.SearchType.MEDICATEDDIET;
import static com.ketty.chinesemedicine.main.search.SearchType.PRESCRIPTION;
import static com.ketty.chinesemedicine.main.search.SearchType.STORE;
import static com.ketty.chinesemedicine.main.search.SearchType.TYPHOIDTHEORY;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.adapter.SearchHistoryAdapter;
import com.ketty.chinesemedicine.component.XEditText;
import com.ketty.chinesemedicine.databinding.ActivityDetailsBinding;
import com.ketty.chinesemedicine.databinding.ActivitySearchHistoryBinding;
import com.ketty.chinesemedicine.sqlite.HistorySQLiteOpenHelper;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.LogUtils;
import com.ketty.chinesemedicine.util.T;
import com.lihang.ShadowLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class SearchHistoryActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySearchHistoryBinding bind;
    private String title;
    private HistorySQLiteOpenHelper helper;
    private SQLiteDatabase db;
    private int type;
    private InputMethodManager imm;
    private final ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                String str = result.getData().getStringExtra("data");
                bind.etSearch.requestFocus();
                showSoftInput(bind.etSearch);
                bind.etSearch.setTextEx(str);
            }
        }
    });

    @Override
    protected View initLayout() {
        bind = ActivitySearchHistoryBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        bind.cancel.setOnClickListener(this);
        bind.ivClear.setOnClickListener(this);
        bind.slSearch.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");

        bind.etSearch.requestFocus();
        showSoftInput(bind.etSearch);
        bind.etSearch.setDisableEmoji(true);

        if (TextUtils.equals(title, "中药")) {
            type = CHINESEHERB;
            bind.etSearch.setHint("请输入中药名");
        } else if (TextUtils.equals(title, "方剂")) {
            type = PRESCRIPTION;
            bind.etSearch.setHint("请输入方剂名");
        } else if (TextUtils.equals(title, "中成药")) {
            type = CHINESEPATENTMEDICINE;
            bind.etSearch.setHint("请输入药名");
        } else if (TextUtils.equals(title, "药膳")) {
            type = MEDICATEDDIET;
            bind.etSearch.setHint("请输入药膳名称");
        } else if (TextUtils.equals(title, "伤寒论查阅")) {
            type = TYPHOIDTHEORY;
            bind.etSearch.setHint("请输入经方名或中药名");
        } else if (TextUtils.equals(title, "社区")) {
            type = COMMUNITY;
        } else if (TextUtils.equals(title, "商城")) {
            type = STORE;
        }

        LogUtils.i(String.valueOf(type));

        helper = new HistorySQLiteOpenHelper(SearchHistoryActivity.this);
        queryData("",true);

        bind.etSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String name = bind.etSearch.getTextTrimmed();
                    if (!TextUtils.isEmpty(name)) {
                        setItemClick(name);
                        boolean hasData = hasData(name);
                        if (!hasData) {
                            insertData(name);
                        }
                    } else {
                        T.showShort("请输入关键词");
                    }
                }
                return false;
            }
        });

        bind.etSearch.setOnXTextChangeListener(new XEditText.OnXTextChangeListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String name = bind.etSearch.getTextTrimmed();
                if (!TextUtils.isEmpty(name)) {
                    queryData(name,false);
                } else {
                    queryData("",true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.sl_search:
                bind.etSearch.requestFocus();
                showSoftInput(bind.etSearch);
                break;
            case R.id.iv_clear:
                deleteData();
                queryData("",true);
                break;
        }
    }

    private void initFlexboxLayout(List<String> tags) {
        bind.flexboxHistory.removeAllViews();
        for (int i = 0; i < tags.size(); i++) {
            String temp = tags.get(i);
            LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            rootParams.setMargins(0,DisplayUtil.dip2px(getContent(),10),
                    DisplayUtil.dip2px(getContent(),10),0);
            ShadowLayout shadowLayout = new ShadowLayout(getContent());
            shadowLayout.setLayoutParams(rootParams);
            shadowLayout.setPadding(DisplayUtil.dip2px(getContent(),15),
                    DisplayUtil.dip2px(getContent(),6),
                    DisplayUtil.dip2px(getContent(),15),
                    DisplayUtil.dip2px(getContent(),6));
            shadowLayout.setCornerRadius(DisplayUtil.dip2px(getContent(),50));
            shadowLayout.setStrokeColor(0xFFe6e6e6);
            TextView textView = new TextView(getContent());
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            textView.setTextSize(14);
            textView.setTextColor(0xFF333333);
            textView.setText(temp);
            shadowLayout.addView(textView);
            shadowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setItemClick(temp);
                }
            });
            bind.flexboxHistory.addView(shadowLayout);
        }
    }

    private void initRecyclerView(List<String> list) {
        bind.recyclerHistory.setLayoutManager(new LinearLayoutManager(this));
        SearchHistoryAdapter mAdapter = new SearchHistoryAdapter(list);
        bind.recyclerHistory.setAdapter(mAdapter);
        bind.recyclerHistory.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mAdapter.setOnItemClickListener(new SearchHistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String title) {
                setItemClick(title);
            }
        });
    }

    private void setItemClick(String title) {
        if (TextUtils.isEmpty(bind.etSearch.getTextEx())) {
            bind.etSearch.setTextEx(title);
        }
        Intent intent = new Intent(getContent(), SearchResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putInt("type", type);
        intent.putExtras(bundle);
        mActivityLauncher.launch(intent);
    }

    @SuppressLint("Range")
    private void queryData(String name, boolean isEmpty) {
        List<String> list = new ArrayList<>();
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select _id as id,name from info where type = '" + type + "' and name like '%" + name + "%' order by id desc", null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex("name"));
                list.add(name);
            }
            if (isEmpty) {
                bind.recyclerHistory.setVisibility(View.GONE);
                bind.llHistory.setVisibility(View.VISIBLE);
                initFlexboxLayout(list);
            } else {
                bind.llHistory.setVisibility(View.GONE);
                bind.recyclerHistory.setVisibility(View.VISIBLE);
                initRecyclerView(list);
            }
        } else {
            bind.llHistory.setVisibility(View.GONE);
            bind.recyclerHistory.setVisibility(View.GONE);
        }
        db.close();
        cursor.close();
    }

    private void deleteData() {
        db = helper.getWritableDatabase();
        db.execSQL("delete from info where type = '" + type + "'");
        db.close();
    }

    private boolean hasData(String name) {
        boolean hasData;
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from info where type = '" + type + "' and name = '" + name + "'", null);
        hasData = cursor.moveToNext();
        db.close();
        cursor.close();
        return hasData;
    }

    private void insertData(String name) {
        db = helper.getWritableDatabase();
        db.execSQL("insert into info(type,name) values('" + type + "','" + name + "')");
        db.close();
    }

    private Context getContent() {
        return SearchHistoryActivity.this;
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }

}