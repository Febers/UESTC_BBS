package com.febers.uestc_bbs.module.user.contract

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.mvp.BasePresenter
import com.febers.uestc_bbs.base.mvp.BaseView
import com.febers.uestc_bbs.entity.UserDetailBean
import com.febers.uestc_bbs.entity.UserPostBean
import com.febers.uestc_bbs.entity.UserUpdateResultBean

interface UserContract {

    interface View: BaseView {
        fun showUserPost(event: BaseEvent<UserPostBean>){}
        fun showUserDetail(event: BaseEvent<UserDetailBean>){}
        fun showUserUpdate(event: BaseEvent<UserUpdateResultBean>){}
    }

    abstract class Presenter(view: View): BasePresenter<View>(view) {
        abstract fun userPostRequest(uid: Int, type: Int, page: Int)
        abstract fun userDetailRequest(uid: Int)
        abstract fun <T> userUpdateRequest(type: String, newValue: T, oldValue: T? = null)
    }
}