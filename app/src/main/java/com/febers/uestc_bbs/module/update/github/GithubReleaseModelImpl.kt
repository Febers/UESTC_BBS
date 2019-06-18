package com.febers.uestc_bbs.module.update.github

import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.entity.GithubReleaseBean
import com.febers.uestc_bbs.utils.ApiUtils
import com.febers.uestc_bbs.utils.log
import com.febers.uestc_bbs.utils.postEvent
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubReleaseModelImpl {

    fun get() {
        Thread{ doGet() }.start()
    }

    private fun doGet() {
        Retrofit.Builder()
                .baseUrl(ApiUtils.GIHUB_BASE_API)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(GithubUpdateInterface::class.java)
                .latestRelease()
                .enqueue(object : Callback<GithubReleaseBean> {
                    override fun onFailure(call: Call<GithubReleaseBean>, t: Throwable) {
                        log("github release err: $t")
                    }

                    override fun onResponse(call: Call<GithubReleaseBean>, response: Response<GithubReleaseBean>) {
                        val release = response.body()
                        if (response.code() != 200 || release == null) {
                            postEvent(BaseEvent(BaseCode.FAILURE, release))
                            return
                        }
                        if (hasNewVersion(release.tag_name)) {
                            postEvent(BaseEvent<GithubReleaseBean>(BaseCode.SUCCESS, release))
                        } else {
                            EventBus.getDefault().post(BaseEvent<GithubReleaseBean>(BaseCode.LOCAL, release))
                            //postEvent(BaseEvent(BaseCode.LOCAL, release as GithubReleaseBean?))
                        }
                    }
                })
    }

    private fun hasNewVersion(rawResultVersion: String?): Boolean {
        rawResultVersion ?: return false

        val rawNowVersion = MyApp.context().getString(R.string.version_value)

        val resultList = rawResultVersion.toCharArray().filter { it.isDigit() }
        val nowList = rawNowVersion.toCharArray().filter { it.isDigit() }
        var hasNewVersion = false
        for (i in resultList.indices) {
            if (i < nowList.size) {
                if (resultList[i] > nowList[i]) {
                    hasNewVersion = true
                    break
                }
            }
        }
        return hasNewVersion
    }
}