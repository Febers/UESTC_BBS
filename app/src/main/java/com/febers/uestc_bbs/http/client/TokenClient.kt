package com.febers.uestc_bbs.http.client

import com.febers.uestc_bbs.MyApp
import okhttp3.*
import java.util.concurrent.TimeUnit

object TokenClient {

    fun get(): OkHttpClient =
            OkHttpClient.Builder().addInterceptor(TokenInterceptor())
                    .connectTimeout(18, TimeUnit.SECONDS)
                    .readTimeout(18, TimeUnit.SECONDS)
                    //.cookieJar(MyCookiesManager())
                    .build()
}

class TokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originRequest: Request = chain.request()
        val modifiedUrl: HttpUrl = originRequest.url().newBuilder()
                .addQueryParameter("accessToken", MyApp.user().token)
                .addQueryParameter("accessSecret", MyApp.user().secrete)
                .build()
        val newRequest: Request = originRequest.newBuilder().url(modifiedUrl).build()
        return chain.proceed(newRequest)
    }
}