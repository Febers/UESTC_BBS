package com.febers.uestc_bbs.base

abstract class BasePresenter<V : BaseView>(protected var mView: V?) {

    open fun detachView() {
        mView = null
    }

    fun errorResult(error: String) {
        mView?.showError(error)
    }
}
