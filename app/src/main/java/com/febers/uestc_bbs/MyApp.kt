package com.febers.uestc_bbs

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.Intent
import android.os.Process
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.io.UserHelper
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.setting.refresh_style.REFRESH_HEADER_CODE
import com.febers.uestc_bbs.module.setting.refresh_style.RefreshViewHelper
import com.febers.uestc_bbs.utils.PreferenceUtils
import kotlin.properties.Delegates
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import androidx.multidex.MultiDexApplication
import com.febers.uestc_bbs.module.setting.UpdateActivity
import com.febers.uestc_bbs.utils.ApiUtils
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.upgrade.UpgradeListener
import com.tencent.bugly.crashreport.CrashReport
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class MyApp: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return
//        }
//        LeakCanary.install(this)

        context = applicationContext

        /**
         * 初始化Bugly
         */
        val packageName = applicationContext.packageName
        val processName = getProcessName(Process.myPid())
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(MyApp.context())
        strategy.isUploadProcess = processName == null || processName == packageName

        Beta.autoInit = true
        Beta.autoCheckUpgrade = true
        Beta.upgradeListener = UpgradeListener { ret, strategy, isManual, isSilence ->
            if (strategy != null) {
                context.startActivity(
                        Intent().apply {
                            setClass(context, UpdateActivity::class.java)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                )
            }
        }
        Bugly.init(context, ApiUtils.BUGLY_APP_ID, false)
    }

    companion object {
        private var context: Context by Delegates.notNull()
        var uiHidden: Boolean = false
        fun context() = context
        fun getUser(): UserSimpleBean = UserHelper.getNowUser()

        init {
            /**
             * 初始化刷新控件
             */
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

    /**
     * 系统根据不同的内存状态回调该线程
     *
     * @param level 不同的内存状态
     * TRIM_MEMORY_COMPLETE：内存不足，并且该进程在后台进程列表最后一个，马上就要被清理
     * TRIM_MEMORY_MODERATE：内存不足，并且该进程在后台进程列表的中部。
     * TRIM_MEMORY_BACKGROUND：内存不足，并且该进程是后台进程。
     * TRIM_MEMORY_UI_HIDDEN：内存不足，并且该进程的UI已经不可见了。
     */
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            uiHidden = true
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }

    /**
     * 在系统内存不足，所有后台程序都被杀死时，
     * 系统会调用OnLowMemory
     */
    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
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
}
