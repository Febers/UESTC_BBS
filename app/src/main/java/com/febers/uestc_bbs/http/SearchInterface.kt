package com.febers.uestc_bbs.http

import com.febers.uestc_bbs.entity.SearchPostBean
import com.febers.uestc_bbs.entity.SearchUserBean
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface SearchInterface {
    @FormUrlEncoded
    @POST(ApiUtils.BBS_SEARCH_URL)
    fun getSearchPost(@Field("keyword")keyword: String, @Field("page")page :String,
                      @Field("pageSize")pageSize: String) : Call<SearchPostBean>

    @FormUrlEncoded
    @POST(ApiUtils.BBS_SEARCH_USER_URL)
    fun getSearchUser(@Field("keyword")keyword: String, @Field("page")page :String,
                      @Field("pageSize")pageSize: String, @Field("searchid")searchid: String = "0") : Call<SearchUserBean>
}