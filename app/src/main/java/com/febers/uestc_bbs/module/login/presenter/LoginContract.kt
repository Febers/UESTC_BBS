/*
 * Created by Febers at 18-6-11 下午5:27.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-10 下午1:49.
 */

package com.febers.uestc_bbs.module.login.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView
import com.febers.uestc_bbs.entity.UserSimpleBean

interface LoginContract {

    interface Model {
        fun loginService(userName: String, userPw: String)
    }

    interface View: BaseView {
        fun loginResult(event: BaseEvent<UserSimpleBean>)
    }
    abstract class Presenter(view: BaseView): BasePresenter<BaseView>(mView = view) {
        abstract fun loginRequest(userName: String, userPw: String)
        abstract fun loginResult(event: BaseEvent<UserSimpleBean>)
    }
}