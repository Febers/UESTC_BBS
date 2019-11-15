package com.febers.uestc_bbs.http.client

import android.webkit.CookieManager
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import okhttp3.Cookie
import okhttp3.HttpUrl
import com.febers.uestc_bbs.MyApp
import okhttp3.CookieJar



object WebViewClient {

    fun get(): OkHttpClient =
            OkHttpClient.Builder()
                    .connectTimeout(18, TimeUnit.SECONDS)
                    .readTimeout(18, TimeUnit.SECONDS)
                    .cookieJar(MyCookiesManager())
                    .build()
}

class MyCookiesManager : CookieJar {

    private val cookieStore = PersistentCookieStore(MyApp.context())
    private val cookieManager = CookieManager.getInstance()

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (cookies.isNotEmpty()) {
            for (item in cookies) {
                cookieStore.add(url, item)
                cookieManager.setCookie(url.toString(), item.toString())
                //                String cookieStr = item.name() + item.value()+";domain="+item.domain();
                //                cookieManager.setCookie(url.toString(), cookieStr); //为webview添加
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        var cookies = cookieStore.get(url)
        val cookiesStr = cookieManager.getCookie(url.toString())
        if (cookiesStr != null && cookiesStr!!.isNotEmpty()) {
            val cookieHeaders = cookiesStr!!.split(";".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
            cookies = ArrayList(cookieHeaders.size)
            for (header in cookieHeaders) {
                cookies.add(Cookie.parse(url, header))
            }
        }
        return cookies
    }
}