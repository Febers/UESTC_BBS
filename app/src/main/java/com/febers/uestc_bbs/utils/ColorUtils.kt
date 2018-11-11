package com.febers.uestc_bbs.utils

import android.graphics.Color

object ColorUtils {

    fun decimalToRGB(color: Int) {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
    }

    fun toDarkColor(color: Int): Int {
        val hsv: FloatArray = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] = hsv[2] * 0.9f
        return Color.HSVToColor(hsv)
    }

    fun isLightColor(color: Int): Boolean {
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)
        return r*0.299 + g*0.578 + b*0.114 >= 192
    }
}