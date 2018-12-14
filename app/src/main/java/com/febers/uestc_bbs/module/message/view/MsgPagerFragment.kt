package com.febers.uestc_bbs.module.message.view

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.MsgPagerAdapter
import com.febers.uestc_bbs.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_msg_pager.*

class MsgPagerFragment: BaseFragment() {

    override fun setView(): Int {
        return R.layout.fragment_msg_pager
    }

    override fun initView() {
        val messageViewPageAdapter = MsgPagerAdapter(childFragmentManager)
        view_pager_message.adapter = messageViewPageAdapter
        view_pager_message.offscreenPageLimit = 4
        tab_layout_message.setupWithViewPager(view_pager_message)
        //view_pager_message.setCurrentItem(1, true)
    }
}