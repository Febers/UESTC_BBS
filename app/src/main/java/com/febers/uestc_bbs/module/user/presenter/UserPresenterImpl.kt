package com.febers.uestc_bbs.module.user.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.UserPostBean
import com.febers.uestc_bbs.module.user.model.UserPostModelImpl

class UserPresenterImpl(var view: UserContract.View): UserContract.Presenter(view) {

    override fun userPostRequest(uid: String, type: Int, page: String) {
        val userPostModel: UserContract.Model = UserPostModelImpl(this)
        userPostModel.userPostService(uid, type, page)
    }

    override fun userPostResult(event: BaseEvent<UserPostBean>) {
        view.showUserPost(event)
    }
}