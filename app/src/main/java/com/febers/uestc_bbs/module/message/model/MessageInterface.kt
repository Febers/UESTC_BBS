package com.febers.uestc_bbs.module.message.model

import com.febers.uestc_bbs.entity.MsgPrivateBean
import com.febers.uestc_bbs.entity.PMDetailBean
import com.febers.uestc_bbs.entity.PMSendResultBean
import com.febers.uestc_bbs.utils.ApiUtils
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface MessageInterface {
    @FormUrlEncoded
    @POST(ApiUtils.BBS_MESSAGE_NOTIFY_LIST)
    fun getReplyAndSystemAndAt(
            @Field("type")type: String, @Field("page")page: String, @Field("pageSize")pageSize: String):
            Call<JsonObject>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_MESSAGE_PM_SESSION_LIST)
    fun getPrivate(@Field("json")json: String): Call<MsgPrivateBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_MESSAGE_PM_LIST)
    fun getPMDetail(@Field("pmlist")pmlist: String): Call<PMDetailBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_MESSAGE_PM_ADMIN)
    fun pmSendResult(@Field("json")json: String): Call<PMSendResultBean>
}