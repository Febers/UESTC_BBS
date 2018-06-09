/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

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