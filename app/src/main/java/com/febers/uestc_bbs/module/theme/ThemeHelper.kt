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
import com.febers.uestc_bbs.utils.log
import com.scwang.smartrefresh.layout.SmartRefreshLayout

const val COLOR_PRIMARY_DARK = "color_primary_dark"
const val MY_COLOR_PRIMARY = "my_color_primary"

object ThemeHelper {

    private const val LLCP = "last_light_color_primary"
    private val blueIntValue = Color.parseColor("#2196f3")

    private val themeChangeSubscribers: MutableList<View> = ArrayList()

    /**
     * 进行日间主题和夜间主题的切换
     * 首先打开一个过渡的Activity
     * 在过渡的Activity中切换到新的主Activity
     * 主Activity应该知道它是更换主题之后的，所以需要finish之前的主Activity
     *
     * @param context
     */
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
                colorStatusBarAuto()
            }
            setColorPrimaryToSp(lastColorPrimary)
        } else {
            var lastColorPrimary by PreferenceUtils(context, LLCP, -2201331)
            lastColorPrimary = getColorPrimary()
            Aesthetic.config {
                activityTheme(R.style.AppThemeDark)
                isDark(true)
                colorPrimary(res = R.color.color_black)
                colorAccent(res = R.color.color_gray_light)
                colorPrimaryDark(res = R.color.color_black)
                colorWindowBackground(res = R.color.color_black)
                attribute(R.attr.app_color_primary, res = R.color.color_black)
                colorStatusBarAuto()
            }
            setColorPrimaryToSp(Color.parseColor("#707070"))    //存储一个灰色值
        }
    }

    fun setTheme(context: Context) {
        setTheme(context, blueIntValue, blueIntValue)
    }

    fun setTheme(context: Context, colorPrimary: Int, colorAccent: Int) {
        val colorDark by PreferenceUtils(context, COLOR_PRIMARY_DARK, true)
        Aesthetic.config {
            colorPrimary(colorPrimary)
            colorAccent(colorPrimary)
            colorPrimaryDark(if (colorDark) ColorUtils.toDarkColor(colorPrimary) else colorPrimary)
            attribute(R.attr.app_color_primary, colorPrimary, null, true)
            attribute(R.attr.app_color_accent, colorAccent, null, true)
            colorStatusBarAuto()
        }
        setColorPrimaryToSp(colorPrimary)
        onThemeChange(colorPrimary, colorAccent)
    }

    fun getColorPrimary(): Int =  Aesthetic.get().colorPrimary().blockingFirst()

    /**
     * 保存的colorPrimary
     * 用于闪屏、回复的数目、图标的颜色获取等
     * 需要注意的是主题色为全白时要返回灰色，否则将与背景同色
     */
    fun getColorPrimaryBySp(): Int {
        val colorPrimary by PreferenceUtils(MyApp.context(), MY_COLOR_PRIMARY, blueIntValue)
        if (colorPrimary == Color.WHITE) return Color.GRAY
        return colorPrimary
    }

    /**
     * 每次更换主题的时候记得更新此值
     *
     * @param color
     */
    private fun setColorPrimaryToSp(color: Int) {
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
                if (isWhiteTheme()) {
                    it.setPrimaryColors(Color.GRAY, Color.WHITE)
                } else {
                    it.setPrimaryColors(colorPrimary, getRefreshTextColor())
                }
            }
            if (it is AHBottomNavigation) {
                it.accentColor = getBottomNavigationColorAccent()
            }
        }
    }

    fun isDarkTheme(): Boolean = Aesthetic.get().isDark.blockingFirst()

    fun isWhiteTheme(): Boolean {
        val colorPrimary by PreferenceUtils(MyApp.context(), MY_COLOR_PRIMARY, blueIntValue)
        return colorPrimary == Color.WHITE
    }

    fun getTextColorPrimary(): Int = Aesthetic.get().textColorPrimary().blockingFirst()

    fun getRefreshTextColor(): Int = when {
        ColorUtils.isLightColor(getColorPrimary()) -> Color.GRAY
        else -> Color.WHITE
    }

    fun getTabIndicatorSelectedColor(): Int {
        val colorPrimary by PreferenceUtils(MyApp.context(), MY_COLOR_PRIMARY, blueIntValue)
        return if (ColorUtils.isLightColor(colorPrimary))  Color.GRAY else Color.WHITE
    }

    fun getBottomNavigationColorAccent(): Int = if (ColorUtils.isLightColor(getColorPrimary()))
        Color.BLACK
    else getColorAccent()

}