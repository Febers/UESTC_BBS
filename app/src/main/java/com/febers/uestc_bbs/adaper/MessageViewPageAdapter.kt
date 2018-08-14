/*
 * Created by Febers at 18-8-14 上午2:03.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 上午2:03.
 */

package com.febers.uestc_bbs.adaper

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.Log.i
import android.view.View
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.view.manager.MessageFragmentManager

class MessageViewPageAdapter : FragmentPagerAdapter {

    constructor(fragmentManager: FragmentManager): super(fragmentManager)

    private val titles = arrayOf(getString(R.string.post_reply_message),
            getString(R.string.private_message),
            "@我",
            getString(R.string.system_message))

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return MessageFragmentManager.getInctance(0)
            1 -> return MessageFragmentManager.getInctance(1)
            2 -> return MessageFragmentManager.getInctance(2)
            3 -> return MessageFragmentManager.getInctance(3)
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