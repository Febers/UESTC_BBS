package com.febers.uestc_bbs.utils

import android.app.Activity
import android.content.Context
import android.support.v7.widget.SearchView
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyBoardUtils {

    fun openKeyboard(editText: EditText, context: Context) {
        val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY)
    }


    fun closeKeyboard(editText: EditText, context: Context) {
        val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    fun closeKeyboard(searchView: SearchView, context: Context) {
        val imm = context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchView.windowToken, 0)
    }

    fun isSoftInputShow(activity: Activity): Boolean {
        // 虚拟键盘隐藏 判断view是否为空
        val view = activity.window.peekDecorView()
        if (view != null) {
            // 隐藏虚拟键盘
            val inputManger = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            return inputManger.isActive && activity.window.currentFocus != null
        }
        return false
}
}