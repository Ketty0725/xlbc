package com.ketty.chinesemedicine.main.search;

import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEHERB;
import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEPATENTMEDICINE;
import static com.ketty.chinesemedicine.main.search.SearchType.COMMUNITY;
import static com.ketty.chinesemedicine.main.search.SearchType.MEDICATEDDIET;
import static com.ketty.chinesemedicine.main.search.SearchType.PRESCRIPTION;
import static com.ketty.chinesemedicine.main.search.SearchType.STORE;
import static com.ketty.chinesemedicine.main.search.SearchType.TYPHOIDTHEORY;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.databinding.ActivityDetailsBinding;
import com.ketty.chinesemedicine.databinding.ActivitySearchResultBinding;
import com.ketty.chinesemedicine.util.LogUtils;

public class SearchResultActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySearchResultBinding bind;
    private FragmentManager fm;
    private String title;
    private int type;

    @Override
    protected View initLayout() {
        bind = ActivitySearchResultBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        fm = getSupportFragmentManager();

        bind.ivBack.setOnClickListener(this);
        bind.slSearch.setOnClickListener(this);
        bind.ivClear.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        title = bundle.getString("title");
        type = bundle.getInt("type");

        bind.tvSearch.setText(title);

        initTabLayout();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.iv_clear:
                setResultCallback("");
                break;
            case R.id.sl_search:
                setResultCallback(bind.tvSearch.getText().toString());
                break;
        }
    }

    private void setResultCallback(String data) {
        Intent intent = new Intent();
        intent.putExtra("data",data);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void initTabLayout() {
        if (type == CHINESEHERB || type == PRESCRIPTION
                || type == CHINESEPATENTMEDICINE || type == MEDICATEDDIET || type == TYPHOIDTHEORY) {
            bind.tabLayout.getTabAt(0).select();
            initFragment(SearchToDataFragment.newInstance(type,title));
        } else if (type == COMMUNITY) {
            bind.tabLayout.getTabAt(1).select();
            initFragment(SearchToCommunityFragment.newInstance(title));
        } else if (type == STORE) {
            bind.tabLayout.getTabAt(2).select();
            initFragment(SearchToStoreFragment.newInstance(title));
        }

        LogUtils.i(String.valueOf(type));

        bind.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    initFragment(SearchToDataFragment.newInstance(type,title));
                } else if (tab.getPosition() == 1) {
                    initFragment(SearchToCommunityFragment.newInstance(title));
                } else if (tab.getPosition() == 2) {
                    initFragment(SearchToStoreFragment.newInstance(title));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initFragment(Fragment fragment) {
        if (fragment != null) {
            fm.beginTransaction().replace(R.id.frame_layout, fragment).commit();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            setResultCallback("");
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        setResultCallback("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}