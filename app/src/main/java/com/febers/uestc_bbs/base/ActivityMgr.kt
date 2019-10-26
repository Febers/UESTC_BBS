package com.febers.uestc_bbs.base

import android.app.Activity
import com.febers.uestc_bbs.utils.log
import java.lang.ref.WeakReference
import java.util.*

/**
 * 目前只为 主题切换时，跳转新的主界面、finish 之前的主界面 服务
 */
object ActivityMgr {

    private val activityMap: MutableMap<String, WeakReference<Activity>> = HashMap()

    fun putActivity(activity: Activity) {
        activityMap[activity.toString()] = WeakReference(activity)
    }

    fun removeActivity(activity: Activity) {
        activityMap.remove(activity.toString())
    }

    fun removeAllActivitiesExceptOne(activity: Activity) {
        log { "退出除调用方($activity)外的所有activity, 当前map数量为：${activityMap.size}" }
        val iterator = activityMap.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.key != activity.toString()) {
                iterator.remove()
                entry.value.get()?.finish()
            }
        }
    }

    fun removeAllActivities() {
        activityMap.forEach {
            activityMap.remove(it.key)
            it.value.get()?.finish()
        }
    }
}
