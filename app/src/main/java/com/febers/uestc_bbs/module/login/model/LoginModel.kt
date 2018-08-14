/*
 * Created by Febers at 18-6-11 下午5:27.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-10 下午2:14.
 */

package com.febers.uestc_bbs.module.login.model

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.dao.UserSaver
import com.febers.uestc_bbs.entity.LoginResultBean
import com.febers.uestc_bbs.module.login.presenter.LoginContract
import com.febers.uestc_bbs.utils.ApiUtils
import com.febers.uestc_bbs.utils.BBS_URL
import com.febers.uestc_bbs.utils.CustomPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val LOGIN_SECCESS_RS = "1"

class LoginModel(val loginPresenter: LoginContract.Presenter): ILoginModel {

    private lateinit var mUserName: String
    private lateinit var mUserPw: String
    private val mContext = BaseApplication.context()

    override fun loginService(_userName: String, _userPw: String) {
        mUserName = _userName
        mUserPw = _userPw
        Thread(object : Runnable {
            override fun run() {
                login()
            }
        }).start()
    }

    private fun login() {
        val retrofit = Retrofit.Builder()
                .baseUrl(ApiUtils.BBS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val loginRequest = retrofit.create(LoginInterface::class.java)
        val call = loginRequest.login(
                type = "login",
                username = mUserName,
                password = mUserPw,
                mobile = "",
                code = "",
                isValidation = "")
        call.enqueue(object : Callback<LoginResultBean> {
            override fun onFailure(call: Call<LoginResultBean>?, t: Throwable?) {
                loginPresenter.loginResult(BaseEvent(BaseCode.ERROR, "登录出错:${t.toString()}"))
            }

            override fun onResponse(call: Call<LoginResultBean>?, response: Response<LoginResultBean>?) {
                val body = response?.body()
                if (body == null) {
                    loginPresenter.loginResult(BaseEvent(BaseCode.ERROR, "服务器响应为空"))
                    return
                }
                resolveLoginResult(body)
            }
        })
    }

    private fun resolveLoginResult(loginResultBean: LoginResultBean) {
        val rs = loginResultBean.rs
        if (rs != LOGIN_SECCESS_RS ) {
            loginPresenter.loginResult(BaseEvent(BaseCode.FAILURE, loginResultBean.head.errInfo))
            return
        }
        loginPresenter.loginResult(BaseEvent(BaseCode.SUCCESS, ""))
        var uid by CustomPreference(mContext, mContext.getString(R.string.sp_user_uid), "")
        uid = loginResultBean.uid
        UserSaver.save(uid, loginResultBean)
    }
}