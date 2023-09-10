package com.ketty.chinesemedicine.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.entity.Communityimage;

import java.util.ArrayList;
import java.util.List;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ViewHolder> {
    private List<Communityimage> imgList = new ArrayList<>();

    public void setData(List<Communityimage> imgList) {
        if (null != imgList) {
            this.imgList.clear();
            this.imgList.addAll(imgList);
            notifyDataSetChanged();
        }
    }

    public List<Communityimage> getImgList() {
        return imgList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.details_image_item,parent,false),mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = imgList.get(position).getImageUrl().trim();
        Log.i("url",url);

        RequestOptions requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .placeholder(com.luck.picture.lib.R.drawable.ps_image_placeholder);

        Glide.with(holder.itemView.getContext())
                .load(url)
                .apply(requestOptions)
                .into(holder.mIvDetails);
    }

    @Override
    public int getItemCount() {
        return imgList.size() <= 0 ? 0 : imgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mIvDetails;
        OnItemClickListener mOnItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            this.mOnItemClickListener = listener;
            mIvDetails = itemView.findViewById(R.id.iv_details);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view,getLayoutPosition());
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

}
