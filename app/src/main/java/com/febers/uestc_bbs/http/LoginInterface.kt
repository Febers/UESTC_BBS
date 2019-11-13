package com.febers.uestc_bbs.http

import com.febers.uestc_bbs.entity.LoginResultBean
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.http.*

interface LoginInterface {

    @FormUrlEncoded
    @POST(ApiUtils.BBS_LOGIN_URL)
    fun login(@Field("type") type: String, @Field("username") username: String,
              @Field("password") password: String, @Field("mobile") mobile: String,
              @Field("code") code: String, @Field("isValidation") isValidation: String):
            Call<LoginResultBean>
}
