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
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.view.adapter.MoreItemAdapter
import com.febers.uestc_bbs.entity.MoreItemBean
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.login.view.LoginFragment
import com.febers.uestc_bbs.module.post.view.PostEditActivity
import com.febers.uestc_bbs.module.search.view.SearchFragment
import com.febers.uestc_bbs.module.user.view.UserDetailActivity
import com.febers.uestc_bbs.module.user.view.UserPListFragment
import com.febers.uestc_bbs.view.utils.GlideCircleTransform
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
    private val SEARCH_ITEM = 4

    private val THEME_ITEM = 0
    private val SETTING_ITEM = 1

    private lateinit var user: UserBean
    private lateinit var mParentFragment: BaseFragment
    override fun registerEventBus(): Boolean = true

    override fun setContentView(): Int {
        return R.layout.fragment_more
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        user = MyApplication.getUser()
        mParentFragment = parentFragment as BaseFragment
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
            text_view_fragment_user_name.text = user.name
            text_view_fragment_user_title.text = user.title
        }
        Glide.with(this).load(user.avatar).transform(GlideCircleTransform(context))
                .placeholder(R.mipmap.ic_default_avatar)
                .into(image_view_fragment_user_avatar)

        btn_test_emoji.setOnClickListener {
            startActivity(Intent(activity, PostEditActivity::class.java))
        }
    }

    private fun initMoreItem1(): List<MoreItemBean> {
        val item1 = MoreItemBean("我的帖子", R.mipmap.ic_write_blue)
        val item2 = MoreItemBean("我的回复", R.mipmap.ic_message_red)
        val item3 = MoreItemBean("我的收藏", R.mipmap.ic_star_blue)
        val item4 = MoreItemBean("我的好友", R.mipmap.ic_friend_blue)
        val item5 = MoreItemBean("搜索", R.mipmap.ic_search_blue)
        return listOf(item1, item2, item3, item4, item5)
    }

    private fun initMoreItem2(): List<MoreItemBean> {
        val item1 = MoreItemBean("主题选择", R.mipmap.ic_theme_purple)
        val item2 = MoreItemBean("设置", R.mipmap.ic_setting_gray)
        return listOf(item1, item2)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSeccess(event: BaseEvent<UserBean>) {
        i("MORE", "")
        text_view_fragment_user_name.text = event.data.name
        text_view_fragment_user_title.text = event.data.title
        Glide.with(this).load(user.avatar).placeholder(R.mipmap.ic_launcher)
                .transform(GlideCircleTransform(context))
                .into(image_view_fragment_user_avatar)
        user = event.data
    }

    private fun itemClick(view: Int, position: Int) {
        if (view == FIRST_ITEM_VIEW) {
            if (MyApplication.getUser().valid) {
                startActivity(Intent(activity, UserDetailActivity::class.java))
            } else {
                mParentFragment.start(LoginFragment.newInstance(true))
            }
            return
        }
        if (view == SECOND_ITEM_VIEW) {
            if (position == USER_POST_ITEM) {
                mParentFragment.start(UserPListFragment.newInstance(user.uid, type = USER_START_POST, showBottomBarOnDestroy = true))
                return
            }
            if (position == USER_REPLY_ITEM) {
                mParentFragment.start(UserPListFragment.newInstance(user.uid, type = USER_REPLY_POST, showBottomBarOnDestroy = true))
                return
            }
            if (position == USER_FAV_ITEM) {
                mParentFragment.start(UserPListFragment.newInstance(user.uid, type = USER_FAV_POST, showBottomBarOnDestroy = true))
                return
            }
            if (position == USER_FRIEND_ITEM) {

                return
            }
            if (position == SEARCH_ITEM) {
                mParentFragment.start(SearchFragment.newInstance(true))
                return
            }
        }
        if (view == THIRD_ITEM_VIEW) {
            if (position == THEME_ITEM) {
                mParentFragment.start(ThemeFragment.newInstance(true))
                return
            }
            if (position == SETTING_ITEM) {
                mParentFragment.start(SettingFragment.newInstance(true))
                return
            }

        }
    }
}