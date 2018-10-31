/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.home

import android.annotation.SuppressLint
import android.content.Intent
import androidx.core.app.ActivityCompat
import android.util.Log.i
import android.view.View
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.module.service.HeartMsgService
import com.febers.uestc_bbs.module.theme.AppColor
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.utils.ViewClickUtils
import kotlinx.android.synthetic.main.activity_home.*
import me.yokeyword.fragmentation.ISupportFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeActivity: BaseActivity() {

    private var mFragments : MutableList<ISupportFragment> = ArrayList()
    private var msgCount = 0

    override fun registerEventBus(): Boolean = true

    override fun setView(): Int {
        return R.layout.activity_home
    }

    override fun initView() {
        val firstFragment: ISupportFragment? = findFragment(HomeFirstContainer::class.java)
        if (firstFragment == null) {
            with(mFragments) {
                add(0, HomeFirstContainer())
                add(1, HomeSecondContainer())
                add(2, HomeThirdContainer())
                add(3, HomeFourthContainer())
            }
            loadMultipleRootFragment(R.id.activity_home_container, 0,
                    mFragments[0], mFragments[1], mFragments[2], mFragments[3])
        } else {
            with(mFragments) {
                add(0,firstFragment)
                add(1, findFragment(HomeSecondContainer::class.java))
                add(2, findFragment(HomeThirdContainer::class.java))
                add(3, findFragment(HomeFourthContainer::class.java))
            }
        }
        bottom_navigation_home.apply {
            addItem(AHBottomNavigationItem(getString(R.string.home_page), R.drawable.ic_windmill_gray))
            addItem(AHBottomNavigationItem(getString(R.string.forum_list_page), R.drawable.ic_forum_list_gray))
            addItem(AHBottomNavigationItem(getString(R.string.message_page), R.drawable.ic_message_gray))
            addItem(AHBottomNavigationItem(getString(R.string.more_page), R.drawable.ic_more_gray))
            titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
            accentColor = ThemeHelper.getBottomNavigationColorAccent()
            defaultBackgroundColor = ThemeHelper.getColor(AppColor.COLOR_BACKGROUND)
            manageFloatingActionButtonBehavior(fab_home)
            setOnTabSelectedListener { position, wasSelected -> onTabSelected(position, wasSelected) }
            ThemeHelper.subscribeOnThemeChange(bottom_navigation_home)
        }
        fab_home.setOnClickListener {
            ViewClickUtils.clickToPostEdit(this@HomeActivity, 0) }
        fab_home.visibility = View.GONE
        startService()
    }

    @SuppressLint("RestrictedApi")
    private fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
        if (position == 2) {
            bottom_navigation_home.setNotification("", 2)
            msgCount = 0
        }
        if (position == 0) fab_home.visibility = View.VISIBLE else fab_home.visibility = View.GONE
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
        EventBus.getDefault().post(TabReselectedEvent(BaseCode.SUCCESS, position))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiveNewMsg(event: MsgEvent) {
        msgCount = event.count
        bottom_navigation_home.setNotification(msgCount, 2)
    }

    /**
     * Activity通过Intent的FLAG_ACTIVITY_SINGLE_TOP启动时
     * 生命周期为 onNewIntent -> onResume()
     * 在其中接收Service传过来的数据
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        i("HOME", "newsInstance")
        showHideFragment(mFragments[2])
    }

    override fun onResume() {
        super.onResume()
        i("Home", "onResume")
        if (bottom_navigation_home.currentItem == 0) {
            fab_home.visibility = View.VISIBLE
        }
    }

    override fun onStop() {
        super.onStop()
        i("Home", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        i("Home", "onDestroy")
        stopService(Intent(this, HeartMsgService::class.java))
    }

    private fun startService() {
        val intent = Intent(this, HeartMsgService::class.java)
        startService(intent)
    }
}
