package com.febers.uestc_bbs.module.user.model

import com.febers.uestc_bbs.entity.UserDetailBean
import com.febers.uestc_bbs.entity.UserPostBean
import com.febers.uestc_bbs.entity.UserUpdateResultBean
import com.febers.uestc_bbs.utils.ApiUtils
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UserInterface {
    @FormUrlEncoded
    @POST(ApiUtils.BBS_USER_START_URL)
    fun getUserStartPList(@Field("uid")uid: String, @Field("page")page :String,
                          @Field("pageSize")pageSize: String) : Call<UserPostBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_USER_REPLY_URL)
    fun getUserReplyPList(@Field("uid")uid: String, @Field("page")page :String,
                          @Field("pageSize")pageSize: String) : Call<UserPostBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_USER_FAV_URL)
    fun getUserFavPList(@Field("uid")uid: String, @Field("page")page :String,
                        @Field("pageSize")pageSize: String) : Call<UserPostBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_USER_INFO_URL)
    fun getUserDetail(@Field("userId")userId: String) : Call<UserDetailBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_UPDATE_USER_SIGN_URL)
    fun updateUserSign(@Field("sign")sign: String) : Call<UserUpdateResultBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_UPDATE_USER_GENDER_URL)
    fun updateUserGender(@Field("gender")gender: String) : Call<UserUpdateResultBean>

    @Multipart
    @POST(ApiUtils.BBS_UPDATE_USER_AVATAR_URL)
    fun updateUserAvatar(@Part()avatar: MultipartBody.Part) : Call<UserUpdateResultBean>
}