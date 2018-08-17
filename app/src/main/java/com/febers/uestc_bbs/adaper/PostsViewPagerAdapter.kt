/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.adaper

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.module.post.view.SubTopicFragment

class PostsViewPagerAdapter: FragmentPagerAdapter {

    constructor(fragmentManager: FragmentManager): super(fragmentManager)

    private val titles = arrayOf(getString(R.string.new_post), getString(R.string.new_reply), getString(R.string.hot_article))

    override fun getItem(position: Int): SubTopicFragment {
        when(position) {
            0 -> return SubTopicFragment.newInstance("0")
            1 -> return SubTopicFragment.newInstance("1")
            2 -> return SubTopicFragment.newInstance("2")
            else -> return SubTopicFragment.newInstance("0")
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