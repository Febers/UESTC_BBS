package com.febers.uestc_bbs.module.login.view

import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.login.presenter.LoginContract
import com.febers.uestc_bbs.module.login.presenter.LoginPresenterImpl
import com.febers.uestc_bbs.utils.KeyboardUtils
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginContract.View {

    private lateinit var loginPresenter: LoginContract.Presenter

    override fun setView(): Int {
        return R.layout.activity_login
    }

    override fun setToolbar(): Toolbar? = toolbar_login

    override fun initView() {
        loginPresenter = LoginPresenterImpl(this)
        btn_login.setOnClickListener { login() }
    }

    private fun login() {
        loginPresenter.loginRequest(edit_text_user_name.text.toString(), edit_text_user_pw.text.toString())
    }

    override fun loginResult(event: BaseEvent<UserSimpleBean>) {
        if (event.code == BaseCode.SUCCESS) {
            showToast("登录成功")
            KeyboardUtils.closeKeyboard(edit_text_user_pw, this)
        }
    }
}
