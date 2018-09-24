package com.febers.uestc_bbs.module.user.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.UserPListBean
import com.febers.uestc_bbs.module.user.model.UserPListModelImpl

class UserPresenterImpl(var view: UserContract.View): UserContract.Presenter(view) {

    override fun userPListRequest(uid: String, type: String, page: String) {
        val userPostModel: UserContract.Model = UserPListModelImpl(this)
        userPostModel.userPListService(uid, type, page)
    }

    override fun userPListResult(event: BaseEvent<UserPListBean>) {
        view.showUserPList(event)
    }
}