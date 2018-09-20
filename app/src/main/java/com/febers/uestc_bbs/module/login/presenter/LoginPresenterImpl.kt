/*
 * Created by Febers at 18-6-10 下午1:44.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-10 下午1:44.
 */

package com.febers.uestc_bbs.module.login.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.login.model.LoginModelImpl
import org.greenrobot.eventbus.EventBus

class LoginPresenterImpl(val view: LoginContract.View): LoginContract.Presenter(view){

    override fun loginRequest(userName: String, userPw: String) {
        val loginModel: LoginContract.Model = LoginModelImpl(this)
        loginModel.loginService(userName, userPw)
    }

    override fun loginResult(event: BaseEvent<UserBean>) {
        view.loginResult(event)
        EventBus.getDefault().post(event)
    }
}