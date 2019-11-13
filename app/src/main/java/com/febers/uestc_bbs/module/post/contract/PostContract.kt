package com.febers.uestc_bbs.module.post.contract

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.*
import com.febers.uestc_bbs.module.post.view.bottom_sheet.ITEM_ORDER_POSITIVE

interface PostContract {

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

        abstract fun postReplyRequest(postId: Int, isQuota: Int, replyId: Int, aid: String, vararg contents: Pair<Int, String>)

        abstract fun postFavRequest(action: String)

        abstract fun postSupportRequest(postId: Int, tid: Int)

        abstract fun postVoteRequest(pollItemId: List<Int>)

        abstract fun userAtRequest(page: Int)
    }
}