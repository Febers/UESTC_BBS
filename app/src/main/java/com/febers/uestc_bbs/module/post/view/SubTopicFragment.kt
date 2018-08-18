/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.view

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log.i
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.PostItemAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.SimpleTopicBean
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.post.presenter.TopicContract
import com.febers.uestc_bbs.module.post.presenter.TopicPresenterImpl
import kotlinx.android.synthetic.main.fragment_sub_post.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 首页Fragment包含三个Fragment
 * 依次为最新回去，最新发表，热门帖子
 */
class SubTopicFragment: BaseFragment(), TopicContract.View {

    private val topicList: MutableList<SimpleTopicBean> = ArrayList()
    private lateinit var mParentFragment: BaseFragment
    private lateinit var mPParentFragment: BaseFragment
    private lateinit var postItemAdapter: PostItemAdapter
    private lateinit var user: UserBean
    private var topicPresenter:
            TopicContract.Presenter = TopicPresenterImpl(this)
    private var page: Int = 1
    private var shouldRefresh = true

    override fun setContentView(): Int {
        postItemAdapter = PostItemAdapter(context!!, topicList, true)
        return R.layout.fragment_sub_post
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        user = BaseApplication.getUser()
        mParentFragment = parentFragment as BaseFragment
        postItemAdapter.setLoadingView(R.layout.layout_loading)
        postItemAdapter.setOnLoadMoreListener { getPost(++page, true) }
        postItemAdapter.setLoadEndView(R.layout.layout_load_end)
        recyclerview_subpost_fragment.layoutManager = LinearLayoutManager(context)
        recyclerview_subpost_fragment.adapter = postItemAdapter
        recyclerview_subpost_fragment.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

        refresh_layout_post_fragment.isRefreshing = true
        refresh_layout_post_fragment.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                page = 1
                getPost(page, true)
            }
        })
        getPost(page, true)
        postItemAdapter.setOnItemClickListener { viewHolder, simplePostBean, i -> clickItem(simplePostBean) }
    }

    fun getPost(page: Int, refresh: Boolean) {
        i("GET", "${page}")
        topicPresenter.topicRequest(fid = param1!!, page = page, refresh = refresh)
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
    override fun topicResult(event: BaseEvent<List<SimpleTopicBean>?>) {
        refresh_layout_post_fragment?.isRefreshing = false
        if (event.code == BaseCode.FAILURE) {
            onError(event!!.data!![0]!!.title!!)    //我佛了
            return
        }
        if (page == 1) {
            postItemAdapter.setNewData(event.data)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            postItemAdapter.loadEnd()
            return
        }
        postItemAdapter.setLoadMoreData(event.data)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                SubTopicFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }

    override fun registEventBus(): Boolean {
        return true
    }

    private fun setEmptyView() {
        val emptyView: View = LayoutInflater
                .from(context!!)
                .inflate(R.layout.layout_empty, recyclerview_subpost_fragment.parent as ViewGroup, false)
        postItemAdapter.setEmptyView(emptyView)
    }

    private fun clickItem(topic: SimpleTopicBean) {
        var tid = topic.topic_id
        if(tid == null) {
            i("STF null", "${topic.topic_id == null}")
             tid = topic.source_id
        }
        mParentFragment.start(PostDetailFragment.newInstance(tid!!))
    }
}