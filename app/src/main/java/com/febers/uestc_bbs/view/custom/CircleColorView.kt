package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.febers.uestc_bbs.R

/**
 * 绘制一个带有色彩的圆形View
 */
class CircleColorView : View {

    private lateinit var paint: Paint
    private var color: Int = 0
    private var radius: Int = 0

    constructor(context: Context) : super(context) {init(null)}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {init(attrs)}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {init(attrs)}

    private fun init(attrs: AttributeSet?) {
        attrs ?: return
        paint = Paint()
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CircleColorView)
        color = typeArray.getColor(R.styleable.CircleColorView_color, Color.BLUE)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val w = width /2
        val h = height /2
        radius = Math.min(w, h)
        val c = radius

        paint.style = Paint.Style.FILL
        paint.color = color
        paint.isAntiAlias = false
        canvas?.drawCircle(c.toFloat(), c.toFloat(), radius.toFloat(), paint)
    }
}
