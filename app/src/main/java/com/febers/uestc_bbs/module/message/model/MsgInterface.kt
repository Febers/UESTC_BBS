package com.febers.uestc_bbs.module.message.model

import com.febers.uestc_bbs.entity.MsgReplyBean
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MsgInterface {
    @FormUrlEncoded
    @POST(ApiUtils.BBS_MESSAGE_NOTIFY_LIST)
    fun hotPosts(@Field("r")r: String, @Field("moduleId")moduleId: String,
                 @Field("page")page: String, @Field("pageSize")pageSize: String):
            Call<MsgReplyBean>
}