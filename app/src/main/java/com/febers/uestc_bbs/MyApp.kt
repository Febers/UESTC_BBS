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
import com.febers.uestc_bbs.dao.UserStore
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.setting.refresh_style.REFRESH_HEADER_CODE
import com.febers.uestc_bbs.module.setting.refresh_style.RefreshViewHelper
import com.febers.uestc_bbs.utils.PreferenceUtils
import kotlin.properties.Delegates
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.BallPulseFooter


class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        private var context: Context by Delegates.notNull()
        var uiHidden: Boolean = false
        fun context() = context
        fun getUser(): UserSimpleBean = UserStore.getNowUser()

        init {

            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                val styleCode by PreferenceUtils(context, REFRESH_HEADER_CODE, 0)
                val helper = RefreshViewHelper(context)
                helper.getHeader(styleCode)
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                layout.setFooterHeight(38f)
                BallPulseFooter(context)
            }
        }
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            uiHidden = true
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }



    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }
}
