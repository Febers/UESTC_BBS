package com.febers.uestc_bbs.module.setting.push

import com.febers.uestc_bbs.base.ThreadPoolMgr
import com.febers.uestc_bbs.entity.PushMessageBean
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

class PushManager {

    fun getHttpMessages(listener: PushMessageListener) {
        ThreadPoolMgr.execute(Runnable {
            val request = Request.Builder().url("https://raw.githubusercontent.com/Febers/UESTC_BBS/master/build.gradle").build()
            OkHttpClient.Builder().callTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build()
                    .newCall(request)
                    .enqueue(object : Callback {
                        override fun onFailure(call: Call, e: IOException) {
                            listener.fail("同步在线公告失败，你可以访问网站直接查看: ${e.message}")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val json = response.body().toString()
                            listener.success(Gson().fromJson(json, PushMessageBean::class.java))
                            saveMessagesJson(json)
                        }
                    })

        })

    }

    fun getLocalMessages(): PushMessageBean {
        return PushMessageBean()
    }

    fun saveMessagesJson(json: String) {

    }
}