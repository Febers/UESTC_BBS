package com.febers.uestc_bbs.module.update

import com.febers.uestc_bbs.module.update.github.GithubUpdateHelper

object UpdateHelper {
    fun check(manual: Boolean = false) {
        GithubUpdateHelper.check(manual)
    }
}