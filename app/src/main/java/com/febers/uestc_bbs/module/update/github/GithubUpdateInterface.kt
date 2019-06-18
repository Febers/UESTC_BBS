package com.febers.uestc_bbs.module.update.github

import com.febers.uestc_bbs.entity.GithubReleaseBean
import com.febers.uestc_bbs.utils.ApiUtils
import retrofit2.Call
import retrofit2.http.GET

interface GithubUpdateInterface {

    @GET(ApiUtils.GITHUB_RELEASE_LATEST)
    fun latestRelease(): Call<GithubReleaseBean>
}