/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.home

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.PostsViewPagerAdapter
import com.febers.uestc_bbs.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_post.*

class PostFragment: BaseFragment() {

    override fun setContentView(): Int {
        return R.layout.fragment_post
    }

    override fun initView() {
        val postsViewPagerAdapter = PostsViewPagerAdapter(fragmentManager!!)
        view_pager_posts.adapter = postsViewPagerAdapter
        tab_layout_posts.setupWithViewPager(view_pager_posts)
    }

    override fun lazyLoad() {

    }
}