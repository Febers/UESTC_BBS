package com.febers.uestc_bbs.module.user.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.UserDetailBean
import com.febers.uestc_bbs.entity.UserPostBean

interface UserContract {

    interface Model {
        fun userPostService(uid: Int, type: String, page: Int)
        fun userDetailService(uid: Int)
    }

    interface View: BaseView {
        fun showUserPost(event: BaseEvent<UserPostBean>){}
        fun showUserDetail(event: BaseEvent<UserDetailBean>){}
    }

    abstract class Presenter(view: BaseView): BasePresenter<BaseView>(view) {
        abstract fun userPostRequest(uid: Int, type: String, page: Int)
        abstract fun userPostResult(event: BaseEvent<UserPostBean>)

        abstract fun userDetailRequest(uid: Int)
        abstract fun userDetailResult(event: BaseEvent<UserDetailBean>)
    }
}