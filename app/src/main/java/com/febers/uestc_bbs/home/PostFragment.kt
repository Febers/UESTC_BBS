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
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.entity.EndRefreshEvent
import com.febers.uestc_bbs.entity.StartRefreshEvent

import kotlinx.android.synthetic.main.fragment_post.*
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PostFragment: BaseFragment() {

    override fun setContentView(): Int {
        return R.layout.fragment_post
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        initMyView()
    }

    private fun initMyView() {
        val postsViewPagerAdapter = PostsViewPagerAdapter(childFragmentManager)
        view_pager_posts.adapter = postsViewPagerAdapter
        view_pager_posts.offscreenPageLimit = 3
        tab_layout_post.setupWithViewPager(view_pager_posts)
        refresh_layout_post_fragment.setOnRefreshListener { EventBus.getDefault().post(StartRefreshEvent(true)) }
    }

    override fun registEventBus(): Boolean {
        return true
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun endRefresh(event: EndRefreshEvent) {
        refresh_layout_post_fragment.isRefreshing = false
    }
}