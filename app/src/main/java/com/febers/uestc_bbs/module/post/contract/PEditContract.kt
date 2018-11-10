package com.febers.uestc_bbs.module.post.contract

import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.PostSendResultBean

interface PEditContract {

    interface Model {
        fun newPostService(json: String)
    }

    interface View: BaseView {
        fun showNewPostResult(event: PostSendResultBean)
    }

    abstract class Presenter(view: View): BasePresenter<View>(view) {
        abstract fun newPostRequest(json: String)
        abstract fun newPostResult(event: PostSendResultBean)
    }
}