package com.ketty.chinesemedicine.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.ketty.chinesemedicine.R;

public class DownloadProgressView extends View {
    /**
     * 控制模式-普通
     */
    private static final int MODE_NORMAL = 1;
    /**
     * 控制模式-触摸
     */
    private static final int MODE_TOUCH = 2;

    /**
     * View默认最小宽度
     */
    private int mDefaultMinWidth;
    /**
     * View默认最小高度
     */
    private int mDefaultMinHeight;

    /**
     * 控件宽
     */
    private int mViewWidth;
    /**
     * 控件高
     */
    private int mViewHeight;

    /**
     * 圆角弧度
     */
    private float mRadius;
    /**
     * 背景画笔
     */
    private Paint mBgPaint;
    /**
     * 进度画笔
     */
    private Paint mProgressPaint;
    /**
     * 文字画笔
     */
    private Paint mTextPaint;
    /**
     * 当前进度
     */
    private int mProgress;
    /**
     * 背景颜色
     */
    private int mBgColor;
    /**
     * 进度背景颜色
     */
    private int mProgressBgColor;
    /**
     * 进度百分比文字的颜色
     */
    private int mPercentageTextColor;
    /**
     * 第二层进度百分比文字的颜色
     */
    private int mPercentageTextColor2;
    /**
     * 进度百分比文字的字体大小
     */
    private float mPercentageTextSize;
    /**
     * 最大进度值
     */
    private int mMaxProgress;
    /**
     * 进度更新监听
     */
    private OnProgressUpdateListener mOnProgressUpdateListener;
    /**
     * 控制模式
     */
    private int mControlMode;
    /**
     * 按下时Down事件的x坐标
     */
    private float mTouchDownX;

    public DownloadProgressView(Context context) {
        this(context, null);
    }

