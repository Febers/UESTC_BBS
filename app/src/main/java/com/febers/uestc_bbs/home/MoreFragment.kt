/*
 * Created by Febers at 18-6-12 下午12:45.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-12 下午12:45.
 */

package com.febers.uestc_bbs.home

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.MoreItemAdapter
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.entity.MoreItemBean
import com.febers.uestc_bbs.module.login.view.LoginActivity
import kotlinx.android.synthetic.main.fragment_more.*

class MoreFragment: BaseFragment() {


    override fun setContentView(): Int {
        return R.layout.fragment_more
    }

    override fun initView() {
        val moreItemAdapter = MoreItemAdapter(mContext, initMoreItem(), false)
        more_fragment_recyclerview.layoutManager = LinearLayoutManager(mContext)
        more_fragment_recyclerview.adapter = moreItemAdapter
        //more_fragment_recyclerview.addItemDecoration(DividerItemDecoration(mContext,LinearLayoutManager.VERTICAL))
        more_fragment_header.setOnClickListener { startActivity(Intent(activity, LoginActivity::class.java)) }
    }

    override fun lazyLoad() {

    }

    private fun initMoreItem(): List<MoreItemBean> {
        val item1 = MoreItemBean("我的帖子", R.mipmap.ic_write_color)
        val item2 = MoreItemBean("我的回复", R.mipmap.ic_love_color)
        val item3 = MoreItemBean("我的收藏", R.mipmap.ic_star_color)
        val item4 = MoreItemBean("我的好友", R.mipmap.ic_man_color)
        val item5 = MoreItemBean("设置", R.mipmap.ic_option_color)
        return listOf(item1, item2, item3, item4, item5)
    }
}