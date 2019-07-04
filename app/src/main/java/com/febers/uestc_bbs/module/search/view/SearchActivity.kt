package com.febers.uestc_bbs.module.search.view

import android.app.ProgressDialog
import androidx.annotation.UiThread
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.io.SearchStore
import com.febers.uestc_bbs.module.search.contract.SearchContract
import com.febers.uestc_bbs.module.search.presenter.SearchPresenterImpl
import com.febers.uestc_bbs.utils.KeyboardUtils
import com.febers.uestc_bbs.view.adapter.SearchAdapter
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.view.custom.TextViewWithBackground
import com.google.android.flexbox.FlexboxLayout
import kotlinx.android.synthetic.main.activity_search.*
import org.jetbrains.anko.indeterminateProgressDialog

class SearchActivity: BaseActivity(), SearchContract.View {

    private val searchPostList: MutableList<SearchPostBean.SearchPostBeanList> = ArrayList()
    private lateinit var searchPresenter: SearchContract.Presenter
    private var progressDialog: ProgressDialog? = null
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var searchView: SearchView
    private var menuItem: MenuItem? = null
    private var keyword = ""
    private var page: Int = 1

    private val searchHistories: MutableList<String> = ArrayList()

    override fun setView(): Int = R.layout.activity_search

    override fun setToolbar(): Toolbar? = toolbar_search

    override fun initView() {
        toolbar_search.title = getString(R.string.search)

//        var pagerFragment: ISupportFragment? = findFragment(SearchPagerFragment::class.java)
//        if (pagerFragment == null) {
//            pagerFragment = SearchPagerFragment()
//        }
//        loadRootFragment(R.id.container_search_activity, pagerFragment)

        searchPresenter = SearchPresenterImpl(this)

        searchHistories.addAll(SearchStore.get(mContext))
        for (history in searchHistories) {
            layout_search_history.addView(createSearchHistoryItem(history))
        }
        btn_search_history_clear.setOnClickListener {
            SearchStore.clear(mContext, searchHistories)
            layout_search_history.removeAllViews()
            btn_search_history_clear.visibility = View.GONE
        }
        searchAdapter = SearchAdapter(mContext, searchPostList, false).apply {
            setOnItemClickListener { viewHolder, listBean, i -> onItemClick(listBean) }
        }
        recyclerview_search.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(context)
        }
        refresh_layout_search.apply {
            setEnableRefresh(false)
            setEnableLoadMore(false)
            setOnLoadMoreListener {
                search(keyword, ++page)
            }
        }
    }

    override fun afterCreated() {
        progressDialog = indeterminateProgressDialog(getString(R.string.searching)) {
            setCanceledOnTouchOutside(false)
        }.apply { hide() }
    }

    private fun search(keyword: String, page: Int) {
        refresh_layout_search.setNoMoreData(false)
        searchPresenter.searchPostRequest(keyword, page)
        SearchStore.save(mContext, keyword, searchHistories)
    }

    @UiThread
    override fun showPostSearchResult(event: BaseEvent<SearchPostBean>) {
        progressDialog?.dismiss()
        layout_search_history.visibility = View.GONE
        btn_search_history_clear.visibility = View.GONE

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
            refresh_layout_search?.finishLoadMoreWithNoMoreData()
        }
        searchAdapter.setLoadMoreData(event.data.list)
    }

    /**
     * 将toolbar和searchView结合起来
     * 实际是将后者作为一个menuItem
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search_fragment, menu)
        menuItem = menu?.findItem(R.id.menu_item_search_search_fragment)
        menuItem?.isChecked = true
        searchView = menuItem?.actionView as SearchView
        val listener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty() || query.trim().isEmpty()) {
                    return true
                }
                keyword = query
                page = 1
                KeyboardUtils.closeKeyboard(searchView, mContext)
                progressDialog?.show()
                search(keyword, 0)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    layout_search_history.visibility = View.VISIBLE
                    searchPostList.clear()
                    searchAdapter.notifyDataSetChanged()
                }
                return true
            }
        }
        searchView.apply {
            isIconified = false
            queryHint = context.getString(R.string.search_content)
            setOnQueryTextListener(listener)
        }
        return true
    }


    private fun onItemClick(item: SearchPostBean.SearchPostBeanList) {
        KeyboardUtils.closeKeyboard(searchView, mContext)
        searchView.clearFocus()
        val tid = item.topic_id
        ClickContext.clickToPostDetail(mContext, tid, item.title, item.user_nick_name)
    }

    override fun showError(msg: String) {
        showHint(msg)
        progressDialog?.dismiss()
        refresh_layout_search?.apply {
            finishRefresh(false)
            finishLoadMore(false)
        }
    }

    private fun createSearchHistoryItem(text: String): View = TextViewWithBackground(mContext).apply {
        this.text = text
        setRectRound(4f)
        setRectPadding(16)
        val lp = FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.setMargins(16,16,16,16)
        layoutParams = lp
        setTextColor(ThemeHelper.getColorPrimaryBySp())
        textSize = 16f
        setOnClickListener {
            searchView.setQuery(text, false)
            search(text, 0)
            progressDialog?.show() }
    }

    /**
     * 在销毁的时候必须使progressDialog为空，否则报
     * android.view.WindowLeaked 错误
     */
    override fun onDestroy() {
        super.onDestroy()
        progressDialog?.dismiss()
        progressDialog = null
    }
}