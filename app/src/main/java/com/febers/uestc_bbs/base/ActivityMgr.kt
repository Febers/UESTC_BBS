package com.febers.uestc_bbs.base

import android.app.Activity

import java.util.Stack

/**
 * Activity的管理栈，防止用户不停跳转Activity，造成Activity生命周期的不可控
 * 此栈有一个最大值，maxSize，在push方法中读取改值，
 * 当栈内的Activity数目为maxSize时，pop除了栈顶和栈底外的所有Activity
 * 栈顶的Activity为当前打开的Activity，栈底为HomeActivity
 */
object ActivityMgr {

    private var activityStack: Stack<Activity> = Stack()
    private val deleteStack = Stack<Activity>()
    private const val maxActivityCount = 10

    /**
     * 获得现在栈内还有多少activity
     *
     * @return 个数
     */
    val count: Int
        get() = activityStack.size

    fun getActivityStack(): Stack<Activity>? {
        return activityStack
    }

    /**
     * 返回当前栈顶的activity
     *
     * @return
     */
    fun currentActivity(): Activity? {
        return if (activityStack.size == 0) {
            null
        } else activityStack.lastElement()
    }

    /**
     * 栈内是否包含此activity
     *
     * @param cls
     * @return
     */
    fun isContains(cls: Class<*>): Boolean {
        for (activity in activityStack) {
            if (activity.javaClass == cls) {
                return true
            }
        }
        return false
    }

    /**
     * 栈内是否包含此activity
     *
     * @param a
     * @return
     */
    fun isContains(a: Activity): Boolean {
        for (activity in activityStack) {
            if (activity == a) {
                return true
            }
        }
        return false
    }


        /**
     * activity入栈
     * 一般在baseActivity的onCreate里面加入
     *
     * @param activity
     */
    fun pushActivity(activity: Activity) {
        if (count >= maxActivityCount) {
            //TODO
            //popAllActivityUntilOne(HomeActivity::class.java)
        }
        activityStack.add(activity)
    }


    /**
     * 移除栈顶第一个activity
     */
    fun popTopActivity() {
        val activity = activityStack.lastElement()
        if (activity != null && !activity.isFinishing) {
            activity.finish()
        }
    }

    /**
     * activity出栈
     * 一般在baseActivity的onDestroy里面加入
     */
    fun popActivity(activity: Activity) {
        activityStack.remove(activity)
        if (!activity.isFinishing) {
            activity.finish()
        }
    }

    /**
     * activity出栈
     * 一般在baseActivity的onDestroy里面加入
     */
    fun popActivity(cls: Class<*>) {
        var deleteActivity: Activity? = null
        for (activity in activityStack) {
            if (activity.javaClass == cls && !activity.isFinishing) {
                deleteActivity = activity
                activity.finish()
            }
        }
        activityStack.remove(deleteActivity)
    }


    /**
     * 从栈顶往下移除 直到cls这个activity为止
     * 如： 现有ABCD popAllActivityUntilOne(B.class)
     * 则： 还有AB存在
     *
     *
     * 注意此方法 会把自身也finish掉
     *
     * @param cls
     */
    fun popAllActivityUntilOne(cls: Class<*>) {
        while (true) {
            val activity = currentActivity() ?: break
            if (activity.javaClass == cls) {
                break
            }
            popActivity(activity)
        }
    }

    /**
     * 所有的栈元素 除了 cls的留下 其他全部移除
     * 如： 现有ABCD popAllActivityUntilOne(B.class)
     * 则： 只有B存在
     * 注意此方法 会把自身也finish掉
     */
    fun popAllActivityExceptOne(cls: Class<*>) {
        val iterator = activityStack.iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next() as Activity
            if (activity.javaClass != cls && !activity.isFinishing) {
                iterator.remove()
                activity.finish()
            }
        }
    }

    /**
     * 移除所有的activity
     * 退出应用的时候可以调用
     * （非杀死进程）
     */
    fun popAllActivity() {
        for (i in activityStack.indices) {
            if (null != activityStack[i] && !activityStack[i].isFinishing) {
                activityStack[i].finish()
            }
        }
        activityStack.clear()
    }
}
