package com.febers.uestc_bbs.module.post.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import androidx.annotation.UiThread
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adapter.PostListAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.BoardListBean_
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.module.post.contract.PListContract
import com.febers.uestc_bbs.module.post.presenter.PListPresenterImpl
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.context.ClickContext.clickToPostDetail
import com.febers.uestc_bbs.module.theme.ThemeManager
import com.febers.uestc_bbs.utils.*
import com.febers.uestc_bbs.view.adapter.StickyPostAdapter
import com.febers.uestc_bbs.view.helper.FABBehaviorHelper
import kotlinx.android.synthetic.main.activity_post_list.*
import kotlinx.android.synthetic.main.layout_server_null.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.IndexOutOfBoundsException

class PListActivity: BaseActivity(), PListContract.View {

    private val postList: MutableList<PostListBean.ListBean> = ArrayList()
    private lateinit var pListPresenter: PListContract.Presenter
    private var postListAdapter: PostListAdapter? = null
    private var boardDetailDialog: Dialog? = null

    private var boardSpinnerAdapter: ArrayAdapter<String>? = null
    private var boardSpinner: Spinner? = null
    private val boardNames: MutableList<String> = ArrayList()
    private val boardIds: MutableList<Int> = ArrayList()

    private val stickyPostList: MutableList<PostListBean.TopTopicListBean> = ArrayList()
    private var stickyPostAdapter: StickyPostAdapter? = null
    private var itemShowStickyPost: MenuItem? = null
    private var isShowStickyPost = false

    private var title: String? = "板块名称"
    private var mFid: Int = 0
    private var page: Int = 1

    private var classificationNameList: MutableList<String> = ArrayList()
    private var classificationIdList: MutableList<Int> = ArrayList()
    private var classificationDialog: Dialog? = null
    private var classificationId: Int = 0
    private var filterType: String = PLIST_SORT_BY_TYPE

    override fun setToolbar(): Toolbar? = toolbar_post_list

    override fun setMenu(): Int? = R.menu.menu_post_list

    override fun registerEventBus(): Boolean = true

    override fun setView(): Int {
        mFid = intent.getIntExtra(FID, 0)
        title = intent.getStringExtra(TITLE)
        return R.layout.activity_post_list
    }

    override fun initView() {
        toolbar_post_list.inflateMenu(R.menu.menu_post_list)
        toolbar_post_list.title = ""
    }

