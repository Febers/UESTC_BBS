package com.febers.uestc_bbs.module.search.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.module.post.view.PostDetailActivity
import com.febers.uestc_bbs.module.search.presenter.SearchContrect
import com.febers.uestc_bbs.module.search.presenter.SearchPresenterImpl
import com.febers.uestc_bbs.view.adapter.SearchAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import org.jetbrains.anko.indeterminateProgressDialog

class SearchFragment: BaseSwipeFragment(), SearchContrect.View {

    private var progressDialog: ProgressDialog? = null
    private val searchPostList: MutableList<SearchPostBean.ListBean> = ArrayList()
    private lateinit var searchPresenter: SearchContrect.Presenter
    private lateinit var searchAdapter: SearchAdapter
    private var keyword = ""
    private var page: Int = 1

    override fun setToolbar(): Toolbar? {
        return toolbar_search
    }

    override fun setMenu(): Int? {
        return R.menu.menu_search_fragment
    }

    override fun setContentView(): Int {
        return R.layout.fragment_search
    }

    override fun initView() {
        progressDialog = context!!.indeterminateProgressDialog("搜索中") {
            setCanceledOnTouchOutside(false)
        }.apply { hide() }
        searchPresenter = SearchPresenterImpl(this)
        searchAdapter = SearchAdapter(context!!, searchPostList, false).apply {
            setOnItemClickListener { viewHolder, listBean, i -> onItemClick(listBean) }
        }
        recyclerview_search.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
        }
        refresh_layout_search.apply {
            setEnableRefresh(false)
            setEnableLoadMore(false)
            setOnLoadMoreListener {
                search(keyword, ++page)
            }
        }
    }

    private fun search(keyword: String, page: Int) {
        refresh_layout_search.setNoMoreData(false)
        searchPresenter.searchRequest(keyword, page)
    }

    @UiThread
    override fun showSearchResult(event: BaseEvent<SearchPostBean>) {
        progressDialog?.dismiss()
        if (event.code == BaseCode.FAILURE) {
            onError(event.data.errcode!!)
            refresh_layout_search?.apply {
                finishRefresh(false)
                finishLoadMore(false)
            }
            return
        }
        refresh_layout_search?.apply {
            finishRefresh(true)
            finishLoadMore(true)
            setEnableLoadMore(true)
        }
        if (page == 1) {
            searchAdapter.setNewData(event.data.list)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_search.finishLoadMoreWithNoMoreData()
        }
        searchAdapter.setLoadMoreData(event.data.list)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_search_fragment, menu)
        val menuItem = menu?.findItem(R.id.item_search)
        menuItem?.isChecked = true
        val searchView: SearchView = menuItem?.actionView as SearchView

        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty() || query?.trim().isNullOrEmpty()) {
                    return true
                }
                keyword = query!!
                page = 1
                hideSoftInput()
                progressDialog?.show()
                search(keyword, 0)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        }
        searchView.apply {
            isIconified = false
            queryHint = "搜索内容"
            setOnQueryTextListener(listener)
        }
    }

    private fun onItemClick(item: SearchPostBean.ListBean) {
        val tid = item.topic_id
        hideSoftInput()
        startActivity(Intent(activity, PostDetailActivity::class.java).apply { putExtra("mFid", ""+tid) })
    }
    companion object {
        @JvmStatic
        fun newInstance(showBottomBarOnDestroy: Boolean) =
                SearchFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
                    }
                }
    }
}