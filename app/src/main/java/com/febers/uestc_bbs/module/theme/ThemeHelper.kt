package com.febers.uestc_bbs.module.theme

import android.content.Context
import android.graphics.Color
import android.view.View
import com.afollestad.aesthetic.Aesthetic
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.febers.uestc_bbs.MyApp
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
const val MY_COLOR_PRIMARY = "my_color_primary"

object ThemeHelper {

    private const val LLCP = "last_light_color_primary"
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
                colorWindowBackground(res = R.color.color_white)
                //colorWindowBackgroundRes(R.color.color_white)
                colorStatusBarAuto()
            }
            setColorPrimaryToSp(lastColorPrimary)
        } else {
            var lastColorPrimary by PreferenceUtils(context, LLCP, -2201331)
            lastColorPrimary = getColorPrimary()
            Aesthetic.config {
                activityTheme(R.style.AppThemeDark)
                isDark(true)
                colorPrimary(res = R.color.color_black_tint)
                //colorPrimaryRes(R.color.color_black_tint)
                colorAccent(res = R.color.color_gray_light)
                //colorAccentRes(R.color.color_gray_light)
                colorPrimaryDark(res = R.color.color_black_tint)
                //colorPrimaryDarkRes(R.color.color_black_tint)
                colorWindowBackground(res = R.color.color_black_tint)
                //colorWindowBackgroundRes(R.color.color_black_tint)
                attribute(R.attr.app_color_primary, res = R.color.color_black_tint)
                //attributeRes(R.attr.app_color_primary, R.color.color_black_tint)
                colorStatusBarAuto()
            }
            setColorPrimaryToSp(Color.parseColor("#1d2323"))
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
            attribute(R.attr.app_color_primary, colorPrimary, null, true)
            attribute(R.attr.app_color_accent, colorAccent, null, true)
            colorStatusBarAuto()
        }
        setColorPrimaryToSp(colorPrimary)
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

    /**
     * 保存的colorPrimary，用于闪屏
     */
    fun getColorPrimaryBySp(): Int {
        val colorPrimary by PreferenceUtils(MyApp.context(), MY_COLOR_PRIMARY, blueIntValue)
        return colorPrimary
    }

    /**
     * 每次更换主题的时候记得更新此值
     *
     * @param color
     */
    fun setColorPrimaryToSp(color: Int) {
        var colorPrimary by PreferenceUtils(MyApp.context(), MY_COLOR_PRIMARY, blueIntValue)
        colorPrimary = color
    }

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

    //fun getPMLeftTextColor(): Int = if (isDarkTheme()) Color.GRAY else Color.WHITE

    fun getBottomNavigationColorAccent(): Int = if (ColorUtils.isLightColor(getColorPrimary()))
        Color.BLACK
    else getColorAccent()
}