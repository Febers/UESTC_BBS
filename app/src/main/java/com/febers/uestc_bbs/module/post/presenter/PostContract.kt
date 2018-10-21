/*
 * Created by Febers at 18-8-17 下午8:45.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 下午8:45.
 */

package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.entity.PostFavResultBean
import com.febers.uestc_bbs.entity.PostVoteResultBean
import com.febers.uestc_bbs.entity.ReplySendResultBean

interface PostContract {

    interface Model {
        fun postDetailService(postId: Int, page: Int, authorId: Int, order: Int)
        fun postReplyService(isQuote: Int, replyId: Int, vararg contents: Pair<Int, String>)
        fun postFavService(action: String)
        fun postVoteService(pollItemId: List<Int>)
    }

    interface View: BaseView {
        fun showPostDetail(event: BaseEvent<PostDetailBean>)
        fun showPostReplyResult(event: BaseEvent<ReplySendResultBean>)
        fun showPostFavResult(event: BaseEvent<PostFavResultBean>)
        fun showVoteResult(event: BaseEvent<PostVoteResultBean>)
    }

    abstract class Presenter(view: View) : BasePresenter<View>(view) {
        abstract fun postDetailRequest(postId: Int, page: Int, authorId: Int = 0, order: Int = ITEM_ORDER_POSITIVE)
        abstract fun postDetailResult(event: BaseEvent<PostDetailBean>)

        abstract fun postReplyRequest(isQuote: Int, replyId: Int, vararg contents: Pair<Int, String>)
        abstract fun postReplyResult(event: BaseEvent<ReplySendResultBean>)

        abstract fun postFavRequest(action: String)
        abstract fun postFavResult(event: BaseEvent<PostFavResultBean>)

        abstract fun postVoteRequest(pollItemId: List<Int>)
        abstract fun postVoteResult(event: BaseEvent<PostVoteResultBean>)
    }
}