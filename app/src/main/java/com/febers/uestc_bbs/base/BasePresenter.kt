package com.febers.uestc_bbs.base

import com.febers.uestc_bbs.http.client.TokenClient
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BasePresenter<V : BaseView>(protected var mView: V?) {

    open fun detachView() {
        mView = null
    }

    fun errorResult(error: String) {
        mView?.showError(error)
    }

    protected fun getRetrofit(): Retrofit = Retrofit.Builder()
            .baseUrl(ApiUtils.BBS_BASE_URL)
            .client(TokenClient.get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}
