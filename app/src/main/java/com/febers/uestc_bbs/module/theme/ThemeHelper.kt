package com.febers.uestc_bbs.module.theme

import android.graphics.Color
import android.view.View
import com.afollestad.aesthetic.Aesthetic
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.febers.uestc_bbs.R
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlin.math.absoluteValue

enum class AppColor{
    COLOR_PRIMARY,
    COLOR_ACCENT,
    COLOR_BACKGROUND
}

object ThemeHelper {

    private const val whiteColorBoundaryValue = 1000000
    private val themeChangeSubscribers: MutableList<View> = ArrayList()

    fun defaultTheme() {
        setTheme(Color.parseColor("#db4437"), Color.parseColor("#db4437"))
    }

    fun dayAndNightThemeChange() {
        if (Aesthetic.get().isDark.blockingFirst()) {
            Aesthetic.config {
                activityTheme(R.style.DefaultThemeLight)
                isDark(false)
                colorPrimaryRes(R.color.color_blue_zhihu)
                colorAccentRes(R.color.color_blue_zhihu)
                attributeRes(R.attr.app_color_primary, R.color.color_blue_zhihu)
                colorWindowBackgroundRes(R.color.color_white)
                colorStatusBarAuto()
            }
        } else {
            Aesthetic.config {
                activityTheme(R.style.DefaultThemeDark)
                isDark(true)
                colorPrimaryRes(R.color.color_black)
                colorAccentRes(R.color.color_black)
                colorWindowBackgroundRes(R.color.color_black)
                attributeRes(R.attr.app_color_primary, R.color.color_black)
                colorStatusBarAuto()
            }
        }
    }

    fun setTheme(colorPrimary: Int, colorAccent: Int) {
        Aesthetic.config {
            colorPrimary(colorPrimary)
            colorAccent(colorAccent)
            colorPrimaryDark(colorPrimary)
            lightStatusBarMode()
            attribute(R.attr.app_color_primary, colorPrimary, true)
            attribute(R.attr.app_color_accent, colorAccent, true)
            colorStatusBarAuto()
        }
        onThemeChange(colorPrimary, colorAccent)
    }

    fun getColor(color: AppColor): Int {
        return when(color) {
            AppColor.COLOR_PRIMARY -> Aesthetic.get().colorPrimary().blockingFirst()
            AppColor.COLOR_ACCENT -> Aesthetic.get().colorAccent().blockingFirst()
            AppColor.COLOR_BACKGROUND -> Aesthetic.get().colorWindowBackground().blockingFirst()
        }
    }

    fun  subscribeOnThemeChange(view: View) {
        themeChangeSubscribers.add(view)
    }

    private fun onThemeChange(colorPrimary: Int, colorAccent: Int) {
        themeChangeSubscribers.forEach {
            if (it is SmartRefreshLayout) {
                it.setPrimaryColors(colorPrimary, getRefreshTextColor())
            }
            if (it is AHBottomNavigation) {
                it.accentColor = getBottomNavigationColorAccent()
            }
        }
    }

    fun getRefreshTextColor(): Int = if (getColor(AppColor.COLOR_PRIMARY).absoluteValue < whiteColorBoundaryValue)
        Color.GRAY
    else Color.WHITE

    fun getBottomNavigationColorAccent(): Int = if (getColor(AppColor.COLOR_PRIMARY).absoluteValue < whiteColorBoundaryValue)
        Color.BLACK
    else getColor(AppColor.COLOR_ACCENT)
}