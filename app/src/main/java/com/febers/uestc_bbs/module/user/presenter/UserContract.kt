package com.febers.uestc_bbs.module.user.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.UserPListBean

interface UserContract {

    interface Model {
        fun userPListService(uid: String, type: String, page: String)
    }

    interface View: BaseView {
        fun showUserPList(event: BaseEvent<UserPListBean>)
    }

    abstract class Presenter(view: BaseView): BasePresenter<BaseView>(view) {
        abstract fun userPListRequest(uid: String, type: String, page: String)
        abstract fun userPListResult(event: BaseEvent<UserPListBean>)
    }
}