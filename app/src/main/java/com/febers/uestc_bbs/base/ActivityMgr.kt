package com.febers.uestc_bbs.base

import android.app.Activity
import java.util.*

/**
 * 目前只为 主题切换时，跳转新的主界面、finish 之前的主界面 服务
 */
object ActivityMgr {

    private val activityMap: MutableMap<String, Activity> = WeakHashMap()

    fun putActivity(activity: Activity) {
        activityMap[activity.toString()] = activity
    }

    fun removeActivity(activity: Activity) {
        activityMap.remove(activity.toString())
    }

    fun removeAllActivitiesExceptOne(activity: Activity) {
        val shouldRemoveMap = activityMap.filter {
            it.key != activity.toString()
        }
        shouldRemoveMap.forEach {
            activityMap.remove(it.key)
            it.value.finish()
        }
    }

    fun removeAllActivities() {
        activityMap.forEach {
            activityMap.remove(it.key)
            it.value.finish()
        }
    }
}
