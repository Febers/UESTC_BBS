/*
 * Created by Febers at 18-8-17 下午8:45.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-17 下午8:45.
 */

package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.entity.ReplySendResultBean

interface PostContract {

    interface Model {
        fun postDetailService(postId: Int, page: Int, authorId: Int, order: Int)
        fun postReplyService(tid: Int, isQuote: Int, replyId: Int, vararg contents: Pair<Int, String>)
    }

    interface View: BaseView {
        fun showPostDetail(event: BaseEvent<PostDetailBean>)
        fun showPostReplyResult(event: BaseEvent<ReplySendResultBean>)
    }

    abstract class Presenter(view: View) : BasePresenter<View>(view) {
        abstract fun postDetailRequest(postId: Int, page: Int, authorId: Int = 0, order: Int = ITEM_ORDER_POSITIVE)
        abstract fun postDetailResult(event: BaseEvent<PostDetailBean>)

        abstract fun postReplyRequest(tid: Int, isQuote: Int, replyId: Int, vararg contents: Pair<Int, String>)
        abstract fun postReplyResult(event: BaseEvent<ReplySendResultBean>)

    }
}