package com.ketty.chinesemedicine.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.entity.Product;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
    private List<Product> list = new ArrayList<>();

    public StoreAdapter(List<Product> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.store_list_item,parent,false),
                mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView).load(list.get(position).getImage()).into(holder.mIvThumbnail);
        holder.mTvName.setText(list.get(position).getName());

        String text = String.valueOf(list.get(position).getPrice());
        if (text.contains(".")) {
            List<TextColorSizeHelper.SpanInfo> spanInfos = new ArrayList<TextColorSizeHelper.SpanInfo>();
            spanInfos.add(
                    new TextColorSizeHelper.SpanInfo(
                            text.substring(0,text.indexOf(".")),
                            DisplayUtil.dip2px(holder.itemView.getContext(),18),
                            Color.parseColor("#FF6E00"),
                            false
                    )
            );
            spanInfos.add(
                    new TextColorSizeHelper.SpanInfo(
                            text.substring(text.indexOf(".")),
                            DisplayUtil.dip2px(holder.itemView.getContext(),14),
                            Color.parseColor("#FF6E00"),
                            false
                    )
            );
            holder.mTvPrice.setText(TextColorSizeHelper.getTextSpan(holder.itemView.getContext(), text, spanInfos));
        } else {
            holder.mTvPrice.setText(text);
        }

    }

    @Override
    public int getItemCount() {
        return list.size() > 0 ? list.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClickListener mOnItemClickListener;
        ImageView mIvThumbnail, mIvShopCart;
        TextView mTvName, mTvPrice;

        public ViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            this.mOnItemClickListener = listener;
            mIvThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvPrice = itemView.findViewById(R.id.tv_price);
            mIvShopCart = itemView.findViewById(R.id.iv_shop_cart);

            itemView.setOnClickListener(this);
            mIvShopCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.addShopCart(list.get(getLayoutPosition()).getId());
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(list.get(getLayoutPosition()).getId());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Long id);
        void addShopCart(Long productId);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
