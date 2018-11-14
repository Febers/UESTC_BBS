/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.view

import android.os.Bundle
import androidx.annotation.UiThread
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.PostListAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.module.post.contract.PListContract
import com.febers.uestc_bbs.module.post.presenter.PListPresenterImpl
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.utils.ViewClickUtils
import com.febers.uestc_bbs.view.helper.finishFail
import com.febers.uestc_bbs.view.helper.finishSuccess
import com.febers.uestc_bbs.view.helper.initAttrAndBehavior
import kotlinx.android.synthetic.main.fragment_post_list.*
import kotlinx.android.synthetic.main.fragment_post_list_home.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.runOnUiThread

/**
 * 首页Fragment包含三个Fragment
 * 依次为最新回复，最新发表，热门帖子
 */
class PListHomeFragment: BaseFragment(), PListContract.View {

    private val postSimpleList: MutableList<PostListBean.ListBean> = ArrayList()
    private lateinit var postListAdapter: PostListAdapter
    private lateinit var pListPresenter: PListContract.Presenter
    private var page: Int = 1
    private var loadFinish = false

    override fun registerEventBus(): Boolean = true

    override fun setContentView(): Int {
        return R.layout.fragment_post_list_home
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        pListPresenter = PListPresenterImpl(this)
        postListAdapter = PostListAdapter(context!!, postSimpleList).apply {
            setOnItemClickListener { viewHolder, simplePostBean, i ->
                ViewClickUtils.clickToPostDetail(context,simplePostBean.topic_id ?: simplePostBean.source_id) }
            setOnItemChildClickListener(R.id.image_view_item_post_avatar) {
                viewHolder, simplePostBean, i -> ViewClickUtils.clickToUserDetail(context, simplePostBean.user_id)
            }
        }
        recyclerview_subpost_fragment.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postListAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        refresh_layout_post_fragment.apply {
            initAttrAndBehavior()
            setOnRefreshListener {
                page = 1
                getPost(page, true)
            }
            setOnLoadMoreListener {
                page++
                getPost(page, true)
            }
        }
        ThemeHelper.subscribeOnThemeChange(refresh_layout_post_fragment)
        postListAdapter.notifyDataSetChanged()
    }

    private fun getPost(page: Int, refresh: Boolean) {
        refresh_layout_post_fragment.setNoMoreData(false)
        pListPresenter.pListRequest(fid = mFid, page = page, refresh = refresh)
    }

    @UiThread
    override fun showPList(event: BaseEvent<PostListBean>) {
        loadFinish = true
        if (event.code == BaseCode.LOCAL) {
            context?.runOnUiThread {
                postListAdapter.setNewData(event.data.list)
            }
            return
        }
        refresh_layout_post_fragment?.finishSuccess()
        if (page == 1) {
            postListAdapter.setNewData(event.data.list)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_fragment?.finishLoadMoreWithNoMoreData()
            return
        }
        postListAdapter.setLoadMoreData(event.data.list)
    }

    override fun showError(msg: String) {
        showToast(msg)
        refresh_layout_post_fragment?.finishFail()
    }

    companion object {
        @JvmStatic
        fun newInstance(fid: Int) =
                PListHomeFragment().apply {
                    arguments = Bundle().apply {
                        putInt(FID, fid)
                    }
                }
    }

    /**
     * 接收用户重复按下tab时的消息
     * 然后刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTabReselceted(event: TabReselectedEvent) {
        if (isSupportVisible && loadFinish && event.position == 0) {
            scroll_view_plist_home?.scrollTo(0, 0)
            refresh_layout_post_fragment?.autoRefresh()
        }
    }

    /**
     * 接收到新帖发布之后
     * 刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPostNew(event: BaseEvent<String>) {
        if (isSupportVisible && event.code == BaseCode.SUCCESS) {
            scroll_view_plist_home?.scrollTo(0, 0)
            refresh_layout_post_list.autoRefresh()
        }
    }

    private fun setEmptyView() {
        val emptyView: View = LayoutInflater
                .from(context!!)
                .inflate(R.layout.layout_empty, recyclerview_subpost_fragment.parent as ViewGroup, false)
        postListAdapter.setEmptyView(emptyView)
    }

    override fun onDestroy() {
        super.onDestroy()
        postSimpleList.clear()
    }
}