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
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.PostSimpleItemAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.SimplePListBean
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.post.presenter.PListContract
import com.febers.uestc_bbs.module.post.presenter.PListPresenterImpl
import kotlinx.android.synthetic.main.fragment_post_list_home.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 首页Fragment包含三个Fragment
 * 依次为最新回复，最新发表，热门帖子
 */
class PListHomeFragment: BaseFragment(), PListContract.View {

    private val PListList: MutableList<SimplePListBean> = ArrayList()
    private lateinit var postSimpleItemAdapter: PostSimpleItemAdapter
    private lateinit var user: UserBean
    private var PListPresenter:
            PListContract.Presenter = PListPresenterImpl(this)
    private var page: Int = 1
    private var shouldRefresh = true

    override fun setContentView(): Int {
        postSimpleItemAdapter = PostSimpleItemAdapter(context!!, PListList, true)
        return R.layout.fragment_post_list_home
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        user = BaseApplication.getUser()
        recyclerview_subpost_fragment.layoutManager = LinearLayoutManager(context)
        recyclerview_subpost_fragment.adapter = postSimpleItemAdapter
        recyclerview_subpost_fragment.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

        refresh_layout_post_fragment.setEnableLoadMore(false)
        refresh_layout_post_fragment.autoRefresh()
        refresh_layout_post_fragment.setOnRefreshListener {
            page = 1
            getPost(page, true)
        }
        refresh_layout_post_fragment.setOnLoadMoreListener { getPost(++page, true) }
        postSimpleItemAdapter.setOnItemClickListener { viewHolder, simplePostBean, i -> clickItem(simplePostBean) }
    }

    private fun getPost(page: Int, refresh: Boolean) {
        refresh_layout_post_fragment.setNoMoreData(false)
        PListPresenter.pListRequest(fid = fid!!, page = page, refresh = refresh)
    }

    /**
     * 登录成功,获取数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSeccess(event: BaseEvent<UserBean>) {
        user = event.data
        shouldRefresh = true
    }

    @UiThread
    override fun pListResult(event: BaseEvent<List<SimplePListBean>?>) {
        if (event.code == BaseCode.FAILURE) {
            onError(event!!.data!![0]!!.title!!)    //我佛了
            refresh_layout_post_fragment?.finishRefresh(false)
            refresh_layout_post_fragment?.finishLoadMore(false)
            return
        }
        refresh_layout_post_fragment?.finishRefresh()
        refresh_layout_post_fragment?.finishLoadMore()
        refresh_layout_post_fragment?.setEnableLoadMore(true)
        if (page == 1) {
            i("DATA", event.data.toString())
            postSimpleItemAdapter.setNewData(event.data)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_fragment?.finishLoadMoreWithNoMoreData()
            return
        }
        postSimpleItemAdapter.setLoadMoreData(event.data)
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

    override fun registeEventBus(): Boolean {
        return true
    }

    private fun setEmptyView() {
        val emptyView: View = LayoutInflater
                .from(context!!)
                .inflate(R.layout.layout_empty, recyclerview_subpost_fragment.parent as ViewGroup, false)
        postSimpleItemAdapter.setEmptyView(emptyView)
    }

    private fun clickItem(PList: SimplePListBean) {
        var mParentFragment = parentFragment as BaseFragment
        var tid = PList.topic_id
        if(tid == null) {
            i("STF null", "${PList.topic_id == null}")
             tid = PList.source_id
        }
        mParentFragment.start(PostDetailFragment.newInstance(fid = tid!!, showBottomBarOnDestroy = true))
    }
}