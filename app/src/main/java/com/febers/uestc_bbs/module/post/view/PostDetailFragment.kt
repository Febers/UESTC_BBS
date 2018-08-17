/*
 * Created by Febers at 18-8-15 下午11:40.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午11:40.
 */

package com.febers.uestc_bbs.module.post.view

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.v7.widget.Toolbar
import android.util.Log.i
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.ARG_PARAM1
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePopFragment
import com.febers.uestc_bbs.entity.PostResultBean
import com.febers.uestc_bbs.module.post.presenter.PostContract
import com.febers.uestc_bbs.module.post.presenter.PostPresenterImpl
import kotlinx.android.synthetic.main.fragment_post_detail.*

class PostDetailFragment: BasePopFragment(), PostContract.View {

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
        getPost(postId, page)

    }

    private fun getPost(_postId: String, _page: Int, _authorId: String = "", _order: String = "") {
        postPresenter.postRequest(_postId, _page, _authorId, _order)
    }

    @UiThread
    override fun postResult(event: BaseEvent<PostResultBean>) {
        if (event.code == BaseCode.FAILURE) {
            onError(event.data.rs!!)
            return
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


}