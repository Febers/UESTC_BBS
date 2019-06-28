package com.febers.uestc_bbs.module.update.github

object GithubUpdateHelper {
    fun check(manual: Boolean) {
        GithubReleaseModelImpl().get(manual)
    }
}