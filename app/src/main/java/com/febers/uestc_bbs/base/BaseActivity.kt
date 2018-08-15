/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.base

import android.os.Bundle
import android.support.annotation.MainThread
import android.support.v7.app.AppCompatActivity
import android.util.Log.i
import android.view.MenuItem
import android.view.View
import org.jetbrains.anko.toast

/**
 * 抽象Activity
 */
abstract class BaseActivity : AppCompatActivity(), BaseView {

    protected val contentView: Int
        get() = setView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
        initView()
    }

    protected abstract fun setView(): Int

    protected abstract fun initView()

    @MainThread
    override fun onError(error: String) {
        toast(error)
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                finish()
            }
            else -> {}
        }
        return super.onOptionsItemSelected(item)
    }
}
