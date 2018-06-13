/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.base

import android.os.Bundle
import android.support.annotation.MainThread
import android.support.v7.app.AppCompatActivity
import com.febers.uestc_bbs.view.CustomProgressDialog
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition
import org.jetbrains.anko.toast

/**
 * 抽象Activity
 */
abstract class BaseActivity : AppCompatActivity(), BaseView {

    protected var mProgressDialog: CustomProgressDialog? = null

    protected val contentView: Int
        get() = setView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
        if (isSlideBack()) {
            val slidrConfig = SlidrConfig.Builder()
                    .position(SlidrPosition.LEFT)
                    .build()
            Slidr.attach(this, slidrConfig)
        }
        initView()
    }

    protected abstract fun setView(): Int

    protected abstract fun initView()

    //必须加open关键字，否则无法重写该方法
    open protected fun isSlideBack(): Boolean {
        return false
    }

    override fun showProgressDialog(title: String) {
        if (mProgressDialog == null) {
            mProgressDialog = CustomProgressDialog(this)
        }
        mProgressDialog!!.show()
    }

    override fun dismissProgressDialog() {
        if (mProgressDialog == null) {
            return
        }
        mProgressDialog!!.dismiss()
    }

    @MainThread
    override fun onError(error: String) {
        toast(error)
    }
}
