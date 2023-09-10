package com.ketty.chinesemedicine.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.entity.FlowCategory;

import java.util.ArrayList;
import java.util.List;

public class FlowCategoryFatherAdapter extends RecyclerView.Adapter<FlowCategoryFatherAdapter.ViewHolder> {
    private List<FlowCategory> list = new ArrayList<>();

    public FlowCategoryFatherAdapter(List<FlowCategory> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_flow_category_father,parent,false),
                mOnItemClickListener,parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getFatherTitle());

        FlowCategoryChildAdapter childAdapter = new FlowCategoryChildAdapter(list.get(position).getChildList());
        holder.recyclerView.setAdapter(childAdapter);
        childAdapter.setOnItemClickListener(new FlowCategoryChildAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String tag) {
                mOnItemClickListener.onItemClick(tag);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        ImageView imageView;
        RecyclerView recyclerView;
        OnItemClickListener mOnItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener, Context context) {
            super(itemView);
            this.mOnItemClickListener = listener;
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
            recyclerView = itemView.findViewById(R.id.recycler_view);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            recyclerView.setNestedScrollingEnabled(false);

            imageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onTitleClick(textView.getText().toString());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String tag);
        void onTitleClick(String title);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
