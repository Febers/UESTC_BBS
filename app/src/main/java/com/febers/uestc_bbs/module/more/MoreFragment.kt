/*
 * Created by Febers at 18-8-17 下午4:13.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 下午4:12.
 */

package com.febers.uestc_bbs.module.more

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.i
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.MoreItemAdapter
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.entity.MoreItemBean
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.login.view.LoginFragment
import com.febers.uestc_bbs.module.user.view.UserDetailActivity
import com.febers.uestc_bbs.view.utils.GlideCircleTransform
import com.febers.uestc_bbs.module.user.view.UserDetailFragment
import com.febers.uestc_bbs.module.user.view.UserRepliesFragment
import kotlinx.android.synthetic.main.fragment_more.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MoreFragment: BaseFragment() {

    private val FIRST_ITEM_VIEW = -1
    private val SECOND_ITEM_VIEW = 0
    private val THIRD_ITEM_VIEW = 1

    private val USER_DETAIL_ITEM = -1
    private val USER_POST_ITEM = 0
    private val USER_REPLY_ITEM = 1
    private val USER_FAV_ITEM = 2
    private val USER_FRIEND_ITEM = 3

    private val THEME_ITEM = 0
    private val SETTING_ITEM = 1

    private lateinit var user: UserBean

    override fun registeEventBus(): Boolean = true

    override fun setContentView(): Int {
        return R.layout.fragment_more
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        user = BaseApplication.getUser()
        more_fragment_header.setOnClickListener { itemClick(FIRST_ITEM_VIEW, USER_DETAIL_ITEM) }

        val moreItemAdapter1 = MoreItemAdapter(context!!, initMoreItem1(), false)
        moreItemAdapter1.setOnItemClickListener { p0, p1, p2 -> itemClick(view = SECOND_ITEM_VIEW, position = p2) }
        more_fragment_recyclerview_1.layoutManager = LinearLayoutManager(context)
        more_fragment_recyclerview_1.adapter = moreItemAdapter1
        more_fragment_recyclerview_1.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

        val moreItemAdapter2 = MoreItemAdapter(context!!, initMoreItem2(), false)
        moreItemAdapter2.setOnItemClickListener { p0, p1, p2 -> itemClick(view = THIRD_ITEM_VIEW, position = p2) }
        more_fragment_recyclerview_2.layoutManager = LinearLayoutManager(context)
        more_fragment_recyclerview_2.adapter = moreItemAdapter2
        more_fragment_recyclerview_2.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

        if (user.valid) {
            text_view_fragment_user_name.setText(user.name)
            text_view_fragment_user_title.setText(user.title)
        }
        Glide.with(this).load(user.avatar).transform(GlideCircleTransform(context))
                .into(image_view_fragment_user_avatar)
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
        i("MORE", "")
        text_view_fragment_user_name.setText(event.data.name)
        text_view_fragment_user_title.setText(event.data.title)
        Glide.with(this).load(user.avatar).placeholder(R.mipmap.ic_launcher)
                .transform(GlideCircleTransform(context))
                .into(image_view_fragment_user_avatar)
        user = event.data
    }

    private fun itemClick(view: Int, position: Int) {
        val parentFragment: BaseFragment = parentFragment as BaseFragment
        if (view == FIRST_ITEM_VIEW) {
            if (BaseApplication.getUser().valid) {
                //parentFragment.start(UserDetailFragment.newInstance(""))
                startActivity(Intent(activity, UserDetailActivity::class.java))
            } else {
                parentFragment.start(LoginFragment.newInstance(""))
            }
            return
        }
        if (view == SECOND_ITEM_VIEW) {
            if (position == USER_POST_ITEM) {

                return
            }
            if (position == USER_REPLY_ITEM) {
                parentFragment.start(UserRepliesFragment.newInstance(""))
                return
            }
            if (position == USER_FAV_ITEM) {

                return
            }
            if (position == USER_FRIEND_ITEM) {

                return
            }
        }
        if (view == THIRD_ITEM_VIEW) {
            if (position == THEME_ITEM) {
                parentFragment.start(ThemeFragment.newInstance(""))
                return
            }
            if (position == SETTING_ITEM) {
                startActivity(Intent(activity, UserDetailActivity::class.java))
                return
            }

        }
    }
}