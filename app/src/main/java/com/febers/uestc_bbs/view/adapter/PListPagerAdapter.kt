/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.view.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.MyApplication
import com.febers.uestc_bbs.base.HOME_POSTS_HOT
import com.febers.uestc_bbs.base.HOME_POSTS_REPLY
import com.febers.uestc_bbs.base.HOME_POSTS_NEW
import com.febers.uestc_bbs.module.post.view.PListHomeFragment

class PListPagerAdapter: FragmentPagerAdapter {

    constructor(fragmentManager: FragmentManager): super(fragmentManager)

    private val titles = arrayOf(getString(R.string.new_post), getString(R.string.new_reply), getString(R.string.hot_article))

    override fun getItem(position: Int): PListHomeFragment {
        return when(position) {
            0 -> PListHomeFragment.newInstance(HOME_POSTS_NEW)
            1 -> PListHomeFragment.newInstance(HOME_POSTS_REPLY)
            2 -> PListHomeFragment.newInstance(HOME_POSTS_HOT)
            else -> PListHomeFragment.newInstance(HOME_POSTS_NEW)
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