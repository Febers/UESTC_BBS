/*
 * Created by Febers at 18-6-12 下午12:45.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-12 下午12:45.
 */

package com.febers.uestc_bbs.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.MoreItemAdapter
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.MoreItemBean
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.login.view.LoginActivity
import kotlinx.android.synthetic.main.fragment_more.*
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MoreFragment: SupportFragment() {

    private lateinit var user: UserBean

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_more, container, false)
        EventBus.getDefault().register(this)
        return view
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        user = BaseApplication.getUser()
        val moreItemAdapter = MoreItemAdapter(context!!, initMoreItem(), false)
        more_fragment_recyclerview.layoutManager = LinearLayoutManager(context)
        more_fragment_recyclerview.adapter = moreItemAdapter
        more_fragment_recyclerview.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        more_fragment_header.setOnClickListener { startActivity(Intent(activity, LoginActivity::class.java)) }

        Glide.with(this).load(R.mipmap.ic_launcher).apply(RequestOptions().circleCrop()).into(image_view_fragment_user_avatar)
        if (user != null && user.valid != false) {
            text_view_fragment_user_name.setText(user.name)
            text_view_fragment_user_title.setText(user.title)
            Glide.with(this).load(user.avatar).apply(RequestOptions().circleCrop()).into(image_view_fragment_user_avatar)
        }
    }

    private fun initMoreItem(): List<MoreItemBean> {
        val item1 = MoreItemBean("我的帖子", R.mipmap.ic_write_color)
        val item2 = MoreItemBean("我的回复", R.mipmap.ic_love_color)
        val item3 = MoreItemBean("我的收藏", R.mipmap.ic_star_color)
        val item4 = MoreItemBean("我的好友", R.mipmap.ic_man_color)
        val item5 = MoreItemBean("设置", R.mipmap.ic_option_color)
        return listOf(item1, item2, item3, item4, item5)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSeccess(event: BaseEvent<UserBean>) {
        text_view_fragment_user_name.setText(event.data.name)
        text_view_fragment_user_title.setText(event.data.title)
        Glide.with(this).load(event.data.avatar).into(image_view_fragment_user_avatar)
        user = event.data
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}