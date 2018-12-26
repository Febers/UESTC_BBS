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
import com.febers.uestc_bbs.module.search.view.SearchActivity
import com.febers.uestc_bbs.module.setting.SettingActivity
import com.febers.uestc_bbs.module.theme.ThemeActivity
import com.febers.uestc_bbs.module.user.view.UserPostActivity
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.module.post.view.PListActivity
import com.febers.uestc_bbs.module.setting.AboutActivity
import com.febers.uestc_bbs.module.context.ClickContext
import kotlinx.android.synthetic.main.fragment_more.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MoreFragment: BaseFragment() {

    private val FIRST_ITEM_VIEW = -1
    private val SECOND_ITEM_VIEW = 0
    private val THIRD_ITEM_VIEW = 1

    private val USER_DETAIL_ITEM = -1
    private val USER_POST_ITEM = 0
    private val USER_REPLY_ITEM = 1
    private val USER_FAV_ITEM = 2
    private val USER_FRIEND_ITEM = 3
    private val SEARCH_ITEM = 4

    private val NAVIGATION_ITEM = 0
    private val THEME_ITEM = 1
    private val SETTING_ITEM = 2
    private val ABOUT_ITEM = 3

    private lateinit var userSimple: UserSimpleBean
    private lateinit var mParentFragment: BaseFragment

    private var navigationDialog: AlertDialog? = null
    private var tvNaviLostAndFound: LinearLayout? = null
    private var tvNaviSchoolBus: LinearLayout? = null
    private var tvNaviCalendar: LinearLayout? = null
    private var tvNaviNewer: LinearLayout? = null
    private var btnNaviEnter: Button? = null

    override fun registerEventBus(): Boolean = true

    override fun setView(): Int {
        return R.layout.fragment_more
    }

    override fun initView() {
        initMenu()
        userSimple = MyApp.getUser()
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
                ThemeHelper.dayAndNightThemeChange(context!!)
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
        val item1 = MoreItemBean(getString(R.string.my_start_post), R.drawable.xic_edit_blue_24dp)
        val item2 = MoreItemBean(getString(R.string.my_reply_post), R.drawable.xic_reply_red_24dp)
        val item3 = MoreItemBean(getString(R.string.my_fav_post), R.drawable.xic_star_border_teal_24dp)
//        val item4 = MoreItemBean("", R.drawable.xic_person_blue_24dp)
//        val item5 = MoreItemBean("", R.drawable.xic_search_white_24dp)
        return listOf(item1, item2, item3)
    }

    private fun initMoreItem2(): List<MoreItemBean> {
        val item4 = MoreItemBean(getString(R.string.bbs_navigation), R.drawable.xic_navigation_blue_24dp)
        val item1 = MoreItemBean(getString(R.string.theme_style), R.drawable.xic_style_pink_24dp, showSwitch = true, isCheck = ThemeHelper.isDarkTheme())
        val item2 = MoreItemBean(getString(R.string.setting_and_account), R.drawable.ic_setting_gray)
        val item3 = MoreItemBean(getString(R.string.about), R.drawable.xic_emot_blue_24dp)
        return listOf(item4, item1, item2, item3)
    }

    private fun initUserDetail() {
        text_view_fragment_user_name.text = getString(R.string.please_login_or_sign_up)
        text_view_fragment_user_title.text = " "
        ImageLoader.loadResource(context, R.drawable.ic_default_avatar_circle, image_view_fragment_user_avatar, isCircle = true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSuccess(event: BaseEvent<UserSimpleBean>) {
        if (event.code != BaseCode.FAILURE) {
            text_view_fragment_user_name.text = event.data.name
            text_view_fragment_user_title.text = event.data.title
            ImageLoader.load(context!!, event.data.avatar, image_view_fragment_user_avatar, clickToViewer = false)
            userSimple = event.data
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
            if (position == SETTING_ITEM) {
                startActivity(Intent(activity, SettingActivity::class.java))
                return
            }
            if (position == ABOUT_ITEM) {
                startActivity(Intent(activity, AboutActivity::class.java))
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
        if (item?.itemId == R.id.menu_item_search_more_fragment && LoginContext.userState(context!!)) {
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
        return view
    }
}