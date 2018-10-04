/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.view

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febers.uestc_bbs.MyApplication

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.PostSimpleAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.SimplePListBean
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.post.presenter.PListContract
import com.febers.uestc_bbs.module.post.presenter.PListPresenterImpl
import com.febers.uestc_bbs.utils.ViewClickUtils
import kotlinx.android.synthetic.main.fragment_post_list_home.*
import org.jetbrains.anko.runOnUiThread

/**
 * 首页Fragment包含三个Fragment
 * 依次为最新回复，最新发表，热门帖子
 */
class PListHomeFragment: BaseFragment(), PListContract.View {

    private val PListList: MutableList<SimplePListBean> = ArrayList()
    private lateinit var postSimpleAdapter: PostSimpleAdapter
    private lateinit var user: UserBean
    private var PListPresenter:
            PListContract.Presenter = PListPresenterImpl(this)
    private var page: Int = 1
    private var shouldRefresh = true

    override fun setContentView(): Int {
        postSimpleAdapter = PostSimpleAdapter(context!!, PListList, false)
        return R.layout.fragment_post_list_home
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        user = MyApplication.getUser()
        recyclerview_subpost_fragment.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postSimpleAdapter
            addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        }

        refresh_layout_post_fragment.apply {
            setEnableLoadMore(false)
            autoRefresh()
            setOnRefreshListener {
                page = 1
                getPost(page, true)
            }
            setOnLoadMoreListener {
                getPost(++page, true)
            }
        }

        postSimpleAdapter.setOnItemClickListener { viewHolder, simplePostBean, i ->
            ViewClickUtils.clickToPostDetail(context, activity, simplePostBean.topic_id ?: simplePostBean.source_id) }

        //以下代码用来当Recyclerview滑动时不加载图片，暂时失效
        recyclerview_subpost_fragment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {   //滑动静止时
                    postSimpleAdapter.setScrolling(false)
                    postSimpleAdapter.notifyDataSetChanged()
                } else {
                    postSimpleAdapter.setScrolling(true)
                }
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
    }

    private fun getPost(page: Int, refresh: Boolean) {
        refresh_layout_post_fragment.setNoMoreData(false)
        PListPresenter.pListRequest(fid = mFid!!, page = page, refresh = refresh)
    }

    @UiThread
    override fun showPList(event: BaseEvent<List<SimplePListBean>?>) {
        if (event.code == BaseCode.FAILURE) {
            onError(event.data!![0].title!!)
            refresh_layout_post_fragment?.apply {
                finishRefresh(false)
                finishLoadMore(false)
            }
            return
        }
        if (event.code == BaseCode.LOCAL) {
            context?.runOnUiThread {
                postSimpleAdapter.setNewData(event.data)
            }
            return
        }
        refresh_layout_post_fragment?.apply {
            finishRefresh(true)
            finishLoadMore(true)
            setEnableLoadMore(true)
        }
        if (page == 1) {
            postSimpleAdapter.setNewData(event.data)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_fragment?.finishLoadMoreWithNoMoreData()
            return
        }
        postSimpleAdapter.setLoadMoreData(event.data)
    }

    companion object {
        @JvmStatic
        fun newInstance(fid: String) =
                PListHomeFragment().apply {
                    arguments = Bundle().apply {
                        putString(FID, fid)
                    }
                }
    }

//    override fun registerEventBus(): Boolean {
//        return true
//    }

    /**
     * 登录成功,获取数据
     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onLoginSuccess(event: BaseEvent<UserBean>) {
//        user = event.data
//        shouldRefresh = true
//    }

    private fun setEmptyView() {
        val emptyView: View = LayoutInflater
                .from(context!!)
                .inflate(R.layout.layout_empty, recyclerview_subpost_fragment.parent as ViewGroup, false)
        postSimpleAdapter.setEmptyView(emptyView)
    }
}