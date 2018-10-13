package com.febers.uestc_bbs.http

import com.febers.uestc_bbs.MyApplication
import okhttp3.*
import java.util.concurrent.TimeUnit

object TokenClient {

    fun get(): OkHttpClient =
            OkHttpClient.Builder().addInterceptor(TokenInterceptor())
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    //.cookieJar(MyCookiesManager())
                    .build()
}

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest: Request = chain.request()
        val modifiedUrl: HttpUrl = originRequest.url().newBuilder()
                .addQueryParameter("accessToken", MyApplication.getUser().token)
                .addQueryParameter("accessSecret", MyApplication.getUser().secrete)
                .build()
        val newRequest: Request = originRequest.newBuilder().url(modifiedUrl).build()
        return chain.proceed(newRequest)

    }
}