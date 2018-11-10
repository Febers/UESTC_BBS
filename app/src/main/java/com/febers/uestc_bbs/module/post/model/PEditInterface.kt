package com.febers.uestc_bbs.module.post.model

import com.febers.uestc_bbs.entity.PostSendResultBean
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PEditInterface {

    @FormUrlEncoded
    @POST(ApiUtils.BBS_TOPIC_ADMIN_URL)
    fun newPost(@Field("act")act: String = "new", @Field("json")json: String): Call<PostSendResultBean>

}