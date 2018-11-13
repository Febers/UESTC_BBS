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