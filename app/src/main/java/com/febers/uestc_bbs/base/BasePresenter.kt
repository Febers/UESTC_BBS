/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.base

abstract class BasePresenter<V : BaseView>(protected var mView: V?) {

    open fun detachView() {
        mView = null
    }

    fun errorResult(error: String) {
        mView?.showError(error)
    }
}
