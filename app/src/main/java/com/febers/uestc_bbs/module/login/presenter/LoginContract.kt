/*
 * Created by Febers at 18-6-11 下午5:27.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-10 下午1:49.
 */

package com.febers.uestc_bbs.module.login.presenter

import com.febers.uestc_bbs.base.BasePresenter
import com.febers.uestc_bbs.base.BaseView

interface LoginContract {
    interface View: BaseView {
        fun loginResult(result: Boolean)
    }
    abstract class Presenter(view: View): BasePresenter<View>(mView = view) {
        abstract fun loginRequest(userName: String, userPw: String)
        abstract fun loginResult(result: String)
    }
}