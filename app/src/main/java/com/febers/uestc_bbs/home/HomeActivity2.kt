package com.febers.uestc_bbs.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.MoreItemBean
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.context.LoginContext
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.module.more.*
import com.febers.uestc_bbs.module.post.view.PListActivity
import com.febers.uestc_bbs.module.search.view.SearchActivity
import com.febers.uestc_bbs.module.setting.AboutActivity
import com.febers.uestc_bbs.module.setting.SettingActivity
import com.febers.uestc_bbs.module.theme.ThemeActivity
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.module.user.view.UserPostActivity
import com.febers.uestc_bbs.utils.log
import com.febers.uestc_bbs.view.adapter.MoreItemAdapter
import kotlinx.android.synthetic.main.activity_home_2.*
import kotlinx.android.synthetic.main.layout_drawer_home_2.*
import kotlinx.android.synthetic.main.layout_header_drawer.*
import me.yokeyword.fragmentation.ISupportFragment
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class HomeActivity2: BaseActivity() {

    private var mFragments : MutableList<ISupportFragment> = ArrayList()
    private var pagePositionNow = PAGE_POSITION_HOME

    private lateinit var userSimple: UserSimpleBean

    private var navigationDialog: AlertDialog? = null
    private var tvNaviLostAndFound: LinearLayout? = null
    private var tvNaviSchoolBus: LinearLayout? = null
    private var tvNaviCalendar: LinearLayout? = null
    private var tvNaviNewer: LinearLayout? = null
    private var btnNaviEnter: Button? = null

    override fun setView(): Int = R.layout.activity_home_2

    override fun setToolbar(): Toolbar? = toolbar_home_2

    override fun setMenu(): Int? = R.menu.menu_home_2

    override fun setTitle(): String? = getString(R.string.app_name)

    override fun registerEventBus(): Boolean = true

    override fun initView() {
        initToolbar()
        val firstFragment: ISupportFragment? = findFragment(HomeFirstContainer::class.java)
        if (firstFragment == null) {
            with(mFragments) {
                add(PAGE_POSITION_HOME, HomeFirstContainer())
                add(PAGE_POSITION_BLOCK, HomeSecondContainer())
                add(PAGE_POSITION_MESSAGE, HomeThirdContainer())
            }
            loadMultipleRootFragment(R.id.container_home_2, 0,
                    mFragments[0], mFragments[1], mFragments[2])
        } else {
            with(mFragments) {
                add(PAGE_POSITION_HOME, firstFragment)
                add(PAGE_POSITION_BLOCK, findFragment(HomeSecondContainer::class.java))
                add(PAGE_POSITION_MESSAGE, findFragment(HomeThirdContainer::class.java))
            }
        }
        fab_home.setOnClickListener {
            ClickContext.clickToPostEdit(context, fid = 0, title = "") }
        userSimple = MyApp.getUser()
        onThemeChanged()
        initDrawer()
    }

    private fun initToolbar() {
        val toggle = ActionBarDrawerToggle(context,
                drawer_layout_home_2, toolbar_home_2,
                R.string.search, R.string.search)
        toggle.syncState()
        drawer_layout_home_2.addDrawerListener(toggle)
    }

    private fun initDrawer() {
        if (userSimple.valid) {
            tv_user_name_drawer_header.text = userSimple.name
            tv_user_level_drawer_header.text = userSimple.title
            tv_user_name_drawer_header.setTextColor(ThemeHelper.getRefreshTextColor())
            tv_user_level_drawer_header.setTextColor(ThemeHelper.getRefreshTextColor())
            ImageLoader.load(context, userSimple.avatar, iv_user_avatar_drawer_header, clickToViewer = false)
        }
        drawer_header_home.setOnClickListener { ClickContext.clickToUserDetail(context, userSimple.uid) }

        val menuItemAdapter1 = MoreItemAdapter(context, initDrawerItem1(), false)
        menuItemAdapter1.setOnItemClickListener { p0, p1, p2 -> onFirstItemClick(position = p2) }
        recycler_view_drawer_1.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = menuItemAdapter1
        }

        val menuItemAdapter2 = MoreItemAdapter(context!!, initDrawerItem2(), false).apply {
            setOnItemClickListener { p0, p1, p2 -> onSecondItemClick(position = p2) }
            setOnItemChildClickListener(R.id.switch_more_item) {
                viewHolder, moreItemBean, i ->
                drawer_layout_home_2.closeDrawers()
                ThemeHelper.dayAndNightThemeChange(context!!)
            }
        }
        recycler_view_drawer_2.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = menuItemAdapter2
        }
    }

    private fun initDrawerItem1(): List<MoreItemBean> {
        val item1 = MoreItemBean(getString(R.string.my_start_post), R.drawable.xic_edit_blue_24dp)
        val item2 = MoreItemBean(getString(R.string.my_reply_post), R.drawable.xic_reply_red_24dp)
        val item3 = MoreItemBean(getString(R.string.my_fav_post), R.drawable.xic_star_border_teal_24dp)
        return listOf(item1, item2, item3)
    }

    private fun initDrawerItem2(): List<MoreItemBean> {
        val item4 = MoreItemBean(getString(R.string.bbs_navigation), R.drawable.xic_navigation_blue_24dp)
        val item1 = MoreItemBean(getString(R.string.theme_style), R.drawable.xic_style_pink_24dp, showSwitch = true, isCheck = ThemeHelper.isDarkTheme())
        val item2 = MoreItemBean(getString(R.string.setting_and_account), R.drawable.ic_setting_gray)
        val item3 = MoreItemBean(getString(R.string.about), R.drawable.xic_emot_blue_24dp)
        return listOf(item4, item1, item2, item3)
    }

    private fun onFirstItemClick(position: Int) {
        if (!LoginContext.userState(context)) return
        drawer_layout_home_2.closeDrawers()
        if (position == USER_POST_ITEM) {
            startActivity(Intent(context, UserPostActivity::class.java).apply {
                putExtra(USER_ID, userSimple.uid)
                putExtra(USER_POST_TYPE, USER_START_POST) })
            return
        }
        if (position == USER_REPLY_ITEM) {
            startActivity(Intent(context, UserPostActivity::class.java).apply {
                putExtra(USER_ID, userSimple.uid)
                putExtra(USER_POST_TYPE, USER_REPLY_POST) })
            return
        }
        if (position == USER_FAV_ITEM) {
            startActivity(Intent(context, UserPostActivity::class.java).apply {
                putExtra(USER_ID, userSimple.uid)
                putExtra(USER_POST_TYPE, USER_FAV_POST) })
            return
        }
    }

    private fun onSecondItemClick(position: Int) {
        drawer_layout_home_2.closeDrawers()
        if (position == NAVIGATION_ITEM) {
            showNavigationDialog()
            return
        }
        if (position == THEME_ITEM) {
            startActivity(Intent(context, ThemeActivity::class.java))
            return
        }
        if (position == SETTING_ITEM) {
            startActivity(Intent(context, SettingActivity::class.java))
            return
        }
        if (position == ABOUT_ITEM) {
            startActivity(Intent(context, AboutActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.menu_item_search_home_2 -> { startActivity(Intent(context, SearchActivity::class.java)) }
            R.id.menu_item_block_home_2 -> {
                pagePositionNow = if (pagePositionNow == PAGE_POSITION_BLOCK) {
                    showHideFragment(mFragments[PAGE_POSITION_HOME])
                    toolbar_home_2.title = getString(R.string.home_page)
                    PAGE_POSITION_HOME
                } else {
                    showHideFragment(mFragments[PAGE_POSITION_BLOCK])
                    toolbar_home_2.title = getString(R.string.forum_list_page)
                    PAGE_POSITION_BLOCK
                }
            }
            R.id.menu_item_msg_home_2 -> {
                pagePositionNow = if (pagePositionNow == PAGE_POSITION_MESSAGE) {
                    showHideFragment(mFragments[PAGE_POSITION_HOME])
                    toolbar_home_2.title = getString(R.string.home_page)
                    PAGE_POSITION_HOME
                } else {
                    showHideFragment(mFragments[PAGE_POSITION_MESSAGE])
                    toolbar_home_2.title = getString(R.string.message_page)
                    PAGE_POSITION_MESSAGE
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressedSupport() {
        log("stack ${supportFragmentManager.backStackEntryCount}")
        if (pagePositionNow != PAGE_POSITION_HOME) {
            showHideFragment(mFragments[PAGE_POSITION_HOME])
            toolbar_home_2.title = getString(R.string.home_page)
            pagePositionNow = PAGE_POSITION_HOME
        } else {
            ActivityCompat.finishAfterTransition(this)
        }
        //super.onBackPressedSupport()
    }

    private fun onThemeChanged() {
        val themeChanged: Boolean = intent.getBooleanExtra(DAY_NIGHT_THEME_CHANGE, false)
        if (themeChanged) {
            ActivityMgr.removeAllActivitiesExceptOne(this@HomeActivity2)
        }
    }

    private fun showNavigationDialog() {
        if (navigationDialog == null) {
            navigationDialog = AlertDialog.Builder(context)
                    .create()
        }
        navigationDialog?.show()
        navigationDialog?.setContentView(getNavigationDialogView())
        tvNaviLostAndFound?.setOnClickListener {
            startActivity(Intent(context, PListActivity::class.java).apply {
                putExtra(FID, 305)
                putExtra(TITLE, "失物招领")
            })
            navigationDialog?.dismiss()
        }
        tvNaviSchoolBus?.setOnClickListener {
            ClickContext.clickToPostDetail(context, 1430861)
            navigationDialog?.dismiss()
        }
        tvNaviCalendar?.setOnClickListener {
            ClickContext.clickToPostDetail(context, 1493930)
            navigationDialog?.dismiss()
        }
        tvNaviNewer?.setOnClickListener {
            ClickContext.clickToPostDetail(context, 1456557)
            navigationDialog?.dismiss()
        }
        btnNaviEnter?.setOnClickListener {
            navigationDialog?.dismiss()
        }
    }

    private fun getNavigationDialogView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_navigation, null)
        tvNaviLostAndFound = view.findViewById(R.id.navigation_lost_and_found)
        tvNaviSchoolBus = view.findViewById(R.id.navigation_school_bus)
        tvNaviCalendar = view.findViewById(R.id.navigation_calendar)
        tvNaviNewer = view.findViewById(R.id.navigation_novice)
        btnNaviEnter = view.findViewById(R.id.navigation_enter)
        return view
    }

    @Subscribe(threadMode = ThreadMode.MAIN) fun onThemeChanged(event: BaseEvent<ThemeChangedEvent>) {
        tv_user_name_drawer_header.setTextColor(ThemeHelper.getRefreshTextColor())
        tv_user_level_drawer_header.setTextColor(ThemeHelper.getRefreshTextColor())
    }
}