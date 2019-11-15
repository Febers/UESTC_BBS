package com.febers.uestc_bbs.module.login.presenter

import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.http.client.WebViewClient
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.utils.log
import okhttp3.*
import java.io.IOException

class LoginMockWebView {

    fun login(userName: String, userPw: String) {

        val httpClient = WebViewClient.get()
        /**
         * formhash:41f37e1c
        referer:http://bbs.uestc.edu.cn/?mobile=yes
        fastloginfield:username
        cookietime:2592000
        username:四条眉毛
        password:hzg2016030301007
        questionid:0
        answer:
        submit:登录
         */
        val hashArray = try {
            getHashArray()
        } catch (e: Exception) {
            emptyArray<String>()
        }
        if (hashArray.isEmpty()) {
            return
        }
        val loginHash = hashArray[0]
        val formHash = hashArray[1]
        val body = FormBody.Builder()
                .add("username", userName)
                .add("password", userPw)
                .add("referer", "http://bbs.uestc.edu.cn/?mobile=yes")
                .add("formhash", formHash)
                .add("fastloginfield", "username")
                .add("cookietime", "2592000")
                .add("submit", "登录")
                .add("questionid", "0")
                .add("answer", "")
                .build()
        val request = Request.Builder()
                .url("http://bbs.uestc.edu.cn/member.php?mod=logging&action=login&loginsubmit=yes&loginhash=$loginHash&mobile=yes")
                .post(body)
                .build()
        httpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                log { "模拟登录出错：$e" }
            }

            override fun onResponse(call: Call, response: Response) {
                val content = response.body()?.string() + ""
                ClickContext.clickToDebug(MyApp.context(), content)
            }
        })
    }

    private fun getHashArray(): Array<String> {
        val request = Request.Builder()
                .url("http://bbs.uestc.edu.cn/member.php?mod=logging&action=login&mobile=yes")
                .build()

        //第一次登录
        var response = WebViewClient.get().newCall(request).execute()
        if (response.body() == null) {
            return emptyArray()
        }
        var source = response.body()!!.string()
        if (source.contains("欢迎")) {
            log { "已经登录过WebView了，需要先注销" }
            logout(source)
        }

        //第二次登录
        response = WebViewClient.get().newCall(request).execute()
        if (response.body() == null) {
            return emptyArray()
        }
        source = response.body()!!.string()
        if (source.contains("欢迎")) {
            log { "异常链路：注销失败" }
            return emptyArray()
        }

        var pre = source.indexOf("id=\"formhash\" value='")+21
        var end = source.indexOf("/>", pre)+2
        val formHash = source.substring(pre, end)

        pre = source.indexOf("loginhash=")+10
        end = source.indexOf("&amp", pre)+4
        val loginHash = source.substring(pre, end)
        if (loginHash.length > 16 || formHash.length > 16) {
            log { "异常链路：hash值过长" }
        }

        log { "loginHash: ${loginHash}, formHash: $formHash" }
        return arrayOf(loginHash, formHash)
    }

    private fun logout(source: String)  {
        val pre = source.indexOf("formhash=")+9
        val end = source.indexOf("&amp")+4
        val formHash = try {
            source.substring(pre, end)
        } catch (e: Exception) {
            ""
        }

        val request = Request.Builder()
                .url("http://bbs.uestc.edu.cn/member.php?mod=logging&action=logout&formhash=$formHash&mobile=yes")
                .build()
        WebViewClient.get().newCall(request).execute()
        log { "注销完毕" }
    }
}