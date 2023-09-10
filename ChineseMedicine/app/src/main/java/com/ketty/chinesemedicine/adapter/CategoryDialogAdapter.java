package com.ketty.chinesemedicine.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ketty.chinesemedicine.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

public class CategoryDialogAdapter extends RecyclerView.Adapter<CategoryDialogAdapter.ViewHolder> {
    private List<String> mList = new ArrayList<>();

    public CategoryDialogAdapter(List<String> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = new TextView(parent.getContext());
        textView.setTextSize(12);
        textView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(textView,mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(mList.get(position));
        if (position < mList.size() - 1) {
            holder.textView.setPadding(DisplayUtil.dip2px(holder.itemView.getContext(),10),
                    DisplayUtil.dip2px(holder.itemView.getContext(),15),
                    DisplayUtil.dip2px(holder.itemView.getContext(),10),
                    DisplayUtil.dip2px(holder.itemView.getContext(),20));
        } else {
            holder.textView.setPadding(DisplayUtil.dip2px(holder.itemView.getContext(),10),
                    DisplayUtil.dip2px(holder.itemView.getContext(),15),
                    DisplayUtil.dip2px(holder.itemView.getContext(),10),
                    DisplayUtil.dip2px(holder.itemView.getContext(),15));
        }

        if (position == getmPosition()) {
            holder.textView.setTextColor(0xFF5abec0);
        } else {
            holder.textView.setTextColor(0xFF6b6a6f);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView;
        OnItemClickListener mOnItemClickListener;

        public ViewHolder(@NonNull TextView view, OnItemClickListener listener) {
            super(view);
            this.mOnItemClickListener = listener;
            this.textView = view;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view,getLayoutPosition());
                Log.i("info", String.valueOf(getLayoutPosition()));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    private int mPosition;

    public int getmPosition() {
        return mPosition;
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }
}
