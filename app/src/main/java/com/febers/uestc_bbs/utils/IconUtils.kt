package com.febers.uestc_bbs.utils

import android.content.Intent
import android.app.Activity
import android.app.ActivityManager
import android.content.pm.PackageManager
import android.content.ComponentName
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.module.setting.ICON_CODE


/**
 * 通过动态关闭或者开启组件的方式，更换桌面图标
 * 需要Manifest文件设置对应的activity-alias标签
 */
object IconUtils {

    /**
     * 更改图标,参考 https://blog.csdn.net/fu908323236/article/details/78813136
     * 需要注意activityPath需要跟Manifest中activity-alias的"name"标签的值相同
     *
     * @param iconCode
     * @param lastComponentName
     * @param newComponentName
     */
    fun changeIcon(iconCode: Int, lastComponentName: String, newComponentName: String) {
        disableComponent(MyApp.context().packageManager, ComponentName(MyApp.context(), lastComponentName))
        enableComponent(MyApp.context().packageManager, ComponentName(MyApp.context(), newComponentName))
        var lastCode: Int by PreferenceUtils(MyApp.context(), ICON_CODE, 0)
        lastCode = iconCode
    }

    /**
     * 启用组件
     *
     * @param packageManager
     * @param componentName
     */
    private fun enableComponent(packageManager: PackageManager, componentName: ComponentName) {
        val state = packageManager.getComponentEnabledSetting(componentName)
        if (state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
            //已经启用
            return
        }
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
    }

    /**
     * 禁用组件
     *
     * @param packageManager
     * @param componentName
     */
    private fun disableComponent(packageManager: PackageManager, componentName: ComponentName) {
        val state = packageManager.getComponentEnabledSetting(componentName)
        if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED) {
            //已经禁用
            return
        }
        packageManager.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
    }

    /**
     * 重启桌面，可以加速icon的更换显示效率
     *
     * @param activity
     * @param pm
     */
    private fun restartSystemLauncher(activity: Activity, pm: PackageManager) {
        val am = activity.getSystemService(Activity.ACTIVITY_SERVICE) as ActivityManager?
        val i = Intent(Intent.ACTION_MAIN)
        i.addCategory(Intent.CATEGORY_HOME)
        i.addCategory(Intent.CATEGORY_DEFAULT)
        val resolves = pm.queryIntentActivities(i, 0)
        for (res in resolves) {
            if (res.activityInfo != null) {
                am!!.killBackgroundProcesses(res.activityInfo.packageName)
            }
        }
    }
}