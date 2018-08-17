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
import android.util.Log.i

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.PostItemAdapter
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.entity.EndRefreshEvent
import com.febers.uestc_bbs.entity.SimplePostBean
import com.febers.uestc_bbs.entity.StartRefreshEvent
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.post.presenter.PostContract
import com.febers.uestc_bbs.module.post.presenter.PostPresenterImpl
import com.febers.uestc_bbs.utils.AdapterWrapper
import com.febers.uestc_bbs.utils.SwipeToLoadHelper
import com.othershe.baseadapter.interfaces.OnLoadMoreListener
import kotlinx.android.synthetic.main.fragment_sub_post.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 首页Fragment包含三个Fragment
 * 依次为最新回去，最新发表，热门帖子
 */
class SubPostFragment: BaseFragment(), PostContract.View {

    private val postList: MutableList<SimplePostBean> = ArrayList()
    private lateinit var postItemAdapter: PostItemAdapter
    private lateinit var user: UserBean
    private var postPresenter:
            PostContract.Presenter = PostPresenterImpl(this)
    private var position: Int = 0
    private var page: Int = 1
    private var shouldRefresh = true

    override fun setContentView(): Int {
        postItemAdapter = PostItemAdapter(context!!, postList, true)
        return R.layout.fragment_sub_post
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        user = BaseApplication.getUser()
        postItemAdapter.setLoadingView(R.layout.layout_loading)
        postItemAdapter.setOnLoadMoreListener { i("SF", "Load") }

        recyclerview_subpost_fragment.layoutManager = LinearLayoutManager(context)
        recyclerview_subpost_fragment.adapter = postItemAdapter
        recyclerview_subpost_fragment.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        recyclerview_subpost_fragment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                i("RV", "scroll")
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                i("RV", "${newState}")
            }
        })


//        val emptyView: View = LayoutInflater
//                .from(context!!)
//                .inflate(R.layout.layout_empty, recyclerview_subpost_fragment.parent as ViewGroup, false)
//        postItemAdapter.setEmptyView(emptyView)

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

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    fun startRefresh(event: StartRefreshEvent) {
        page = 1
        postList.clear()
        getPost(page, true)
    }

    @UiThread
    override fun postResult(event: BaseEvent<List<SimplePostBean>?>) {
        EventBus.getDefault().post(EndRefreshEvent(true))
        if (event.data == null || event.code != BaseCode.SUCCESS) {
            return
        }
        postList.addAll(0,event.data!!)
        postItemAdapter?.notifyDataSetChanged()
        page++
    }

    companion object {
        fun getInstance(args: Bundle): SubPostFragment {
            val fragment = SubPostFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun registEventBus(): Boolean {
        return true
    }
}