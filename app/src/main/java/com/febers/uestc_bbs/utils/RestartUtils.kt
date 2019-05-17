package com.febers.uestc_bbs.utils

import android.content.Intent

import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.base.ActivityMgr
import com.febers.uestc_bbs.base.RESTART_APP
import com.febers.uestc_bbs.home.SplashActivity

object RestartUtils {

    /**
     * 重新启动App -> 杀进程,会短暂黑屏,启动慢
     */
    fun restartApp() {
        //启动页
        val intent = Intent(MyApp.context(), SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        MyApp.context().startActivity(intent)
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    /**
     * 重新启动App -> 不杀进程,缓存的东西不清除,启动快
     */
    fun restartApp2() {
        val intent = MyApp.context().packageManager
                .getLaunchIntentForPackage(MyApp.context().packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent?.putExtra(RESTART_APP, true)
        MyApp.context().startActivity(intent)
    }
}
