package com.febers.uestc_bbs.module.more

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.ThemeAdapter
import com.febers.uestc_bbs.base.FID
import com.febers.uestc_bbs.base.BaseSwipeFragment
import com.febers.uestc_bbs.base.SHOW_BOTTOM_BAR_ON_DESTROY
import com.febers.uestc_bbs.entity.ThemeItemBean
import com.febers.uestc_bbs.utils.ThemeUtils
import kotlinx.android.synthetic.main.fragment_theme.*

class ThemeFragment : BaseSwipeFragment() {

    override fun setToolbar(): Toolbar? {
        return toolbar_theme
    }

    override fun setContentView(): Int {
        return R.layout.fragment_theme
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        val themeAdapter = ThemeAdapter(context!!, initThemeList(), false)
        themeAdapter.setOnItemClickListener { viewHolder, themeItemBean, i ->  reChooseTheme(i)}
        recyclerview_theme.layoutManager = LinearLayoutManager(context)
        recyclerview_theme.adapter = themeAdapter
    }

    private fun initThemeList(): List<ThemeItemBean> {
        val item1 = ThemeItemBean(R.drawable.ic_circle_green, getString(R.string.theme_green), false)
        val item2 = ThemeItemBean(R.drawable.ic_circle_red, getString(R.string.theme_red), false)
        val item3 = ThemeItemBean(R.drawable.ic_circle_pink, getString(R.string.theme_pink), false)
        val item4 = ThemeItemBean(R.drawable.ic_circle_cyan, getString(R.string.theme_cyan), false)
        val item5 = ThemeItemBean(R.drawable.ic_circle_teal, getString(R.string.theme_teal), false)
        val item6 = ThemeItemBean(R.drawable.ic_circle_purple, getString(R.string.theme_purple), false)
        val item7 = ThemeItemBean(R.drawable.ic_circle_blue, getString(R.string.theme_blue), false)
        val item8 = ThemeItemBean(R.drawable.ic_circle_gray, getString(R.string.theme_gray), false)
        return listOf(item1, item2, item3, item4, item5, item6, item7, item8)
    }

    private fun reChooseTheme(position: Int) {
        ThemeUtils.saveTheme(position)
        activity?.recreate()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, showBottomBarOnDestroy: Boolean) =
                ThemeFragment().apply {
                    arguments = Bundle().apply {
                        putString(FID, param1)
                        putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
                    }
                }
    }
}