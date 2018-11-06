package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.module.theme.AppColor
import com.febers.uestc_bbs.module.theme.ThemeHelper

/**
 * 自定义的指示器
 */
class IndicatorView : View {

    private var indicationSize = -1
    private var curIndex = -1
    private lateinit var mPaint: Paint

    private var radius = 0
    private var bgColor = Color.GRAY
    private var selectedColor = Color.BLUE

    private val bgView = RectF(0f,0f,0f,0f)
    private val selected = RectF(0f,0f,0f,0f)

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        mPaint = Paint()
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView)
            radius = ta.getDimensionPixelOffset(R.styleable.IndicatorView_radius, 0)
            bgColor = ta.getColor(R.styleable.IndicatorView_background_color, Color.GRAY)
            //selectedColor = ta.getColor(R.styleable.IndicatorView_selected_color, Color.BLUE)
            selectedColor = ThemeHelper.getColor(AppColor.COLOR_PRIMARY)
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width
        val height = height
        //画圆角矩形
        mPaint.style = Paint.Style.FILL//充满
        mPaint.color = bgColor
        mPaint.isAntiAlias = true// 设置画笔的锯齿效果
        bgView.set(0f,0f,width.toFloat(), height.toFloat()) // 设置个新的长方形
        canvas.drawRoundRect(bgView, radius.toFloat(), radius.toFloat(), mPaint)//第二个参数是x半径，第三个参数是y半径
        if (indicationSize == -1) {
            return
        }
        if (curIndex == -1) {
            return
        }
        val itemW = width / indicationSize
        val index = curIndex

        mPaint.color = selectedColor
        val left = index * itemW
        selected.set(left.toFloat(), 0f, (left + itemW).toFloat(), height.toFloat())
        canvas.drawRoundRect(selected, radius.toFloat(), radius.toFloat(), mPaint)//第二个参数是x半径，第三个参数是y半径
    }

    fun setMaxSize(size: Int) {
        indicationSize = size
    }

    fun setCurIndex(index: Int) {
        curIndex = index
        postInvalidate()
    }
}

