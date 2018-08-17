/*
 * Created by Febers at 18-6-12 下午1:00.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-12 下午1:00.
 */

package com.febers.uestc_bbs.module.user.view

import android.os.Bundle
import android.util.Log
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.MessageViewPageAdapter
import com.febers.uestc_bbs.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_message.*
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.EventBus

class MessageFragment: BaseFragment() {

    var messageViewPageAdapter: MessageViewPageAdapter? = null

    override fun setContentView(): Int {
        return R.layout.fragment_message
    }

    override fun initView() {

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        messageViewPageAdapter = MessageViewPageAdapter(childFragmentManager)
        view_pager_message.adapter = messageViewPageAdapter
        tab_layout_message.setupWithViewPager(view_pager_message)
    }

}