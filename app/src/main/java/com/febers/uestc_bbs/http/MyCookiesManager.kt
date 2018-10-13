/*
 * Created by Febers 2018.
 * Copyright (c). All rights reserved.
 *
 * Last Modified 18-6-4 下午10:04
 *
 */

package com.febers.uestc_bbs.http

import android.util.Log.i
import android.webkit.CookieManager
import com.febers.uestc_bbs.MyApplication

import java.util.ArrayList

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

class MyCookiesManager : CookieJar {

    private val cookieStore = PersistentCookieStore(MyApplication.context())
    private val mCookieManager = CookieManager.getInstance()    //WebView

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (cookies.isNotEmpty()) {
            for (item in cookies) {
                cookieStore.add(url, item)
                mCookieManager.setCookie(url.toString(), item.toString())
                i("BA", item.toString() + " " + url.toString())
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        var cookies: MutableList<Cookie> = cookieStore.get(url)
        val cookiesStr = mCookieManager.getCookie(url.toString())

//        if (cookiesStr != null && !cookiesStr.isEmpty()) {
//            val cookieHeaders = cookiesStr.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
//            cookies = ArrayList(cookieHeaders.size)
//            for (header in cookieHeaders) {
//                cookies.add(Cookie.parse(url, header)!!)
//            }
//        }
        return cookies
    }
}
