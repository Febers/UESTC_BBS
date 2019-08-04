package com.febers.uestc_bbs

import android.content.Intent
import com.febers.uestc_bbs.module.more.ExceptionActivity
import com.febers.uestc_bbs.utils.log

class UEHandler private constructor() : Thread.UncaughtExceptionHandler {

    private var defaultHandler: Thread.UncaughtExceptionHandler? = null

    fun init() {
        log("init UEHandler")
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(this)
        log("default: ${Thread.getDefaultUncaughtExceptionHandler()}")
    }

    override fun uncaughtException(p0: Thread, p1: Throwable) {
        val thread: String = p0.toString()
        val trace = StringBuilder()
        p1.stackTrace.forEach {
            trace.append(it)
        }
        log("[Uncaught Exception] Thread: $thread , Throwable: $trace")
        MyApp.context().startActivity(Intent(MyApp.context(), ExceptionActivity::class.java).apply {
            putExtra("thread", thread)
            putExtra("throwable", trace.toString())
        })
        if (defaultHandler != null) {
            defaultHandler!!.uncaughtException(p0, p1)
        }
    }

    companion object {
        private val INSTANCE = UEHandler()
        fun getInstance(): UEHandler = INSTANCE

        fun testDivZeroException() {
            log("[Exception Test: 1/0]")
            var i = 1/0
        }

        fun testNullPointerException() {
            log("[Exception Test: Null Pointer]")
            val i: String? = null
            i!!.length
        }

        fun testLimitlessRecursion() {
            log("[Exception Test: Limited Recursion]")
            fun test() { test() }
            test()
        }
    }
}