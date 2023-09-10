package com.ketty.chinesemedicine.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ketty.chinesemedicine.MainActivity;
import com.ketty.chinesemedicine.R;
import com.ketty.chinesemedicine.adapter.ListCategoryAdapter;
import com.ketty.chinesemedicine.main.publish.PubActivity;
import com.ketty.chinesemedicine.util.DisplayUtil;
import com.ketty.chinesemedicine.util.LogUtils;

public class BottomNavigationBar extends LinearLayout {
    private Paint paint;
    private Path path;
    private float width;
    private RadioGroup radioGroup;
    private RadioButton rbHome, rbCommunity, rbStore, rbMine;
    private ImageView rbAdd;

    public BottomNavigationBar(Context context) {
        super(context);
        init(context);
    }

    public BottomNavigationBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        path = new Path();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.WHITE);
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_navigator, this);

        radioGroup = view.findViewById(R.id.radioGroup);
        rbHome = view.findViewById(R.id.rb_home);
        rbCommunity = view.findViewById(R.id.rb_community);
        rbStore = view.findViewById(R.id.rb_store);
        rbMine = view.findViewById(R.id.rb_mine);
        rbAdd = view.findViewById(R.id.rbAdd);
        setWillNotDraw(false);

        int width1 = DisplayUtil.dip2px(getContext(),22);
        int height1 = DisplayUtil.dip2px(getContext(),22);

        Drawable dbHome = getResources().getDrawable(R.drawable.btn_tab_home_selector);
        dbHome.setBounds(0, 0, width1, height1);
        rbHome.setCompoundDrawables(null, dbHome, null, null);

        Drawable dbCommunity = getResources().getDrawable(R.drawable.btn_tab_community_selector);
        dbCommunity.setBounds(0, 0, width1, height1);
        rbCommunity.setCompoundDrawables(null, dbCommunity, null, null);

        Drawable dbStore = getResources().getDrawable(R.drawable.btn_tab_store_selector);
        dbStore.setBounds(0, 0, width1, height1);
        rbStore.setCompoundDrawables(null, dbStore, null, null);

        Drawable dbMine = getResources().getDrawable(R.drawable.btn_tab_mine_selector);
        dbMine.setBounds(0, 0, width1, height1);
        rbMine.setCompoundDrawables(null, dbMine, null, null);

        initialize();
        initListener();

        //2、通过Resources获取
        DisplayMetrics dm = getResources().getDisplayMetrics();
        width = dm.widthPixels;

    }

    public void initialize() {
        rbHome.setChecked(true);
    }

    private void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        mOnBottomNavClickListener.onBottomNavClick(0);
                        break;
                    case R.id.rb_community:
                        mOnBottomNavClickListener.onBottomNavClick(1);
                        break;
                    case R.id.rb_store:
                        mOnBottomNavClickListener.onBottomNavClick(2);
                        break;
                    case R.id.rb_mine:
                        mOnBottomNavClickListener.onBottomNavClick(3);
                        break;
                }
            }
        });

        rbAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnBottomNavClickListener.onBottomNavClick(4);
            }
        });

        onBottomDoubleClick(rbHome, 0);
        onBottomDoubleClick(rbCommunity, 1);
        onBottomDoubleClick(rbStore, 2);
        onBottomDoubleClick(rbMine, 3);
    }

    private void onBottomDoubleClick(RadioButton rb, int checkedId) {
        rb.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                mOnBottomNavClickListener.onBottomDoubleClick(checkedId);
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(getResources().getColor(R.color.white));
        paint.setShadowLayer(30,0,20,Color.BLACK);
        path.moveTo(0, dip2px(28));

        path.lineTo(dip2px(130), dip2px(28));
        path.quadTo(width / 2 - dip2px(40), dip2px(28), width / 2 - dip2px(25), dip2px(15));
        path.quadTo(width / 2, -2, width / 2 + dip2px(25), dip2px(15));
        path.quadTo(width / 2 + dip2px(40), dip2px(28), width - dip2px(130), dip2px(28));
        path.lineTo(width, dip2px(28));
        path.lineTo(width, dip2px(81));
        path.lineTo(0, dip2px(81));
        path.close();
        canvas.drawPath(path, paint);
        super.onDraw(canvas);
    }

    private int dip2px(float dpValue) {
        return DisplayUtil.dip2px(getContext(),dpValue);
    }

    public interface OnBottomNavClickListener {
        void onBottomNavClick(int checkedId);
        void onBottomDoubleClick(int checkedId);
    }

    private OnBottomNavClickListener mOnBottomNavClickListener;

    public void setOnBottomNavClickListener(OnBottomNavClickListener listener) {
        this.mOnBottomNavClickListener = listener;
    }

}