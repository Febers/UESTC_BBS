/*
 * Created by Febers at 18-6-11 下午5:27.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-10 下午2:03.
 */

package com.febers.uestc_bbs.module.login.model

import com.febers.uestc_bbs.entity.LoginResult
import retrofit2.Call
import retrofit2.http.*

interface LoginService {
    @FormUrlEncoded
    @POST("mobcent/app/web/index.php?r=user/login")
    fun login(@Field("type") type: String, @Field("username") username: String,
              @Field("password") password: String, @Field("mobile") mobile: String,
              @Field("code") code: String, @Field("isValidation") isValidation: String):
            Call<LoginResult>
}
