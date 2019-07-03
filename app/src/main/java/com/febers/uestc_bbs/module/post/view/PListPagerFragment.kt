package com.febers.uestc_bbs.module.post.view

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.PListPagerAdapter

import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.base.TabSelectedEvent
import com.febers.uestc_bbs.utils.log
import kotlinx.android.synthetic.main.fragment_post_pager.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class PListPagerFragment : BaseFragment() {

    override fun registerEventBus(): Boolean = true

    override fun setView(): Int {
        return R.layout.fragment_post_pager
    }

    override fun initView() {
        val postsViewPagerAdapter = PListPagerAdapter(childFragmentManager)
        view_pager_post.adapter = postsViewPagerAdapter
        view_pager_post.offscreenPageLimit = 3
        tab_layout_post.setupWithViewPager(view_pager_post)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onTabItemSelected(event: TabSelectedEvent) {
        if (event.position == 2) {
            view_pager_post.currentItem = 2
        }
    }

}