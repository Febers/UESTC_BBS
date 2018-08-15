/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.home

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.PostsViewPagerAdapter

import kotlinx.android.synthetic.main.fragment_post.*
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.EventBus

class PostFragment: SupportFragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_post, container, false)
        return view
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initView()
    }

    fun initView() {
        val postsViewPagerAdapter = PostsViewPagerAdapter(childFragmentManager)
        view_pager_posts.adapter = postsViewPagerAdapter
        view_pager_posts.offscreenPageLimit = 3
        tab_layout_post.setupWithViewPager(view_pager_posts)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}