package com.febers.uestc_bbs.module.user.model

import com.febers.uestc_bbs.entity.UserPListBean
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UserInterface {
    @FormUrlEncoded
    @POST(ApiUtils.BBS_USER_START_URL)
    fun getUserStartPList(@Field("uid")uid: String, @Field("page")page :String,
                          @Field("pageSize")pageSize: String) : Call<UserPListBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_USER_REPLY_URL)
    fun getUserReplyPList(@Field("uid")uid: String, @Field("page")page :String,
                          @Field("pageSize")pageSize: String) : Call<UserPListBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_USER_FAV_URL)
    fun getUserFavPList(@Field("uid")uid: String, @Field("page")page :String,
                        @Field("pageSize")pageSize: String) : Call<UserPListBean>
}