    override fun afterCreated() {
        pListPresenter = PListPresenterImpl(this)
        postListAdapter = PostListAdapter(mContext, postList, showBoardName = false)

        onAppbarLayoutOffsetChange()
        FABBehaviorHelper.fabBehaviorWithScrollView(scroll_view_post_list, fab_post_list)
        fab_post_list.backgroundTintList = ColorStateList.valueOf(colorAccent())

        pListPresenter.boardListRequest(mFid)

        boardNames.add(title.toString())
        boardIds.add(mFid)
        boardSpinnerAdapter = ArrayAdapter(mContext,
                R.layout.item_layout_spinner,
                R.id.text_view_item_spinner,
                boardNames).apply {
            setDropDownViewResource(R.layout.item_layout_spinner_dropdown)
        }
        boardSpinner = Spinner(toolbar_post_list.context).apply {
            adapter = boardSpinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    mFid = boardIds[position]
                    refresh_layout_post_list.autoRefresh()
                    classificationId = 0
                    classificationNameList.clear()
                    classificationIdList.clear()
                    classificationDialog = null
                    boardDetailDialog = null
                }
            }
        }
        toolbar_post_list.addView(boardSpinner, 0)

        postListAdapter?.apply {
            setOnItemClickListener { viewHolder, simplePostBean, i ->
                clickToPostDetail(context,simplePostBean.topic_id ?: simplePostBean.source_id, simplePostBean.title, simplePostBean.user_nick_name)
            }
            setOnItemChildClickListener(R.id.image_view_item_post_avatar) {
                viewHolder, simplePListBean, i -> ClickContext.clickToUserDetail(context, simplePListBean.user_id)
            }
            setEmptyView(getEmptyViewForRecyclerView(recyclerview_post_list))
        }
        recyclerview_post_list.apply {
            adapter = postListAdapter
        }

        refresh_layout_post_list.apply {
            initAttrAndBehavior()
            setOnRefreshListener {
                page = 1
                getPost(page) }
            setOnLoadMoreListener {
                getPost(++page) }
        }
        fab_post_list.setOnClickListener {
            ClickContext.clickToPostEdit(mContext, mFid, title!!)
        }
    }

    private fun getPost(page: Int) {
        btn_to_web?.visibility = View.INVISIBLE
        refresh_layout_post_list?.setNoMoreData(false)
        pListPresenter.pListRequest(fid = mFid, page = page, filterType = filterType, filterId = classificationId)
    }

    @UiThread
    override fun showPList(event: BaseEvent<PostListBean>) {
        refresh_layout_post_list?.finishSuccess()
        if (page == 1) {
            postListAdapter?.setNewData(event.data.list)
            //置顶帖数据
            stickyPostList.clear()
            stickyPostList.addAll(event.data.topTopicList!!)
            if (stickyPostAdapter != null) (stickyPostAdapter as StickyPostAdapter).notifyDataSetChanged()
            //标签数据
            if (classificationIdList.isEmpty()) {
                classificationNameList.add("全部")
                classificationIdList.add(0)
                event.data.classificationType_list?.forEach {
                    classificationNameList.add(it.classificationType_name!!)
                    classificationIdList.add(it.classificationType_id)
                }
                classificationDialog = AlertDialog.Builder(mContext)
                        .setTitle(getString(R.string.classification))
                        .setItems(classificationNameList.toTypedArray()) { dialog, which ->
                            classificationId = classificationIdList[which]
                            page = 1
                            refresh_layout_post_list.autoRefresh()
                        }.create()
                //帖子详情
                if (boardDetailDialog == null) {
                    boardDetailDialog = AlertDialog.Builder(mContext)
                            .setTitle(getString(R.string.block_detail))
                            .setMessage("""
版块id: ${event.data.forumInfo?.id}
今日帖子: ${event.data.forumInfo?.td_posts_num}
总帖子: ${event.data.forumInfo?.posts_total_num}
                            """)
                            .setPositiveButton(getString(R.string.confirm)){ dialog, which -> dialog.dismiss() }
                            .create()
                }
            }
            return
        }
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_list?.finishLoadMoreWithNoMoreData()
            return
        }
        postListAdapter?.setLoadMoreData(event.data.list)
    }

    /**
     * 进入总的版块之后，后台获取该板块下的所有子版块，并创建一个spinner显示
     *
     */
    @UiThread
    override fun showBoardList(event: BaseEvent<BoardListBean_>) {
        try {
            event.data.list?.get(0)?.board_list?.forEach {
                boardNames.add(it.board_name.toString())
                boardIds.add(it.board_id)
            }
            boardSpinnerAdapter?.notifyDataSetChanged()
        } catch (e: IndexOutOfBoundsException) {
        }

    }

    /**
     * 接收到新帖发布之后
     * 刷新界面
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPostNew(event: PostNewEvent) {
        if (event.code == BaseCode.SUCCESS) {
            refresh_layout_post_list.scrollTo(0, 0)
            refresh_layout_post_list.autoRefresh()
        }
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
                stickyPostAdapter = StickyPostAdapter(mContext, stickyPostList).apply {
                    setOnItemClickListener { viewHolder, topTopicListBean, i ->
                        ClickContext.clickToPostDetail(context, topTopicListBean.id)
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
    }

    @UiThread
    override fun showError(msg: String) {
        refresh_layout_post_list?.finishFail()
        if (msg.contains(SERVICE_RESPONSE_NULL)) {
            showHint(msg)
            showEmptyView()
            web("http://bbs.uestc.edu.cn/forum.php?mod=forumdisplay&fid=$mFid")
            return
        }
        if (msg.contains(SERVICE_RESPONSE_ERROR)) {
            showHint(getString(R.string.hint_check_network) + ": " + msg)
            return
        }
        showHint(msg)
    }

    private fun showEmptyView() {
        btn_to_web?.visibility = View.VISIBLE
        btn_to_web?.setOnClickListener {
            web("http://bbs.uestc.edu.cn/forum.php?mod=forumdisplay&fid=$mFid")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_item_post_list_sticky -> {
                showOrHideStickyPost()
                if (!isShowStickyPost) itemShowStickyPost?.title = getString(R.string.hide_sticky_post)
                if (isShowStickyPost) itemShowStickyPost?.title = getString(R.string.show_sticky_post)
                isShowStickyPost = !isShowStickyPost
            }
            R.id.menu_item_post_list_web -> {
                web("http://bbs.uestc.edu.cn/forum.php?mod=forumdisplay&fid=$mFid")
            }
            R.id.menu_item_post_list_classification -> {
                classificationDialog?.show()
            }
            R.id.menu_item_post_list_about -> {
                boardDetailDialog?.show()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}