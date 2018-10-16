/*
 * Created by Febers at 18-8-18 上午1:32.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-18 上午1:32.
 */

package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.entity.ReplySendResultBean
import com.febers.uestc_bbs.module.post.model.PostModelImpl

class PostPresenterImpl(var view: PostContract.View): PostContract.Presenter(view) {

    private val postModel: PostContract.Model = PostModelImpl(this)

    override fun postDetailRequest(postId: Int, page: Int, authorId: Int, order: Int) {
        postModel.postDetailService(postId, page, authorId, order)
    }

    override fun postDetailResult(event: BaseEvent<PostDetailBean>) {
        view.showPostDetail(event)
    }

    override fun postReplyRequest(tid: Int, isQuote: Int, replyId: Int, vararg contents: Pair<Int, String>) {
        postModel.postReplyService(tid, isQuote, replyId, *contents)
    }

    override fun postReplyResult(event: BaseEvent<ReplySendResultBean>) {
        view.showPostReplyResult(event)
    }
}