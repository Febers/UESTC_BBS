/*
 * Created by Febers at 18-8-14 上午2:03.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 上午2:03.
 */

package com.febers.uestc_bbs.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.febers.uestc_bbs.MyApplication
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.module.message.view.MessageFragment

class MsgPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val titles = arrayOf(getString(R.string.post_reply_message),
            getString(R.string.private_message),
            "@"+getString(R.string.at_message),
            getString(R.string.system_message))

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> MessageFragment.newInstance(MSG_TYPE_REPLY)
            1 -> MessageFragment.newInstance(MSG_TYPE_PRIVATE)
            2 -> MessageFragment.newInstance(MSG_TYPE_AT)
            3 -> MessageFragment.newInstance(MSG_TYPE_SYSTEM)
            else -> Fragment()
        }
    }

    override fun getCount(): Int {
       return titles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    private fun getString(stringId: Int): String {
        return MyApplication.context().getString(stringId)
    }
}