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
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.utils.ViewClickUtils
import kotlinx.android.synthetic.main.activity_home.*
import me.yokeyword.fragmentation.ISupportFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeActivity: BaseActivity() {

    private val PAGE_POSITION_HOME = 0
    private val PAGE_POSITION_BLOCK = 1
    private val PAGE_POSITION_MESSAGE = 2
    private val PAGE_POSITION_MORE = 3

    private var mFragments : MutableList<ISupportFragment> = ArrayList()
    private var msgCount = 0

    override fun registerEventBus(): Boolean = true

    override fun setView(): Int {
        return R.layout.activity_home
    }

    @SuppressLint("RestrictedApi")
    override fun initView() {
        val firstFragment: ISupportFragment? = findFragment(HomeFirstContainer::class.java)
        if (firstFragment == null) {
            with(mFragments) {
                add(PAGE_POSITION_HOME, HomeFirstContainer())
                add(PAGE_POSITION_BLOCK, HomeSecondContainer())
                add(PAGE_POSITION_MESSAGE, HomeThirdContainer())
                add(PAGE_POSITION_MORE, HomeFourthContainer())
            }
            loadMultipleRootFragment(R.id.activity_home_container, 0,
                    mFragments[0], mFragments[1], mFragments[2], mFragments[3])
        } else {
            with(mFragments) {
                add(PAGE_POSITION_HOME,firstFragment)
                add(PAGE_POSITION_BLOCK, findFragment(HomeSecondContainer::class.java))
                add(PAGE_POSITION_MESSAGE, findFragment(HomeThirdContainer::class.java))
                add(PAGE_POSITION_MORE, findFragment(HomeFourthContainer::class.java))
            }
        }
        bottom_navigation_home.apply {
            addItem(AHBottomNavigationItem(getString(R.string.home_page), R.drawable.ic_windmill_gray))
            addItem(AHBottomNavigationItem(getString(R.string.forum_list_page), R.drawable.ic_forum_list_gray))
            addItem(AHBottomNavigationItem(getString(R.string.message_page), R.drawable.ic_message_gray))
            addItem(AHBottomNavigationItem(getString(R.string.more_page), R.drawable.ic_more_gray))
            titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
            accentColor = ThemeHelper.getBottomNavigationColorAccent()
            defaultBackgroundColor = ThemeHelper.getColorBackground()
            manageFloatingActionButtonBehavior(fab_home)
            setOnTabSelectedListener { position, wasSelected -> onTabSelected(position, wasSelected) }
            ThemeHelper.subscribeOnThemeChange(bottom_navigation_home)
        }
        fab_home.setOnClickListener {
            ViewClickUtils.clickToPostEdit(this@HomeActivity, fid = 0, title = "") }
        fab_home.visibility = View.GONE
        startService()
    }

    @SuppressLint("RestrictedApi")
    private fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
        if (position == PAGE_POSITION_HOME) {
            fab_home.visibility = View.VISIBLE
        } else {
            fab_home.visibility = View.GONE
        }
        if (position == PAGE_POSITION_MESSAGE) {
            bottom_navigation_home.setNotification("", PAGE_POSITION_MESSAGE)
            EventBus.getDefault().post(MsgFeedbackEvent(BaseCode.SUCCESS, MSG_TYPE_ALL))
            msgCount = 0
        }
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

    /**
     *  当后台Service接收到新消息时，此方法会接受到相应的消息
     *  接收到一个msgCount的参数，代表未读消息的数目
     */
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

    @SuppressLint("RestrictedApi")
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
