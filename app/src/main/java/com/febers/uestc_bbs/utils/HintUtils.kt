package com.febers.uestc_bbs.utils

import android.app.Activity
import android.view.View
import android.widget.Toast
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.base.HINT_BY_SNACK_BAR
import com.febers.uestc_bbs.base.HINT_BY_TOAST
import com.febers.uestc_bbs.base.HINT_WAY
import com.google.android.material.snackbar.Snackbar
import org.jetbrains.anko.contentView

/**
 * 提示用户消息的工具类
 * 当前使用toast展示
 *
 */
object HintUtils {

    private var toast: Toast? = null

    /**
     * 传递两个参数，此时根据用户的设置，决定使用哪种表现方式
     * 此方法一般在BaseActivity和BaseFragment中调用，其他地方不需要
     *
     * @param activity
     * @param msg
     */
    fun show(activity: Activity?, msg: String) {
        if (activity == null || activity.contentView == null) {
            show(msg)
            return
        }
        val hintWay by PreferenceUtils(MyApp.context(), HINT_WAY, HINT_BY_TOAST)
        if (hintWay == HINT_BY_TOAST) {
            showByToast(msg)
        } else if (hintWay == HINT_BY_SNACK_BAR) {
            showBySnackBar(activity.contentView, msg)
        }
    }

    /**
     * 传递一个参数，此时采用toast展示
     *
     * @param msg
     */
    fun show(msg: String) {
        showByToast(msg)
    }

    fun showByToast(msg: String) {
        toast = if (toast != null) {
            (toast as Toast).cancel()
            Toast.makeText(MyApp.context(), msg, Toast.LENGTH_SHORT)
        } else {
            Toast.makeText(MyApp.context(), msg, Toast.LENGTH_SHORT)
        }
        (toast as Toast).show()
    }

    fun showBySnackBar(view: View?, msg: String) {
        view ?: return
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show()
    }
}