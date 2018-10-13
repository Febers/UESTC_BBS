package com.febers.uestc_bbs.module.more

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BaseSwipeActivity
import com.febers.uestc_bbs.base.SP_THEME_CODE
import com.febers.uestc_bbs.entity.ThemeItemBean
import com.febers.uestc_bbs.utils.PreferenceUtils
import com.febers.uestc_bbs.utils.ThemeUtils
import com.febers.uestc_bbs.view.adapter.ThemeAdapter
import kotlinx.android.synthetic.main.activity_theme.*
import org.greenrobot.eventbus.EventBus

class ThemeActivity : BaseSwipeActivity() {

    override fun setView(): Int {
        return R.layout.activity_theme
    }

    override fun setToolbar(): Toolbar? {
        return toolbar_theme
    }

    override fun initView() {
        val themeAdapter = ThemeAdapter(this, initThemeList(), false)
        themeAdapter.setOnItemClickListener { viewHolder, themeItemBean, i ->  reChooseTheme(themeItemBean, i)}
        recyclerview_theme.layoutManager = LinearLayoutManager(this)
        recyclerview_theme.adapter = themeAdapter
    }

    private fun reChooseTheme(themeItemBean: ThemeItemBean, position: Int) {
        if (themeItemBean.itemUsing) {
            return
        }
        ThemeUtils.saveTheme(position)
        EventBus.getDefault().post(BaseEvent(BaseCode.SUCCESS, ThemeItemBean()))
        recreate()
    }

    private fun initThemeList(): List<ThemeItemBean> {
        val item1 = ThemeItemBean(R.drawable.ic_circle_green, getString(R.string.theme_green), false)
        val item2 = ThemeItemBean(R.drawable.ic_circle_red, getString(R.string.theme_red), false)
        val item3 = ThemeItemBean(R.drawable.ic_circle_pink, getString(R.string.theme_pink), false)
        val item4 = ThemeItemBean(R.drawable.ic_circle_cyan, getString(R.string.theme_indigo), false)
        val item5 = ThemeItemBean(R.drawable.ic_circle_teal, getString(R.string.theme_teal), false)
        val item6 = ThemeItemBean(R.drawable.ic_circle_purple, getString(R.string.theme_purple), false)
        val item7 = ThemeItemBean(R.drawable.ic_circle_blue, getString(R.string.theme_blue), false)
        val item8 = ThemeItemBean(R.drawable.ic_circle_gray, getString(R.string.theme_gray), false)
        val themeCode by PreferenceUtils(this, name = SP_THEME_CODE, default = 1)
        return listOf(item1, item2, item3, item4, item5, item6, item7, item8).apply {
            get(themeCode).itemUsing = true
        }
    }

    override fun hideStatusBar(): Boolean = true
}
