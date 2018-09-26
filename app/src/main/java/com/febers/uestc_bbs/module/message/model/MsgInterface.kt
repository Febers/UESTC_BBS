package com.febers.uestc_bbs.module.message.model

import com.febers.uestc_bbs.base.MSG_TYPE_PRIVATE
import com.febers.uestc_bbs.base.MSG_TYPE_REPLY
import com.febers.uestc_bbs.entity.MsgPrivateBean
import com.febers.uestc_bbs.entity.MsgReplyBean
import com.febers.uestc_bbs.utils.ApiUtils
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MsgInterface {
    @FormUrlEncoded
    @POST(ApiUtils.BBS_MESSAGE_NOTIFY_LIST)
    fun getReply(
            @Field("accessToken")accessToken: String, @Field("accessSecret")accessSecret: String,
            @Field("type")type: String, @Field("page")page: String, @Field("pageSize")pageSize: String):
            Call<JsonObject>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_MESSAGE_PM_SESSION_LIST)
    fun getPrivate(
            @Field("accessToken")accessToken: String, @Field("accessSecret")accessSecret: String,
            @Field("type")type: String, @Field("page")page: String, @Field("pageSize")pageSize: String):
            Call<MsgPrivateBean>
}