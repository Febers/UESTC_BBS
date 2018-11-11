package com.febers.uestc_bbs.module.setting

import android.content.Context
import android.content.Intent
import android.os.Process
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.utils.ApiUtils
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.beta.upgrade.UpgradeListener
import com.tencent.bugly.crashreport.CrashReport
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

class BuglyHelper {

    fun init() {
        val packageName = MyApp.context().packageName
        val processName = getProcessName(Process.myPid())
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(MyApp.context())
        strategy.isUploadProcess = processName == null || processName == packageName

        Beta.autoInit = true
        Beta.autoCheckUpgrade = true
        Beta.upgradeListener = UpgradeListener { ret, strategy, isManual, isSilence ->
            if (strategy != null) {
                MyApp.context().startActivity(
                        Intent().apply {
                            setClass(MyApp.context(), UpdateActivity::class.java)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                )
            }
        }
        Bugly.init(MyApp.context(), ApiUtils.BUGLY_APP_ID, false)
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
}
