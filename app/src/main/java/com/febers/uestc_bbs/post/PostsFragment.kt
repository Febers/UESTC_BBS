/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.post

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.PostsViewPagerAdapter
import com.febers.uestc_bbs.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_posts.*

class PostsFragment: BaseFragment() {

    override fun setContentView(): Int {
        return R.layout.fragment_posts
    }

    override fun initView() {
        val postsViewPagerAdapter = PostsViewPagerAdapter(fragmentManager!!)
        view_pager_posts.adapter = postsViewPagerAdapter
        tab_layout_posts.setupWithViewPager(view_pager_posts)
    }

    override fun lazyLoad() {

    }

    override fun onCompleted(any: Any) {

    }

    override fun onError(error: String) {

    }
}