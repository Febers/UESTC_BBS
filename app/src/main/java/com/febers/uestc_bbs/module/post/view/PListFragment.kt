package com.febers.uestc_bbs.module.post.view

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.PostListAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.module.post.presenter.PListContract
import com.febers.uestc_bbs.module.post.presenter.PListPresenterImpl
import com.febers.uestc_bbs.utils.ViewClickUtils
import com.febers.uestc_bbs.utils.ViewClickUtils.clickToPostDetail
import com.febers.uestc_bbs.view.adapter.StickyPostAdapter
import com.febers.uestc_bbs.view.utils.FABBehaviorHelper
import kotlinx.android.synthetic.main.fragment_post_list.*

class PListFragment: BaseSwipeFragment(), PListContract.View {

    private val stickyPostList: MutableList<PostListBean.TopTopicListBean> = ArrayList()
    private val postList: MutableList<PostListBean.ListBean> = ArrayList()
    private lateinit var pListPresenter: PListContract.Presenter
    private var stickyPostAdapter: StickyPostAdapter? = null
    private lateinit var postListAdapter: PostListAdapter
    private lateinit var itemShowStickyPost: MenuItem
    private var isShowStickyPost = false
    private var title = "板块名称"
    private var page: Int = 1

    override fun setToolbar(): Toolbar? = toolbar_post_list

    override fun setContentView(): Int {
        arguments?.let {
            title = it.getString("title")
        }
        return R.layout.fragment_post_list
    }

    override fun initView() {
        setHasOptionsMenu(true)
        toolbar_post_list.inflateMenu(R.menu.menu_post_list)
        pListPresenter = PListPresenterImpl(this)
        postListAdapter = PostListAdapter(context!!, postList)
        coo_layout_post_list_fragment.title = title
        onAppbarLayoutOffsetChange()
        FABBehaviorHelper.fabBehaviorWithScrollView(scroll_view_post_list, fab_post_list)
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        postListAdapter.apply {
            setOnItemClickListener { viewHolder, simplePostBean, i ->
                clickToPostDetail(context,simplePostBean.topic_id ?: simplePostBean.source_id)
            }
            setOnItemChildClickListener(R.id.image_view_item_post_avatar) {
                viewHolder, simplePListBean, i -> ViewClickUtils.clickToUserDetail(context, simplePListBean.user_id)
            }
        }
        recyclerview_post_list.apply {
            adapter = postListAdapter
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
        fab_post_list.setOnClickListener {
            ViewClickUtils.clickToPostEdit(context, mFid)
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
        //顶部文字
        text_view_post_list_today_num.text = getString(R.string.block_today_num) + ": " +event.data.forumInfo?.td_posts_num
        text_view_post_list_all_num.text = getString(R.string.block_all_num) + ": " + event.data.forumInfo?.posts_total_num
        //置顶帖数据
        stickyPostList.clear()
        stickyPostList.addAll(event.data.topTopicList!!)
        if (stickyPostAdapter != null) (stickyPostAdapter as StickyPostAdapter).notifyDataSetChanged()

        if (page == 1) {
            postListAdapter.setNewData(event.data.list)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_list?.finishLoadMoreWithNoMoreData()
            return
        }
        postListAdapter.setLoadMoreData(event.data.list)
    }

    /**
     * 显示或者隐藏置顶帖
     * 默认置顶帖的可见性是GONE
     * 点击菜单之后将其设为visibility
     */
    private fun showOrHideStickyPost() {
        if (stickyPostList.isEmpty()) return
        if (!isShowStickyPost) {
            if (stickyPostAdapter == null) {
                stickyPostAdapter = StickyPostAdapter(context!!, stickyPostList).apply {
                    setOnItemClickListener { viewHolder, topTopicListBean, i ->
                        ViewClickUtils.clickToPostDetail(context, topTopicListBean.id)
                    }
                }
            }
            recyclerview_post_list_sticky?.apply {
                adapter = stickyPostAdapter
                visibility = View.VISIBLE
            }
        } else {
            recyclerview_post_list_sticky.visibility = View.GONE
        }
    }

    /**
     * 根据Appbar的偏移量来控制其中控件的显示与隐藏
     * 全部展开时，偏移量应该为0
     * 网上收缩时，逐渐减小
     */
    private fun onAppbarLayoutOffsetChange() {
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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_post_list, menu)
        if (menu != null) itemShowStickyPost = menu.findItem(R.id.menu_item_post_list_sticky)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.menu_item_post_list_sticky) {
            showOrHideStickyPost()
            if (!isShowStickyPost) itemShowStickyPost.title = "隐藏置顶帖"
            if (isShowStickyPost) itemShowStickyPost.title = "显示置顶帖"
            isShowStickyPost = !isShowStickyPost
        }
        return super.onOptionsItemSelected(item)
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