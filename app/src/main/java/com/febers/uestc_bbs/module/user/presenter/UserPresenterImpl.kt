package com.febers.uestc_bbs.module.user.presenter

import android.util.Log.i
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.UserDetailBean
import com.febers.uestc_bbs.entity.UserPListBean
import com.febers.uestc_bbs.module.user.model.UserModelImpl

class UserPresenterImpl(var view: UserContract.View): UserContract.Presenter(view) {

    override fun userPListRequest(uid: String, type: String, page: String) {
        val userModel: UserContract.Model = UserModelImpl(this)
        userModel.userPListService(uid, type, page)
    }

    override fun userPListResult(event: BaseEvent<UserPListBean>) {
        view.showUserPList(event)
    }

    override fun userDetailRequest(uid: String) {
        val userModel: UserContract.Model = UserModelImpl(this)
        userModel.userDetailService(uid)
    }

    override fun userDetailResult(event: BaseEvent<UserDetailBean>) {
        view.showUserDetail(event)
    }
}