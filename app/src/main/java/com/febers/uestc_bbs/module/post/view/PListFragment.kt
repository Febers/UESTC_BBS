package com.febers.uestc_bbs.module.post.view

import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log.i
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.PostSimpleItemAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.SimplePListBean
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.post.presenter.PListContract
import com.febers.uestc_bbs.module.post.presenter.PListPresenterImpl
import kotlinx.android.synthetic.main.fragment_post_list.*

class PListFragment: BaseSwipeFragment(), PListContract.View {

    private val PListList: MutableList<SimplePListBean> = ArrayList()
    private lateinit var postSimpleItemAdapter: PostSimpleItemAdapter
    private lateinit var user: UserBean
    private var PListPresenter:
            PListContract.Presenter = PListPresenterImpl(this)
    private var page: Int = 1
    private var shouldRefresh = true

    override fun setToolbar(): Toolbar? {
        return toolbar_post_list
    }

    override fun setContentView(): Int {
        postSimpleItemAdapter = PostSimpleItemAdapter(context!!, PListList, true)
        return R.layout.fragment_post_list
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        user = BaseApplication.getUser()
        recyclerview_post_list.layoutManager = LinearLayoutManager(context)
        recyclerview_post_list.adapter = postSimpleItemAdapter
        recyclerview_post_list.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))

        refresh_layout_post_list.setEnableLoadMore(false)
        refresh_layout_post_list.autoRefresh()
        refresh_layout_post_list.setOnRefreshListener {
            page = 1
            getPost(page, true)
        }
        refresh_layout_post_list.setOnLoadMoreListener { getPost(++page, true) }
        postSimpleItemAdapter.setOnItemClickListener { viewHolder, simplePostBean, i -> clickItem(simplePostBean) }
    }

    private fun getPost(page: Int, refresh: Boolean) {
        refresh_layout_post_list.setNoMoreData(false)
        PListPresenter.pListRequest(fid = fid!!, page = page, refresh = refresh)
    }

    override fun pListResult(event: BaseEvent<List<SimplePListBean>?>) {
        if (event.code == BaseCode.FAILURE) {
            onError(event!!.data!![0]!!.title!!)    //我佛了
            refresh_layout_post_list?.finishRefresh(false)
            refresh_layout_post_list?.finishLoadMore(false)
            return
        }
        refresh_layout_post_list?.finishRefresh()
        refresh_layout_post_list?.finishLoadMore()
        refresh_layout_post_list?.setEnableLoadMore(true)
        if (page == 1) {
            postSimpleItemAdapter.setNewData(event.data)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_list?.finishLoadMoreWithNoMoreData()
            return
        }
        postSimpleItemAdapter.setLoadMoreData(event.data)
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

    private fun clickItem(PList: SimplePListBean) {
        var tid = PList.topic_id
        if(tid == null) {
            i("STF null", "${PList.topic_id == null}")
            tid = PList.source_id
        }
        start(PostDetailFragment.newInstance(fid = tid!!, showBottomBarOnDestroy = false))
    }
}