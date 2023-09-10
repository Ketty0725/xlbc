package com.ketty.chinesemedicine.component

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.ketty.chinesemedicine.R

/**
 * 限制 View 的尺寸自定义 View
 *
 * 使用方式：
 * 将指定 View 包到该 View 中，即可限制其尺寸
 *
 * 限制方式：
 * 方式 1：将高度设置为自适应，并设置最大高度的尺寸，当高度增大到一定程度时就不再增加尺寸了
 * 方式 2：限制高度：设定宽高比，设置限制高度，组件会根据宽高比和自身宽度限制其高度
 * 方式 3：限制宽度：设定宽高比，设置限制宽度，组件会根据宽高比和自身高度限制其宽度
 *
 * 应用场景举例：banner 宽度为屏幕宽度，高度为按设计图宽高比自适应，则可通过该组件轻松设定。
 */
class SizeLimitView : FrameLayout {

    private var maxHeight = 0f          // 最大高度
    private var widthHeightRatio = 0f   // 宽高比
    private var isLimitWidth = false    // 是否根据宽高比限制宽度
    private var isLimitHeight = false   // 是否根据宽高比限制高度

    constructor(context: Context): this(context, null)
    constructor(context: Context, attr: AttributeSet?): super(context, attr) {
        if (attr == null) {
            return
        }

        val ta = context.obtainStyledAttributes(attr, R.styleable.SizeLimitView)

        maxHeight = ta.getDimension(R.styleable.SizeLimitView_maxHeight, 0f)
        widthHeightRatio = ta.getFloat(R.styleable.SizeLimitView_widthHeightRatio, 0f)
        isLimitHeight = ta.getBoolean(R.styleable.SizeLimitView_limitHeightByWidth, false)
        if (!isLimitHeight) {
            isLimitWidth = ta.getBoolean(R.styleable.SizeLimitView_limitWidthByHeight, false)
        }

        ta.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)

        when {
            maxHeight != 0f -> {
                heightSize = if (heightSize > maxHeight) maxHeight.toInt() else heightSize
            }
            isLimitWidth && widthHeightRatio != 0f -> {
                widthSize = (heightSize * widthHeightRatio).toInt()
            }
            isLimitHeight && widthHeightRatio != 0f -> {
                heightSize = (widthSize / widthHeightRatio).toInt()
            }
        }

        val widthSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode)
        val heightSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode)
        super.onMeasure(widthSpec, heightSpec)
    }
}