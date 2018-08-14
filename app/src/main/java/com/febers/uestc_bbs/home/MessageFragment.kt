/*
 * Created by Febers at 18-6-12 下午1:00.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-12 下午1:00.
 */

package com.febers.uestc_bbs.home

import android.util.Log
import android.util.Log.i
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.MessageViewPageAdapter
import com.febers.uestc_bbs.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_message.*

class MessageFragment: BaseFragment() {

    var messageViewPageAdapter: MessageViewPageAdapter? = null

    override fun setContentView(): Int {
        i("MF", "setcontent")
        return R.layout.fragment_message
    }

    override fun initView() {

    }

    override fun lazyLoad() {
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden) {
            if (messageViewPageAdapter == null) {
                messageViewPageAdapter = MessageViewPageAdapter(childFragmentManager)
                view_pager_message.adapter = messageViewPageAdapter
                view_pager_message.offscreenPageLimit = 4
                tab_layout_message.setupWithViewPager(view_pager_message)
            }
        }
    }
}