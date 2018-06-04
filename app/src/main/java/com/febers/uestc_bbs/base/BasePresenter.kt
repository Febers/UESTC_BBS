package com.febers.uestc_bbs.base

abstract class BasePresenter<V : BaseView>(protected var mView: V?) {

    fun detachView() {
        mView = null
    }
}

/*
 java 代码如下
 public abstract class BasePresenter<V extends BaseView> {

    protected V mView;

    public BasePresenter(V view) {
        mView = view;
    }

    public void detachView() {
        mView = null;
    }
}
 */
