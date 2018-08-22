/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.base

import android.app.Application
import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.dao.UserSaver
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.utils.PreferenceUtils
import kotlin.properties.Delegates
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshHeader
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import android.support.annotation.NonNull
import android.util.TypedValue
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.api.RefreshFooter
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator
import com.scwang.smartrefresh.layout.header.BezierRadarHeader
import com.scwang.smartrefresh.layout.header.ClassicsHeader





class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        private var context: Context by Delegates.notNull()
        fun context() = context
        fun getUser(): UserBean {
            val uid by PreferenceUtils(context, context.getString(R.string.sp_user_uid), "")
            return UserSaver.get(uid)
        }
        init {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
//                layout.setPrimaryColorsId(typeValue.data, android.R.color.white);//全局设置主题颜色
                ClassicsHeader(context)
            }
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                layout.setFooterHeight(38f)
                ClassicsFooter(context).setDrawableSize(20f)
            }
        }
    }
}