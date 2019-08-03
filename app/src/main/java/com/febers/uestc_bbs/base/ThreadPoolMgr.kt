package com.febers.uestc_bbs.base

import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ThreadPoolMgr {

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

    fun execute(runnable: Runnable) {
        executor.execute(runnable)
    }

    fun remove(runnable: Runnable) {
        executor.remove(runnable)
    }
}