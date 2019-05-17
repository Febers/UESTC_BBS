package com.febers.uestc_bbs.view.helper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.Gravity
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.utils.ColorUtils

class RedPointDrawable(context: Context, private val origin: Drawable) : Drawable() {
    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private var radius: Int
    private var gravity: Int
    private var isShow = true

    init {
        paint.color = if (ColorUtils.colorCompare(ThemeHelper.getColorPrimaryBySp(), Color.RED)) Color.WHITE else Color.RED
        radius = 6
        gravity = Gravity.LEFT
    }

    fun showRedDrawable(show: Boolean) {
        isShow = show
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        origin.draw(canvas)
        if (isShow) {
            // 获取原图标的右上角坐标
            var cx = bounds.right
            var cy = bounds.top
            // 计算我们的小红点的坐标
            if (Gravity.LEFT and gravity == Gravity.LEFT) {
                cx -= radius
            } else if (Gravity.RIGHT and gravity == Gravity.RIGHT) {
                cx += radius
            }
            if (Gravity.TOP and gravity == Gravity.TOP) {
                cy -= radius
            } else if (Gravity.BOTTOM and gravity == Gravity.BOTTOM) {
                cy += radius
            }
            canvas.drawCircle(cx.toFloat(), cy.toFloat(), radius.toFloat(), paint)//绘制小红点
        }
    }

    fun setGrivaty(gravity: Int) {
        this.gravity = gravity
    }

    override fun setAlpha(alpha: Int) {
        origin.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        origin.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return origin.opacity
    }

    override fun getIntrinsicWidth(): Int {
        return origin.intrinsicWidth
    }

    override fun getIntrinsicHeight(): Int {
        return origin.intrinsicHeight
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        origin.setBounds(left, top, right, bottom)
    }

    override fun setBounds(bounds: Rect) {
        super.setBounds(bounds)
        origin.bounds = bounds
    }

    companion object {

        fun wrap(context: Context, drawable: Drawable): RedPointDrawable {
            return drawable as? RedPointDrawable ?: RedPointDrawable(context, drawable)
        }
    }
}
