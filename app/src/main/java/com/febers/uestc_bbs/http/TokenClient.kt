package com.febers.uestc_bbs.http

import com.febers.uestc_bbs.MyApp
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
                .addQueryParameter("accessToken", MyApp.getUser().token)
                .addQueryParameter("accessSecret", MyApp.getUser().secrete)
                .build()
        val newRequest: Request = originRequest.newBuilder().url(modifiedUrl).build()
        return chain.proceed(newRequest)
    }
}