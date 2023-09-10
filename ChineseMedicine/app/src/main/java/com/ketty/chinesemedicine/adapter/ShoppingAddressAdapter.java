package com.ketty.chinesemedicine.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.component.SmoothCheckBox;
import com.ketty.chinesemedicine.entity.Product;
import com.ketty.chinesemedicine.entity.Productshopcart;
import com.ketty.chinesemedicine.entity.Shoppingaddress;
import com.ketty.chinesemedicine.util.T;

import java.util.ArrayList;
import java.util.List;

public class ShoppingAddressAdapter extends RecyclerView.Adapter<ShoppingAddressAdapter.ViewHolder> {
    private List<Shoppingaddress> list = new ArrayList<>();

    public ShoppingAddressAdapter(List<Shoppingaddress> list) {
        this.list = list;
    }

    public void update(List<Shoppingaddress> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void delete(Shoppingaddress bean) {
        int position = list.indexOf(bean);
        list.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(
                parent.getContext()).inflate(R.layout.item_shipping_address,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mTvName.setText(list.get(position).getName());
        holder.mTvPhone.setText(list.get(position).getPhone());
        holder.mTvAddress.setText(list.get(position).getArea() + " " + list.get(position).getAddress());
        if (list.get(position).getIsDefault() == 1) {
            holder.mIvDefault.setVisibility(View.VISIBLE);
            holder.mTvFirstName.setVisibility(View.GONE);
            holder.mIvSign.setImageResource(R.drawable.ic_default_address);
        } else {
            holder.mIvDefault.setVisibility(View.GONE);
            holder.mTvFirstName.setVisibility(View.VISIBLE);
            holder.mTvFirstName.setText(list.get(position).getName().substring(0,1));
            holder.mIvSign.setImageResource(R.drawable.ic_circle);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mIvDefault, mIvSign, mIvAlter;
        RelativeLayout mRlSign;
        TextView mTvFirstName, mTvName, mTvPhone, mTvAddress, mTvDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvDefault = itemView.findViewById(R.id.iv_default);
            mIvSign = itemView.findViewById(R.id.iv_sign);
            mIvAlter = itemView.findViewById(R.id.iv_alter);
            mRlSign = itemView.findViewById(R.id.rl_sign);
            mTvFirstName = itemView.findViewById(R.id.tv_first_name);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvPhone = itemView.findViewById(R.id.tv_phone);
            mTvAddress = itemView.findViewById(R.id.tv_address);
            mTvDelete = itemView.findViewById(R.id.tv_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(list.get(getLayoutPosition()));
                }
            });

            mTvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.size() > 1) {
                        mOnItemClickListener.onItemDelete(list.get(getLayoutPosition()));
                    } else {
                        T.showShort("只有一个地址无法删除哦~");
                    }
                }
            });

            mIvAlter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemAlter(list.get(getLayoutPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Shoppingaddress bean);
        void onItemDelete(Shoppingaddress bean);
        void onItemAlter(Shoppingaddress bean);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
