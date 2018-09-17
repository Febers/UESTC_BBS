/*
 * Created by Febers at 18-8-17 下午3:59.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 下午3:46.
 */

package com.febers.uestc_bbs.module.post.view

import android.content.Intent
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.PListViewPagerAdapter

import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.base.PostEvent
import kotlinx.android.synthetic.main.fragment_post_pager.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class PListPagerFragment : BaseFragment(){
    override fun setContentView(): Int {
        return R.layout.fragment_post_pager
    }

    override fun initView() {
        val postsViewPagerAdapter = PListViewPagerAdapter(childFragmentManager)
        view_pager_post.adapter = postsViewPagerAdapter
        view_pager_post.offscreenPageLimit = 3
        tab_layout_post.setupWithViewPager(view_pager_post)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun startAnotherPost(event: PostEvent) {
        //start(PostDetailFragment.newInstance(event.tid, true))
        startActivity(Intent(activity, PostDetailActivity::class.java).apply { putExtra("fid", event.tid) })
    }

    override fun registerEventBus(): Boolean {
        return true
    }
}