package com.febers.uestc_bbs.module.theme

import android.content.Intent
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.DAY_NIGHT_THEME_CHANGE
import com.febers.uestc_bbs.base.HOME_VIEW_STYLE_BOTTOM
import com.febers.uestc_bbs.home.HomeActivity
import com.febers.uestc_bbs.home.HomeActivity2

class ThemeChangeActivity: BaseActivity() {

    override fun enableThemeHelper(): Boolean = false

    override fun setView(): Int = R.layout.activity_theme_change

    override fun initView() {
        startActivity(Intent(this@ThemeChangeActivity,
                if (MyApp.homeLayout() == HOME_VIEW_STYLE_BOTTOM) HomeActivity::class.java
                else HomeActivity2::class.java).apply {
            putExtra(DAY_NIGHT_THEME_CHANGE, true)
        })
        overridePendingTransition(0, 0)
    }
}