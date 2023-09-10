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
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.component.SmoothCheckBox;
import com.ketty.chinesemedicine.entity.Product;
import com.ketty.chinesemedicine.entity.Productshopcart;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.T;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SettlementAdapter extends RecyclerView.Adapter<SettlementAdapter.ViewHolder> {
    private List<Productshopcart> list = new ArrayList<>();
    private float totalPrice = 0.00f;
    private List<Product> products = new ArrayList<>();

    public SettlementAdapter(List<Productshopcart> list, List<Product> products) {
        this.list = list;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_shopping_cart,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext())
                .load(products.get(position).getImage()).into(holder.mIvThumbnail);
        holder.mTvName.setText(products.get(position).getName());


        String price = "￥" + products.get(position).getPrice();
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

        holder.mTvNumber.setText("x"+list.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SmoothCheckBox mCheckBox;
        ImageView mIvThumbnail, mIvSubtract, mIvAdd;
        TextView mTvName, mTvPrice, mTvNumber;
        View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            mCheckBox = itemView.findViewById(R.id.scb);
            mIvThumbnail = itemView.findViewById(R.id.iv_thumbnail);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvPrice = itemView.findViewById(R.id.tv_price);
            mIvSubtract = itemView.findViewById(R.id.iv_subtract);
            mIvAdd = itemView.findViewById(R.id.iv_add);
            mTvNumber = itemView.findViewById(R.id.tv_number);

            mCheckBox.setVisibility(View.GONE);
            mIvSubtract.setVisibility(View.INVISIBLE);
            mIvAdd.setVisibility(View.INVISIBLE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(list.get(getLayoutPosition()).getProductId());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Long productId);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
