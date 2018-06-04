package com.febers.uestc_bbs.base

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

class BaseApplication: Application() {


    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        var context: Context by Delegates.notNull()
            private set
    }
}