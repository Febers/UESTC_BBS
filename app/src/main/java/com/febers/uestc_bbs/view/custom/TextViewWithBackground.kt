package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.Gravity
import android.widget.TextView

class TextViewWithBackground: TextView {

    private var paint: Paint = Paint()
    private lateinit var rectF: RectF
    private var firstPadding = true

    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet): super(context, attrs)

    override fun onDraw(canvas: Canvas?) {
        paint.apply {
            isAntiAlias = true //抗锯齿
            color = Color.parseColor("#f5f5f5")
            style = Paint.Style.FILL
        }
        if (firstPadding) {
            setPadding(8, 8, 8, 8)
            gravity = Gravity.CENTER
            firstPadding = false
        }
        rectF = RectF(0f, 0f, measuredWidth + 0f, measuredHeight + 0f)
        canvas?.drawRoundRect(rectF, 10f, 10f, paint)
        //canvas?.translate(10f, 0f)
        super.onDraw(canvas)
    }
}