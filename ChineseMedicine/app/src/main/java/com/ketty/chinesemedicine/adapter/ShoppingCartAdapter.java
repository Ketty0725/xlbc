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

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {
    private List<Productshopcart> list = new ArrayList<>();
    private HashMap<Integer, Boolean> isCheckedHasMap = new HashMap<>();
    private float totalPrice = 0.00f;
    private List<Product> products = new ArrayList<>();
    private List<Productshopcart> productSelected = new ArrayList<>();

    public ShoppingCartAdapter(List<Productshopcart> list, List<Product> products) {
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

        holder.mTvNumber.setText(String.valueOf(list.get(position).getNumber()));
        holder.mCheckBox.setChecked(isCheckedHasMap.get(position),true);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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

            mIvSubtract.setOnClickListener(this);
            mIvAdd.setOnClickListener(this);
            mCheckBox.setOnClickListener(this);

            for (int i = 0; i < list.size(); i++) {
                isCheckedHasMap.put(i, false);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(list.get(getLayoutPosition()).getProductId());
                }
            });
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_subtract:
                    decrement();
                    break;
                case R.id.iv_add:
                    increment();
                    break;
                case R.id.scb:
                    setChecked();
                    break;
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

            int num = list.get(getLayoutPosition()).getNumber();
            float price = products.get(getLayoutPosition()).getPrice().floatValue();
            if (isCheckedHasMap.get(getLayoutPosition())) {
                totalPrice += num * price;
                totalPrice = Math.round(totalPrice * 100) / 100f;
                mOnItemClickListener.onCalculatePrice(totalPrice);
                productSelected.add(list.get(getLayoutPosition()));
            } else {
                totalPrice -= num * price;
                totalPrice = Math.round(totalPrice * 100) / 100f;
                mOnItemClickListener.onCalculatePrice(totalPrice);
                productSelected.remove(list.get(getLayoutPosition()));
            }
            mOnItemClickListener.onFinished(productSelected);
        }

        private void increment() {
            int newCount = list.get(getLayoutPosition()).getNumber();
            newCount++;
            if (newCount <= products.get(getLayoutPosition()).getInventory()) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("userId", App.getMmkvUtil().getString("user","uId",null));
                map.put("productId", list.get(getLayoutPosition()).getProductId());

                int finalNewCount = newCount;
                RetrofitManager
                        .getInstance()
                        .getApiService()
                        .postMethod("/productshopcart/addOrIncrementToRedis", map)
                        .compose(RxHelper.observableIO2Main(itemView.getContext()))
                        .subscribe(new BaseObserver() {
                            @Override
                            public void onSuccess(Map<String, Object> response) {
                                mTvNumber.setText(String.valueOf(finalNewCount));
                                list.get(getLayoutPosition()).setNumber(finalNewCount);
                                changePrice(1);
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg) {

                            }
                        });
            } else {
                T.showShort("超出库存上限");
            }
        }

        private void decrement() {
            int newCount = list.get(getLayoutPosition()).getNumber();
            if (newCount > 1) {
                newCount--;
                HashMap<String, Object> map = new HashMap<>();
                map.put("userId", App.getMmkvUtil().getString("user","uId",null));
                map.put("productId", list.get(getLayoutPosition()).getProductId());

                int finalNewCount = newCount;
                RetrofitManager
                        .getInstance()
                        .getApiService()
                        .postMethod("/productshopcart/decrementToRedis", map)
                        .compose(RxHelper.observableIO2Main(itemView.getContext()))
                        .subscribe(new BaseObserver() {
                            @Override
                            public void onSuccess(Map<String, Object> response) {
                                mTvNumber.setText(String.valueOf(finalNewCount));
                                list.get(getLayoutPosition()).setNumber(finalNewCount);
                                changePrice(-1);
                            }

                            @Override
                            public void onFailure(Throwable e, String errorMsg) {

                            }
                        });
            }
        }

        private void changePrice(int num) {
            if (mCheckBox.isChecked()) {
                float price = products.get(getLayoutPosition()).getPrice().floatValue();
                totalPrice += num * price;
                totalPrice = Math.round(totalPrice * 100) / 100f;
                mOnItemClickListener.onCalculatePrice(totalPrice);
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
        totalPrice = 0.00f;
        for (int i = 0; i < list.size(); i++) {
            int num = list.get(i).getNumber();
            float price = products.get(i).getPrice().floatValue();
            totalPrice += num * price;
        }
        totalPrice = Math.round(totalPrice * 100) / 100f;
        mOnItemClickListener.onCalculatePrice(totalPrice);
        productSelected.clear();
        productSelected.addAll(list);
        mOnItemClickListener.onFinished(productSelected);
        notifyDataSetChanged();
    }

    public void revertSelected() {
        Set<Map.Entry<Integer, Boolean>> entries = isCheckedHasMap.entrySet();
        for (Map.Entry<Integer, Boolean> entryset : entries) {
            entryset.setValue(!entryset.getValue());
        }
        totalPrice = 0.00f;
        mOnItemClickListener.onCalculatePrice(totalPrice);
        productSelected.clear();
        mOnItemClickListener.onFinished(productSelected);
        notifyDataSetChanged();
    }

    public void remove(Productshopcart bean) {
        int position = list.indexOf(bean);
        list.remove(position);
        totalPrice = 0.00f;
        mOnItemClickListener.onCalculatePrice(totalPrice);
        mOnItemClickListener.onAllChecked(false);
        productSelected.remove(bean);
        mOnItemClickListener.onFinished(productSelected);
        notifyItemRemoved(position);
    }

    public interface OnItemClickListener {
        void onItemClick(Long productId);
        void onCalculatePrice(float price);
        void onAllChecked(boolean checked);
        void onFinished(List<Productshopcart> productSelected);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

}
