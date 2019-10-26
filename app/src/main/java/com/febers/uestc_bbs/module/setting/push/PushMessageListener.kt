package com.febers.uestc_bbs.module.setting.push

import com.febers.uestc_bbs.entity.PushMessageBean

interface PushMessageListener {
    fun success(message: PushMessageBean)
    fun fail(message: String)
}