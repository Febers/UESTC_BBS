package com.febers.uestc_bbs.module.post.view

import android.annotation.SuppressLint
import android.os.Build
import android.support.annotation.UiThread
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.adaper.PostReplyItemAdapter
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BaseSwipeActivty
import com.febers.uestc_bbs.entity.PostReplyBean
import com.febers.uestc_bbs.entity.PostResultBean
import com.febers.uestc_bbs.module.post.presenter.PostContract
import com.febers.uestc_bbs.module.post.presenter.PostPresenterImpl
import com.febers.uestc_bbs.view.utils.PostContentViewUtils
import com.febers.uestc_bbs.view.utils.GlideCircleTransform
import kotlinx.android.synthetic.main.activity_post_detail.*
import kotlinx.android.synthetic.main.layout_bottom_post_reply.*

class PostDetailActivity : BaseSwipeActivty(), PostContract.View {

    private var replyList: MutableList<PostReplyBean> = ArrayList()
    private lateinit var postPresenter: PostContract.Presenter
    private lateinit var replyItemAdapter: PostReplyItemAdapter
    private var page = 1
    private var authorId = ""
    private var postId: String = "0"
    private var order = ""
    private lateinit var bottomSheetDialog: BottomSheetDialog

    override fun setView(): Int = R.layout.activity_post_detail

    override fun setToolbar(): Toolbar? {
        return toolbar_post_detail
    }

    override fun initView() {
        postId = intent.getStringExtra("fid")
        postPresenter = PostPresenterImpl(this)
        replyItemAdapter = PostReplyItemAdapter(this, replyList, false)

        bottomSheetDialog = PostBottomSheet(this, R.style.PinkBottomSheetTheme)
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_reply)

        btn_reply.setOnClickListener { bottomSheetDialog.show() }

        refresh_layout_post_detail.apply {
            setEnableLoadMore(false)
            autoRefresh()
            setOnRefreshListener {
                page = 1
                getPost(postId, page) }
            setOnLoadMoreListener {
                getPost(postId, ++page) }
        }
        replyItemAdapter.setLoadEndView(R.layout.layout_load_end)
        recyclerview_post_detail_replies.apply {
            layoutManager = LinearLayoutManager(this@PostDetailActivity)
            addItemDecoration(DividerItemDecoration(this@PostDetailActivity, LinearLayoutManager.VERTICAL))
            adapter = replyItemAdapter
        }
    }

    /*
    获取数据之后,恢复加载更多设置
    (由于已经关闭加载更多，只能由刷新触发)，
     */
    private fun getPost(_postId: String, _page: Int, _authorId: String = "", _order: String = "") {
        refresh_layout_post_detail.setNoMoreData(false)
        postPresenter.postRequest(_postId, _page, _authorId, _order)
    }

    @SuppressLint("SetTextI18n")
    @UiThread
    override fun showPost(event: BaseEvent<PostResultBean>) {
        if (event.code == BaseCode.FAILURE) {
            if (event.data.rs == null) {
                return
            }
            onError(event.data.errcode!!)
            refresh_layout_post_detail?.apply {
                finishRefresh(false)
                finishLoadMore(false)
            }
            return
        }
        refresh_layout_post_detail?.apply {
            finishLoadMore(true)
            finishRefresh(true)
            setEnableLoadMore(true)
        }
        if (page == 1) {
            //绘制主贴视图，否则只需要添加评论内容
            linear_layout_detail_divide?.visibility = View.VISIBLE
            if (image_view_post_detail_author_avatar != null) {
                image_view_post_detail_author_avatar.visibility = View.VISIBLE
                if (!this.isDestroyed) {
                    Glide.with(this).load(event.data.topic?.icon).transform(GlideCircleTransform(this))
                            .into(image_view_post_detail_author_avatar)
                }
            }
            text_view_post_detail_title?.text = event.data.topic?.title
            text_view_post_detail_author?.text = event.data.topic?.user_nick_name
            text_view_post_detail_author_title?.text = event.data.topic?.userTitle
            text_view_post_detail_date?.text = event.data.topic?.create_date
            btn_reply?.text = event.data.topic?.replies+"条评论"
            PostContentViewUtils.create(linear_layout_detail_content, event.data.topic?.content)
            replyList.clear()
        }
        replyList.addAll(event.data.list!!)
        replyItemAdapter.notifyDataSetChanged()
        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_detail?.finishLoadMoreWithNoMoreData()
            return
        }
    }
}
