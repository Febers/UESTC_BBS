/*
 * Created by Febers at 18-6-11 下午5:27.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-10 下午2:14.
 */

package com.febers.uestc_bbs.module.login.model

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.dao.UserSaver
import com.febers.uestc_bbs.entity.LoginResultBean
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.login.presenter.LoginContract
import com.febers.uestc_bbs.utils.ApiUtils
import com.febers.uestc_bbs.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginModelImpl(val loginPresenter: LoginContract.Presenter): ILoginModel {

    private val user: UserBean = UserBean()
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
                user.msg = "${SERVICE_RESPONSE_ERROR + t.toString()}"
                loginPresenter.loginResult(BaseEvent(BaseCode.FAILURE, user))
            }

            override fun onResponse(call: Call<LoginResultBean>?, response: Response<LoginResultBean>?) {
                val body = response?.body()
                if (body == null) {
                    user.msg = SERVICE_RESPONSE_NULL
                    loginPresenter.loginResult(BaseEvent(BaseCode.FAILURE, user))
                    return
                }
                resolveLoginResult(body)
            }
        })
    }

    private fun resolveLoginResult(loginResultBean: LoginResultBean) {
        val rs = loginResultBean.rs
        if (rs != REQUEST_SECCESS_RS ) {
            user.msg = loginResultBean.head.errInfo
            loginPresenter.loginResult(BaseEvent(BaseCode.FAILURE, user))
            return
        }
        user.name = loginResultBean.userName
                user.uid = loginResultBean.uid
                user.title = loginResultBean.userTitle
        user.gender = loginResultBean.gender
        user.token = loginResultBean.token
        user.secrete = loginResultBean.secret
        user.avatar = loginResultBean.avatar
        user.credits = loginResultBean.creditShowList[0].data
        user.extcredits2 = loginResultBean.creditShowList[1].data
        user.groupId = loginResultBean.groupid
        user.mobile = loginResultBean.mobile
        user.valid = true

        loginPresenter.loginResult(BaseEvent(BaseCode.SUCCESS, user))
        var uid by PreferenceUtils(mContext, mContext.getString(R.string.sp_user_uid), "")
        uid = loginResultBean.uid
        UserSaver.save(uid, user)
    }
}