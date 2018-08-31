package com.febers.uestc_bbs.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyBoardUtils {

    fun openKeyboard(mEditText: EditText, mContext: Context) {
        val imm = mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY)
    }


    fun closeKeyboard(mEditText: EditText, mContext: Context) {
        val imm = mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    fun isSoftInputShow(activity: Activity): Boolean {
        // 虚拟键盘隐藏 判断view是否为空
        val view = activity.window.peekDecorView()
        if (view != null) {
            // 隐藏虚拟键盘
            val inputmanger = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            return inputmanger!!.isActive() && activity.window.currentFocus != null
        }
        return false
}
}