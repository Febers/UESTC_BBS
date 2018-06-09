/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.home

import android.support.v4.app.FragmentTransaction
import android.util.Log.d
import android.view.View
import android.widget.CompoundButton
import com.ashokvarma.bottomnavigation.BottomNavigationBar
import com.ashokvarma.bottomnavigation.BottomNavigationItem
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.R
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IProfile
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.toast

class HomeActivity: BaseActivity(), BottomNavigationBar.OnTabSelectedListener,
        Drawer.OnDrawerItemClickListener, OnCheckedChangeListener{

    var homeDrawer: Drawer? = null

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
                                .withIcon(R.drawable.ic_android_blue_24dp)
                )
                .addProfiles(
                        ProfileDrawerItem()
                                .withName("Second")
                                .withEmail("second@gmail.com")
                                .withIcon(R.drawable.ic_person_white_24dp)
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
                                return false
                            }

                            override fun onProfileImageLongClick(view: View, profile: IProfile<*>, current: Boolean): Boolean {
                                return false
                            }
                        })
                .build()
        homeDrawer = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar_home)
                .withAccountHeader(acountHeader)
                .withActionBarDrawerToggleAnimated(true)
                .addDrawerItems(
                        SectionDrawerItem()
                                .withName("分组"),
                        PrimaryDrawerItem()
                                .withName(getString(R.string.account_detail))
                                .withIcon(R.drawable.ic_android_blue_24dp)
                                .withIdentifier(0),
                        PrimaryDrawerItem()
                                .withName(getString(R.string.account_detail))
                                .withIcon(R.drawable.ic_android_blue_24dp)
                                .withIdentifier(1),
                        SwitchDrawerItem()
                                .withSwitchEnabled(true)
                                .withName("开关")
                                .withIcon(R.drawable.ic_person_white_24dp)
                                .withCheckable(false)
                                .withOnCheckedChangeListener(this))
                .withOnDrawerItemClickListener(this)
                .build()
        bottom_navigation_bar
                .addItem(BottomNavigationItem(R.drawable.home_gray, getString(R.string.home_page)))
                .addItem(BottomNavigationItem(R.drawable.forum_list_gray, getString(R.string.forum_list_page)))
                .addItem(BottomNavigationItem(R.drawable.message_gray, getString(R.string.message_page)))
                .addItem(BottomNavigationItem(R.drawable.more_gray, getString(R.string.more_page)))
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setFirstSelectedPosition(0)
                .initialise()
        bottom_navigation_bar.setTabSelectedListener(this)

        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment_layout_home, HomeFragmentManager.getInstance(0))
        fragmentTransaction.commit()
        fragmentTransaction.show(HomeFragmentManager.getInstance(0))
    }

    override fun onTabSelected(position: Int) {
        when(position) {
            0 -> {

            }
            1 -> {

            }

            2 -> {
            }

            3 -> {
            }

            else -> {}
        }
    }

    private fun showHomeFragments(position: Int) {

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

    override fun onCompleted(any: Any) {

    }

    override fun onError(error: String) {
        toast(error)
    }


    //drawer开关
    override fun onCheckedChanged(drawerItem: IDrawerItem<*, *>?, buttonView: CompoundButton?, isChecked: Boolean) {

    }

    //bottomTab二次选择
    override fun onTabReselected(position: Int) {

    }

    //bottomTab取消选择
    override fun onTabUnselected(position: Int) {

    }
}
