package com.ketty.chinesemedicine.main.store;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.entity.Productcategory;
import com.lihang.ShadowLayout;

import java.util.List;

/**
 * @Auther: Ketty Allen
 * @Date:2022/10/19 - 10:18
 * @version: 1.0
 */
public class StoreTabLayout extends TabLayout {

    public StoreTabLayout(Context context) {
        super(context);
        init();
    }

    public StoreTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StoreTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.addOnTabSelectedListener(new OnTabSelectedListener() {

            @Override
            public void onTabSelected(Tab tab) {
                if (tab != null && tab.getCustomView() != null) {
                    TextView text = tab.getCustomView().findViewById(R.id.tab_layout_text);
                    text.setTextColor(0xFFffffff);
                    text.setBackgroundResource(R.drawable.store_tablayout_item_pressed);
                    ShadowLayout slImage = tab.getCustomView().findViewById(R.id.shadow_img);
                    slImage.setStrokeColor(0xFF53b65f);
                }
            }

            @Override
            public void onTabUnselected(Tab tab) {
                if (tab != null && tab.getCustomView() != null) {
                    TextView text = tab.getCustomView().findViewById(R.id.tab_layout_text);
                    text.setTextColor(0xFF666666);
                    text.setBackgroundResource(0);
                    ShadowLayout slImage = tab.getCustomView().findViewById(R.id.shadow_img);
                    slImage.setStrokeColor(0xFFffffff);
                }
            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
    }

    public void setData(List<Productcategory> list) {
        for (Productcategory bean : list) {
            Tab tab = newTab();
            tab.setCustomView(R.layout.store_tablayout_item);

            if (tab.getCustomView() != null) {
                TextView text = tab.getCustomView().findViewById(R.id.tab_layout_text);
                text.setText(bean.getCategoryName());
                ImageView image = tab.getCustomView().findViewById(R.id.tab_layout_image);
                Glide.with(getContext()).load(bean.getCategoryIcon()).into(image);
            }
            this.addTab(tab);
        }

    }

}

