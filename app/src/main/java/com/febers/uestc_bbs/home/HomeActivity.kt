/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.home

import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.util.Log.d
import android.view.View
import android.widget.CompoundButton
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.module.login.view.LoginActivity
import com.febers.uestc_bbs.utils.CustomPreference
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity: BaseActivity(), AHBottomNavigation.OnTabSelectedListener,
        Drawer.OnDrawerItemClickListener, OnCheckedChangeListener{

    private var mHomeDrawer: Drawer? = null
    private val mHomeFragments: MutableList<Fragment> = ArrayList()
    private val mPostFragment = HomeFragmentManager.getInstance(0)
    private val mForumListFragment = HomeFragmentManager.getInstance(1)
    private val mNoticeFragment = HomeFragmentManager.getInstance(2)
    private val mMessageFragment = HomeFragmentManager.getInstance(3)
    private val mMoreFragment = HomeFragmentManager.getInstance(4)

    override fun setView(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
        val acountHeader = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.color.primary)
                //添加用户
                .addProfiles(
                        ProfileDrawerItem()
                                .withName("Mike Penz")
                                .withEmail("mikepenz@gmail.com")
                                .withIcon(R.drawable.ic_forum_gray_water)
                )
                .addProfiles(
                        ProfileDrawerItem()
                                .withName("Second")
                                .withEmail("second@gmail.com")
                                .withIcon(R.drawable.ic_forum_gray_tiyu)
                )
                //头部点击事件
                .withOnAccountHeaderSelectionViewClickListener { view, profile -> false }
                //头部箭头点击事件
                .withOnAccountHeaderListener { view, profile, current ->
                    d("here", "${profile.name}")
                    false
                }
                //头像点击
                .withOnAccountHeaderProfileImageListener(
                        object : AccountHeader.OnAccountHeaderProfileImageListener {
                            override fun onProfileImageClick(view: View, profile: IProfile<*>, current: Boolean): Boolean {
                                d("here", "click image")
                                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                                return false
                            }

                            override fun onProfileImageLongClick(view: View, profile: IProfile<*>, current: Boolean): Boolean {
                                return false
                            }
                        })
                .build()
        mHomeDrawer = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar_home)
                .withAccountHeader(acountHeader)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        SectionDrawerItem()
                                .withName("分组"),
                        PrimaryDrawerItem()
                                .withName(getString(R.string.account_detail))
                                .withIcon(R.drawable.ic_forum_gray_linux)
                                .withIdentifier(0),
                        PrimaryDrawerItem()
                                .withName(getString(R.string.account_detail))
                                .withIcon(R.drawable.ic_forum_gray_music)
                                .withIdentifier(1),
                        SwitchDrawerItem()
                                .withSwitchEnabled(true)
                                .withName("开关")
                                .withIcon(R.drawable.ic_person_white_24dp)
                                .withCheckable(false)
                                .withOnCheckedChangeListener(this))
                .withOnDrawerItemClickListener(this)
                .build()

        bottom_navigation_home.addItem(AHBottomNavigationItem(getString(R.string.home_page), R.drawable.ic_home_gray))
        bottom_navigation_home.addItem(AHBottomNavigationItem(getString(R.string.forum_list_page), R.drawable.ic_forum_list_gray))
        bottom_navigation_home.addItem(AHBottomNavigationItem(getString(R.string.notice_page), R.drawable.ic_notice_gray))
        bottom_navigation_home.addItem(AHBottomNavigationItem(getString(R.string.message_page), R.drawable.ic_message_gray))
        bottom_navigation_home.addItem(AHBottomNavigationItem(getString(R.string.more_page), R.drawable.ic_more_gray))
        bottom_navigation_home.titleState = AHBottomNavigation.TitleState.ALWAYS_SHOW
        bottom_navigation_home.setOnTabSelectedListener(this)
        bottom_navigation_home.manageFloatingActionButtonBehavior(action_button_home)

        val mFragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        mFragmentTransaction
                .add(R.id.fragment_layout_home, mPostFragment)
                .add(R.id.fragment_layout_home, mForumListFragment)
                .add(R.id.fragment_layout_home, mNoticeFragment)
                .add(R.id.fragment_layout_home, mMessageFragment)
                .add(R.id.fragment_layout_home, mMoreFragment)
                .commit()
        mHomeFragments.add(mPostFragment)
        mHomeFragments.add(mForumListFragment)
        mHomeFragments.add(mNoticeFragment)
        mHomeFragments.add(mMessageFragment)
        mHomeFragments.add(mMoreFragment)
        showHomeFragments(0)
        var name by CustomPreference(this, "name", "1")
        d("home", name)
        name = "20"
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

    override fun onItemClick(view: View?, position: Int, drawerItem: IDrawerItem<*, *>?): Boolean {
        when(position) {
            1 -> d("here click", "$position")
            2 -> d("here click", "$position")
            else -> return true
        }
        //关闭drawer
        return false
    }

    //drawer开关
    override fun onCheckedChanged(drawerItem: IDrawerItem<*, *>?, buttonView: CompoundButton?, isChecked: Boolean) {

    }

    private fun showFloatingActionButton(show: Boolean) {
        if(show) {
            action_button_home.visibility = View.VISIBLE
        } else {
            action_button_home.visibility = View.GONE
        }
    }

    override fun isSlideBack(): Boolean {
        return false
    }
}
