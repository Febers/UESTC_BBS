package com.febers.uestc_bbs.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Build
import androidx.core.app.ActivityCompat
import android.view.View
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.febers.uestc_bbs.MyApp

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.GithubReleaseBean
import com.febers.uestc_bbs.entity.PushMessageBean
import com.febers.uestc_bbs.module.service.HeartMsgService
import com.febers.uestc_bbs.module.theme.ThemeManager
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.service.NetworkCallbackImpl
import com.febers.uestc_bbs.module.setting.push.PushManager
import com.febers.uestc_bbs.module.setting.push.PushMessageListener
import com.febers.uestc_bbs.module.update.UpdateDialogHelper
import com.febers.uestc_bbs.utils.PreferenceUtils
import com.febers.uestc_bbs.utils.log
import com.febers.uestc_bbs.utils.postEvent
import com.febers.uestc_bbs.utils.postSticky
import com.febers.uestc_bbs.view.dialog.PushMessageDialog
import kotlinx.android.synthetic.main.activity_home.*
import me.yokeyword.fragmentation.ISupportFragment
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

const val PAGE_POSITION_HOME = 0
const val PAGE_POSITION_BLOCK = 1
const val PAGE_POSITION_MESSAGE = 2
const val PAGE_POSITION_MORE = 3

class HomeActivity: BaseActivity() {

    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    private lateinit var connectivityManager: ConnectivityManager

    private var mFragments : MutableList<ISupportFragment> = ArrayList()

    override fun registerEventBus(): Boolean = true

    override fun setView(): Int = R.layout.activity_home

    @SuppressLint("RestrictedApi")
    override fun initView() {
        setSwipeBackEnable(false)
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
            addItem(AHBottomNavigationItem(getString(R.string.home_page), R.drawable.xic_find_small))
            addItem(AHBottomNavigationItem(getString(R.string.forum_list_page), R.drawable.xic_location_small))
            addItem(AHBottomNavigationItem(getString(R.string.message_page), R.drawable.xic_message_small))
            addItem(AHBottomNavigationItem(getString(R.string.more_page), R.drawable.xic_user_small))
            titleState = AHBottomNavigation.TitleState.ALWAYS_HIDE
            manageFloatingActionButtonBehavior(fab_home)
            setOnTabSelectedListener { position, wasSelected -> onTabSelected(position, wasSelected) }
            ThemeManager.viewInitAndSubscribe(this)
        }
        fab_home.setOnClickListener {
            ClickContext.clickToPostEdit(this@HomeActivity, fid = 0, title = "") }
        fab_home.visibility = View.GONE
        ThemeManager.viewInitAndSubscribe(fab_home)
        startService()
        getShortcutMsg()
    }

    @SuppressLint("RestrictedApi")
    private fun onTabSelected(position: Int, wasSelected: Boolean): Boolean {
        if (position == PAGE_POSITION_MESSAGE) {
            bottom_navigation_home.setNotification("", PAGE_POSITION_MESSAGE)
            MyApp.msgCount = 0
        }
        if(wasSelected) {
            onTabReselected(position)
            return true
        }
        showHideFragment(mFragments[position])
        if (position == PAGE_POSITION_HOME) {
            fab_home.visibility = View.VISIBLE
        } else {
            fab_home.visibility = View.GONE
        }
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
        postEvent(TabReselectedEvent(BaseCode.SUCCESS, position))
    }

    /**
     *  当后台Service接收到新消息时，此方法会接受到相应的消息
     *  接收到一个msgCount的参数，代表未读消息的数目
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = false)
    fun onReceiveNewMsg(event: MsgEvent) {
        log { "接收到消息: ${event.count}" }
        if (bottom_navigation_home.currentItem != PAGE_POSITION_MESSAGE) {
            MyApp.msgCount = event.count
            bottom_navigation_home.setNotification(MyApp.msgCount, PAGE_POSITION_MESSAGE)
        }
    }

    /**
     * Activity通过Intent的FLAG_ACTIVITY_SINGLE_TOP启动时，即通过通知打开应用时
     * 生命周期为 onNewIntent -> onResume()
     * 在其中接收 Service 或者 shortcut 传过来的数据
     */
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent?.getIntExtra(MSG_COUNT, 0) ?:0 > 0) {
            bottom_navigation_home.currentItem = PAGE_POSITION_MESSAGE
            showHideFragment(mFragments[PAGE_POSITION_MESSAGE])
        }
        getShortcutMsg()
    }

    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()

        if (bottom_navigation_home.currentItem == 0) {
            fab_home.visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, HeartMsgService::class.java))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
    }

    private fun startService() {
        val loopReceiveMsg by PreferenceUtils(MyApp.context(), LOOP_RECEIVE_MSG, true)
        if (loopReceiveMsg) {
            val intent = Intent(this, HeartMsgService::class.java)
            startService(intent)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            networkCallback = NetworkCallbackImpl()
            connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connectivityManager.registerNetworkCallback(NetworkRequest.Builder().build(), networkCallback)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onCheckUpdateResult(event: UpdateCheckEvent) {
        if (event.code == BaseCode.SUCCESS) {
            showUpdateDialog(event.result!!)
        }
    }

    private fun getShortcutMsg() {
        if (intent?.getBooleanExtra(SHORTCUT_HOT, false) == true) {
            postSticky(TabSelectedEvent(position = 2))
        }
        if (intent?.getBooleanExtra(SHORTCUT_MSG, false) == true) {
            bottom_navigation_home.currentItem = PAGE_POSITION_MESSAGE
            showHideFragment(mFragments[PAGE_POSITION_MESSAGE])
        }
    }

    private fun showUpdateDialog(githubReleaseBean: GithubReleaseBean) {
        val dialogHelper = UpdateDialogHelper(mContext)
        dialogHelper.showGithubUpdateDialog(githubReleaseBean)
    }

    override fun afterCreated() {
        PushManager.getHttpMessages(object : PushMessageListener {
            override fun success(message: PushMessageBean) {
                if (!message.msg.isNullOrEmpty()) {
                    if (message.msg!!.first().show) {
                        var lastId by PreferenceUtils(this@HomeActivity, PUSH_MESSAGE_ID_LAST, 0)
                        if (message.msg!!.first().id > lastId) {
                            runOnUiThread { PushMessageDialog(this@HomeActivity).show(message.msg!!.first().text) }
                            lastId = message.msg!!.first().id
                        }
                    }
                }
            }
            override fun fail(message: String) {
            }
        })
    }
}
