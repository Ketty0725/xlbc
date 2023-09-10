package com.ketty.chinesemedicine.adapter;

import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEPATENTMEDICINE;
import static com.ketty.chinesemedicine.main.search.SearchType.MEDICATEDDIET;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.entity.Medicateddiet;
import com.ketty.chinesemedicine.entity.Prescription;
import com.lihang.ShadowLayout;

import java.util.List;

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.ViewHolder> {
    private int type;
    private List<?> mList;

    public ListCategoryAdapter(int type, List<?> list) {
        this.type = type;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_list_category,parent,false),
                mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (type == MEDICATEDDIET) {
            holder.mTvProvenance.setVisibility(View.VISIBLE);
            holder.mTvName.setText(((Medicateddiet) mList.get(position)).getMedicatedDietName());
            holder.mTvProvenance.setText(((Medicateddiet) mList.get(position)).getDerivation());
        } else if (type == CHINESEPATENTMEDICINE) {
            holder.mTvProvenance.setVisibility(View.GONE);
            holder.mTvName.setText(((String) mList.get(position)));
        } else {
            holder.mTvName.setText(((Prescription) mList.get(position)).getPrescriptionName());
            holder.mTvProvenance.setText(((Prescription) mList.get(position)).getProvenance());
        }
        if (position < mList.size() - 1) {
            holder.mView.setVisibility(View.VISIBLE);
        } else {
            holder.mView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTvName, mTvProvenance;
        ShadowLayout mShadowLayout;
        View mView;
        OnItemClickListener mOnItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            this.mOnItemClickListener = listener;
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvProvenance = itemView.findViewById(R.id.tv_provenance);
            mShadowLayout = itemView.findViewById(R.id.shadow_layout);
            mView = itemView.findViewById(R.id.view);

            mShadowLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mTvName.getText().toString());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String title);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
