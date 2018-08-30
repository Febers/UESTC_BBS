/*
 * Created by Febers at 18-8-16 下午6:33.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-16 下午6:33.
 */

package com.febers.uestc_bbs.module.login.view

import android.os.Bundle
import android.support.annotation.UiThread

import android.support.v7.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.login.presenter.LoginContract
import com.febers.uestc_bbs.module.login.presenter.LoginPresenter
import kotlinx.android.synthetic.main.fragment_login.*
import org.greenrobot.eventbus.EventBus

class LoginFragment: BasePopFragment(), LoginContract.View {

    override fun setToolbar(): Toolbar {
        return toolbar_login
    }

    override fun setContentView(): Int {
        return R.layout.fragment_login
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        btn_login.setOnClickListener { login() }
    }

    private fun login() {
        val loginPresenter: LoginContract.Presenter = LoginPresenter(this)
        loginPresenter.loginRequest(edit_text_user_name.text.toString(), edit_text_user_pw.text.toString())
    }

    @UiThread
    override fun loginResult(event: BaseEvent<UserBean>) {
        if (event.code == BaseCode.SUCCESS) {
            onError("登录成功")
            EventBus.getDefault().post(event)
            pop()
        } else {
            onError("${event.data.msg}")
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param: String) =
                LoginFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param)
                    }
                }
    }
}