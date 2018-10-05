package com.febers.uestc_bbs.utils

object GenderUtils {
    fun change(raw: String?): String {
        raw ?: return ""
        if (raw == "0") return "未知"
        if (raw == "1") return "男"
        if (raw == "2") return "女"
        return raw
    }
}