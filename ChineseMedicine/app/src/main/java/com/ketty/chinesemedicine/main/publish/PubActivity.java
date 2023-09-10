package com.ketty.chinesemedicine.main.publish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.ketty.chinesemedicine.BaseActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.databinding.ActivityPubBinding;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.T;

import java.util.ArrayList;
import java.util.List;

public class PubActivity extends BaseActivity implements View.OnClickListener {
    private ActivityPubBinding bind;
    private List<LinearLayout> mLays = new ArrayList();

    @Override
    protected View initLayout() {
        bind = ActivityPubBinding.inflate(getLayoutInflater());
        View view = bind.getRoot();
        return view;
    }

    @Override
    protected void initView() {
        initWindow();
        mLays.add(bind.llPubDraft);
        mLays.add(bind.llPubVideo);
        mLays.add(bind.llPubNote);
        bind.llPubNote.setOnClickListener(this);
        bind.llPubVideo.setOnClickListener(this);
        bind.llPubDraft.setOnClickListener(this);
        bind.rlMain.setOnClickListener(this);
    }

    protected void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
    }

    public static void show(Context context) {
        context.startActivity(new Intent(context, PubActivity.class));
    }

    @Override
    public void onBackPressed() {
        dismiss();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        bind.btnPub.animate()
                .rotation(135.0f)
                .setDuration(180)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                    }
                })
                .start();
        show(0);
        show(1);
        show(2);
    }

    private void show(int position) {
        int angle = 30 + position * 60;
        float x = (float) Math.cos(angle * (Math.PI / 180)) * DisplayUtil.dip2px(this, 100);
        float y = (float) -Math.sin(angle * (Math.PI / 180)) * DisplayUtil.dip2px(this, position != 1 ? 160 : 100);
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mLays.get(position), "translationX", 0, x);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mLays.get(position), "translationY", 0, y);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(180);
        animatorSet.play(objectAnimatorX).with(objectAnimatorY);
        animatorSet.start();
    }

    private void close() {
        bind.btnPub.clearAnimation();
        bind.btnPub.animate()
                .rotation(0f)
                .setDuration(200)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        bind.btnPub.setVisibility(View.GONE);
                        finish();
                    }
                })
                .start();
    }

    private void dismiss() {
        close();
        close(0);
        close(1);
        close(2);
    }

    private void close(final int position) {
        int angle = 30 + position * 60;
        float x = (float) Math.cos(angle * (Math.PI / 180)) * DisplayUtil.dip2px(this, 100);
        float y = (float) -Math.sin(angle * (Math.PI / 180)) * DisplayUtil.dip2px(this, position != 1 ? 160 : 100);
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(mLays.get(position), "translationX", x, 0);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(mLays.get(position), "translationY", y, 0);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(180);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.play(objectAnimatorX).with(objectAnimatorY);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mLays.get(position).setVisibility(View.GONE);
            }
        });
        animatorSet.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_pub_note:
                startActivity(new Intent(PubActivity.this,PublishActivity.class),
                        ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
                finish();
                break;
            case R.id.ll_pub_video:
            case R.id.ll_pub_draft:
                T.showShort("正在开发中，敬请期待");
                break;
            case R.id.rl_main:
                dismiss();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bind = null;
    }
}
