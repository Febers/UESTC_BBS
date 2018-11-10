package com.febers.uestc_bbs.module.login.view

import android.view.View
import androidx.annotation.UiThread
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.login.contract.LoginContract
import com.febers.uestc_bbs.module.login.presenter.LoginPresenterImpl
import com.febers.uestc_bbs.utils.KeyboardUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.browse

class LoginActivity : BaseActivity(), LoginContract.View {

    private val signUpUrl = "http://bbs.uestc.edu.cn/member.php?mod=register"
    private lateinit var loginPresenter: LoginContract.Presenter

    override fun setView(): Int {
        return R.layout.activity_login
    }

    override fun setToolbar(): Toolbar? = toolbar_login

    override fun initView() {
        loginPresenter = LoginPresenterImpl(this)
        btn_login.setOnClickListener { login() }
        btn_sign_up.setOnClickListener {
            browse(signUpUrl, true)
        }
    }

    private fun login() {
        loginPresenter.loginRequest(edit_text_user_name.text.toString(), edit_text_user_pw.text.toString())
        progress_bar_login.visibility = View.VISIBLE
    }

    @UiThread
    override fun loginResult(event: BaseEvent<UserSimpleBean>) {
        showToast("登录成功")
        progress_bar_login.visibility = View.INVISIBLE
        EventBus.getDefault().post(event)
        KeyboardUtils.closeKeyboard(edit_text_user_pw, this)
        finish()
    }

    @UiThread
    override fun showError(msg: String) {
        progress_bar_login.visibility = View.INVISIBLE
        edit_text_user_pw.error = msg
    }
}
