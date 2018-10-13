package com.febers.uestc_bbs.module.user.view

import android.content.Intent
import android.support.v7.widget.Toolbar
import android.util.Log.i
import android.view.MenuItem
import android.view.View
import com.febers.uestc_bbs.MyApplication
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.UserDetailAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.DetailItemBean
import com.febers.uestc_bbs.entity.UserDetailBean
import com.febers.uestc_bbs.module.user.presenter.UserContract
import com.febers.uestc_bbs.module.user.presenter.UserPresenterImpl
import com.febers.uestc_bbs.utils.GenderUtils
import com.febers.uestc_bbs.utils.ImageLoader
import com.febers.uestc_bbs.utils.ViewClickUtils
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : BaseSwipeActivity(), UserContract.View {

    private lateinit var userBottomSheet: UserDetailBottomSheet
    private lateinit var userPresenter: UserContract.Presenter
    private var userItSelf = false
    private var userId: Int = 0

    override fun hideStatusBar(): Boolean = true

    override fun setMenu(): Int? {
        if (userItSelf) {
            return R.menu.menu_user_detail
        }
        return null
    }

    override fun setView(): Int {
        userId = intent.getIntExtra(USER_ID, 0)
        if (userId == MyApplication.getUser().uid) userItSelf = true
        return R.layout.activity_user_detail
    }

    override fun setToolbar(): Toolbar? {
        return toolbar_user_detail
    }

    override fun initView() {
        i("User", userId.toString())
        userPresenter = UserPresenterImpl(this)
        refresh_layout_user_detail.apply {
            autoRefresh()
            setEnableLoadMore(false)
            setOnRefreshListener { getUserDetail() }
        }
        if (userItSelf) {
            userBottomSheet = UserDetailBottomSheet(this, R.style.PinkBottomSheetTheme)
        }
    }

    private fun getUserDetail() {
        userPresenter.userDetailRequest(userId)
    }

    override fun showUserDetail(event: BaseEvent<UserDetailBean>) {
        recyclerview_user_detail?.adapter = UserDetailAdapter(this, initUserItem(event.data))
        collapsing_toolbar_layout_detail?.title = event.data.name
        ImageLoader.load(this, event.data.icon, image_view_detail_blur_avatar, placeImage = null, isCircle = false, isBlur = true, noCache = true)
        ImageLoader.load(this, event.data.icon, image_view_detail_avatar, placeImage = null, isCircle = true, noCache = true)
        image_view_detail_avatar.setOnClickListener {
            ViewClickUtils.clickToImageViewerByUid(uid = userId, context = this)
        }
        refresh_layout_user_detail?.finishRefresh()
        if (userItSelf) return

        fab_user_detail?.let { it ->
            it.visibility = View.VISIBLE
            it.setOnClickListener { ViewClickUtils.clickToPrivateMsg(this@UserDetailActivity, userId, event.data.name) }
        }
        linear_layout_user_post_start?.apply {
            visibility = View.VISIBLE
            setOnClickListener{
                startActivity(Intent(this@UserDetailActivity, UserPostActivity::class.java).apply {
                    putExtra(USER_ID, userId)
                    putExtra(USER_POST_TYPE, USER_START_POST) })
            }
        }
        linear_layout_user_post_reply?.apply {
            visibility = View.VISIBLE
            setOnClickListener{
                startActivity(Intent(this@UserDetailActivity, UserPostActivity::class.java).apply {
                    putExtra(USER_ID, userId)
                    putExtra(USER_POST_TYPE, USER_REPLY_POST) })
            }
        }
    }

    private fun initUserItem(user: UserDetailBean): List<DetailItemBean> {
        val item1 = DetailItemBean("签名", if(user.sign.isNullOrEmpty()) "签名未设置" else user.sign)
        val item2 = DetailItemBean("性别", GenderUtils.change(user.gender.toString()))
        val item3 = DetailItemBean("等级", user.userTitle)
        val item4 = DetailItemBean("积分", user.score.toString())
        val item5 = DetailItemBean("水滴", user.gold_num.toString())
        if (user.body?.profileList!!.isNotEmpty()) {

        }
        return arrayListOf(item1, item2, item3, item4, item5)
    }

    override fun showError(msg: String) {
        showToast(msg)
        refresh_layout_user_detail?.finishRefresh(false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_item_user_edit) {
            userBottomSheet.show()
        }
        return super.onOptionsItemSelected(item)
    }
}
