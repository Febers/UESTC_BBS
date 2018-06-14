/*
 * Created by Febers at 18-6-11 下午5:27.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-10 下午2:14.
 */

package com.febers.uestc_bbs.module.login.model

import android.util.Log.d
import com.febers.uestc_bbs.base.BaseApplication
import com.febers.uestc_bbs.entity.LoginResult
import com.febers.uestc_bbs.module.login.presenter.LoginContract
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
                .baseUrl("http://bbs.uestc.edu.cn/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val loginRequest = retrofit.create(LoginService::class.java)
        val call = loginRequest.login(
                type = "login",
                username = mUserName,
                password = mUserPw,
                mobile = "",
                code = "",
                isValidation = "")
        call.enqueue(object : Callback<LoginResult> {
            override fun onFailure(call: Call<LoginResult>?, t: Throwable?) {
                d("error", "${t.toString()}")
            }

            override fun onResponse(call: Call<LoginResult>?, response: Response<LoginResult>?) {
                val body = response?.body()
                if (body == null) {
                    return
                }
                resolveLoginResult(body!!)
                d("result", "${body?.toString()}")
            }
        })
    }

    private fun resolveLoginResult(loginResult: LoginResult) {
        val rs = loginResult.rs
        if (rs != LOGIN_SECCESS_RS ) {
            loginPresenter.loginResult(loginResult.head.errInfo)
            return
        }
        var userName by CustomPreference(BaseApplication.context(), "sp_user_name", "mUserName")
        userName = mUserName
        var userPw by CustomPreference(BaseApplication.context(), "sp_user_password", "mUserPw")
        userPw = mUserPw
        var userTitle by CustomPreference(BaseApplication.context(), "sp_user_title", "userTitle")
        userTitle = loginResult.userTitle
    }
}