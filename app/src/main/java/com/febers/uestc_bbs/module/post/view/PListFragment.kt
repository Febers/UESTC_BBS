package com.febers.uestc_bbs.module.post.view

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log.i
import android.view.View
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.PostSimpleAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.module.post.presenter.PListContract
import com.febers.uestc_bbs.module.post.presenter.PListPresenterImpl
import com.febers.uestc_bbs.utils.ViewClickUtils
import com.febers.uestc_bbs.utils.ViewClickUtils.clickToPostDetail
import kotlinx.android.synthetic.main.fragment_post_list.*

class PListFragment: BaseSwipeFragment(), PListContract.View {

    private val postList: MutableList<PostListBean.ListBean> = ArrayList()
    private lateinit var postSimpleAdapter: PostSimpleAdapter
    private lateinit var pListPresenter: PListContract.Presenter
    private var title = "板块名称"
    private var page: Int = 1

    override fun setToolbar(): Toolbar? {
        return toolbar_post_list
    }

    override fun setContentView(): Int {
        arguments?.let {
            title = it.getString("title")
        }
        return R.layout.fragment_post_list
    }

    override fun initView() {
        pListPresenter = PListPresenterImpl(this)
        postSimpleAdapter = PostSimpleAdapter(context!!, postList, true).apply {
            setOnItemClickListener { viewHolder, simplePostBean, i ->
                clickToPostDetail(context,simplePostBean.topic_id ?: simplePostBean.source_id)
            }
            setOnItemChildClickListener(R.id.image_view_item_post_avatar) {
                viewHolder, simplePListBean, i -> ViewClickUtils.clickToUserDetail(context, simplePListBean.user_id)
            }
        }

        coo_layout_post_list_fragment.title = title
        onAppBarOffsetChange()
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        recyclerview_post_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postSimpleAdapter
            addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL)) }

        refresh_layout_post_list.apply {
            setEnableLoadMore(false)
            autoRefresh()
            setOnRefreshListener {
                page = 1
                getPost(page, true) }
            setOnLoadMoreListener {
                getPost(++page, true) }
        }
    }

    private fun getPost(page: Int, refresh: Boolean) {
        refresh_layout_post_list.setNoMoreData(false)
        pListPresenter.pListRequest(fid = mFid, page = page, refresh = refresh)
    }

    @UiThread
    override fun showPList(event: BaseEvent<PostListBean>) {
        refresh_layout_post_list?.apply {
            finishRefresh()
            finishLoadMore()
            setEnableLoadMore(true)
        }
        text_view_post_list_today_num.text = "${getString(R.string.block_today_num)}: ${event.data.forumInfo?.td_posts_num}"
        text_view_post_list_all_num.text = "${getString(R.string.block_all_num)}: ${event.data.forumInfo?.posts_total_num}"
        if (page == 1) {
            postSimpleAdapter.setNewData(event.data.list)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_list?.finishLoadMoreWithNoMoreData()
            return
        }
        postSimpleAdapter.setLoadMoreData(event.data.list)
    }

    /**
     * 根据Appbar的偏移量来控制其中控件的显示与隐藏
     * 全部展开时，偏移量应该为0
     * 网上收缩时，逐渐减小
     */
    private fun onAppBarOffsetChange() {
        app_bar_post_list.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            when {
                verticalOffset == 0 -> {
                    //CollapsingToolBar展开
                }
                Math.abs(verticalOffset) >= app_bar_post_list.totalScrollRange -> {
                    //CollapsingToolBar折叠
                }
                Math.abs(verticalOffset) <= 150 -> {
                    linear_layout_post_list_header.visibility = View.VISIBLE
                }
                Math.abs(verticalOffset) >= 150 -> {
                    linear_layout_post_list_header.visibility = View.GONE
                }
            }
        }
    }

    override fun showError(msg: String) {
        showToast(msg)
        refresh_layout_post_list?.apply {
            finishRefresh(false)
            finishLoadMore(false)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(fid: Int, title: String, showBottomBarOnDestroy: Boolean) =
                PListFragment().apply {
                    arguments = Bundle().apply {
                        putInt(FID, fid)
                        putString("title", title)
                        putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
                    }
                }
    }
}