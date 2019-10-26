package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView
import com.febers.uestc_bbs.R

class TextViewWithBackground: TextView {

    private var paint: Paint = Paint()
    private lateinit var rectF: RectF
    private var firstPadding = true
    private var bgColor = Color.parseColor("#f5f5f5")

    private var rectRound = 0f
    private var rectPadding = 4

    constructor(context: Context) :this(context, null)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewWithBackground)
        rectRound = typeArray.getDimension(R.styleable.TextViewWithBackground_rectRound, 0f)
        rectPadding = typeArray.getInt(R.styleable.TextViewWithBackground_rectPadding, 0)
        bgColor = typeArray.getColor(R.styleable.TextViewWithBackground_bgColor, bgColor)
        typeArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        paint.apply {
            isAntiAlias = true //抗锯齿
            color = bgColor
            style = Paint.Style.FILL
        }
        gravity = Gravity.CENTER
        setPadding(rectPadding*2, rectPadding, rectPadding*2, rectPadding)

        rectF = RectF(0f, 0f, measuredWidth + 0f, measuredHeight + 0f)
        canvas?.drawRoundRect(rectF, rectRound, rectRound, paint)
        super.onDraw(canvas)
    }

    fun setRectRound(round: Float) {
        rectRound = round
    }

    fun setRectPadding(padding: Int) {
        rectPadding = padding
    }
}