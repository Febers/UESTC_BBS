package com.febers.uestc_bbs.utils

import android.graphics.Color
import kotlin.math.*


object ColorUtils {

    fun decimalToRGB(color: Int) {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
    }

    fun int2String(colorInt: Int): String = String.format("#%06X", 0xFFFFFF and colorInt)

    /**
     * 通过改变HSV(色调、饱和度、明度)中的V(明度)
     * 将颜色变得更深
     * 用于改变主题时StatusBar的ColorPrimaryDark效果
     *
     * @param color Color的Int值
     * @return 转换之后的Color
     */
    fun toDarkColor(color: Int): Int {
        val hsv = FloatArray(3)
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

    /**
     * 比较两个颜色是否相近
     *
     * @param first
     * @param second
     * @return 是否相近
     */
    fun colorCompare(first: Int, second: Int): Boolean {
        val r1 = Color.red(first)
        val g1 = Color.green(first)
        val b1 = Color.blue(first)

        val r2 = Color.red(second)
        val g2 = Color.green(second)
        val b2 = Color.blue(second)

        var distance = abs(r1 * r1 - r2 * r2 ) * 0.299 + abs(g1 * g1 - g2 * g2) + 0.587 + abs(b1 * b1 - b2 * b2) + 0.114
//        log("d r ${r1 - r2}")
//        log("d g ${g1 - g2}")
//        log("d b ${b1 - b2}")
//        log("distance $distance")

        return abs(r1 - r2) + abs(g1 - g2) + abs(b1 - b2) < 150
    }

}