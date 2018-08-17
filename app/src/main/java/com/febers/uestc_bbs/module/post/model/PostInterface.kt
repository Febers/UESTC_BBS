/*
 * Created by Febers at 18-8-16 下午7:21.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午9:20.
 */

package com.febers.uestc_bbs.module.post.model

import com.febers.uestc_bbs.entity.LoginResultBean
import com.febers.uestc_bbs.entity.PostResultBean
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.http.*

interface PostInterface {
    @FormUrlEncoded
    @POST(ApiUtils.BBS_LOGIN_URL)
    fun hostPost(@Field("r") r: String, @Field("moduleId") moduleId: String,
              @Field("page") page: String, @Field("pageSize") pageSize: String):
            Call<PostResultBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_LOGIN_URL)
    fun newPost(@Field("r") r: String, @Field("boardId") boardId: String,
                @Field("page") page: String, @Field("pageSize") pageSize: String,
                @Field("sortby") sortby: String): Call<PostResultBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_LOGIN_URL)
    fun normalPost(): Call<PostResultBean>
}
