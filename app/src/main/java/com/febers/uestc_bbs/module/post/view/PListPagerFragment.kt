package com.febers.uestc_bbs.module.post.view

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.PListPagerAdapter

import com.febers.uestc_bbs.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_post_pager.*


class PListPagerFragment : BaseFragment() {
    override fun setContentView(): Int {
        return R.layout.fragment_post_pager
    }

    override fun initView() {
        val postsViewPagerAdapter = PListPagerAdapter(childFragmentManager)
        view_pager_post.adapter = postsViewPagerAdapter
        view_pager_post.offscreenPageLimit = 3
        tab_layout_post.setupWithViewPager(view_pager_post)
    }
}