package com.febers.uestc_bbs.module.search.view

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.base.SearchSubmitEvent
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.search.contract.SearchContract
import com.febers.uestc_bbs.module.search.presenter.SearchPresenterImpl
import com.febers.uestc_bbs.view.adapter.SEARCH_TYPE_POST
import com.febers.uestc_bbs.view.adapter.PostSearchAdapter
import kotlinx.android.synthetic.main.fragment_search_post.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.indeterminateProgressDialog

class SearchPostFragment: BaseFragment(), SearchContract.View {

    private val postList: MutableList<SearchPostBean.SearchPostBeanList> = ArrayList()
    private lateinit var postAdapter: PostSearchAdapter
    private lateinit var presenter: SearchContract.Presenter
    private var progressDialog: ProgressDialog? = null
    private var keyword: String = ""
    private var page: Int = 0

    override fun setView(): Int = R.layout.fragment_search_post

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        presenter = SearchPresenterImpl(this)
        postAdapter = PostSearchAdapter(context!!, postList).apply {
            setOnItemClickListener { viewHolder, data, position ->
                ClickContext.clickToPostDetail(context,data.topic_id, data.title, data.user_nick_name)
            }
            setEmptyView(getEmptyViewForRecyclerView(recycler_view_search_post))
        }
        refresh_layout_search_post.apply {
            setEnableRefresh(false)
            setEnableAutoLoadMore(false)
            setOnLoadMoreListener { search(keyword, ++page) }
        }
        recycler_view_search_post.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(context!!)
        }
        progressDialog = context?.indeterminateProgressDialog(getString(R.string.searching)) {
            setCanceledOnTouchOutside(false)
        }?.apply { hide() }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    private fun search(keyword: String, page: Int) {
        refresh_layout_search_post.setNoMoreData(false)
        presenter.searchPostRequest(keyword, page)
    }

    override fun showPostSearchResult(event: BaseEvent<SearchPostBean>) {
        progressDialog?.dismiss()
        refresh_layout_search_post.apply {
            finishRefresh(true)
            finishLoadMore(true)
            setEnableLoadMore(true)
        }
        if (page == 1) {
            postAdapter.setNewData(event.data.list)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_search_post.finishLoadMoreWithNoMoreData()
        }
        postAdapter.setLoadMoreData(event.data.list)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onSearchSubmit(event: SearchSubmitEvent) {
        if (event.type == SEARCH_TYPE_POST) {
            keyword = event.keyword
            progressDialog?.show()
            page = 1
            search(keyword, page)
        }
    }

    override fun onBackPressedSupport(): Boolean {
        return true
    }

    override fun getEmptyViewForRecyclerView(recyclerView: RecyclerView): View =
            LayoutInflater
                    .from(context)
                    .inflate(R.layout.layout_search_empty, recyclerView.parent as ViewGroup, false)
}