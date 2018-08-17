/*
 * Created by Febers at 18-6-12 下午12:45.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-12 下午12:45.
 */

package com.febers.uestc_bbs.home

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log.i
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.MoreItemAdapter
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.entity.MoreItemBean
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.login.view.LoginFragment
import com.febers.uestc_bbs.module.user.view.DetailFragment
import com.febers.uestc_bbs.module.user.view.RepliesFragment
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.interfaces.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_more.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MoreFragment: BaseFragment() {

    private lateinit var user: UserBean

    override fun setContentView(): Int {
        return R.layout.fragment_more
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        user = BaseApplication.getUser()
        more_fragment_header.setOnClickListener { itemClick(-1) }

        val moreItemAdapter1 = MoreItemAdapter(context!!, initMoreItem1(), false)
        moreItemAdapter1.setOnItemClickListener(object : OnItemClickListener<MoreItemBean> {
            override fun onItemClick(p0: ViewHolder?, p1: MoreItemBean?, p2: Int) {
                itemClick(p2)
            }
        })
        more_fragment_recyclerview_1.layoutManager = LinearLayoutManager(context)
        more_fragment_recyclerview_1.adapter = moreItemAdapter1
        more_fragment_recyclerview_1.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

        val moreItemAdapter2 = MoreItemAdapter(context!!, initMoreItem2(), false)
        moreItemAdapter2.setOnItemClickListener(object : OnItemClickListener<MoreItemBean> {
            override fun onItemClick(p0: ViewHolder?, p1: MoreItemBean?, p2: Int) {
                itemClick(p2)
            }
        })
        more_fragment_recyclerview_2.layoutManager = LinearLayoutManager(context)
        more_fragment_recyclerview_2.adapter = moreItemAdapter2
        more_fragment_recyclerview_2.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

        if (user != null && user.valid != false) {
            text_view_fragment_user_name.setText(user.name)
            text_view_fragment_user_title.setText(user.title)
        }
        Glide.with(this).load(user.avatar).apply(RequestOptions().placeholder(R.mipmap.ic_launcher))
                .apply(RequestOptions().circleCrop()).into(image_view_fragment_user_avatar)
    }

    private fun initMoreItem1(): List<MoreItemBean> {
        val item1 = MoreItemBean("我的帖子", R.mipmap.ic_write_blue)
        val item2 = MoreItemBean("我的回复", R.mipmap.ic_message_red)
        val item3 = MoreItemBean("我的收藏", R.mipmap.ic_star_blue)
        val item4 = MoreItemBean("我的好友", R.mipmap.ic_friend_blue)
        return listOf(item1, item2, item3, item4)
    }

    private fun initMoreItem2(): List<MoreItemBean> {
        val item5 = MoreItemBean("主题选择", R.mipmap.ic_theme_purple)
        val item6 = MoreItemBean("设置", R.mipmap.ic_setting_gray)

        return listOf(item5, item6)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSeccess(event: BaseEvent<UserBean>) {
        text_view_fragment_user_name.setText(event.data.name)
        text_view_fragment_user_title.setText(event.data.title)
        Glide.with(this).load(user.avatar).apply(RequestOptions().placeholder(R.mipmap.ic_launcher))
                .apply(RequestOptions().circleCrop()).into(image_view_fragment_user_avatar)
        user = event.data
    }

    private fun itemClick(position: Int) {
        val supportActivity: BaseActivity = activity as BaseActivity
        if (position == -1) {
            if (BaseApplication.getUser().valid) {
                supportActivity.start(DetailFragment.newInstance(""))
            } else {
                supportActivity.start(LoginFragment.newInstance(""))
            }
            return
        }
        supportActivity.start(RepliesFragment.newInstance(""))
    }
}