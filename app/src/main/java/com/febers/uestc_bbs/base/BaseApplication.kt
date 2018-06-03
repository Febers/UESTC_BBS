package com.febers.uestc_bbs.base

import android.app.Application
import android.content.Context

class BaseApplication: Application() {

    private var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    fun getContext(): Context? {
        return context
    }
}