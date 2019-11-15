package com.febers.uestc_bbs.utils

import android.content.Context
import android.view.WindowManager
import com.febers.uestc_bbs.MyApp

fun getWindowWidth(): Int {
    val windowManager: WindowManager = MyApp.context().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.defaultDisplay.width
}

fun getWindowHeiht(): Int {
    val windowManager: WindowManager = MyApp.context().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return windowManager.defaultDisplay.height
}