/*
 * Created by Febers at 18-6-11 下午5:27.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-10 下午1:49.
 */

package com.febers.uestc_bbs.module.login.view

import android.graphics.BitmapFactory
import android.view.MenuItem
import android.view.View
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.module.login.presenter.LoginContract
import com.febers.uestc_bbs.module.login.presenter.LoginPresenter
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: BaseActivity(), View.OnClickListener, LoginContract.View {

    override fun setView(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        setSupportActionBar(toolbar_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btn_login.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_login -> {login()}
            else -> {}
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {finish()}
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }

    private fun login() {
        val loginPresenter: LoginContract.Presenter = LoginPresenter(this)
        loginPresenter.loginRequest(edit_text_user_name.text.toString(), edit_text_user_pw.text.toString())
    }

    override fun loginResult(event: BaseEvent<String>) {
    }
}