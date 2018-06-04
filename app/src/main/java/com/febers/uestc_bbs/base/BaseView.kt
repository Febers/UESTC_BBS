package com.febers.uestc_bbs.base

import com.febers.uestc_bbs.view.MyProgressDialog

/**
 * View公共接口
 */
interface BaseView {
    fun onCompleted(any: Any)
    fun onError(error: String)
    fun showProgressDialog(title: String)
    fun dismissProgressDialog()
}