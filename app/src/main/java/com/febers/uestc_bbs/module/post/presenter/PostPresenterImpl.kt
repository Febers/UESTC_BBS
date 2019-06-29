package com.febers.uestc_bbs.module.post.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.*
import com.febers.uestc_bbs.module.post.contract.PostContract
import com.febers.uestc_bbs.module.post.model.PostModelImpl

class PostPresenterImpl(var view: PostContract.View): PostContract.Presenter(view) {

    private val postModel: PostContract.Model = PostModelImpl(this)

    override fun postDetailRequest(postId: Int, page: Int, authorId: Int, order: Int) {
        postModel.postDetailService(postId, page, authorId, order)
    }

    override fun postDetailResult(event: BaseEvent<PostDetailBean>) {
        view.showPostDetail(event)
    }

    override fun postReplyRequest(postId: Int, isQuota: Int, replyId: Int, aid: String, vararg contents: Pair<Int, String>) {
        postModel.postReplyService(postId, isQuota, replyId, aid, *contents)
    }

    override fun postReplyResult(event: BaseEvent<PostSendResultBean>) {
        view.showPostReplyResult(event)
    }

    override fun postFavRequest(action: String) {
        postModel.postFavService(action)
    }

    override fun postFavResult(event: BaseEvent<PostFavResultBean>) {
        view.showPostFavResult(event)
    }

    override fun postSupportRequest(postId: Int, tid: Int) {
        postModel.postSupportService(postId, tid)
    }

    override fun postSupportResult(event: BaseEvent<PostSupportResultBean>) {
        view.showPostSupportResult(event)
    }

    override fun postVoteRequest(pollItemId: List<Int>) {
        postModel.postVoteService(pollItemId)
    }

    override fun postVoteResult(event: BaseEvent<PostVoteResultBean>) {
        view.showVoteResult(event)
    }

    override fun userAtRequest(page: Int) {
        postModel.userAtService(page)
    }

    override fun userAtResult(event: BaseEvent<UserCanAtBean>) {
        view.showUserAtResult(event)
    }
}