package com.febers.uestc_bbs.base

import com.febers.uestc_bbs.MyApplication
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.http.TokenClient
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseModel {

    protected val COMMON_PAGE_SIZE = "30"
    protected val FIRST_PAGE = "1"

    protected var mFid: String = ""
    protected var mPostId: String = ""
    protected var mUid: String = ""
    protected var mPage: String = ""
    protected var mOrder: String = ""
    protected var mAuthorId: String = ""
    protected var mKeyword: String = ""
    protected var mType: String = ""

    protected fun getUser(): UserSimpleBean = MyApplication.getUser()

    protected fun getRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(ApiUtils.BBS_BASE_URL)
            .client(TokenClient.get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}