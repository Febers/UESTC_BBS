package com.febers.uestc_bbs.module.user.view

import android.content.Intent
import android.support.v7.widget.Toolbar
import android.util.Log.i
import android.view.MenuItem
import android.view.View
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.UserDetailAdapter
import com.febers.uestc_bbs.MyApplication
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.DetailItemBean
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.entity.UserDetailBean
import com.febers.uestc_bbs.module.user.presenter.UserContract
import com.febers.uestc_bbs.module.user.presenter.UserPresenterImpl
import com.febers.uestc_bbs.utils.GenderUtils
import com.febers.uestc_bbs.utils.ImageLoader
import com.febers.uestc_bbs.utils.ViewClickUtils
import kotlinx.android.synthetic.main.activity_user_detail.*

class UserDetailActivity : BaseSwipeActivity(), UserContract.View {

    private lateinit var userPresenter: UserContract.Presenter
    private lateinit var userSimple: UserSimpleBean
    private var userItSelf = false
    private var userId: String? = ""

    override fun noStatusBar(): Boolean = true

    override fun setMenu(): Int? {
        if (userItSelf) {
            return R.menu.menu_user_detail
        }
        return null
    }

    override fun setView(): Int {
        userItSelf = intent.getBooleanExtra(USER_IT_SELF, false)
        userId = intent.getStringExtra(USER_ID)
        return R.layout.activity_user_detail
    }

    override fun setToolbar(): Toolbar? {
        return toolbar_user_detail
    }

    override fun initView() {
        if (userItSelf) {
            initUserItSelfView()
            return
        }
        i("User", userId)
        userPresenter = UserPresenterImpl(this)
        refresh_layout_user_detail.apply {
            autoRefresh()
            setEnableLoadMore(false)
            setOnRefreshListener { getUserDetail() }
        }
    }

    private fun getUserDetail() {
        userPresenter.userDetailRequest(userId.toString())
    }

    override fun showUserDetail(event: BaseEvent<UserDetailBean>) {
        if (event.code == BaseCode.FAILURE) {
            refresh_layout_user_detail?.finishRefresh(false)
            showToast(event.data.errcode)
            return
        }
        recyclerview_user_detail?.adapter = UserDetailAdapter(this, initUserItem(event.data))
        collapsing_toolbar_layout_detail?.title = event.data.name
        ImageLoader.load(this, event.data.icon, image_view_detail_blur_avatar, placeImage = null, isCircle = false, isBlur = true, noCache = true)
        ImageLoader.load(this, event.data.icon, image_view_detail_avatar, placeImage = null, isCircle = true, noCache = true)
        image_view_detail_avatar.setOnClickListener {
            ViewClickUtils.clickToImageViewByUid(uid = userId, context = this)
        }
        fab_user_detail?.let { it ->
            it.visibility = View.VISIBLE
            it.setOnClickListener { ViewClickUtils.clickToPM(this@UserDetailActivity, userId, event.data.name) }
        }
        linear_layout_user_post_start?.visibility = View.VISIBLE
        linear_layout_user_post_reply?.visibility = View.VISIBLE
        refresh_layout_user_detail?.finishRefresh()
    }

    private fun initUserItSelfView() {
        userSimple = MyApplication.getUser()
        refresh_layout_user_detail.apply {
            isEnabled = false
        }
        recyclerview_user_detail?.adapter = UserDetailAdapter(this, initUserItSelfItem())
        collapsing_toolbar_layout_detail?.title = userSimple.name
        ImageLoader.load(this, userSimple.avatar, image_view_detail_blur_avatar, placeImage = null, isCircle = false, isBlur = true, noCache = true)
        ImageLoader.load(this, userSimple.avatar, image_view_detail_avatar, placeImage = null, isCircle = true, noCache = true)
        image_view_detail_avatar.setOnClickListener {
            ViewClickUtils.clickToImageViewByUid(uid = userSimple.uid, context = this)
        }
    }

    private fun initUserItSelfItem(): List<DetailItemBean> {
        val item1 = DetailItemBean("签名", if(userSimple.sign.isEmpty()) "签名未设置" else userSimple.sign)
        val item2 = DetailItemBean("性别", GenderUtils.change(userSimple.gender))
        val item3 = DetailItemBean("等级", userSimple.title)
        val item4 = DetailItemBean("积分", userSimple.credits)
        val item5 = DetailItemBean("水滴", userSimple.extcredits2)
        return arrayListOf(item1, item2, item3, item4, item5)
    }

    private fun initUserItem(user: UserDetailBean): List<DetailItemBean> {
        val item1 = DetailItemBean("签名", if(user.sign.isNullOrEmpty()) "该用户未设置签名" else user.sign)
        val item2 = DetailItemBean("性别", GenderUtils.change(user.gender.toString()))
        val item3 = DetailItemBean("等级", user.userTitle)
        val item4 = DetailItemBean("积分", user.score.toString())
        val item5 = DetailItemBean("水滴", user.gold_num.toString())
        return arrayListOf(item1, item2, item3, item4, item5)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        startActivity(Intent(this, UserEditActivity::class.java))
        return super.onOptionsItemSelected(item)
    }
}
