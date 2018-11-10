package com.febers.uestc_bbs.module.search.view

import android.app.ProgressDialog
import android.os.Bundle
import androidx.annotation.UiThread
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.module.search.contract.SearchContrect
import com.febers.uestc_bbs.module.search.presenter.SearchPresenterImpl
import com.febers.uestc_bbs.view.adapter.SearchAdapter
import com.febers.uestc_bbs.utils.ViewClickUtils
import kotlinx.android.synthetic.main.fragment_search.*
import org.jetbrains.anko.indeterminateProgressDialog

class SearchFragment: BaseSwipeFragment(), SearchContrect.View {

    private val searchPostList: MutableList<SearchPostBean.ListBean> = ArrayList()
    private lateinit var searchPresenter: SearchContrect.Presenter
    private lateinit var progressDialog: ProgressDialog
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchView: SearchView
    private var menuItem: MenuItem? = null
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
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
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
        menuItem = menu?.findItem(R.id.menu_item_search_search_fragment)
        menuItem?.isChecked = true
        searchView = menuItem?.actionView as SearchView
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
        hideSoftInput()
        searchView.clearFocus()
        val tid = item.topic_id
        ViewClickUtils.clickToPostDetail(context, tid)
    }

    override fun showError(msg: String) {
        showToast(msg)
        refresh_layout_search?.apply {
            finishRefresh(false)
            finishLoadMore(false)
        }
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