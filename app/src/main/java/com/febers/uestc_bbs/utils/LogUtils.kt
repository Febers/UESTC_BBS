package com.febers.uestc_bbs.utils

import android.util.Log

object LogUtils {

    fun i(message: String) {
        i("App", message)
    }

    fun i(tag: String, message: String) {
        Log.i(tag, message)
    }
}