package com.febers.uestc_bbs.base

import com.febers.uestc_bbs.entity.UserBean
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseModel {

    protected lateinit var mPostId: String
    protected lateinit var mUid: String
    protected lateinit var mPage: String
    protected lateinit var mOrder: String
    protected lateinit var mAuthorId: String

    protected fun getUser(): UserBean = BaseApplication.getUser()

    protected fun getRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(ApiUtils.BBS_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}