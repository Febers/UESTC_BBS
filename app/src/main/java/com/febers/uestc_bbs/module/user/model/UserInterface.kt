package com.febers.uestc_bbs.module.user.model

import com.febers.uestc_bbs.entity.UserPostBean
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserInterface {
    @FormUrlEncoded
    @POST(ApiUtils.BBS_USER_START_URL)
    fun getUserStartPost(@Field("accessToken")accessToken: String, @Field("accessSecret")accessSecret: String,
                         @Field("uid")uid: String, @Field("page")page :String,
                         @Field("pageSize")pageSize: String) : Call<UserPostBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_USER_REPLY_URL)
    fun getUserReplyPost(@Field("accessToken")accessToken: String, @Field("accessSecret")accessSecret: String,
                         @Field("uid")uid: String, @Field("page")page :String,
                         @Field("pageSize")pageSize: String) : Call<UserPostBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_USER_FAV_URL)
    fun getUserFavPost(@Field("accessToken")accessToken: String, @Field("accessSecret")accessSecret: String,
                       @Field("uid")uid: String, @Field("page")page :String,
                       @Field("pageSize")pageSize: String) : Call<UserPostBean>
}