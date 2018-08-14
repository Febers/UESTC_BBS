/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log.d
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.utils.CustomPreference
import com.febers.uestc_bbs.view.manager.HomeFragmentManager
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity: BaseActivity(), AHBottomNavigation.OnTabSelectedListener {

    private val mHomeFragments: MutableList<Fragment> = ArrayList()
    private val mPostFragment = HomeFragmentManager.getInstance(0)
    private val mForumListFragment = HomeFragmentManager.getInstance(1)
    private val mNoticeFragment = HomeFragmentManager.getInstance(2)
    private val mMoreFragment = HomeFragmentManager.getInstance(3)

    override fun setView(): Int {
        return R.layout.activity_home
    }

    override fun initView() {

        bottom_navigation_home.addItem(AHBottomNavigationItem(getString(R.string.home_page), R.drawable.ic_home_gray))
        bottom_navigation_home.addItem(AHBottomNavigationItem(getString(R.string.forum_list_page), R.drawable.ic_forum_list_gray))
        bottom_navigation_home.addItem(AHBottomNavigationItem(getString(R.string.message_page), R.drawable.ic_message_gray))
        bottom_navigation_home.addItem(AHBottomNavigationItem(getString(R.string.more_page), R.drawable.ic_more_gray))
        bottom_navigation_home.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        bottom_navigation_home.canScrollHorizontally(AHBottomNavigation.LAYOUT_DIRECTION_INHERIT)
        bottom_navigation_home.setOnTabSelectedListener(this)
//        bottom_navigation_home.manageFloatingActionButtonBehavior(action_button_home)

        val mFragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        mFragmentTransaction
                .add(R.id.fragment_layout_home, mPostFragment)
                .add(R.id.fragment_layout_home, mForumListFragment)
                .add(R.id.fragment_layout_home, mNoticeFragment)
                .add(R.id.fragment_layout_home, mMoreFragment)
                .commit()
        mHomeFragments.add(mPostFragment)
        mHomeFragments.add(mForumListFragment)
        mHomeFragments.add(mNoticeFragment)
        mHomeFragments.add(mMoreFragment)
        showHomeFragments(0)
        var name by CustomPreference(this, "name", "1")
        d("home", name)
    }

    override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
        showHomeFragments(position)
        return true
    }

    private fun showHomeFragments(position: Int) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        for (f in mHomeFragments) {
            fragmentTransaction.hide(f)
        }
        fragmentTransaction.show(mHomeFragments.get(position))
        fragmentTransaction.commitAllowingStateLoss()
        if (position != 0) {
            showFloatingActionButton(false)
        } else{
            showFloatingActionButton(true)
        }
    }

    private fun showFloatingActionButton(show: Boolean) {
        if(show) {
//            action_button_home.visibility = View.VISIBLE
        } else {
//            action_button_home.visibility = View.GONE
        }
    }

    override fun isSlideBack(): Boolean {
        return false
    }
}
