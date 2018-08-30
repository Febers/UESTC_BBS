package com.febers.uestc_bbs.module.message.view

import android.support.design.widget.TabLayout
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.MessageViewPageAdapter
import com.febers.uestc_bbs.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_message_pager.*

class MsgPagerFragment(): BaseFragment() {

    override fun setContentView(): Int {
        return R.layout.fragment_message_pager
    }

    override fun initView() {
        val messageViewPageAdapter = MessageViewPageAdapter(childFragmentManager)
        view_pager_message.adapter = messageViewPageAdapter
        view_pager_message.offscreenPageLimit = 4
        tab_layout_message.setupWithViewPager(view_pager_message)
    }
}