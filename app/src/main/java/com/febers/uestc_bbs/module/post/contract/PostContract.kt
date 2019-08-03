package com.febers.uestc_bbs.module.post.contract

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.*
import com.febers.uestc_bbs.module.post.view.bottom_sheet.ITEM_ORDER_POSITIVE

interface PostContract {

    interface Model {
        fun postDetailService(postId: Int, page: Int, authorId: Int, order: Int)
        fun postReplyService(postId: Int, isQuota: Int, replyId: Int, aid: String, vararg contents: Pair<Int, String>)
        fun postFavService(action: String)
        fun postSupportService(postId: Int, tid: Int)
        fun postVoteService(pollItemId: List<Int>)
        fun userAtService(page: Int)
    }

    interface View: BaseView {
        fun showPostDetail(event: BaseEvent<PostDetailBean>){}
        fun showPostReplyResult(event: BaseEvent<PostSendResultBean>){}
        fun showPostFavResult(event: BaseEvent<PostFavResultBean>){}
        fun showPostSupportResult(event: BaseEvent<PostSupportResultBean>){}
        fun showVoteResult(event: BaseEvent<PostVoteResultBean>){}
        fun showUserAtResult(event: BaseEvent<UserCanAtBean>){}
    }

    abstract class Presenter(view: View) : BasePresenter<View>(view) {
        abstract fun postDetailRequest(postId: Int, page: Int, authorId: Int = 0, order: Int = ITEM_ORDER_POSITIVE)
        abstract fun postDetailResult(event: BaseEvent<PostDetailBean>)

        abstract fun postReplyRequest(postId: Int, isQuota: Int, replyId: Int, aid: String, vararg contents: Pair<Int, String>)
        abstract fun postReplyResult(event: BaseEvent<PostSendResultBean>)

        abstract fun postFavRequest(action: String)
        abstract fun postFavResult(event: BaseEvent<PostFavResultBean>)

        abstract fun postSupportRequest(postId: Int, tid: Int)
        abstract fun postSupportResult(event: BaseEvent<PostSupportResultBean>)

        abstract fun postVoteRequest(pollItemId: List<Int>)
        abstract fun postVoteResult(event: BaseEvent<PostVoteResultBean>)

        abstract fun userAtRequest(page: Int)
        abstract fun userAtResult(event: BaseEvent<UserCanAtBean>)
    }
}