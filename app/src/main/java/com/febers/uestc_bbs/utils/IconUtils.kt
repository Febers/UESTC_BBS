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
     * 更改图标之后，ide的调试将失败，需要重新改回来才能调试
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
     * 使用另一个方法，正常情况下，可以成功地更换icon
     * 但是当用户清除应用数据之后，由于sp的数据被清除，但是已开启/关闭的组件并不会变化
     * 由此将造成数据的不对应，之后再次更换图标，就会出现多个组件被开启，出现多个图标
     * 因此，采用该方法，先关闭所有的组件，然后再打开新选中的
     *
     * @param iconCode
     * @param newComponentName
     * @param allComponentName
     */
    fun changeIcon(iconCode: Int, newComponentName: String, allComponentName: Array<String>) {
        allComponentName.forEach {
            if (it != newComponentName) {
                disableComponent(MyApp.context().packageManager, ComponentName(MyApp.context(), it))
            }
        }
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
//                am!!.killBackgroundProcesses(res.activityInfo.packageName)
            }
        }
    }
}