/*
 * Created by Febers at 18-6-12 下午1:00.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-12 下午1:00.
 */

package com.febers.uestc_bbs.home

import android.os.Bundle
import android.util.Log
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.MessageViewPageAdapter
import kotlinx.android.synthetic.main.fragment_message.*
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.EventBus

class MessageFragment: SupportFragment() {

    var messageViewPageAdapter: MessageViewPageAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_message, container, false)
        return view
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        messageViewPageAdapter = MessageViewPageAdapter(childFragmentManager)
        view_pager_message.adapter = messageViewPageAdapter
        tab_layout_message.setupWithViewPager(view_pager_message)
    }

}