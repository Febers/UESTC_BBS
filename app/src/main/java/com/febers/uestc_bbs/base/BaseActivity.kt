/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.base

import android.os.Bundle
import android.view.MenuItem
import org.jetbrains.anko.toast
import org.greenrobot.eventbus.EventBus
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.view.custom.SupportActivity
import com.febers.uestc_bbs.view.helper.hideStatusBar


/**
 * 抽象Activity
 */
abstract class BaseActivity : SupportActivity(), BaseView {

    protected val contentView: Int
        get() = setView()

    protected open fun setToolbar(): Toolbar? = null

    protected open fun setMenu(): Int? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
        if (!enableThemeHelper()) {
            hideStatusBar()
        }
        setSupportActionBar(setToolbar())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (setMenu() != null) {
            setToolbar()!!.inflateMenu(setMenu()!!)
        }
        if (registerEventBus()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
        }
        initView()
    }

    protected abstract fun setView(): Int

    protected abstract fun initView()

    protected open fun registerEventBus() = false

    override fun showToast(msg: String) {
        runOnUiThread {
            toast(msg)
        }
    }

    override fun showError(msg: String) {
        showToast(msg)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (setMenu()!=null) {
            menuInflater.inflate(setMenu()!!, menu)
        }
        return true
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

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onRestart() {
        super.onRestart()
        MyApp.uiHidden = false
    }
}
