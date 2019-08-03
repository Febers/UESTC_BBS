package com.febers.uestc_bbs.module.login.model

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.io.UserHelper
import com.febers.uestc_bbs.entity.LoginResultBean
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.login.contract.LoginContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginModelImpl(val loginPresenter: LoginContract.Presenter): BaseModel(), LoginContract.Model {

    private val mUserSimple: UserSimpleBean = UserSimpleBean()
    private lateinit var mUserName: String
    private lateinit var mUserPw: String

    override fun loginService(userName: String, userPw: String) {
        mUserName = userName
        mUserPw = userPw
        ThreadPoolMgr.execute(Runnable { login() })
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
                loginPresenter.errorResult(SERVICE_RESPONSE_ERROR + t.toString())
            }

            override fun onResponse(call: Call<LoginResultBean>?, response: Response<LoginResultBean>?) {
                val body = response?.body()
                if (body == null) {
                    loginPresenter.errorResult(SERVICE_RESPONSE_NULL)
                    return
                }
                resolveLoginResult(body)
            }
        })
    }

    private fun resolveLoginResult(loginResultBean: LoginResultBean) {
        val rs = loginResultBean.rs
        if (rs != REQUEST_SUCCESS_RS ) {
            loginPresenter.errorResult(loginResultBean.head.errInfo)
            return
        }

        /*
            保存一个User类，里面有用户的关键信息
            用于所有跟服务器的交互
         */
        mUserSimple.name = loginResultBean.userName + ""
        mUserSimple.uid = loginResultBean.uid
        mUserSimple.title = loginResultBean.userTitle
        mUserSimple.gender = loginResultBean.gender
        mUserSimple.token = loginResultBean.token
        mUserSimple.secrete = loginResultBean.secret
        mUserSimple.avatar = loginResultBean.avatar
        mUserSimple.credits = loginResultBean.creditShowList[0].data
        mUserSimple.extcredits2 = loginResultBean.creditShowList[1].data
        mUserSimple.groupId = loginResultBean.groupid
        mUserSimple.mobile = loginResultBean.mobile
        mUserSimple.valid = true

        loginPresenter.loginResult(BaseEvent(BaseCode.SUCCESS, mUserSimple))

        UserHelper.addUser(loginResultBean.uid, mUserSimple)
    }
}