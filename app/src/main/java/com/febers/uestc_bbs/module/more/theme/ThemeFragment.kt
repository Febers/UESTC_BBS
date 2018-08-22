package com.febers.uestc_bbs.module.more.theme

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.ThemeAdapter
import com.febers.uestc_bbs.base.ARG_PARAM1
import com.febers.uestc_bbs.base.BasePopFragment
import com.febers.uestc_bbs.entity.ThemeItemBean
import kotlinx.android.synthetic.main.fragment_theme.*

class ThemeFragment : BasePopFragment() {

    override fun setToolbar(): Toolbar? {
        return toolbar_theme
    }

    override fun setContentView(): Int {
        return R.layout.fragment_theme
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        val themeAdapter = ThemeAdapter(context!!, initThemeList(), false)
        themeAdapter.setOnItemClickListener { viewHolder, themeItemBean, i ->  }
        recyclerview_theme.layoutManager = LinearLayoutManager(context)
        recyclerview_theme.adapter = themeAdapter
    }

    private fun initThemeList(): List<ThemeItemBean> {
        val item1 = ThemeItemBean(R.drawable.ic_circle_red, "红色", false)
        val item2 = ThemeItemBean(R.drawable.ic_circle_blue, "蓝色", false)
        val item3 = ThemeItemBean(R.drawable.ic_circle_purple, "紫色", false)
        val item4 = ThemeItemBean(R.drawable.ic_circle_green, "绿色", false)
        val item5 = ThemeItemBean(R.drawable.ic_circle_yellow, "黄色", false)
        val item6 = ThemeItemBean(R.drawable.ic_circle_pink, "粉红", false)
        val item7 = ThemeItemBean(R.drawable.ic_circle_teal, "鸭绿", false)
        val item8 = ThemeItemBean(R.drawable.ic_circle_cyan, "靛蓝", false)
        return listOf(item1, item2, item3, item4, item5, item6, item7, item8)
    }

    private fun reChooseTheme(position: Int) {

    }

    //切换主题时候重新创建Fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contextThemeWrapper = ContextThemeWrapper(context, R.style.AppTheme)
        val localInflater = inflater.cloneInContext(contextThemeWrapper)
        val view = localInflater.inflate(R.layout.fragment_theme, container, false)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                ThemeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }
}