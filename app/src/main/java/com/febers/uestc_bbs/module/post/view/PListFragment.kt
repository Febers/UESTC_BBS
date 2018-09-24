package com.febers.uestc_bbs.module.post.view

import android.content.Intent
import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log.i
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.PostSimpleAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.SimplePListBean
import com.febers.uestc_bbs.module.post.presenter.PListContract
import com.febers.uestc_bbs.module.post.presenter.PListPresenterImpl
import kotlinx.android.synthetic.main.fragment_post_list.*
import org.jetbrains.anko.runOnUiThread

class PListFragment: BaseSwipeFragment(), PListContract.View {

    private val PListList: MutableList<SimplePListBean> = ArrayList()
    private lateinit var postSimpleAdapter: PostSimpleAdapter
    private var pListPresenter:
            PListContract.Presenter = PListPresenterImpl(this)
    private var page: Int = 1
    private var shouldRefresh = true

    override fun setToolbar(): Toolbar? {
        return toolbar_post_list
    }

    override fun setContentView(): Int {
        postSimpleAdapter = PostSimpleAdapter(context!!, PListList, true)
        return R.layout.fragment_post_list
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        recyclerview_post_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postSimpleAdapter
            addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL)) }

        refresh_layout_post_list.apply {
            setEnableLoadMore(false)
            autoRefresh() }
        refresh_layout_post_list.setOnRefreshListener {
            page = 1
            getPost(page, true)
        }
        refresh_layout_post_list.setOnLoadMoreListener { getPost(++page, true) }
        postSimpleAdapter.setOnItemClickListener { viewHolder, simplePostBean, i -> clickItem(simplePostBean) }
    }

    private fun getPost(page: Int, refresh: Boolean) {
        refresh_layout_post_list.setNoMoreData(false)
        i("PLIST", "${fid}")
        pListPresenter.pListRequest(fid = fid!!, page = page, refresh = refresh)
    }

    @UiThread
    override fun showPList(event: BaseEvent<List<SimplePListBean>?>) {
        if (event.code == BaseCode.FAILURE) {
            onError(event.data!![0].title!!)
            refresh_layout_post_list?.apply {
                finishRefresh(false)
                finishLoadMore(false)
            }
            return
        }
        if (event.code == BaseCode.LOCAL) {
            context?.runOnUiThread {
                postSimpleAdapter.setNewData(event.data)
            }
            return
        }
        refresh_layout_post_list?.apply {
            finishRefresh()
            finishLoadMore()
            setEnableLoadMore(true)
        }
        if (page == 1) {
            postSimpleAdapter.setNewData(event.data)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_list?.finishLoadMoreWithNoMoreData()
            return
        }
        postSimpleAdapter.setLoadMoreData(event.data)
    }

    companion object {
        @JvmStatic
        fun newInstance(fid: String, showBottomBarOnDestroy: Boolean) =
                PListFragment().apply {
                    arguments = Bundle().apply {
                        putString(FID, fid)
                        putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
                    }
                }
    }

    private fun clickItem(simplePList: SimplePListBean) {
        var tid = simplePList.topic_id
        if(tid == null) {
            tid = simplePList.source_id
        }
        startActivity(Intent(activity, PostDetailActivity::class.java).apply { putExtra("fid", tid) })
    }
}