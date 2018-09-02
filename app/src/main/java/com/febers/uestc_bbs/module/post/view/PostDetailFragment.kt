/*
 * Created by Febers at 18-8-15 下午11:40.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午11:40.
 */

package com.febers.uestc_bbs.module.post.view

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.adaper.PostReplyItemAdapter
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostReplyBean
import com.febers.uestc_bbs.entity.PostResultBean
import com.febers.uestc_bbs.module.post.presenter.PostContract
import com.febers.uestc_bbs.module.post.presenter.PostPresenterImpl
import com.febers.uestc_bbs.view.utils.GlideCircleTransform
import com.febers.uestc_bbs.module.post.utils.PostContentViewUtils
import kotlinx.android.synthetic.main.fragment_post_detail.*
import kotlinx.android.synthetic.main.layout_bottom_post_reply.*

class PostDetailFragment: BasePopFragment(), PostContract.View {

    private var replyList: MutableList<PostReplyBean> = ArrayList()
    private lateinit var postPresenter: PostContract.Presenter
    private lateinit var replyItemAdapter: PostReplyItemAdapter
    private var page = 1
    private var authorId = ""
    private lateinit var postId: String
    private var order = ""
    private lateinit var bottomSheetDialog: BottomSheetDialog
    override fun setToolbar(): Toolbar? {
        return toolbar_post_detail
    }

    override fun setContentView(): Int {
        postPresenter = PostPresenterImpl(this)
        replyItemAdapter = PostReplyItemAdapter(context!!, replyList, false)
        postId = param1!!
        return R.layout.fragment_post_detail
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        bottomSheetDialog = PostBottomSheet(context!!, R.style.PinkBottomSheetTheme)
        bottomSheetDialog.setContentView(R.layout.layout_bottom_sheet_reply)

        btn_reply.setOnClickListener { openBottomSheet() }

        refresh_layout_post_detail.setEnableLoadMore(false)
        refresh_layout_post_detail.autoRefresh()
        refresh_layout_post_detail.setOnRefreshListener {
            page = 1
            getPost(postId, page) }
        refresh_layout_post_detail.setOnLoadMoreListener { getPost(postId, ++page) }
        replyItemAdapter.setLoadEndView(R.layout.layout_load_end)
        recyclerview_post_detail_replies.layoutManager = LinearLayoutManager(context)
        recyclerview_post_detail_replies.addItemDecoration(DividerItemDecoration(context,LinearLayoutManager.VERTICAL))
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
                Glide.with(context!!).load(event.data.topic?.icon).transform(GlideCircleTransform(context))
                        .into(image_view_post_detail_author_avatar)
            }
            text_view_post_detail_title?.setText(event.data.topic?.title)
            text_view_post_detail_author?.setText(event.data.topic?.user_nick_name)
            text_view_post_detail_author_title?.setText(event.data.topic?.userTitle)
            text_view_post_detail_date?.setText(event.data.topic?.create_date)
            btn_reply?.setText(event.data.topic?.replies+"条评论")
            PostContentViewUtils.create(context, linear_layout_detail_content, event.data.topic?.content)
            replyList.clear()
        }
        replyList.addAll(event.data.list!!)
        replyItemAdapter.notifyDataSetChanged()

        if (event.code == BaseCode.SUCCESS_END) {
            refresh_layout_post_detail?.finishLoadMoreWithNoMoreData()
            return
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                PostDetailFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }

    override fun onBackPressedSupport(): Boolean {
        pop()
        return true
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        return attachToSwipeBack(view!!)

    }
    private fun openBottomSheet() {
        bottomSheetDialog.show()
        //startActivity(Intent(activity, ReplyEditActivity::class.java))
    }
}