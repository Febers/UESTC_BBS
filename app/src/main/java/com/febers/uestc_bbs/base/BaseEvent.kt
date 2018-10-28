/*
 * Created by Febers at 18-8-14 上午4:04.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 上午4:04.
 */

package com.febers.uestc_bbs.base

import android.view.View
import android.widget.EditText


open class BaseEvent<T>(var code: BaseCode, var data: T)

enum class BaseCode {
    SUCCESS, SUCCESS_END, FAILURE, LOCAL, UPDATE, TIME_OUT
}

data class MsgEvent(var code: BaseCode, var count: Int)

data class TabReselectedEvent(var code: BaseCode, var position: Int)

data class MsgFeedbackEvent(var code: BaseCode, var type: String)
