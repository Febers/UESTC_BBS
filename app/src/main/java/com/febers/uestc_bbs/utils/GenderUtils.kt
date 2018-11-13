package com.febers.uestc_bbs.utils

import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R

object GenderUtils {

    /**
     * 将服务器返回的性别代码转换为具体性别
     */
    fun change(raw: String?): String {
        raw ?: return ""
        if (raw == "0") return MyApp.context().getString(R.string.unknwon)
        if (raw == "1") return MyApp.context().getString(R.string.man)
        if (raw == "2") return MyApp.context().getString(R.string.woman)
        return raw
    }
}