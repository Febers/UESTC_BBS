package com.febers.uestc_bbs.utils

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseApplication

const val MY_THEME_RED = 0
const val MY_THEME_BLUE = 0
const val MY_THEME_PURPLE = 0
const val MY_THEME_GREEN = 0
const val MY_THEME_YELLOW = 0
const val MY_THEME_PINK = 0
const val MY_THEME_TEAL = 0
const val MY_THEME_CYAN = 0

object ThemeUtils {
    fun getTheme() : Int{
        var code by PreferenceUtils(BaseApplication.context(), "theme_code", MY_THEME_RED)
        when(code) {
            MY_THEME_RED  -> return R.style.PinkTheme
            MY_THEME_BLUE -> return R.style.PinkTheme
            MY_THEME_PURPLE -> return R.style.PinkTheme
            MY_THEME_GREEN -> return R.style.PinkTheme
            MY_THEME_YELLOW -> return R.style.PinkTheme
            MY_THEME_PINK -> return R.style.PinkTheme
            MY_THEME_TEAL -> return R.style.PinkTheme
            else -> return R.style.PinkTheme
        }
    }

    fun saveTheme(position: Int) {
        var code by PreferenceUtils(BaseApplication.context(), "theme_code", MY_THEME_RED)
        code = position
    }
}