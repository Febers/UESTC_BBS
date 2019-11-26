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
import com.febers.uestc_bbs.entity.SearchUserBean
import com.febers.uestc_bbs.entity.SearchUserBeanList
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.search.contract.SearchContract
import com.febers.uestc_bbs.module.search.presenter.SearchPresenterImpl
import com.febers.uestc_bbs.view.adapter.SEARCH_TYPE_USER
import com.febers.uestc_bbs.view.adapter.UserSearchAdapter
import kotlinx.android.synthetic.main.fragment_search_user.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.indeterminateProgressDialog

class SearchUserFragment: BaseFragment(), SearchContract.View {

    private val userList: MutableList<SearchUserBeanList> = ArrayList()
    private lateinit var userAdapter: UserSearchAdapter
    private lateinit var presenter: SearchContract.Presenter
    private var progressDialog: ProgressDialog? = null
    private var keyword: String = ""
    private var page: Int = 0

    override fun setView(): Int = R.layout.fragment_search_user

    override fun registerEventBus(): Boolean = true

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        presenter = SearchPresenterImpl(this)
        userAdapter = UserSearchAdapter(context!!, userList).apply {
            setOnItemClickListener { viewHolder, data, position ->
                ClickContext.clickToUserDetail(context, data.uid)
            }
            setEmptyView(getEmptyViewForRecyclerView(recycler_view_search_user))
        }
        refresh_layout_search_user.apply {
            setEnableRefresh(false)
            setEnableAutoLoadMore(false)
            setOnLoadMoreListener { search(keyword, ++page) }
        }
        recycler_view_search_user.apply {
            adapter = userAdapter
            layoutManager = LinearLayoutManager(context)
        }

        progressDialog = context?.indeterminateProgressDialog(getString(R.string.searching)) {
            setCanceledOnTouchOutside(false)
        }?.apply { hide() }

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    private fun search(keyword: String, page: Int) {
        refresh_layout_search_user.setNoMoreData(false)
        presenter.searchUserRequest(keyword, page)
    }

    override fun showUserSearchResult(event: BaseEvent<SearchUserBean>) {
        progressDialog?.dismiss()
        refresh_layout_search_user.apply {
            finishRefresh(true)
            finishLoadMore(true)
            setEnableLoadMore(true)
        }
        if (page == 1) {
            userAdapter.setNewData(event.data.body?.list ?: emptyList())
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_search_user.finishLoadMoreWithNoMoreData()
        }
        userAdapter.setLoadMoreData(event.data.body?.list)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onSearchSubmint(event: SearchSubmitEvent) {
        if (event.type == SEARCH_TYPE_USER) {
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