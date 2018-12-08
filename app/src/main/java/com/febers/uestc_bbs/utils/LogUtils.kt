package com.febers.uestc_bbs.utils

import android.util.Log
import android.util.Log.i

object LogUtils {

    fun i(message: String) {
        i("UESTC", message)
    }

    fun i(tag: String, message: String) {
        Log.i(tag, message)
    }
}

fun log(message: String) {
    LogUtils.i(message)
}