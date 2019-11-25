package com.febers.uestc_bbs.base

import android.content.Context
import org.jetbrains.anko.runOnUiThread
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ThreadMgr {

    private var executor: ThreadPoolExecutor

    init {
        val corePoolSize = Runtime.getRuntime().availableProcessors()*2+1
        val maximunPoolSize = corePoolSize
        val keepAliveTime = 1L
        executor = ThreadPoolExecutor(corePoolSize,
                maximunPoolSize,
                keepAliveTime,
                TimeUnit.MINUTES,
                LinkedBlockingDeque<Runnable>(),
                Executors.defaultThreadFactory(),
                ThreadPoolExecutor.AbortPolicy())
    }

    fun remove(runnable: Runnable) {
        executor.remove(runnable)
    }

    fun network(func: () -> Unit) {
        executor.execute {
            func.invoke()
        }
    }

    fun io(func: () -> Unit) {
        executor.execute {
            func.invoke()
        }
    }

    fun ui(context: Context, func: () -> Unit) {
        context.runOnUiThread {
            func.invoke()
        }
    }
}