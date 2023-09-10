package com.ketty.chinesemedicine.main.store;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.ketty.chinesemedicine.App;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.databinding.DialogLoginOtherBinding;
import com.ketty.chinesemedicine.databinding.DialogShoppingBuyBinding;
import com.ketty.chinesemedicine.entity.Product;
import com.ketty.chinesemedicine.entity.Productshopcart;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.T;
import com.ketty.chinesemedicine.util.TextColorSizeHelper;

import java.util.ArrayList;
import java.util.List;

public class ShoppingBuyDialog extends DialogFragment implements View.OnClickListener {
    private DialogShoppingBuyBinding bind;
    private StateListener mStateListener;
    private Product product;
    private int number = 1;
    private Productshopcart productshopcart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = DialogShoppingBuyBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        product = (Product) bundle.getSerializable("data");
        Glide.with(getContext()).load(product.getImage()).into(bind.ivThumbnail);
        bind.tvName.setText(product.getName());
        String text = "￥" + product.getPrice();
        List<TextColorSizeHelper.SpanInfo> spanInfos = new ArrayList<TextColorSizeHelper.SpanInfo>();
        spanInfos.add(
                new TextColorSizeHelper.SpanInfo(
                        text.substring(0, 1),
                        DisplayUtil.dip2px(getContext(), 14),
                        Color.parseColor("#FF6E00"),
                        false
                )
        );
        spanInfos.add(
                new TextColorSizeHelper.SpanInfo(
                        text.substring(1, text.indexOf(".")),
                        DisplayUtil.dip2px(getContext(), 18),
                        Color.parseColor("#FF6E00"),
                        false
                )
        );
        spanInfos.add(
                new TextColorSizeHelper.SpanInfo(
                        text.substring(text.indexOf(".")),
                        DisplayUtil.dip2px(getContext(), 14),
                        Color.parseColor("#FF6E00"),
                        false
                )
        );
        bind.tvPrice.setText(TextColorSizeHelper.getTextSpan(getContext(), text, spanInfos));

        productshopcart = new Productshopcart();
        productshopcart.setProductId(product.getId());
        productshopcart.setUserId(Long.valueOf(App.getMmkvUtil().getString("user","uId",null)));
        productshopcart.setNumber(1);

        bind.ivClose.setOnClickListener(this);
        bind.ivSubtract.setOnClickListener(this);
        bind.ivAdd.setOnClickListener(this);
        bind.slInstantlyBuy.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;
        params.dimAmount = 0.3f;
        params.windowAnimations = R.style.dialogAnim;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                mStateListener.close();
                break;
            case R.id.iv_subtract:
                if (number > 1) {
                    number--;
                    bind.tvNumber.setText(String.valueOf(number));
                    productshopcart.setNumber(number);
                }
                break;
            case R.id.iv_add:
                if (number < product.getInventory()) {
                    number++;
                    bind.tvNumber.setText(String.valueOf(number));
                    productshopcart.setNumber(number);
                } else {
                    T.showShort("超出库存上限");
                }
                break;
            case R.id.sl_instantly_buy:
                float totalPrice = number * product.getPrice().floatValue();
                totalPrice = Math.round(totalPrice * 100) / 100f;
                mStateListener.onBottomClick(productshopcart, totalPrice);
                break;
        }
    }

    public interface StateListener {
        void close();
        void onBottomClick(Productshopcart productshopcart, float price);
    }

    public void setStateListener(StateListener listener) {
        this.mStateListener = listener;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind = null;
    }
}
