package com.febers.uestc_bbs.module.user.view

import android.content.Intent
import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.UserPListBean
import com.febers.uestc_bbs.module.post.view.PostDetailActivity
import com.febers.uestc_bbs.module.user.presenter.UserContract
import com.febers.uestc_bbs.module.user.presenter.UserPresenterImpl
import com.febers.uestc_bbs.view.adapter.UserPostAdapter
import kotlinx.android.synthetic.main.fragment_user_post.*
import org.jetbrains.anko.runOnUiThread

class UserPListFragment: BaseSwipeFragment(), UserContract.View {

    private val userPListList: MutableList<UserPListBean.ListBean> = ArrayList()
    private lateinit var userPListPresenter: UserContract.Presenter
    private lateinit var userPListAdapter: UserPostAdapter
    private var page = 1
    private var type = USER_START_POST

    override fun setToolbar(): Toolbar? {
        return toolbar_user_post
    }

    override fun setContentView(): Int {
        arguments?.let {
            type = it.getString(USER_POST_TYPE)
        }
        userPListPresenter = UserPresenterImpl(this)
        return R.layout.fragment_user_post
    }


    override fun initView() {
        setToolbarTitle()
        userPListAdapter = UserPostAdapter(context!!, userPListList, false).apply {
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
                getUserPList()
            }
            setOnLoadMoreListener {
                ++page
                getUserPList()
            }
        }
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        getUserPList()
    }

    private fun getUserPList() {
        refresh_layout_user_post.setNoMoreData(false)
        userPListPresenter.userPListRequest(mUid+"", type, ""+page)
    }

    @UiThread
    override fun showUserPList(event: BaseEvent<UserPListBean>) {
        if (event.code == BaseCode.FAILURE) {
            showToast(event.data.errcode!!)
            refresh_layout_user_post?.apply {
                finishRefresh(false)
                finishLoadMore(false)
            }
            return
        }
        if (event.code == BaseCode.LOCAL) {
            context?.runOnUiThread {
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

    private fun onClickItem(item: UserPListBean.ListBean) {
        val tid = item.topic_id
        startActivity(Intent(activity, PostDetailActivity::class.java).apply { putExtra("mFid", ""+tid) })
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

    companion object {
        @JvmStatic
        fun newInstance(uid: String, type: String, showBottomBarOnDestroy: Boolean) =
                UserPListFragment().apply {
                    arguments = Bundle().apply {
                        putString(UID, uid)
                        putString(USER_POST_TYPE, type)
                        putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
                    }
                }
    }

}