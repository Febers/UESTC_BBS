package com.febers.uestc_bbs.base.exception

import android.content.Intent
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.utils.log
import org.jetbrains.anko.getStackTraceString

class ExceptionHandler private constructor() : Thread.UncaughtExceptionHandler {

    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    fun init() {
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(p0: Thread, p1: Throwable) {
        val thread: String = p0.toString()
//        log("捕获到异常:[Uncaught Exception] Thread: $thread , Throwable: $trace")
        val intent = Intent(MyApp.context(), ExceptionActivity::class.java).apply {
            putExtra("thread", thread)
            putExtra("throwable", p1.getStackTraceString())
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        MyApp.context().startActivity(intent)
        defaultHandler?.uncaughtException(p0, p1)
    }

    companion object {

        @Volatile private var INSTANCE = ExceptionHandler()

        fun getInstance(): ExceptionHandler = INSTANCE
    }
}