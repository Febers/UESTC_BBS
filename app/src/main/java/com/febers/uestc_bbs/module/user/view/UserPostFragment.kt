package com.febers.uestc_bbs.module.user.view

import android.content.Intent
import android.os.Bundle
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
import com.febers.uestc_bbs.view.adaper.UserPostAdapter
import kotlinx.android.synthetic.main.fragment_user_post.*

class UserPostFragment: BaseSwipeFragment(), UserContract.View {

    private val userPostList: MutableList<UserPostBean.ListBean> = ArrayList()
    private lateinit var userPostPresenter: UserContract.Presenter
    private lateinit var userPostAdapter: UserPostAdapter
    private var page = 1
    private var type = USER_START_POST

    override fun setToolbar(): Toolbar? {
        return toolbar_user_post
    }

    override fun setContentView(): Int {
        arguments?.let {
            type = it.getInt(USER_POST_TYPE)
        }
        userPostPresenter = UserPresenterImpl(this)
        return R.layout.fragment_user_post
    }


    override fun initView() {
        setToolbarTitle()
        userPostAdapter = UserPostAdapter(context!!, userPostList, false).apply {
            setOnItemClickListener { viewHolder, listBean, i -> onClickItem(listBean) }
        }
        recyclerview_user_post.apply {
            adapter = userPostAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))}
        refresh_layout_user_post.apply {
            setEnableLoadMore(false)
            autoRefresh()
            setOnRefreshListener {
                page = 1
                getUserPost()
            }
            setOnLoadMoreListener {
                ++page
                getUserPost()}
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        getUserPost()
    }

    private fun getUserPost() {
        refresh_layout_user_post.setNoMoreData(false)
        userPostPresenter.userPostRequest(uid+"", type, ""+page)
    }

    @UiThread
    override fun showUserPost(event: BaseEvent<UserPostBean>) {
        if (event.code == BaseCode.FAILURE) {
            onError(event.data.errcode!!)
            refresh_layout_user_post.apply {
                finishRefresh(false)
                finishLoadMore(false)
            }
            return
        }
        refresh_layout_user_post.apply {
            finishRefresh(true)
            finishLoadMore(true)
            setEnableLoadMore(true)
        }
        if (page == 1) {
            userPostAdapter.setNewData(event.data.list)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_user_post.finishLoadMoreWithNoMoreData()
        }
        userPostAdapter.setLoadMoreData(event.data.list)
    }

    private fun onClickItem(item: UserPostBean.ListBean) {
        val tid = item.topic_id
        startActivity(Intent(activity, PostDetailActivity::class.java).apply { putExtra("fid", ""+tid) })
    }

    private fun setToolbarTitle() {
        if (type == USER_START_POST) {
            toolbar_user_post.title = "发表"
        }
        if (type == USER_REPLY_POST) {
            toolbar_user_post.title = "回复"
        }
        if (type == USER_FAV_POST) {
            toolbar_user_post.title = "收藏"
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(uid: String, type: Int, showBottomBarOnDestroy: Boolean) =
                UserPostFragment().apply {
                    arguments = Bundle().apply {
                        putString(UID, uid)
                        putInt(USER_POST_TYPE, type)
                        putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
                    }
                }
    }

}