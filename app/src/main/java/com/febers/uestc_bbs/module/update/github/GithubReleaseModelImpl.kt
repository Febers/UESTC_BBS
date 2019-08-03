package com.febers.uestc_bbs.module.update.github

import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseCode
import com.febers.uestc_bbs.base.BaseEvent
import com.febers.uestc_bbs.base.ThreadPoolMgr
import com.febers.uestc_bbs.base.UpdateCheckEvent
import com.febers.uestc_bbs.entity.GithubReleaseBean
import com.febers.uestc_bbs.entity.UpdateCheckBean
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

    fun get(manual: Boolean) {
        ThreadPoolMgr.execute(Runnable { doGet(manual) })
    }

    private fun doGet(manual: Boolean) {
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
                            postEvent(UpdateCheckEvent(BaseCode.FAILURE, false))
                            return
                        }
                        if (hasNewVersion(release.tag_name)) {
                            //奇怪的逻辑，手动检查更新时使用SUCCESS_END， 因为此时主 Activity 不应该收到该消息
                            postEvent(UpdateCheckEvent(if (manual) BaseCode.SUCCESS_END else BaseCode.SUCCESS, true, release))
                        } else {
                            postEvent(UpdateCheckEvent(BaseCode.LOCAL, false))
                        }
                    }
                })
    }

    /**
     * 判断是否存在新版本
     * 本地获取的版本字符串为 v1.1.4形式
     * 而通过 github 获得的为 tag_name 的值，注意发布新版本时该值的规范性
     *
     * @param rawResultVersion tag_name 的值
     */
    private fun hasNewVersion(rawResultVersion: String?): Boolean {
        rawResultVersion ?: return false

        val rawNowVersion = MyApp.context().getString(R.string.version_value)
        val resultList = rawResultVersion.toCharArray().filter { it.isDigit() }
        val nowList = rawNowVersion.toCharArray().filter { it.isDigit() }
        var hasNewVersion = false
        for (i in resultList.indices) {
            if (i < nowList.size) {     //逐位比较
                if (resultList[i] > nowList[i]) {
                    hasNewVersion = true
                    break
                } else if (resultList[i] < nowList[i]){
                    hasNewVersion = false
                    break
                }
            } else {    //1.1.4.1 与 1.1.4 的情况
                if (resultList[i] > '0') {
                    hasNewVersion = true
                }
            }
        }
        return hasNewVersion
    }
}