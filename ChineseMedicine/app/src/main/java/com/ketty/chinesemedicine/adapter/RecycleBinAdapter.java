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
import com.ketty.chinesemedicine.component.SmoothCheckBox;
import com.ketty.chinesemedicine.entity.Community;
import com.ketty.chinesemedicine.entity.Communityimage;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.util.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecycleBinAdapter extends RecyclerView.Adapter<RecycleBinAdapter.ViewHolder> {
    private List<Community> communities = new ArrayList<>();
    private List<Community> communitySelected = new ArrayList<>();
    private List<Communityimage> images = new ArrayList<>();
    private List<User> users = new ArrayList<>();
    private boolean state = false;
    private HashMap<Integer, Boolean> isCheckedHasMap = new HashMap<>();

    public void changeState(boolean state) {
        this.state = state;
        notifyDataSetChanged();
    }

    public void setData(List<Community> communities,
                        List<Communityimage> images,
                        List<User> users) {
        this.communities = communities;
        this.images = images;
        this.users = users;
        notifyDataSetChanged();
    }

    public void removeItem(Community community) {
        int position = communities.indexOf(community);
        communities.remove(position);
        images.remove(position);
        users.remove(position);
        mOnItemClickListener.onAllChecked(false);
        communitySelected.remove(community);
        mOnItemClickListener.onFinished(communitySelected);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_community, parent, false));
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

        if (state) {
            holder.mScb.setVisibility(View.VISIBLE);
            holder.mScb.setChecked(isCheckedHasMap.get(position),false);
        } else {
            holder.mScb.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return communities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTvTitle, mTvUserName;
        ImageView mIvCover, mHeadIcon;
        SmoothCheckBox mScb;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv_title);
            mTvUserName = itemView.findViewById(R.id.tv_user_name);
            mIvCover = itemView.findViewById(R.id.iv_cover);
            mHeadIcon = itemView.findViewById(R.id.head_icon);
            mScb = itemView.findViewById(R.id.scb);

            itemView.setOnClickListener(this);
            mScb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setChecked();
                }
            });

            for (int i = 0; i < communities.size(); i++) {
                isCheckedHasMap.put(i, false);
            }
        }

        private void setChecked() {
            isCheckedHasMap.put(getLayoutPosition(), !isCheckedHasMap.get(getLayoutPosition()));
            notifyDataSetChanged();

            boolean allChecked = true;
            Set<Map.Entry<Integer, Boolean>> entries = isCheckedHasMap.entrySet();
            for (Map.Entry<Integer, Boolean> entryset : entries) {
                if (!entryset.getValue()) {
                    allChecked = false;
                }
            }
            mOnItemClickListener.onAllChecked(allChecked);

            if (isCheckedHasMap.get(getLayoutPosition())) {
                communitySelected.add(communities.get(getLayoutPosition()));
            } else {
                communitySelected.remove(communities.get(getLayoutPosition()));
            }
            mOnItemClickListener.onFinished(communitySelected);
        }

        @Override
        public void onClick(View view) {
            if (state) {
                setChecked();
            } else {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(communities.get(getLayoutPosition()).getId());
                }
            }
        }
    }

    public void selectedAll() {
        Set<Map.Entry<Integer, Boolean>> entries = isCheckedHasMap.entrySet();
        boolean shouldSelectedAll = false;
        for (Map.Entry<Integer, Boolean> entryset : entries) {
            Boolean aBoolean = entryset.getValue();
            if (!aBoolean) {
                shouldSelectedAll = true;
                break;
            }
        }
        for (Map.Entry<Integer, Boolean> entryset : entries) {
            entryset.setValue(shouldSelectedAll);
        }
        communitySelected.clear();
        communitySelected.addAll(communities);
        mOnItemClickListener.onFinished(communitySelected);
        notifyDataSetChanged();
    }

    public void revertSelected() {
        Set<Map.Entry<Integer, Boolean>> entries = isCheckedHasMap.entrySet();
        for (Map.Entry<Integer, Boolean> entryset : entries) {
            entryset.setValue(!entryset.getValue());
        }
        communitySelected.clear();
        mOnItemClickListener.onFinished(communitySelected);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(long id);
        void onAllChecked(boolean checked);
        void onFinished(List<Community> communitySelected);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}

