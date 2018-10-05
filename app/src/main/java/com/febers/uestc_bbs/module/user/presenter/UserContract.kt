package com.febers.uestc_bbs.module.user.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.UserDetailBean
import com.febers.uestc_bbs.entity.UserPListBean

interface UserContract {

    interface Model {
        fun userPListService(uid: String, type: String, page: String)
        fun userDetailService(uid: String)
    }

    interface View: BaseView {
        fun showUserPList(event: BaseEvent<UserPListBean>){}
        fun showUserDetail(event: BaseEvent<UserDetailBean>){}
    }

    abstract class Presenter(view: BaseView): BasePresenter<BaseView>(view) {
        abstract fun userPListRequest(uid: String, type: String, page: String)
        abstract fun userPListResult(event: BaseEvent<UserPListBean>)
        abstract fun userDetailRequest(uid: String)
        abstract fun userDetailResult(event: BaseEvent<UserDetailBean>)
    }
}