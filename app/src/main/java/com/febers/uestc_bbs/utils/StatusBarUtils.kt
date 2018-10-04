package com.febers.uestc_bbs.utils

import android.content.Context

object StatusBarUtils {
    fun getHeight(context: Context): Int {
        var sHeight = -1
        if (sHeight == -1) {
            //获取status_bar_height资源的ID
            val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                //根据资源ID获取响应的尺寸值
                sHeight = context.resources.getDimensionPixelSize(resourceId)
            }
        }
        return sHeight;
    }
}