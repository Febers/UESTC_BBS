package com.febers.uestc_bbs.module.post.view

import android.os.Bundle
import androidx.annotation.UiThread
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.PostListAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.module.post.contract.PListContract
import com.febers.uestc_bbs.module.post.presenter.PListPresenterImpl
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.view.helper.finishFail
import com.febers.uestc_bbs.view.helper.finishSuccess
import com.febers.uestc_bbs.view.helper.initAttrAndBehavior
import kotlinx.android.synthetic.main.fragment_post_list_home.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.runOnUiThread

/**
 * 首页Fragment包含三个Fragment
 * 依次为最新回复，最新发表，热门帖子
 */
class PListHomeFragment: BaseFragment(), PListContract.View {

    private val postSimpleList: MutableList<PostListBean.ListBean> = ArrayList()
    private lateinit var postListAdapter: PostListAdapter
    private lateinit var pListPresenter: PListContract.Presenter
    private var page: Int = 1
    private var loadFinish = false

    override fun registerEventBus(): Boolean = true

    override fun setView(): Int {
        return R.layout.fragment_post_list_home
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        pListPresenter = PListPresenterImpl(this)
        postListAdapter = PostListAdapter(context!!, postSimpleList).apply {
            setOnItemClickListener { viewHolder, simplePostBean, i ->
                ClickContext.clickToPostDetail(context,simplePostBean.topic_id ?: simplePostBean.source_id) }
            setOnItemChildClickListener(R.id.image_view_item_post_avatar) {
                viewHolder, simplePostBean, i -> ClickContext.clickToUserDetail(context, simplePostBean.user_id)
            }
            setEmptyView(getEmptyViewForRecyclerView(recyclerview_subpost_fragment))
        }
        recyclerview_subpost_fragment.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postListAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        refresh_layout_post_list_home.apply {
            initAttrAndBehavior()
            setOnRefreshListener {
                page = 1
                getPost(page)
            }
            setOnLoadMoreListener {
                page++
                getPost(page)
            }
        }
        ThemeHelper.subscribeOnThemeChange(refresh_layout_post_list_home)
        postListAdapter.notifyDataSetChanged()
    }

    private fun getPost(page: Int) {
        refresh_layout_post_list_home.setNoMoreData(false)
        pListPresenter.pListRequest(fid = mFid, page = page)
    }

    @UiThread
    override fun showPList(event: BaseEvent<PostListBean>) {
        loadFinish = true
        if (event.code == BaseCode.LOCAL) {
            context?.runOnUiThread {
                postListAdapter.setNewData(event.data.list)
            }
            return
        }
        refresh_layout_post_list_home?.finishSuccess()
        if (page == 1) {
            postListAdapter.setNewData(event.data.list)
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_list_home?.finishLoadMoreWithNoMoreData()
            return
        }
        postListAdapter.setLoadMoreData(event.data.list)
    }

    override fun showError(msg: String) {
        showHint(msg)
        refresh_layout_post_list_home?.finishFail()
    }

    companion object {
        @JvmStatic
        fun newInstance(fid: Int) =
                PListHomeFragment().apply {
                    arguments = Bundle().apply {
                        putInt(FID, fid)
                    }
                }
    }

    /**
     * 接收用户重复按下tab时的消息
     * 然后刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onTabReselceted(event: TabReselectedEvent) {
        if (isSupportVisible && loadFinish && event.position == 0) {
            scroll_view_plist_home?.scrollTo(0, 0)
            refresh_layout_post_list_home?.autoRefresh()
        }
    }

    /**
     * 接收到新帖发布之后
     * 刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPostNew(event: BaseEvent<String>) {
        if (isSupportVisible && event.code == BaseCode.SUCCESS) {
            scroll_view_plist_home?.scrollTo(0, 0)
            refresh_layout_post_list_home.autoRefresh()
        }
    }

    private fun setEmptyView() {
        val emptyView: View = LayoutInflater
                .from(context!!)
                .inflate(R.layout.layout_empty_view, recyclerview_subpost_fragment.parent as ViewGroup, false)
        postListAdapter.setEmptyView(emptyView)
    }

    override fun onDestroy() {
        super.onDestroy()
        postSimpleList.clear()
    }
}