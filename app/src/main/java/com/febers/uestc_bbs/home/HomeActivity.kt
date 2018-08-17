/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.home

import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log.i
import android.view.View
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import kotlinx.android.synthetic.main.activity_home.*
import me.yokeyword.fragmentation.ISupportFragment
import me.yokeyword.fragmentation.SupportActivity
import me.yokeyword.fragmentation.SupportFragment

class HomeActivity: BaseActivity(), AHBottomNavigation.OnTabSelectedListener {

    private var mFragments : MutableList<ISupportFragment> = ArrayList()

    override fun setView(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
        var firstFragment: ISupportFragment? = findFragment(PostFragment::class.java)
        if (firstFragment == null) {
            mFragments.add(0, PostFragment())
            mFragments.add(1, BlockFragment())
            mFragments.add(2, MessageFragment())
            mFragments.add(3, MoreFragment())
            loadMultipleRootFragment(R.id.activity_home_container, 0,
                    mFragments[0], mFragments[1], mFragments[2], mFragments[3])
        } else {
            mFragments.add(0,firstFragment)
            mFragments.add(1, findFragment(BlockFragment::class.java))
            mFragments.add(2, findFragment(MessageFragment::class.java))
            mFragments.add(3, findFragment(MoreFragment::class.java))
        }

        bottom_navigation_home.addItem(AHBottomNavigationItem(getString(R.string.home_page), R.drawable.ic_home_gray))
        bottom_navigation_home.addItem(AHBottomNavigationItem(getString(R.string.forum_list_page), R.drawable.ic_forum_list_gray))
        bottom_navigation_home.addItem(AHBottomNavigationItem(getString(R.string.message_page), R.drawable.ic_message_gray))
        bottom_navigation_home.addItem(AHBottomNavigationItem(getString(R.string.more_page), R.drawable.ic_more_gray))
        bottom_navigation_home.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        bottom_navigation_home.canScrollHorizontally(AHBottomNavigation.LAYOUT_DIRECTION_INHERIT)
        bottom_navigation_home.setOnTabSelectedListener(this)
//        bottom_navigation_home.manageFloatingActionButtonBehavior(action_button_home)
    }

    override fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
        if(wasSelected) {
            onTabReselected(position)
            return true
        }
        showHideFragment(mFragments[position])
        return true
    }

    override fun onBackPressedSupport() {
        if(supportFragmentManager.backStackEntryCount > 0) {
            pop()
        } else {
            ActivityCompat.finishAfterTransition(this)
        }
    }

    private fun onTabReselected(position: Int) {
        i("HA", "${position}")
    }
}
