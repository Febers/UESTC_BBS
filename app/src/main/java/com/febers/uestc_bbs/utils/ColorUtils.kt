package com.febers.uestc_bbs.utils

import android.graphics.Color

object ColorUtils {

    fun decimalToRGB(color: Int) {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
    }

    /**
     * 通过改变HSV(色调、饱和度、明度)中的V(明度)
     * 将颜色变得更深
     * 用于改变主题时StatusBar的ColorPrimaryDark效果
     *
     * @param color Color的Int值
     * @return 转换之后的Color
     */
    fun toDarkColor(color: Int): Int {
        val hsv: FloatArray = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] = hsv[2] * 0.9f
        return Color.HSVToColor(hsv)
    }

    /**
     * 根据通用的公式判断一个颜色是否为淡色
     * 用于主题切换时，根据颜色的深浅
     * 决定text的颜色
     *
     * @param color Color的Int值
     * @return 是否为淡色
     */
    fun isLightColor(color: Int): Boolean {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return r*0.299 + g*0.578 + b*0.114 >= 192
    }
}