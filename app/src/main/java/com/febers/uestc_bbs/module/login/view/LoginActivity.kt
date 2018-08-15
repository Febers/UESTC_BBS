/*
 * Created by Febers at 18-6-11 下午5:27.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-10 下午1:49.
 */

package com.febers.uestc_bbs.module.login.view

import android.os.Bundle
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.login.presenter.LoginContract
import com.febers.uestc_bbs.module.login.presenter.LoginPresenter
import kotlinx.android.synthetic.main.activity_login.*
import me.yokeyword.fragmentation.SupportActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast

class LoginActivity: SupportActivity(), LoginContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
    }

    fun initView() {
        setSupportActionBar(toolbar_login)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btn_login.setOnClickListener { login() }
        btn_forum_list.setOnClickListener{ }
    }

    private fun login() {
        val loginPresenter: LoginContract.Presenter = LoginPresenter(this)
        loginPresenter.loginRequest(edit_text_user_name.text.toString(), edit_text_user_pw.text.toString())
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun loginResult(event: BaseEvent<UserBean>) {
        if (event.code == BaseCode.SUCCESS) {
            toast("登录成功")
            finish()
        } else {
            toast("${event.data.msg}")
        }
    }

    override fun onError(error: String) {

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}