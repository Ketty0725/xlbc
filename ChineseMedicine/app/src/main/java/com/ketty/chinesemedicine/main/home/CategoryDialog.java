package com.ketty.chinesemedicine.main.home;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.adapter.CategoryDialogAdapter;
import com.ketty.chinesemedicine.databinding.DialogCategoryBinding;
import com.ketty.chinesemedicine.databinding.FragmentCaptchaBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoryDialog extends DialogFragment implements View.OnClickListener {
    private DialogCategoryBinding bind;
    private StateListener mStateListener;
    private List<String> list = new ArrayList<>();
    private int tag;

    public CategoryDialog(List<String> list, int tag) {
        this.list = list;
        this.tag = tag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bind = DialogCategoryBinding.inflate(inflater, container, false);
        View view = bind.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        if (tag == 1) {
            bind.imageView.setVisibility(View.INVISIBLE);
            bind.textView.setVisibility(View.VISIBLE);
        } else {
            bind.imageView.setVisibility(View.VISIBLE);
            bind.textView.setVisibility(View.INVISIBLE);
        }

        bind.shadowCollapse.setOnClickListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        bind.recyclerView.setLayoutManager(layoutManager);
        CategoryDialogAdapter mAdapter = new CategoryDialogAdapter(list);
        bind.recyclerView.setAdapter(mAdapter);
        bind.recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        Bundle bundle = getArguments();
        int position = bundle.getInt("position");
        mAdapter.setmPosition(position);
        mAdapter.notifyDataSetChanged();

        mAdapter.setOnItemClickListener(new CategoryDialogAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mStateListener.onClick(position);
                mAdapter.setmPosition(position);
                mAdapter.notifyDataSetChanged();
                Log.i("CategoryDialog", String.valueOf(position));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.shadow_collapse:
                this.dismiss();
                break;
        }
    }

    @Override
    public void onStart() {
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = getPeekHeight();
        params.gravity = Gravity.RIGHT;
        params.dimAmount = 0.0F;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialogAnimRight;
        super.onStart();
    }

    protected int getPeekHeight() {
        int peekHeight = getResources().getDisplayMetrics().heightPixels;
        //设置弹窗高度为屏幕高度的3/4
        return peekHeight - peekHeight / 3;
    }

    public interface StateListener {
        void onClick(int position);
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
