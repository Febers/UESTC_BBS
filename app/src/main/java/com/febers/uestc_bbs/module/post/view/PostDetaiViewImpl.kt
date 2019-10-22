package com.febers.uestc_bbs.module.post.view

import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.entity.*
import com.febers.uestc_bbs.module.post.contract.PostContract

class PostDetaiViewImpl constructor(private val activity: BaseActivity,
                                    private val presenter: BasePresenter<PostContract.View>) : PostContract.View {

    override fun showPostDetail(event: BaseEvent<PostDetailBean>) {
        super.showPostDetail(event)
    }

    override fun showPostReplyResult(event: BaseEvent<PostSendResultBean>) {
        super.showPostReplyResult(event)
    }

    override fun showPostFavResult(event: BaseEvent<PostFavResultBean>) {
        super.showPostFavResult(event)
    }

    override fun showPostSupportResult(event: BaseEvent<PostSupportResultBean>) {
        super.showPostSupportResult(event)
    }

    override fun showVoteResult(event: BaseEvent<PostVoteResultBean>) {
        super.showVoteResult(event)
    }

    override fun showUserAtResult(event: BaseEvent<UserCanAtBean>) {
        super.showUserAtResult(event)
    }

    override fun showError(msg: String) {
        super.showError(msg)
    }

    override fun showHint(msg: String) {
    }
}