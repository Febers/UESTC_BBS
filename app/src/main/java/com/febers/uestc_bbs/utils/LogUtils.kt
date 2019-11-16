package com.febers.uestc_bbs.utils

import android.util.Log.*

fun logd(function: () -> String) {
    d("[UESTC]", function.invoke())
}

fun logi(function: () -> String) {
    i("![UESTC]", function.invoke())
}

fun logw(function: () -> String) {
    w("!![UESTC]", function.invoke())
}

fun loge(function: () -> String) {
    e("!!![UESTC]", function.invoke())
}