    public DownloadProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DownloadProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        initAttr(context, attrs, defStyleAttr);
        //取消硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        //背景画笔
        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(mBgColor);
        //进度画笔
        mProgressPaint = new Paint();
        mProgressPaint.setColor(mProgressBgColor);
        mProgressPaint.setAntiAlias(true);
        //文字画笔
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mPercentageTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        //计算默认宽、高
        mDefaultMinWidth = dip2px(context, 180f);
        mDefaultMinHeight = dip2px(context, 40f);
    }

    private void initAttr(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DownloadProgressView, defStyleAttr, 0);
        mBgColor = array.getColor(R.styleable.DownloadProgressView_dpv_bg, Color.argb(100, 169, 169, 169));
        mProgressBgColor = array.getColor(R.styleable.DownloadProgressView_dpv_progress_bg, Color.GRAY);
        //进度百分比文字的颜色，默认和进度背景颜色一致
        mPercentageTextColor = array.getColor(R.styleable.DownloadProgressView_dpv_percentage_text_color, mProgressBgColor);
        //第二层，进度百分比文字的颜色
        mPercentageTextColor2 = array.getColor(R.styleable.DownloadProgressView_dpv_percentage_text_color2, Color.WHITE);
        //进度百分比文字的字体颜色
        mPercentageTextSize = array.getDimension(R.styleable.DownloadProgressView_dpv_percentage_text_size, sp2px(context, 15f));
        //当前进度值
        mProgress = array.getInt(R.styleable.DownloadProgressView_dpv_progress, 0);
        //最大进度值
        mMaxProgress = array.getInteger(R.styleable.DownloadProgressView_dpv_max_progress, 100);
        //控制模式
        mControlMode = array.getInt(R.styleable.DownloadProgressView_dpv_control_mode, MODE_NORMAL);
        array.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        //计算出圆角半径
        mRadius = Math.min(mViewWidth, mViewHeight) / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //裁剪圆角
        clipRound(canvas);
        //画背景
        drawBg(canvas);
        //画进度条
        drawProgress(canvas);
        //画文字图层
        drawText(canvas);
    }

    //------------ getFrameXxx()方法都是处理padding ------------

    private float getFrameLeft() {
        return getPaddingStart();
    }

    private float getFrameRight() {
        return mViewWidth - getPaddingEnd();
    }

    private float getFrameTop() {
        return getPaddingTop();
    }

    private float getFrameBottom() {
        return mViewHeight - getPaddingBottom();
    }

    //------------ getFrameXxx()方法都是处理padding ------------

    /**
     * 裁剪圆角
     */
    private void clipRound(Canvas canvas) {
        Path path = new Path();
        RectF roundRect = new RectF(getFrameLeft(), getFrameTop(), getFrameRight(), getFrameBottom());
        path.addRoundRect(roundRect, mRadius, mRadius, Path.Direction.CW);
        canvas.clipPath(path);
    }

    /**
     * 画背景
     */
    private void drawBg(Canvas canvas) {
        canvas.drawRect(new RectF(getFrameLeft(), getPaddingTop(), getFrameRight(), getFrameBottom()), mBgPaint);
    }

    /**
     * 画进度
     */
    private void drawProgress(Canvas canvas) {
        RectF rect = new RectF(getFrameLeft(), getFrameTop(), getFrameRight() * getProgressRatio(), getFrameBottom());
        canvas.drawRect(rect, mProgressPaint);
    }

    /**
     * 画文字
     */
    private void drawText(Canvas canvas) {
        mTextPaint.setColor(mPercentageTextColor);
        //创建文字图层
        Bitmap textBitmap = Bitmap.createBitmap(mViewWidth, mViewHeight, Bitmap.Config.ARGB_8888);
        Canvas textCanvas = new Canvas(textBitmap);
        String textContent = mProgress + "%";
        //计算文字Y轴坐标
        float textY = mViewHeight / 2.0f - (mTextPaint.getFontMetricsInt().descent / 2.0f + mTextPaint.getFontMetricsInt().ascent / 2.0f);
        textCanvas.drawText(textContent, mViewWidth / 2.0f, textY, mTextPaint);
        //画最上层的白色图层，未相交时不会显示出来
        mTextPaint.setColor(mPercentageTextColor2);
        mTextPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        textCanvas.drawRect(new RectF(getFrameLeft(), getFrameTop(), getFrameRight() * getProgressRatio(), getFrameBottom()), mTextPaint);
        //画结合后的图层
        canvas.drawBitmap(textBitmap, getFrameLeft(), getFrameTop(), mTextPaint);
        mTextPaint.setXfermode(null);
        textBitmap.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(handleMeasure(widthMeasureSpec, true), handleMeasure(heightMeasureSpec, false));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = event.getAction();
        //拦截事件，然后让父类不进行拦截
        if (mControlMode == MODE_TOUCH && action == MotionEvent.ACTION_DOWN) {
            getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //拽托模式下才起效果
        if (mControlMode == MODE_TOUCH) {
            int action = event.getAction();
            //包裹Down事件时的x坐标
            if (action == MotionEvent.ACTION_DOWN) {
                mTouchDownX = event.getX();
                return true;
            } else if (action == MotionEvent.ACTION_MOVE || action == MotionEvent.ACTION_UP) {
                //Move或Up的时候，计算拽托进度
                float endX = event.getX();
                //计算公式：百分比值 = 移动距离 / 总长度
                float distanceX = Math.abs(endX - mTouchDownX);
                float ratio = (distanceX * 1.0f) / (getFrameRight() - getFrameLeft());
                //计算百分比应该有的进度：进度 = 总进度 * 进度百分比值
                float progress = mMaxProgress * ratio;
                setProgress((int) progress);
                return true;
            }
            return super.onTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
        }
    }

    /**
     * 处理MeasureSpec
     */
    private int handleMeasure(int measureSpec, boolean isWidth) {
        int result;
        if (isWidth) {
            result = mDefaultMinWidth;
        } else {
            result = mDefaultMinHeight;
        }
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            //处理wrap_content的情况
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 设置进度最大值
     */
    public DownloadProgressView setMaxProgress(int maxProgress) {
        mMaxProgress = maxProgress;
        invalidate();
        return this;
    }

    /**
     * 设置进度
     */
    public void setProgress(int progress) {
        if (progress >= 0 && progress <= 100) {
            mProgress = progress;
            invalidate();
            if (mOnProgressUpdateListener != null) {
                mOnProgressUpdateListener.onProgressUpdate(progress);
            }
        }
    }

    /**
     * 获取当前进度
     */
    public int getProgress() {
        return mProgress;
    }

    /**
     * 获取最大进度
     */
    public int getMaxProgress() {
        return mMaxProgress;
    }

    public interface OnProgressUpdateListener {
        /**
         * 进度更新时回调
         *
         * @param progress 当前进度
         */
        void onProgressUpdate(int progress);
    }

    public void setOnProgressUpdateListener(OnProgressUpdateListener onProgressUpdateListener) {
        mOnProgressUpdateListener = onProgressUpdateListener;
    }

    /**
     * 获取当前进度值比值
     */
    private float getProgressRatio() {
        return (mProgress / (mMaxProgress * 1.0f));
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
