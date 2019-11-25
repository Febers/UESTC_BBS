package com.febers.uestc_bbs.module.login.presenter

import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.LoginResultBean
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.io.UserManager
import com.febers.uestc_bbs.module.login.contract.LoginContract
import com.febers.uestc_bbs.http.LoginInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginPresenterImpl(view: LoginContract.View): LoginContract.Presenter(view){

    override fun loginRequest(userName: String, userPw: String) {
        ThreadMgr.network { login(userName, userPw) }
    }

    private fun login(userName: String, userPw: String) {
        val loginRequest = getRetrofit().create(LoginInterface::class.java)
        val call = loginRequest.login(
                type = "login",
                username = userName,
                password = userPw,
                mobile = "",
                code = "",
                isValidation = "")
        call.enqueue(object : Callback<LoginResultBean> {
            override fun onFailure(call: Call<LoginResultBean>?, t: Throwable?) {
                errorResult(SERVICE_RESPONSE_ERROR + t.toString())
            }

            override fun onResponse(call: Call<LoginResultBean>?, response: Response<LoginResultBean>?) {
                val body = response?.body()
                if (body == null) {
                    errorResult(SERVICE_RESPONSE_NULL)
                    return
                }
                if (resolveLoginResult(body)) {
//                    ThreadMgr.execute(Runnable { LoginMockWebView().login(userName, userPw) })
                }
            }
        })
    }

    private fun resolveLoginResult(loginResultBean: LoginResultBean): Boolean {
        val rs = loginResultBean.rs
        if (rs != REQUEST_SUCCESS_RS ) {
            errorResult(loginResultBean.head.errInfo)
            return false
        }

        /*
            保存一个User类，里面有用户的关键信息
            用于所有跟服务器的交互
         */
        val userSimple = UserSimpleBean()
        userSimple.name = loginResultBean.userName + ""
        userSimple.uid = loginResultBean.uid
        userSimple.title = loginResultBean.userTitle
        userSimple.gender = loginResultBean.gender
        userSimple.token = loginResultBean.token
        userSimple.secrete = loginResultBean.secret
        userSimple.avatar = loginResultBean.avatar
        userSimple.credits = loginResultBean.creditShowList[0].data
        userSimple.extcredits2 = loginResultBean.creditShowList[1].data
        userSimple.groupId = loginResultBean.groupid
        userSimple.mobile = loginResultBean.mobile
        userSimple.valid = true

        mView?.showLoginResult(BaseEvent(BaseCode.SUCCESS, userSimple))

        UserManager.addUser(loginResultBean.uid, userSimple)
        return true
    }

}