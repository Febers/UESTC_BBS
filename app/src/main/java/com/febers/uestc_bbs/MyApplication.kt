/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.util.Log.i
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.base.SP_USER_ID
import com.febers.uestc_bbs.dao.UserStore
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.utils.PreferenceUtils
import kotlin.properties.Delegates
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader


class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        private var context: Context by Delegates.notNull()
        fun context() = context
        fun getUser(): UserSimpleBean {
            val uid by PreferenceUtils(context, SP_USER_ID, "")
            return UserStore.get(uid)
        }
        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                ClassicsHeader(context)
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                layout.setFooterHeight(38f)
                BallPulseFooter(context)
            }
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        i("App", "trim")
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        i("App", "low")
        Glide.get(this).clearMemory()
    }
}