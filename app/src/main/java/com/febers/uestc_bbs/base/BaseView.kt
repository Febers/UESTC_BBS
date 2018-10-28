/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.base

import androidx.annotation.UiThread

/**
 * View公共接口
 */
interface BaseView {

    @UiThread
    fun showToast(msg: String)

    @UiThread
    fun showError(msg: String){}
}