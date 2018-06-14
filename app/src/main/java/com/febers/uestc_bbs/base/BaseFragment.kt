/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.base

import android.os.Bundle
import android.support.annotation.MainThread
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febers.uestc_bbs.view.CustomProgressDialog
import org.jetbrains.anko.toast

abstract class BaseFragment: Fragment(), BaseView {

    private var isInit = false

    private var isLoad = true

    protected var mProgressDialog: CustomProgressDialog? = null

    private var contentView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = inflater.inflate(setContentView(), container, false)
        isInit = true
        isCanLoadData()
        return contentView
    }

    //在kotlin中， 直接使用xml id对控件进行操作，必须要在这个方法回调之后
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isCanLoadData()
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private fun isCanLoadData() {
        if (!isInit) {
            return
        }

        if (userVisibleHint) {
            lazyLoad()
            isLoad = true
        } else {
            if (isLoad) {
                stopLoad()
            }
        }
    }


    /**
     * 设置Fragment要显示的布局
     *
     * @return 布局的layoutId
     */
    protected abstract fun setContentView(): Int

    protected fun getContentView(): View {
        return contentView!!
    }

    protected fun <T: View> findViewById(id: Int): T {
        return  getContentView().findViewById(id) as T
    }

    protected abstract fun initView()
    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract fun lazyLoad()

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected fun stopLoad() {}

    override fun showProgressDialog(title: String) {
        if (mProgressDialog == null) {
            mProgressDialog = CustomProgressDialog(context!!)
        }
        mProgressDialog!!.show()
    }

    override fun dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }

    @MainThread
    override fun onError(error: String) {
        context?.toast(error)
    }
}