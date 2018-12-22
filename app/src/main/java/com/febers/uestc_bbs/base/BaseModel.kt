package com.febers.uestc_bbs.base

import com.febers.uestc_bbs.http.TokenClient
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseModel {

    protected var mFid: String = ""
    protected var mPostId: String = ""
    protected var mUid: String = ""
    protected var mPage: String = ""
    protected var mOrder: String = ""
    protected var mAuthorId: String = ""
    protected var mKeyword: String = ""
    protected var mType: String = ""
    protected var mTopOrder: String = "1"   //默认返回本版置顶帖
    protected var mPageSize: Int = COMMON_PAGE_SIZE

    protected fun getRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(ApiUtils.BBS_BASE_URL)
            .client(TokenClient.get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}

/**
 *  * ░░░░░░░░░░░░░░░░░░░░░░░░▄░░
 * ░░░░░░░░░▐█░░░░░░░░░░░▄▀▒▌░
 * ░░░░░░░░▐▀▒█░░░░░░░░▄▀▒▒▒▐
 * ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐
 * ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐
 * ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌
 * ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒
 * ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐
 * ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄
 * ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒
 * ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒
 * 单身狗就这样默默地看着你，一句话也不说。
 *
 */