package com.febers.uestc_bbs.module.user.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.UserPostBean

interface UserContract {

    interface Model {
        fun userPostService(uid: String, type: Int, page: String)
    }

    interface View: BaseView {
        fun showUserPost(event: BaseEvent<UserPostBean>)
    }

    abstract class Presenter(view: BaseView): BasePresenter<BaseView>(view) {
        abstract fun userPostRequest(uid: String, type: Int, page: String)
        abstract fun userPostResult(event: BaseEvent<UserPostBean>)
    }
}