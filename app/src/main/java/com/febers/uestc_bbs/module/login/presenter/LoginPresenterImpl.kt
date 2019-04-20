package com.febers.uestc_bbs.module.login.presenter

import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.login.contract.LoginContract
import com.febers.uestc_bbs.module.login.model.LoginModelImpl

class LoginPresenterImpl(val view: LoginContract.View): LoginContract.Presenter(view){

    override fun loginRequest(userName: String, userPw: String) {
        val loginModel: LoginContract.Model = LoginModelImpl(this)
        loginModel.loginService(userName, userPw)
    }

    override fun loginResult(event: BaseEvent<UserSimpleBean>) {
        view.showLoginResult(event)
    }
}