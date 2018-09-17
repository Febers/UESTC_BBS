package com.febers.uestc_bbs.module.post.view

import android.support.annotation.UiThread
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.PostReplyItemAdapter
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BaseSwipeActivty
import com.febers.uestc_bbs.entity.PostReplyBean
import com.febers.uestc_bbs.entity.PostResultBean
import com.febers.uestc_bbs.module.post.presenter.PostContract
import com.febers.uestc_bbs.module.post.presenter.PostPresenterImpl
import com.febers.uestc_bbs.module.post.utils.PostContentViewUtils
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

        refresh_layout_post_detail.setEnableLoadMore(false)
        refresh_layout_post_detail.autoRefresh()
        refresh_layout_post_detail.setOnRefreshListener {
            page = 1
            getPost(postId, page) }
        refresh_layout_post_detail.setOnLoadMoreListener { getPost(postId, ++page) }
        replyItemAdapter.setLoadEndView(R.layout.layout_load_end)
        recyclerview_post_detail_replies.layoutManager = LinearLayoutManager(this)
        recyclerview_post_detail_replies.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))
        recyclerview_post_detail_replies.adapter = replyItemAdapter
    }

    private fun getPost(_postId: String, _page: Int, _authorId: String = "", _order: String = "") {
        //获取数据之后(由于已经关闭加载更多，只能由刷新触发)，恢复加载更多设置
        refresh_layout_post_detail.setNoMoreData(false)
        postPresenter.postRequest(_postId, _page, _authorId, _order)
    }

    @UiThread
    override fun postResult(event: BaseEvent<PostResultBean>) {
        if (event.code == BaseCode.FAILURE) {
            if (event.data.rs == null) {
                return
            }
            onError(event.data.rs!!)
            refresh_layout_post_detail?.finishRefresh(false)
            refresh_layout_post_detail?.finishLoadMore(false)
            return
        }
        refresh_layout_post_detail?.finishLoadMore(true)
        refresh_layout_post_detail?.finishRefresh(true)
        refresh_layout_post_detail?.setEnableLoadMore(true)
        if (page == 1) {
            //绘制主贴视图，否则只需要添加评论内容
            linear_layout_detail_divide?.visibility = View.VISIBLE
            if (image_view_post_detail_author_avatar != null) {
                image_view_post_detail_author_avatar.visibility = View.VISIBLE
                Glide.with(this).load(event.data.topic?.icon).transform(GlideCircleTransform(this))
                        .into(image_view_post_detail_author_avatar)
            }
            text_view_post_detail_title?.setText(event.data.topic?.title)
            text_view_post_detail_author?.setText(event.data.topic?.user_nick_name)
            text_view_post_detail_author_title?.setText(event.data.topic?.userTitle)
            text_view_post_detail_date?.setText(event.data.topic?.create_date)
            btn_reply?.setText(event.data.topic?.replies+"条评论")
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
