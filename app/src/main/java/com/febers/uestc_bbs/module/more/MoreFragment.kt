package com.febers.uestc_bbs.module.more

import android.content.Intent
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.app.AlertDialog
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.view.adapter.MoreItemAdapter
import com.febers.uestc_bbs.entity.MoreItemBean
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.context.LoginContext
import com.febers.uestc_bbs.module.setting.SettingActivity
import com.febers.uestc_bbs.module.theme.ThemeActivity
import com.febers.uestc_bbs.module.user.view.UserPostActivity
import com.febers.uestc_bbs.module.theme.ThemeManager
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.module.post.view.PListActivity
import com.febers.uestc_bbs.module.setting.AboutActivity
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.search.view.SearchActivity
import com.febers.uestc_bbs.module.setting.AccountActivity
import com.febers.uestc_bbs.module.user.view.UserHistoryActivity
import com.febers.uestc_bbs.utils.colorAccent
import kotlinx.android.synthetic.main.fragment_more.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

const val FIRST_ITEM_VIEW = -1
const val SECOND_ITEM_VIEW = 0
const val THIRD_ITEM_VIEW = 1

const val USER_DETAIL_ITEM = -1
const val USER_POST_ITEM = 0
const val USER_REPLY_ITEM = 1
const val USER_FAV_ITEM = 2
const val USER_HISTORY_ITEM = 3
const val USER_FRIEND_ITEM = 4
const val SEARCH_ITEM = 5

const val NAVIGATION_ITEM = 0
const val THEME_ITEM = 1
const val ACCOUNT_ITEM = 2
const val SETTING_ITEM = 3
const val ABOUT_ITEM = 4
const val DEBUG_ITEM = 5

class MoreFragment: BaseFragment() {

    private lateinit var userSimple: UserSimpleBean
    private lateinit var mParentFragment: BaseFragment

    private var navigationDialog: AlertDialog? = null
    private var tvNaviLostAndFound: LinearLayout? = null
    private var tvNaviSchoolBus: LinearLayout? = null
    private var tvNaviCalendar: LinearLayout? = null
    private var tvNaviNewer: LinearLayout? = null
    private var btnNaviEnter: Button? = null

    override fun registerEventBus(): Boolean = true

    override fun setView(): Int = R.layout.fragment_more

    override fun initView() {
        initMenu()
        userSimple = MyApp.user()
        mParentFragment = parentFragment as BaseFragment
        more_fragment_header.setOnClickListener { itemClick(FIRST_ITEM_VIEW, USER_DETAIL_ITEM) }

        val moreItemAdapter1 = MoreItemAdapter(context!!, initMoreItem1(), false)
        moreItemAdapter1.setOnItemClickListener { p0, p1, p2 -> itemClick(view = SECOND_ITEM_VIEW, position = p2) }
        more_fragment_recyclerview_1.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = moreItemAdapter1
        }

