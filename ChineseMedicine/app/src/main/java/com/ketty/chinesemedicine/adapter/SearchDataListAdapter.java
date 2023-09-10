package com.ketty.chinesemedicine.adapter;

import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEHERB;
import static com.ketty.chinesemedicine.main.search.SearchType.CHINESEPATENTMEDICINE;
import static com.ketty.chinesemedicine.main.search.SearchType.MEDICATEDDIET;
import static com.ketty.chinesemedicine.main.search.SearchType.PRESCRIPTION;
import static com.ketty.chinesemedicine.main.search.SearchType.TYPHOIDTHEORY;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.entity.ChineseherbAndImages;
import com.ketty.chinesemedicine.entity.Medicateddiet;
import com.ketty.chinesemedicine.entity.NameAndType;
import com.ketty.chinesemedicine.entity.Prescription;
import com.lihang.ShadowLayout;

import java.util.List;

public class SearchDataListAdapter extends RecyclerView.Adapter<SearchDataListAdapter.ViewHolder> {
    private int type;
    private List<?> mList;

    public SearchDataListAdapter(int type, List<?> list) {
        this.type = type;
        this.mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_search_data_list,parent,false),
                mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (type) {
            case CHINESEHERB:
                holder.mIvPreview.setVisibility(View.VISIBLE);
                holder.mTvAttach.setVisibility(View.VISIBLE);
                Glide.with(holder.itemView.getContext())
                        .load(((ChineseherbAndImages) mList.get(position)).getImages().get(0))
                        .into(holder.mIvPreview);
                holder.mTvName.setText(((ChineseherbAndImages) mList.get(position)).getChineseherb().getMedicinalName());
                holder.mTvAttach.setText(((ChineseherbAndImages) mList.get(position)).getChineseherb().getPinYin());
                break;
            case PRESCRIPTION:
                holder.mIvPreview.setVisibility(View.GONE);
                holder.mTvAttach.setVisibility(View.VISIBLE);
                holder.mTvName.setText(((Prescription) mList.get(position)).getPrescriptionName());
                holder.mTvAttach.setText(((Prescription) mList.get(position)).getProvenance());
                break;
            case CHINESEPATENTMEDICINE:
                holder.mIvPreview.setVisibility(View.GONE);
                holder.mTvAttach.setVisibility(View.GONE);
                holder.mTvName.setText(((String) mList.get(position)));
                break;
            case MEDICATEDDIET:
                holder.mIvPreview.setVisibility(View.GONE);
                holder.mTvAttach.setVisibility(View.VISIBLE);
                holder.mTvName.setText(((Medicateddiet) mList.get(position)).getMedicatedDietName());
                holder.mTvAttach.setText(((Medicateddiet) mList.get(position)).getDerivation());
                break;
            case TYPHOIDTHEORY:
                holder.mIvPreview.setVisibility(View.GONE);
                holder.mTvAttach.setVisibility(View.GONE);
                holder.mTvName.setText(((NameAndType) mList.get(position)).getName());
                holder.mTvType.setText(String.valueOf(((NameAndType) mList.get(position)).getType()));
                break;
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
        TextView mTvName, mTvAttach, mTvType;
        ImageView mIvPreview;
        ShadowLayout mShadowLayout;
        View mView;
        OnItemClickListener mOnItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            this.mOnItemClickListener = listener;
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvAttach = itemView.findViewById(R.id.tv_attach);
            mIvPreview = itemView.findViewById(R.id.iv_preview);
            mShadowLayout = itemView.findViewById(R.id.shadow_layout);
            mView = itemView.findViewById(R.id.view);
            mTvType = itemView.findViewById(R.id.tv_type);

            mShadowLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(mTvName.getText().toString(), Integer.parseInt(mTvType.getText().toString()));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String title, int markType);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
