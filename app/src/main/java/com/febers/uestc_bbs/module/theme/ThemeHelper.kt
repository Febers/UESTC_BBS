package com.febers.uestc_bbs.module.theme

import android.content.Context
import android.graphics.Color
import android.view.View
import com.afollestad.aesthetic.Aesthetic
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.utils.ColorUtils
import com.febers.uestc_bbs.utils.PreferenceUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout

enum class AppColor{
    COLOR_PRIMARY,
    COLOR_ACCENT,
    COLOR_BACKGROUND
}

const val COLOR_PRIMARY_DARK = "color_primary_dark"

object ThemeHelper {

    private const val LLCP = "last_light_color_primary"
    private const val darkInterval = -200
    private val blueIntValue = Color.parseColor("#2196f3")

    private val themeChangeSubscribers: MutableList<View> = ArrayList()

    fun dayAndNightThemeChange(context: Context) {
        if (isDarkTheme()) {
            val lastColorPrimary by PreferenceUtils(context, LLCP, 2201331)
            val colorDark by PreferenceUtils(context, COLOR_PRIMARY_DARK, true)
            Aesthetic.config {
                activityTheme(R.style.AppThemeLight)
                isDark(false)
                colorPrimary(lastColorPrimary)
                colorPrimaryDark(if (colorDark) ColorUtils.toDarkColor(lastColorPrimary) else lastColorPrimary)
                colorAccent(lastColorPrimary)
                attribute(R.attr.app_color_primary, lastColorPrimary)
                colorWindowBackgroundRes(R.color.color_white)
                colorStatusBarAuto()
            }
        } else {
            var lastColorPrimary by PreferenceUtils(context, LLCP, -2201331)
            lastColorPrimary = getColorPrimary()
            Aesthetic.config {
                activityTheme(R.style.AppThemeDark)
                isDark(true)
                colorPrimaryRes(R.color.color_black_tint)
                colorAccentRes(R.color.color_gray_light)
                colorPrimaryDarkRes(R.color.color_black_tint)
                colorWindowBackgroundRes(R.color.color_black_tint)
                attributeRes(R.attr.app_color_primary, R.color.color_black_tint)
                colorStatusBarAuto()
            }
        }
    }

    fun setTheme(context: Context) {
        setTheme(context, blueIntValue, blueIntValue)
    }

    fun setTheme(context: Context, colorPrimary: Int, colorAccent: Int) {
        val colorDark by PreferenceUtils(context, COLOR_PRIMARY_DARK, true)
        Aesthetic.config {
            colorPrimary(colorPrimary)
            colorAccent(colorAccent)
            colorPrimaryDark(if (colorDark) ColorUtils.toDarkColor(colorPrimary) else colorPrimary)
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

    fun getColorPrimary(): Int =  Aesthetic.get().colorPrimary().blockingFirst()

    fun getColorAccent(): Int = Aesthetic.get().colorAccent().blockingFirst()

    fun getColorBackground(): Int = Aesthetic.get().colorWindowBackground().blockingFirst()

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

    fun isDarkTheme(): Boolean = Aesthetic.get().isDark.blockingFirst()

    fun getTextColorPrimary(): Int = Aesthetic.get().textColorPrimary().blockingFirst()

    fun getRefreshTextColor(): Int = if (ColorUtils.isLightColor(getColorPrimary()))
        Color.GRAY
    else Color.WHITE

    fun getBottomNavigationColorAccent(): Int = if (ColorUtils.isLightColor(getColorPrimary()))
        Color.BLACK
    else getColorAccent()
}