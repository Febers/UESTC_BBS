/*
 * Created by Febers at 18-6-11 下午5:27.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-10 下午2:14.
 */

package com.febers.uestc_bbs.module.login.model

import com.febers.uestc_bbs.MyApplication
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.dao.UserStore
import com.febers.uestc_bbs.entity.LoginResultBean
import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.module.login.presenter.LoginContract
import com.febers.uestc_bbs.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginModelImpl(val loginPresenter: LoginContract.Presenter): BaseModel(), LoginContract.Model {

    private val mUser: UserBean = UserBean()
    private lateinit var mUserName: String
    private lateinit var mUserPw: String
    private val mContext = MyApplication.context()

    override fun loginService(userName: String, userPw: String) {
        mUserName = userName
        mUserPw = userPw
        Thread(Runnable { login() }).start()
    }

    private fun login() {
        val loginRequest = getRetrofit().create(LoginInterface::class.java)
        val call = loginRequest.login(
                type = "login",
                username = mUserName,
                password = mUserPw,
                mobile = "",
                code = "",
                isValidation = "")
        call.enqueue(object : Callback<LoginResultBean> {
            override fun onFailure(call: Call<LoginResultBean>?, t: Throwable?) {
                mUser.msg = SERVICE_RESPONSE_ERROR + t.toString()
                loginPresenter.loginResult(BaseEvent(BaseCode.FAILURE, mUser))
            }

            override fun onResponse(call: Call<LoginResultBean>?, response: Response<LoginResultBean>?) {
                val body = response?.body()
                if (body == null) {
                    mUser.msg = SERVICE_RESPONSE_NULL
                    loginPresenter.loginResult(BaseEvent(BaseCode.FAILURE, mUser))
                    return
                }
                resolveLoginResult(body)
            }
        })
    }

    private fun resolveLoginResult(loginResultBean: LoginResultBean) {
        val rs = loginResultBean.rs
        if (rs != REQUEST_SUCCESS_RS ) {
            mUser.msg = loginResultBean.head.errInfo
            loginPresenter.loginResult(BaseEvent(BaseCode.FAILURE, mUser))
            return
        }
        mUser.name = loginResultBean.userName
                mUser.uid = loginResultBean.uid
                mUser.title = loginResultBean.userTitle
        mUser.gender = loginResultBean.gender
        mUser.token = loginResultBean.token
        mUser.secrete = loginResultBean.secret
        mUser.avatar = loginResultBean.avatar
        mUser.credits = loginResultBean.creditShowList[0].data
        mUser.extcredits2 = loginResultBean.creditShowList[1].data
        mUser.groupId = loginResultBean.groupid
        mUser.mobile = loginResultBean.mobile
        mUser.valid = true

        loginPresenter.loginResult(BaseEvent(BaseCode.SUCCESS, mUser))
        var uid by PreferenceUtils(mContext, mContext.getString(R.string.sp_user_uid), "")
        uid = loginResultBean.uid
        UserStore.save(uid, mUser)
    }
}