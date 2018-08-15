/*
 * Created by Febers at 18-6-13 下午5:48.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-13 下午5:48.
 */

package com.febers.uestc_bbs.module.post.view

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.PostItemAdapter
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.SimplePostBean
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.post.presenter.PostContract
import com.febers.uestc_bbs.module.post.presenter.PostPresenterImpl
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_sub_post.*
import me.yokeyword.fragmentation.SupportFragment
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

/**
 * 首页Fragment包含三个Fragment
 * 依次为最新回去，最新发表，热门帖子
 */
class SubPostFragment: SupportFragment(), PostContract.View {

    private val postList: MutableList<SimplePostBean> = ArrayList()
    private lateinit var postItemAdapter: PostItemAdapter
    private lateinit var user: UserBean
    private lateinit var postPresenter: PostContract.Presenter
    private var position: Int = 0
    private var page: Int = 1
    private var shouldRefresh = true

    companion object {
        fun getInstance(args: Bundle): SubPostFragment {
            val fragment = SubPostFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sub_post, container, false)
        val bundle = arguments
        this.position = bundle?.getInt("position") as Int
        return view
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        user = BaseApplication.getUser()
        postItemAdapter = PostItemAdapter(context!!, postList, true)

        recyclerview_subpost_fragment.layoutManager = LinearLayoutManager(context)
        recyclerview_subpost_fragment.adapter = postItemAdapter

        val emptyView: View = LayoutInflater
                .from(context!!)
                .inflate(R.layout.layout_empty, recyclerview_subpost_fragment.parent as ViewGroup, false)
        postItemAdapter.setEmptyView(emptyView)

        refresh_layout_subpost_fragment.setOnRefreshListener(object : OnRefreshListener {
            override fun onRefresh(refreshLayout: RefreshLayout) {
                page = 1
                getPost(page, true)
                refresh_layout_subpost_fragment.finishRefresh(1000)
            }
        })
        refresh_layout_subpost_fragment.setOnLoadMoreListener { getPost(page++, true) }

        postPresenter = PostPresenterImpl(this)
        getPost(page, true)
    }

    fun getPost(page: Int, refresh: Boolean) {
        postPresenter.postRequest(position.toString(), page, refresh)
    }

    /**
     * 登录成功,获取数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLoginSeccess(event: BaseEvent<UserBean>) {
        user = event.data
        shouldRefresh = true
    }

    override fun postResult(event: BaseEvent<SimplePostBean>) {
        page++
    }

    override fun onError(error: String) {
        BaseApplication.context().toast(error)
    }
}