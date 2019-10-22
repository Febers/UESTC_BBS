package com.febers.uestc_bbs.utils

import android.util.Log.e

fun log(message: String) {
    log("[UESTC]", message)
}

fun log(tag: String, message: String) {
    e(tag, message)
}

fun log(function: () -> String) {
    log(function.invoke())
}
