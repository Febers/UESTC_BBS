package com.febers.uestc_bbs.module.post.model.http_interface

import com.febers.uestc_bbs.entity.*
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface PostInterface {

    @FormUrlEncoded
    @POST(ApiUtils.BBS_POST_LIST_URL)
    fun getPostDetail(@Field("topicId")topicId: String, @Field("authorId")authorId: String,
                      @Field("order")order: String, @Field("page")page :String,
                      @Field("pageSize")pageSize: String) : Call<PostDetailBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_TOPIC_ADMIN_URL)
    fun postReply(@Field("act")act: String = "reply", @Field("json")json: String): Call<PostSendResultBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_POST_FAVORITE_URL)
    fun postFavorite(@Field("action")action: String, @Field("id")id: String,
                     @Field("idType")idType: String = "tid"): Call<PostFavResultBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_POST_SUPPORT)
    fun postSupport(@Field("tid")tid: String, @Field("pid")pid: String): Call<PostSupportResultBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_VOTE_URL)
    fun postVote(@Field("tid")tid: String, @Field("options")options: String): Call<PostVoteResultBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_AT_USER_LIST_URL)
    fun userAt(@Field("page")page :String, @Field("pageSize")pageSize: String): Call<UserCanAtBean>
}