        val moreItemAdapter2 = MoreItemAdapter(context!!, initMoreItem2(), false).apply {
            setOnItemClickListener { p0, p1, p2 -> itemClick(view = THIRD_ITEM_VIEW, position = p2) }
            setOnItemChildClickListener(R.id.switch_more_item) {
                viewHolder, moreItemBean, i ->
                ThemeManager.dayAndNightThemeChange(activity)
                activity?.recreate()
            }
        }
        more_fragment_recyclerview_2.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = moreItemAdapter2
        }
        initUserDetail()
        if (userSimple.valid) {
            text_view_fragment_user_name.text = userSimple.name
            text_view_fragment_user_title.text = userSimple.title
            ImageLoader.load(context!!, userSimple.avatar, image_view_fragment_user_avatar, clickToViewer = false)
        }
    }

    private fun initMoreItem1(): List<MoreItemBean> {
        val itemStart = MoreItemBean(getString(R.string.my_start_post), R.drawable.xic_edit_blue_24dp)
        val itemReply = MoreItemBean(getString(R.string.my_reply_post), R.drawable.xic_reply_red_24dp)
        val itemFav = MoreItemBean(getString(R.string.my_fav_post), R.drawable.xic_star_border_teal_24dp)
        val itemHistory = MoreItemBean(getString(R.string.browsing_history), R.drawable.xic_history_purple)
        return listOf(itemStart, itemReply, itemFav)
    }

    private fun initMoreItem2(): List<MoreItemBean> {
        val list: MutableList<MoreItemBean> = ArrayList()
        val itemNav = MoreItemBean(getString(R.string.bbs_navigation), R.drawable.xic_navigation_blue_24dp)
        val itemTheme = MoreItemBean(getString(R.string.theme_style), R.drawable.xic_style_pink_24dp, showSwitch = true, isCheck = ThemeManager.isNightTheme())
        val itemAccount = MoreItemBean(getString(R.string.account), R.drawable.xic_person_blue_24dp)
        val itemSetting = MoreItemBean(getString(R.string.setting), R.drawable.ic_setting_gray)
        val itemAbout = MoreItemBean(getString(R.string.about), R.drawable.xic_emot_blue_24dp)
        list.add(itemNav); list.add(itemTheme); list.add(itemAccount); list.add(itemSetting); list.add(itemAbout)
        if (MyApp.isDebug()) {
            list.add(MoreItemBean("Debug", R.drawable.xic_bug_gray))
        }
        return list
    }

    private fun initUserDetail() {
        text_view_fragment_user_name.text = getString(R.string.please_login_or_sign_up)
        text_view_fragment_user_title.text = " "
        ImageLoader.loadResource(context, R.drawable.ic_default_avatar_circle, image_view_fragment_user_avatar, isCircle = true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onUserUpdate(event: UserUpdateEvent) {
        val simpleBean = event.user
        if (event.code != BaseCode.FAILURE) {
            text_view_fragment_user_name.text = simpleBean.name
            text_view_fragment_user_title.text = simpleBean.title
            ImageLoader.load(context!!, simpleBean.avatar, image_view_fragment_user_avatar, clickToViewer = false)
            userSimple = simpleBean
        } else {
            initUserDetail()
        }
    }

    private fun itemClick(view: Int, position: Int) {
        if (view == FIRST_ITEM_VIEW) {
            ClickContext.clickToUserDetail(context, userSimple.uid)
            return
        }
        if (view == SECOND_ITEM_VIEW) {
            if (!LoginContext.userState(context!!)) return
            if (position == USER_POST_ITEM) {
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
            if (position == USER_HISTORY_ITEM) {
                startActivity(Intent(activity, UserHistoryActivity::class.java))
            }
            if (position == USER_FRIEND_ITEM) {
                return
            }
            if (position == SEARCH_ITEM) {
                startActivity(Intent(activity, SearchActivity::class.java))
                return
            }
        }
        if (view == THIRD_ITEM_VIEW) {
            if (position == NAVIGATION_ITEM) {
                showNavigationDialog()
                return
            }
            if (position == THEME_ITEM) {
                startActivity(Intent(activity, ThemeActivity::class.java))
                return
            }
            if (position == ACCOUNT_ITEM) {
                startActivity(Intent(activity, AccountActivity::class.java))
                return
            }
            if (position == SETTING_ITEM) {
                startActivity(Intent(activity, SettingActivity::class.java))
                return
            }
            if (position == ABOUT_ITEM) {
                startActivity(Intent(activity, AboutActivity::class.java))
            }
            if (position == DEBUG_ITEM) {
                startActivity(Intent(activity, DebugActivity::class.java))
            }
        }
    }

    private fun initMenu() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar_common)
        setHasOptionsMenu(true)
        toolbar_common.inflateMenu(R.menu.menu_more_fragment)
        toolbar_common.title = getString(R.string.more_page)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_more_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_search_more_fragment && LoginContext.userState(context!!)) {
            startActivity(Intent(activity, SearchActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showNavigationDialog() {
        if (navigationDialog == null) {
            navigationDialog = AlertDialog.Builder(context!!)
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
        btnNaviEnter?.setTextColor(colorAccent())
        return view
    }
}