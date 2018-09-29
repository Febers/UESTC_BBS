/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs

import android.app.Application
import android.content.Context
import com.febers.uestc_bbs.dao.UserStore
import com.febers.uestc_bbs.entity.UserBean
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
        fun getUser(): UserBean {
            val uid by PreferenceUtils(context, context.getString(R.string.sp_user_uid), "")
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
}