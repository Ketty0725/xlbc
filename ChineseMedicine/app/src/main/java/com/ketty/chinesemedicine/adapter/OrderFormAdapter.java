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
import com.ketty.chinesemedicine.entity.Orderform;
import com.ketty.chinesemedicine.entity.Product;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;
import com.lihang.ShadowLayout;

import java.util.ArrayList;
import java.util.List;

public class OrderFormAdapter extends RecyclerView.Adapter<OrderFormAdapter.ViewHolder> {
    private List<Orderform> list = new ArrayList<>();
    private List<Product> products = new ArrayList<>();

    public void setData(List<Orderform> list, List<Product> products) {
        this.list = list;
        this.products = products;
        notifyDataSetChanged();
    }

    public void update(List<Orderform> list, List<Product> products) {
        this.list = list;
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_order_form,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(products.get(position).getImage()).into(holder.mIvThumbnail);
        holder.mTvName.setText(products.get(position).getName());
        String price = "￥" + list.get(position).getPrice();
        List<TextColorSizeHelper.SpanInfo> spanInfos = new ArrayList<TextColorSizeHelper.SpanInfo>();
        spanInfos.add(
                new TextColorSizeHelper.SpanInfo(
                        price.substring(0, 1),
                        DisplayUtil.dip2px(holder.itemView.getContext(), 14),
                        Color.parseColor("#333333"),
                        false
                )
        );
        spanInfos.add(
                new TextColorSizeHelper.SpanInfo(
                        price.substring(1, price.indexOf(".")),
                        DisplayUtil.dip2px(holder.itemView.getContext(), 18),
                        Color.parseColor("#333333"),
                        false
                )
        );
        spanInfos.add(
                new TextColorSizeHelper.SpanInfo(
                        price.substring(price.indexOf(".")),
                        DisplayUtil.dip2px(holder.itemView.getContext(), 14),
                        Color.parseColor("#333333"),
                        false
                )
        );
        holder.mTvPrice.setText(TextColorSizeHelper.getTextSpan(holder.itemView.getContext(), price, spanInfos));
        holder.mTvNumber.setText("共" + list.get(position).getNumber() + "件");

        switch (list.get(position).getState()) {
            case 0:
                holder.mSlButton.setLayoutBackground(0xFFFFFFFF);
                holder.mSlButton.setStrokeColor(0xFFCDCDCD);
                holder.mTvButton.setText("取消订单");
                holder.mTvButton.setTextColor(0xFF333333);
                holder.mTvState.setText("待发货");
                holder.mTvState.setTextColor(0xFFff2442);
                holder.mSlButton.setOnClickListener(view -> {
                    mOnItemClickListener.onAlterState(list.get(position),0,"确认取消该订单？","取消订单成功");
                });
                break;
            case 1:
                holder.mSlButton.setLayoutBackground(0xFFff2442);
                holder.mSlButton.setStrokeColor(0xFFff2442);
                holder.mTvButton.setText("确认收货");
                holder.mTvButton.setTextColor(0xFFFFFFFF);
                holder.mTvState.setText("待收货");
                holder.mTvState.setTextColor(0xFFff2442);
                holder.mSlButton.setOnClickListener(view -> {
                    mOnItemClickListener.onAlterState(list.get(position),2,"确认已经收到商品？","确认收货成功");
                });
                break;
            case 2:
                holder.mSlButton.setLayoutBackground(0xFFFFFFFF);
                holder.mSlButton.setStrokeColor(0xFFCDCDCD);
                holder.mTvButton.setText("删除订单");
                holder.mTvButton.setTextColor(0xFF333333);
                holder.mTvState.setText("已完成");
                holder.mTvState.setTextColor(0xFF999999);
                holder.mSlButton.setOnClickListener(view -> {
                    mOnItemClickListener.onAlterState(list.get(position),-1,"确认删除该订单？","删除订单成功");
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvThumbnail;
        TextView mTvName, mTvPrice, mTvNumber, mTvButton, mTvState;
        ShadowLayout mSlButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvPrice = itemView.findViewById(R.id.tv_price);
            mTvNumber = itemView.findViewById(R.id.tv_number);
            mTvButton = itemView.findViewById(R.id.tv_button);
            mSlButton = itemView.findViewById(R.id.sl_button);
            mTvState = itemView.findViewById(R.id.tv_state);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(list.get(getLayoutPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Orderform bean);
        void onAlterState(Orderform bean, Integer newState, String msg1, String msg2);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
