package com.ketty.chinesemedicine.main.search;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.ketty.chinesemedicine.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Ketty Allen
 * @Date:2022/10/19 - 10:18
 * @version: 1.0
 */
public class SearchTabLayout extends TabLayout {
    private List<String> titles;

    public SearchTabLayout(Context context) {
        super(context);
        init();
    }

    public SearchTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        titles = new ArrayList<>();

        this.addOnTabSelectedListener(new OnTabSelectedListener() {

            @Override
            public void onTabSelected(Tab tab) {
                /**
                 * 设置当前选中的Tab为特殊高亮样式。
                 */
                if (tab != null && tab.getCustomView() != null) {
                    TextView tab_layout_text = tab.getCustomView().findViewById(R.id.tab_layout_text);

                    tab_layout_text.setTextColor(0xFF333333);
                    tab_layout_text.setBackgroundResource(R.drawable.tablayout_item_pressed);
                }
            }

            @Override
            public void onTabUnselected(Tab tab) {
                /**
                 * 重置所有未选中的Tab颜色、字体、背景恢复常态(未选中状态)。
                 */
                if (tab != null && tab.getCustomView() != null) {
                    TextView tab_layout_text = tab.getCustomView().findViewById(R.id.tab_layout_text);

                    tab_layout_text.setTextColor(0xFF999999);
                    tab_layout_text.setBackgroundResource(0);
                }
            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
    }

    public void setTitle(List<String> titles) {
        this.titles = titles;

        for (String title : this.titles) {
            Tab tab = newTab();
            tab.setCustomView(R.layout.tablayout_item);

            if (tab.getCustomView() != null) {
                TextView text = tab.getCustomView().findViewById(R.id.tab_layout_text);
                text.setText(title);
                text.setBackgroundResource(0);
            }

            this.addTab(tab);
        }

    }
}

