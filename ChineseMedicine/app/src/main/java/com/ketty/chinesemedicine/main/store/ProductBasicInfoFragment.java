package com.ketty.chinesemedicine.main.store;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.draggable.library.extension.ImageViewerHelper;
import com.google.gson.reflect.TypeToken;
import com.ketty.chinesemedicine.Rxhttp.BaseObserver;
import com.ketty.chinesemedicine.Rxhttp.RetrofitManager;
import com.ketty.chinesemedicine.Rxhttp.RxHelper;
import com.ketty.chinesemedicine.databinding.FragmentProductBasicInfoBinding;
import com.ketty.chinesemedicine.entity.Productbasicinfo;
import com.ketty.chinesemedicine.util.JsonHelper;
import com.ketty.chinesemedicine.util.LogUtils;
import com.trello.rxlifecycle2.components.support.RxFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductBasicInfoFragment extends RxFragment {
    private FragmentProductBasicInfoBinding bind;
    private static final String ARG_PARAM1 = "id";
    private Long id;

    public static ProductBasicInfoFragment newInstance(Long id) {
        ProductBasicInfoFragment fragment = new ProductBasicInfoFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_PARAM1, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bind = FragmentProductBasicInfoBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initRequest();
    }

    private void initRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("productId", id);

        RetrofitManager
                .getInstance()
                .getApiService()
                .postMethod("/productbasicinfo/getByProductId", map)
                .compose(RxHelper.observableIO2Main(getContext()))
                .subscribe(new BaseObserver() {
                    @Override
                    public void onSuccess(Map<String, Object> response) {
                        LogUtils.e("请求成功");
                        String data = JsonHelper.parserObject2Json(response.get("basicinfo"));
                        Productbasicinfo productbasicinfo = JsonHelper.parserJson2Object(
                                data, Productbasicinfo.class);
                        data = JsonHelper.parserObject2Json(response.get("images"));
                        List<String> images = JsonHelper.parserJson2List(
                                data, new TypeToken<List<String>>() {}.getType());
                        initData(productbasicinfo,images);
                    }

                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        LogUtils.e("请求失败");
                    }
                });
    }

    private void initData(Productbasicinfo productbasicinfo, List<String> images) {
        if (productbasicinfo != null) {
            bind.tvCommonName.setText(productbasicinfo.getCommonName() == null ? "" : productbasicinfo.getCommonName());
            bind.tvBrand.setText(productbasicinfo.getBrand() == null ? "" : productbasicinfo.getBrand());
            bind.tvCompany.setText(productbasicinfo.getCompany() == null ? "" : productbasicinfo.getCompany());
            bind.tvApprovalNumber.setText(productbasicinfo.getApprovalNumber() == null ? "" : productbasicinfo.getApprovalNumber());
            bind.tvSpecification.setText(productbasicinfo.getSpecification() == null ? "" : productbasicinfo.getSpecification());
            bind.tvDosageForm.setText(productbasicinfo.getDosageForm() == null ? "" : productbasicinfo.getDosageForm());
            bind.tvUseMethod.setText(productbasicinfo.getUseMethod() == null ? "" : productbasicinfo.getUseMethod());
            bind.tvUseDose.setText(productbasicinfo.getUseDose() == null ? "" : productbasicinfo.getUseDose());
            bind.tvExpirationDate.setText(productbasicinfo.getExpirationDate() == null ? "" : productbasicinfo.getExpirationDate());
            bind.tvTargetUser.setText(productbasicinfo.getTargetUser() == null ? "" : productbasicinfo.getTargetUser());
        }

        if (images != null && images.size() > 0) {
            List<ImageViewerHelper.ImageInfo> imageInfos = new ArrayList<>();
            List<View> views = new ArrayList<>();
            bind.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            bind.recyclerView.setAdapter(new RecyclerView.Adapter<ProductBasicInfoFragment.ViewHolder>() {
                @NonNull
                @Override
                public ProductBasicInfoFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    ImageView imageView = new ImageView(parent.getContext());
                    imageView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    return new ViewHolder(imageView);
                }

                @Override
                public void onBindViewHolder(@NonNull ProductBasicInfoFragment.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
                    Glide.with(holder.itemView).load(images.get(position)).into(holder.imageView);
                    imageInfos.add(new ImageViewerHelper.ImageInfo(images.get(position),"",0));
                    views.add(holder.imageView);
                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ImageViewerHelper.showImages(getContext(), views, imageInfos, position, true);
                        }
                    });
                }

                @Override
                public int getItemCount() {
                    return images.size();
                }
            });
            bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull ImageView view) {
            super(view);
            this.imageView = view;
        }
    }
}
