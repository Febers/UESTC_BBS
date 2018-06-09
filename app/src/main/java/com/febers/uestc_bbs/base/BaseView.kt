/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.base

/**
 * View公共接口
 */
interface BaseView {
    fun onCompleted(any: Any)
    fun onError(error: String)
    fun showProgressDialog(title: String)
    fun dismissProgressDialog()
}