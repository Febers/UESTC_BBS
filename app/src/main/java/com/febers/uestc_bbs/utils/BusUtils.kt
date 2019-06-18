package com.febers.uestc_bbs.utils

import org.greenrobot.eventbus.EventBus

fun defaultBus(): EventBus = EventBus.getDefault()

fun postEvent(event: Any?) {
    defaultBus().post(event)
}