/*
 * Created by Febers at 18-8-15 下午11:40.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午11:40.
 */

package com.febers.uestc_bbs.module.post.view

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.ARG_PARAM1
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePopFragment
import com.febers.uestc_bbs.entity.PostReplyBean
import com.febers.uestc_bbs.entity.PostResultBean
import com.febers.uestc_bbs.module.post.presenter.PostContract
import com.febers.uestc_bbs.module.post.presenter.PostPresenterImpl
import com.febers.uestc_bbs.view.utils.PostContentViewUtils
import kotlinx.android.synthetic.main.fragment_post_detail.*

class PostDetailFragment: BasePopFragment(), PostContract.View {

    private var replyList: List<PostReplyBean> = ArrayList()
    private lateinit var postPresenter: PostContract.Presenter
    private var page = 1
    private var authorId = ""
    private lateinit var postId: String
    private var order = ""

    override fun setToolbar(): Toolbar? {
        return toolbar_post_detail
    }

    override fun setContentView(): Int {
        postPresenter = PostPresenterImpl(this)
        postId = param1!!
        return R.layout.fragment_post_detail
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        image_view_post_detail_author_avatar?.visibility = View.INVISIBLE
        linear_layout_detail_divide?.visibility = View.INVISIBLE
        refresh_layout_post_detail?.isRefreshing = true
        refresh_layout_post_detail.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                page = 1
                getPost(postId, page)
            }
        })
        recyclerview_post_detail_replies.layoutManager = LinearLayoutManager(context)

        getPost(postId, page)
    }

    private fun getPost(_postId: String, _page: Int, _authorId: String = "", _order: String = "") {
        postPresenter.postRequest(_postId, _page, _authorId, _order)
    }

    @UiThread
    override fun postResult(event: BaseEvent<PostResultBean>) {
        refresh_layout_post_detail?.isRefreshing = false
        if (event.code == BaseCode.FAILURE) {
            onError(event.data.rs!!)
            return
        }

        linear_layout_detail_divide?.visibility = View.VISIBLE
        if (image_view_post_detail_author_avatar != null) {
            image_view_post_detail_author_avatar.visibility = View.VISIBLE
            Glide.with(context!!).load(event.data.topic?.icon).transform(GlideCircleTransform(context))
                    .into(image_view_post_detail_author_avatar)
        }

        text_view_post_detail_title?.setText(event.data.topic?.title)
        text_view_post_detail_author?.setText(event.data.topic?.user_nick_name)
        text_view_post_detail_time?.setText(event.data.topic?.create_date)
        PostContentViewUtils.creat(context, linear_layout_detail_content, event.data.topic?.content)

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
}