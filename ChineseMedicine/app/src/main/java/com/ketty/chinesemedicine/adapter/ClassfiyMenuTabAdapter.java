package com.ketty.chinesemedicine.adapter;

import java.util.List;

import q.rorbin.verticaltablayout.adapter.SimpleTabAdapter;
import q.rorbin.verticaltablayout.widget.QTabView;
import q.rorbin.verticaltablayout.widget.TabView;

public class ClassfiyMenuTabAdapter extends SimpleTabAdapter {
    List<String> menus;
    public ClassfiyMenuTabAdapter(List<String> menus) {
        this.menus = menus;
    }
    @Override
    public int getCount() {
        return menus.size();
    }
    @Override
    public TabView.TabBadge getBadge(int position) {
        return null;
    }
    @Override
    public TabView.TabIcon getIcon(int position) {
        return  null;
    }
    @Override
    public TabView.TabTitle getTitle(int position) {
        String classfiy = menus.get(position);
        //自定义Tab选择器的字体大小颜色
        return new QTabView.TabTitle.Builder()
                .setTextColor(0xFF000000,0xFF878787)
                .setTextSize(14)
                .setContent(classfiy)
                .build();
    }
    @Override
    public int getBackground(int position) {
        return -1;
    }
}