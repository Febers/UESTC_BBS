/*
 * Created by Febers at 18-8-14 上午2:03.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 上午2:03.
 */

package com.febers.uestc_bbs.adaper

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.module.message.view.AMFragment
import com.febers.uestc_bbs.module.message.view.PMFragment
import com.febers.uestc_bbs.module.message.view.RMFragment
import com.febers.uestc_bbs.module.message.view.SMFragment

class MessageViewPageAdapter : FragmentPagerAdapter {

    constructor(fragmentManager: FragmentManager): super(fragmentManager)

    private val titles = arrayOf(getString(R.string.post_reply_message),
            getString(R.string.private_message),
            "@我",
            getString(R.string.system_message))

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return RMFragment.newInstance("")
            1 -> return PMFragment.newInstance("")
            2 -> return AMFragment.newInstance("")
            3 -> return SMFragment.newInstance("")
            else -> return Fragment()
        }
    }

    override fun getCount(): Int {
       return titles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }

    private fun getString(stringId: Int): String {
        return BaseApplication.context().getString(stringId)
    }
}