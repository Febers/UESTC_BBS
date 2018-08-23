package com.febers.uestc_bbs.utils

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.base.DEFAULT_THEME_CODE

const val MY_THEME_GREEN = 0
const val MY_THEME_RED = 1
const val MY_THEME_PINK = 2
const val MY_THEME_CYAN = 3
const val MY_THEME_TEAL = 4
const val MY_THEME_PURPLE = 5
const val MY_THEME_BLUE = 6
const val MY_THEME_GRAY = 7

object ThemeUtils {
    fun getTheme() : Int{
        var code by PreferenceUtils(BaseApplication.context(), "theme_code", DEFAULT_THEME_CODE)
        when(code) {
            MY_THEME_GREEN  -> return R.style.GreenTheme
            MY_THEME_RED -> return R.style.RedTheme
            MY_THEME_PINK -> return R.style.PinkTheme
            MY_THEME_CYAN -> return R.style.CyanTheme
            MY_THEME_TEAL -> return R.style.TealTheme
            MY_THEME_PURPLE -> return R.style.PurpleTheme
            MY_THEME_BLUE -> return R.style.BlueTheme
            MY_THEME_GRAY -> return R.style.GrayTheme
            else -> return R.style.BlueTheme
        }
    }

    fun saveTheme(position: Int) {
        var code by PreferenceUtils(BaseApplication.context(), "theme_code", MY_THEME_BLUE)
        code = position
    }
}