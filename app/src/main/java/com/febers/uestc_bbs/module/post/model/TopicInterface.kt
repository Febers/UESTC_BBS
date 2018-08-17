/*
 * Created by Febers at 18-8-16 下午7:21.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午9:20.
 */

package com.febers.uestc_bbs.module.post.model

import com.febers.uestc_bbs.entity.TopicResultBean
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.http.*

interface TopicInterface {
    @FormUrlEncoded
    @POST(ApiUtils.BBS_HOME_POST_URL)
    fun hotPosts(@Field("r")r: String, @Field("moduleId")moduleId: String,
                 @Field("page")page: String, @Field("pageSize")pageSize: String):
            Call<TopicResultBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_HOME_POST_URL)
    fun newPosts(@Field("r")r: String, @Field("boardId")boardId: String,
                 @Field("page")page: String, @Field("pageSize")pageSize: String,
                 @Field("sortby")sortby: String): Call<TopicResultBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_TOPOIC_LIST_URL)
    fun normalPosts(): Call<TopicResultBean>
}
