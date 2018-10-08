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
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.login.presenter.LoginContract
import com.febers.uestc_bbs.module.login.presenter.LoginPresenterImpl
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment: BaseSwipeFragment(), LoginContract.View {

    private lateinit var loginPresenter: LoginContract.Presenter

    override fun setToolbar(): Toolbar {
        return toolbar_login
    }

    override fun setContentView(): Int {
        return R.layout.fragment_login
    }

    override fun initView() {
        loginPresenter = LoginPresenterImpl(this)
        btn_login.setOnClickListener { login() }
    }

    private fun login() {
        loginPresenter.loginRequest(edit_text_user_name.text.toString(), edit_text_user_pw.text.toString())
    }

    @UiThread
    override fun loginResult(event: BaseEvent<UserSimpleBean>) {
        if (event.code == BaseCode.SUCCESS) {
            showToast("登录成功")
            hideSoftInput()
            pop()
        } else {
            showToast(event.data.msg)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(showBottomBarOnDestroy: Boolean) =
                LoginFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
                    }
                }
    }
}