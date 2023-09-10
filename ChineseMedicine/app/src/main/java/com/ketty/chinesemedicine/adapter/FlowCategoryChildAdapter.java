package com.ketty.chinesemedicine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.flexbox.FlexboxLayout;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.entity.FlowCategory;
import com.lihang.ShadowLayout;

import java.util.ArrayList;
import java.util.List;

public class FlowCategoryChildAdapter extends RecyclerView.Adapter<FlowCategoryChildAdapter.ViewHolder> {
    private List<FlowCategory.ChildCategory> list = new ArrayList<>();

    public FlowCategoryChildAdapter(List<FlowCategory.ChildCategory> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_flow_category_child,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTitle.setText(list.get(position).getChildTitle());

        List<String> tags = list.get(position).getTagList();
        holder.mFlexboxLayout.removeAllViews();
        // flexbox布局动态添加标签
        for (int i = 0; i < tags.size(); i++) {
            String temp = tags.get(i);
            View tagView = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.item_tag_cell, null, false);
            TextView tag = tagView.findViewById(R.id.tv_tag);
            tag.setText(temp);
            // 设置标签点击事件
            ShadowLayout slTag = tagView.findViewById(R.id.sl_tag);
            slTag.setOnClickListener(view -> mOnItemClickListener.onItemClick(temp));
            holder.mFlexboxLayout.addView(tagView);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        FlexboxLayout mFlexboxLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.tv_title);
            mFlexboxLayout = itemView.findViewById(R.id.flexbox_layout);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String tag);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
