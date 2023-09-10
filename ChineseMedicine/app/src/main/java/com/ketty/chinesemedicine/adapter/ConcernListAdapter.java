package com.ketty.chinesemedicine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.entity.User;
import com.ketty.chinesemedicine.entity.enums.ConcernEnum;
import com.lihang.ShadowLayout;

import java.util.ArrayList;
import java.util.List;

public class ConcernListAdapter extends RecyclerView.Adapter<ConcernListAdapter.ViewHolder> {
    private List<User> users = new ArrayList<>();
    private List<Integer> states = new ArrayList<>();

    public void setData(List<User> users, List<Integer> states) {
        this.users = users;
        this.states = states;
        notifyDataSetChanged();
    }

    public void setConcerned(int state, int position) {
        states.set(position, state);
        notifyItemChanged(position);
    }

    public void removeItem(int position) {
        users.remove(position);
        states.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_concern_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RequestOptions options = RequestOptions.circleCropTransform();
        Glide.with(holder.itemView.getContext())
                .load(users.get(position).getuHeadicon())
                .apply(options)
                .into(holder.mIvHead);
        holder.mTvName.setText(users.get(position).getuName());
        holder.mTvAbout.setText(users.get(position).getuAbout());

        if (users.get(position).getuId().equals(Long.parseLong(App.getMmkvUtil().getString("user","uId",null)))) {
            holder.mSlConcern.setVisibility(View.GONE);
            holder.mTvMineTag.setVisibility(View.VISIBLE);
        } else {
            holder.mSlConcern.setVisibility(View.VISIBLE);
            holder.mTvMineTag.setVisibility(View.GONE);
            if (states.get(position).equals(ConcernEnum.NONE.getCode()) ||
                    states.get(position).equals(ConcernEnum.OPPOSITE.getCode())) {
                holder.mSlConcern.setStrokeColor(0xFFff2442);
                holder.mTvConcern.setTextColor(0xFFff2442);
                if (states.get(position).equals(ConcernEnum.NONE.getCode())) {
                    holder.mTvConcern.setText("关注");
                } else if (states.get(position).equals(ConcernEnum.OPPOSITE.getCode())) {
                    holder.mTvConcern.setText("回粉");
                }
            } else if (states.get(position).equals(ConcernEnum.ALIKE.getCode()) ||
                    states.get(position).equals(ConcernEnum.ALL.getCode())) {
                holder.mSlConcern.setStrokeColor(0xFFcccccc);
                holder.mTvConcern.setTextColor(0xFF999999);
                if (states.get(position).equals(ConcernEnum.ALIKE.getCode())) {
                    holder.mTvConcern.setText("已关注");
                } else if (states.get(position).equals(ConcernEnum.ALL.getCode())) {
                    holder.mTvConcern.setText("互相关注");
                }
            }
        }

        if (position < users.size() - 1) {
            holder.view.setVisibility(View.VISIBLE);
        } else {
            holder.view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mIvHead;
        TextView mTvName, mTvAbout, mTvMineTag, mTvConcern;
        View view;
        ShadowLayout mShadowLayout, mSlConcern;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvHead = itemView.findViewById(R.id.iv_head);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvAbout = itemView.findViewById(R.id.tv_about);
            mTvMineTag = itemView.findViewById(R.id.tv_mine_tag);
            mTvConcern = itemView.findViewById(R.id.tv_concern);
            mSlConcern = itemView.findViewById(R.id.sl_concern);
            view = itemView.findViewById(R.id.view);
            mShadowLayout = itemView.findViewById(R.id.shadow_layout);

            mShadowLayout.setOnClickListener(this);
            mSlConcern.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.shadow_layout:
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(users.get(getLayoutPosition()).getuId());
                    }
                    break;
                case R.id.sl_concern:
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onConcernClick(
                                users.get(getLayoutPosition()).getuId(),
                                states.get(getLayoutPosition()), getLayoutPosition());
                    }
                    break;
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Long userId);
        void onConcernClick(Long userId, int state, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
