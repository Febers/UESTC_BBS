package com.febers.uestc_bbs.utils

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.MyApplication
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
        val code by PreferenceUtils(MyApplication.context(), MyApplication.context().getString(R.string.sp_theme_code), DEFAULT_THEME_CODE)
        return when(code) {
            MY_THEME_GREEN  -> R.style.GreenTheme
            MY_THEME_RED -> R.style.RedTheme
            MY_THEME_PINK -> R.style.PinkTheme
            MY_THEME_CYAN -> R.style.CyanTheme
            MY_THEME_TEAL -> R.style.TealTheme
            MY_THEME_PURPLE -> R.style.PurpleTheme
            MY_THEME_BLUE -> R.style.BlueTheme
            MY_THEME_GRAY -> R.style.GrayTheme
            else -> R.style.BlueTheme
        }
    }

    fun saveTheme(position: Int) {
        var code by PreferenceUtils(MyApplication.context(), MyApplication.context().getString(R.string.sp_theme_code), MY_THEME_BLUE)
        code = position
    }

    fun getBottomSheetTheme() {

    }
}