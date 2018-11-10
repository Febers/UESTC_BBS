/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.io.UserHelper
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.setting.refresh_style.REFRESH_HEADER_CODE
import com.febers.uestc_bbs.module.setting.refresh_style.RefreshViewHelper
import com.febers.uestc_bbs.utils.PreferenceUtils
import kotlin.properties.Delegates
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import com.tencent.bugly.Bugly
import android.content.Intent
import com.febers.uestc_bbs.module.setting.UpdateActivity
import com.febers.uestc_bbs.utils.ApiUtils
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.upgrade.UpgradeListener
import com.tencent.bugly.crashreport.CrashReport
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException


class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        private var context: Context by Delegates.notNull()
        var uiHidden: Boolean = false
        fun context() = context
        fun getUser(): UserSimpleBean = UserHelper.getNowUser()

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

            /**
             * 获取进程号对应的进程名
             *
             * @param pid 进程号
             * @return 进程名
             */
            fun getProcessName(pid: Int): String? {
                var reader: BufferedReader? = null
                try {
                    reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
                    var processName = reader.readLine()
                    if (!processName.isEmpty()) {
                        processName = processName.trim()
                    }
                    return processName
                } catch (throwable: Throwable) {
                    throwable.printStackTrace()
                } finally {
                    try {
                        reader?.close()
                    } catch (exception: IOException) {
                        exception.printStackTrace()
                    }
                }
                return null
            }

            fun initBugly() {
                val packageName = context().packageName
                val processName = getProcessName(android.os.Process.myPid())
                // 设置是否为上报进程
                val strategy = CrashReport.UserStrategy(context())
                strategy.isUploadProcess = processName == null || processName == packageName

                Beta.autoInit = true
                Beta.autoCheckUpgrade = true
                Beta.upgradeListener = UpgradeListener { ret, strategy, isManual, isSilence ->
                    if (strategy != null) {
                        context().startActivity(
                                Intent().apply {
                                    setClass(context(), UpdateActivity::class.java)
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                }
                        )
                    }
                }
                Bugly.init(context(), ApiUtils.BUGLY_APP_ID, false)
            }
            initBugly()
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
