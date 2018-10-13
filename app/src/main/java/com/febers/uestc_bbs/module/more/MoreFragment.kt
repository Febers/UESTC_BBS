/*
 * Created by Febers at 18-8-17 下午4:13.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 下午4:12.
 */

package com.febers.uestc_bbs.module.more

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.i
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.MyApplication
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.view.adapter.MoreItemAdapter
import com.febers.uestc_bbs.entity.MoreItemBean
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.login.view.LoginFragment
import com.febers.uestc_bbs.module.search.view.SearchFragment
import com.febers.uestc_bbs.module.user.view.UserDetailActivity
import com.febers.uestc_bbs.module.user.view.UserPostActivity
import com.febers.uestc_bbs.view.utils.GlideCircleTransform
import kotlinx.android.synthetic.main.fragment_more.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MoreFragment: BaseFragment() {

    private val FIRST_ITEM_VIEW = -1
    private val SECOND_ITEM_VIEW = 0
    private val THIRD_ITEM_VIEW = 1

    private val USER_DETAIL_ITEM = -1
    private val USER_START_ITEM = 0
    private val USER_REPLY_ITEM = 1
    private val USER_FAV_ITEM = 2
    private val USER_FRIEND_ITEM = 3
    private val SEARCH_ITEM = 4

    private val THEME_ITEM = 0
    private val SETTING_ITEM = 1

    private lateinit var userSimple: UserSimpleBean
    private lateinit var mParentFragment: BaseFragment

    override fun registerEventBus(): Boolean = true

    override fun setContentView(): Int {
        return R.layout.fragment_more
    }

    override fun initView() {
        initMenu()
        userSimple = MyApplication.getUser()
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

        if (userSimple.valid) {
            text_view_fragment_user_name.text = userSimple.name
            text_view_fragment_user_title.text = userSimple.title
        }
        Glide.with(this).load(userSimple.avatar).transform(GlideCircleTransform(context))
                .placeholder(R.mipmap.ic_default_avatar)
                .into(image_view_fragment_user_avatar)
    }

    private fun initMoreItem1(): List<MoreItemBean> {
        val item1 = MoreItemBean(getString(R.string.my_start_post), R.mipmap.ic_write_blue)
        val item2 = MoreItemBean(getString(R.string.my_reply_post), R.mipmap.ic_message_red)
        val item3 = MoreItemBean(getString(R.string.my_fav_post), R.mipmap.ic_star_blue)
        val item4 = MoreItemBean("", R.mipmap.ic_friend_blue)
        val item5 = MoreItemBean("", R.mipmap.ic_search_blue)
        return listOf(item1, item2, item3)
    }

    private fun initMoreItem2(): List<MoreItemBean> {
        val item1 = MoreItemBean(getString(R.string.theme_style), R.mipmap.ic_theme_purple)
        val item2 = MoreItemBean(getString(R.string.setting), R.mipmap.ic_setting_gray)
        return listOf(item1, item2)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(event: BaseEvent<UserSimpleBean>) {
        i("MORE", "")
        text_view_fragment_user_name.text = event.data.name
        text_view_fragment_user_title.text = event.data.title
        Glide.with(this).load(userSimple.avatar).placeholder(R.drawable.ic_person_white_24dp)
                .transform(GlideCircleTransform(context))
                .into(image_view_fragment_user_avatar)
        userSimple = event.data
    }

    private fun itemClick(view: Int, position: Int) {
        if (view == FIRST_ITEM_VIEW) {
            if (MyApplication.getUser().valid) {
                startActivity(Intent(activity, UserDetailActivity::class.java).apply {
                    putExtra(USER_ID, MyApplication.getUser().uid)
                })
            } else {
                mParentFragment.start(LoginFragment.newInstance(true))
            }
            return
        }
        if (view == SECOND_ITEM_VIEW) {
            if (position == USER_START_ITEM) {
                startActivity(Intent(activity, UserPostActivity::class.java).apply {
                    putExtra(USER_ID, userSimple.uid)
                    putExtra(USER_POST_TYPE, USER_START_POST) })
                return
            }
            if (position == USER_REPLY_ITEM) {
                startActivity(Intent(activity, UserPostActivity::class.java).apply {
                    putExtra(USER_ID, userSimple.uid)
                    putExtra(USER_POST_TYPE, USER_REPLY_POST) })
                return
            }
            if (position == USER_FAV_ITEM) {
                startActivity(Intent(activity, UserPostActivity::class.java).apply {
                    putExtra(USER_ID, userSimple.uid)
                    putExtra(USER_POST_TYPE, USER_FAV_POST) })
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
                startActivity(Intent(activity, ThemeActivity::class.java))
                return
            }
            if (position == SETTING_ITEM) {
                startActivity(Intent(activity, SettingActivity::class.java))
                return
            }
        }
    }

    private fun initMenu() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar_more)
        setHasOptionsMenu(true)
        toolbar_more.inflateMenu(R.menu.menu_more_fragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_more_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_item_search_more_fragment) {
            mParentFragment.start(SearchFragment.newInstance(true))
        }
        return super.onOptionsItemSelected(item)
    }
}