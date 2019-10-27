package com.febers.uestc_bbs.module.setting.push

import com.febers.uestc_bbs.base.ThreadPoolMgr
import com.febers.uestc_bbs.entity.PushMessageBean
import com.febers.uestc_bbs.utils.ApiUtils
import com.febers.uestc_bbs.utils.log
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.util.concurrent.TimeUnit

object PushManager {

    fun getHttpMessages(listener: PushMessageListener) {
        ThreadPoolMgr.execute(Runnable {
            val request = Request.Builder().url(ApiUtils.GITHUB_PUSH_MESSAGE_RAW).build()
            OkHttpClient.Builder().callTimeout(10, TimeUnit.SECONDS)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .build()
                    .newCall(request)
                    .enqueue(object : Callback {
                        override fun onFailure(call: Call?, e: IOException) {
                            listener.fail("同步在线推送消息失败，原因: ${e.message}")
                        }

                        override fun onResponse(call: Call, response: Response) {
                            val json = response.body()?.string()
                            if (json.isNullOrEmpty()) {
                                onFailure(null, IOException("消息为空！"))
                                return
                            }
                            try {
                                listener.success(Gson().fromJson(json, PushMessageBean::class.java))
                                saveMessagesJson(json)
                            } catch (e: Exception) {
                                onFailure(null, IOException(e.message))
                                e.printStackTrace()
                            }
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