/*
 * Created by Febers at 18-8-14 上午4:04.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 上午4:04.
 */

package com.febers.uestc_bbs.base

import com.febers.uestc_bbs.entity.UserBean

open class BaseEvent<T>(var code: BaseCode, var data: T)
enum class BaseCode {
    SUCCESS, SUCCESS_END, FAILURE, LOCAL, UPDATE, TIME_OUT
}