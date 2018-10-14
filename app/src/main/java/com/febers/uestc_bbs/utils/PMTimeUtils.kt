package com.febers.uestc_bbs.utils

import android.util.Log.i

/**
 * 私信详情页面的时间工具，传入Adapter中
 * 拥有一个当前的时间变量，并与每个对话的时间相比较
 * 如果两者相等，返回false
 * 如果两者不等，返回true
 * 在Adapter中根据不同情况决定是否显示时间
 */
class PMTimeUtils {

    private var lastTime = "0"

    fun isShowTime(time: String?): Boolean {
        time ?: return false
        val t = TimeUtils.stampChange(time, ignoreSecond = true)
        val lt = TimeUtils.stampChange(lastTime, ignoreSecond = true)
        return if (t == lt) false
        else {
            lastTime = time
            true
        }
    }
}