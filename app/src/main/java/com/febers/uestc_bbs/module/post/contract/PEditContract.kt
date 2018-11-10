package com.febers.uestc_bbs.module.post.contract

import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.PostSendResultBean

interface PEditContract {

    interface Model {
        fun newPostService(fid: Int, title: String, vararg contents: Pair<Int, String>)
    }

    interface View: BaseView {
        fun showNewPostResult(event: PostSendResultBean)
    }

    abstract class Presenter(view: View): BasePresenter<View>(view) {
        abstract fun newPostRequest(fid: Int, title: String, vararg contents: Pair<Int, String>)
        abstract fun newPostResult(event: PostSendResultBean)
    }
}