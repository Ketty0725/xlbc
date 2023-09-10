package com.ketty.chinesemedicine.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.entity.Community;
import com.ketty.chinesemedicine.entity.Communityimage;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Ketty Allen
 * @Date:2022/5/30 - 23:14
 * @version: 1.0
 */
public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.ViewHolder> {
    private List<Community> communities = new ArrayList<>();
    private List<Communityimage> images = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public void setData(List<Community> communities,
                        List<Communityimage> images,
                        List<User> users,
                        int position) {
        this.communities.addAll(communities);
        this.images.addAll(images);
        this.users.addAll(users);
        notifyItemRangeInserted(position, communities.size());
    }

    public void clearData() {
        int count = communities.size();
        communities.clear();
        images.clear();
        users.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        communities.remove(position);
        images.remove(position);
        users.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_community, parent, false);
        ViewHolder mViewHolder = new ViewHolder(itemView,mOnItemClickListener);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mTvTitle.setText(communities.get(position).getTitle());
        holder.mTvUserName.setText(users.get(position).getuName());

        RequestOptions requestOptions = new RequestOptions()
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .skipMemoryCache(false)
                .placeholder(com.luck.picture.lib.R.drawable.ps_image_placeholder);

        Glide.with(holder.itemView.getContext())
                .load(users.get(position).getuHeadicon().trim())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.mHeadIcon);

        Glide.with(holder.itemView.getContext())
                .asBitmap()
                .load(images.get(position).getImageUrl().trim())
                .apply(requestOptions)
                .into(new SimpleTarget<Bitmap>() {
                    public void onResourceReady(@NonNull Bitmap bitmap, @Nullable Transition<? super Bitmap> transition) {
                        ViewGroup.LayoutParams layoutParams = holder.mIvCover.getLayoutParams();
                        layoutParams.height = (int) (((float) bitmap.getHeight()) / bitmap.getWidth() *
                                (ScreenUtils.getScreenWidth(holder.itemView.getContext())) / 2);
                        layoutParams.width = (ScreenUtils.getScreenWidth(holder.itemView.getContext())) / 2;
                        holder.mIvCover.setLayoutParams(layoutParams);
                        holder.mIvCover.setImageBitmap(bitmap);
                    }
                });

    }

    /**
     * 重写 避免滑动过程界面混乱
     */
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return communities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        TextView mTvTitle, mTvUserName;
        ImageView mIvCover, mHeadIcon;
        private OnItemClickListener mOnItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            this.mOnItemClickListener = listener;
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvUserName = itemView.findViewById(R.id.tv_user_name);
            mIvCover = itemView.findViewById(R.id.iv_cover);
            mHeadIcon = itemView.findViewById(R.id.head_icon);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(communities.get(getLayoutPosition()).getId());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemLongClick(getLayoutPosition(),
                        communities.get(getLayoutPosition()).getId());
            }
            return true;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(long id);
        void onItemLongClick(int pos, long id);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}

