package com.febers.uestc_bbs.module.user.view

import android.content.Intent
import android.support.annotation.UiThread
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.UserPostBean
import com.febers.uestc_bbs.module.post.view.PostDetailActivity
import com.febers.uestc_bbs.module.user.presenter.UserContract
import com.febers.uestc_bbs.module.user.presenter.UserPresenterImpl
import com.febers.uestc_bbs.view.adapter.UserPostAdapter
import kotlinx.android.synthetic.main.activity_user_post.*

class UserPostActivity: BaseSwipeActivity(), UserContract.View {

    private val userPostList: MutableList<UserPostBean.ListBean> = ArrayList()
    private lateinit var userPListPresenter: UserContract.Presenter
    private lateinit var userPListAdapter: UserPostAdapter
    private var page = 1
    private var uid = ""
    private var type = USER_START_POST

    override fun setToolbar(): Toolbar? {
        return toolbar_user_post
    }

    override fun setView(): Int {
        uid = intent.getStringExtra(USER_ID)
        type = intent.getStringExtra(USER_POST_TYPE)
        return R.layout.activity_user_post
    }

    override fun initView() {
        setToolbarTitle()
        userPListPresenter = UserPresenterImpl(this)
        userPListAdapter = UserPostAdapter(this, userPostList, false).apply {
            setOnItemClickListener { viewHolder, listBean, i -> onClickItem(listBean) }
        }
        recyclerview_user_post.apply {
            adapter = userPListAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        }
        refresh_layout_user_post.apply {
            setEnableLoadMore(false)
            autoRefresh()
            setOnRefreshListener {
                page = 1
                getUserPost()
            }
            setOnLoadMoreListener {
                ++page
                getUserPost()
            }
        }
    }

    private fun getUserPost() {
        refresh_layout_user_post.setNoMoreData(false)
        userPListPresenter.userPostRequest(uid+"", type, ""+page)
    }

    @UiThread
    override fun showUserPost(event: BaseEvent<UserPostBean>) {
        if (event.code == BaseCode.LOCAL) {
            runOnUiThread {
                userPListAdapter.setNewData(event.data.list)
            }
            return
        }
        refresh_layout_user_post?.apply {
            finishRefresh(true)
            finishLoadMore(true)
            setEnableLoadMore(true)
        }
        if (page == 1) {
            userPListAdapter.setNewData(event.data.list)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_user_post?.finishLoadMoreWithNoMoreData()
        }
        userPListAdapter.setLoadMoreData(event.data.list)
    }

    private fun onClickItem(item: UserPostBean.ListBean) {
        val tid = item.topic_id
        startActivity(Intent(this, PostDetailActivity::class.java).apply { putExtra("mFid", ""+tid) })
    }

    private fun setToolbarTitle() {
        if (type == USER_START_POST) {
            toolbar_user_post?.title = "发表"
        }
        if (type == USER_REPLY_POST) {
            toolbar_user_post?.title = "回复"
        }
        if (type == USER_FAV_POST) {
            toolbar_user_post?.title = "收藏"
        }
    }

    override fun showError(msg: String) {
        showToast(msg)
        refresh_layout_user_post?.apply {
            finishRefresh(false)
            finishLoadMore(false)
        }
    }
}