package com.febers.uestc_bbs.module.update.coolapk

import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.UpdateCheckBean
import com.febers.uestc_bbs.module.update.coolapk.CoolApkHtmlResolver
import com.febers.uestc_bbs.utils.ApiUtils
import com.febers.uestc_bbs.utils.postEvent
import okhttp3.*
import java.io.IOException

class CoolApkUpdateHelper {

    fun checkByCoolApk() {
        Thread{ doCheck() }.start()
    }

    private fun doCheck() {
        val client = OkHttpClient()
        val request: Request = Request.Builder().url(ApiUtils.COOLAPK_URL).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //获取酷安网页失败
            }

            override fun onResponse(call: Call, response: Response) {
                val source = response.body()
                if (source == null) {
                    postEvent(BaseEvent(BaseCode.FAILURE, UpdateCheckBean(false)))
                    return
                }
                val resolveResult = CoolApkHtmlResolver().resolve(source.string())
                if (resolveResult == null) {
                    postEvent(BaseEvent(BaseCode.SUCCESS, UpdateCheckBean(false)))
                } else {
                    postEvent(BaseEvent(BaseCode.SUCCESS, UpdateCheckBean(hasNewerVersion = true,
                            newVersionCode = resolveResult.newVersionCode,
                            newVersionDes = resolveResult.newVersionDes,
                            newVersionDownloadUrl = resolveResult.newVersionDownloadUrl)))
                }
            }
        })
    }
}

