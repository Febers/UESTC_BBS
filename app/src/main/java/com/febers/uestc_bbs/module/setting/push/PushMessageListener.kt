package com.febers.uestc_bbs.module.setting.push

import androidx.annotation.UiThread
import com.febers.uestc_bbs.entity.PushMessageBean

interface PushMessageListener {
    @UiThread
    fun success(message: PushMessageBean)

    @UiThread
    fun fail(message: String)
}