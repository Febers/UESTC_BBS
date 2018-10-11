/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.base

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.annotation.MainThread
import android.view.MenuItem
import android.view.View
import org.jetbrains.anko.toast
import com.febers.uestc_bbs.utils.ThemeUtils
import org.greenrobot.eventbus.EventBus
import android.view.WindowManager
import android.os.Build
import android.os.Looper
import android.os.PersistableBundle
import android.support.annotation.UiThread
import android.util.AttributeSet
import android.util.Log.i
import com.febers.uestc_bbs.view.custom.SupportActivity


/**
 * 抽象Activity
 */
abstract class BaseActivity : SupportActivity(), BaseView {

    protected val contentView: Int
        get() = setView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (hideStatusBar()) {
            //参考 https://www.jianshu.com/p/648176c8b67e
            val window = window
            //透明状态栏，分为sdk>21、21>sdk>19情况设置,sdk<19不支持
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val decorView = getWindow().decorView
                val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                decorView.systemUiVisibility = option
                window.statusBarColor = Color.TRANSPARENT
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // Translucent status bar
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            }
        }
        setTheme(ThemeUtils.getTheme())
        setContentView(contentView)
        if (registerEventBus()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
        }
        initView()
    }

    protected open fun hideStatusBar() = false

    protected abstract fun setView(): Int

    protected abstract fun initView()

    protected open fun registerEventBus() = false

    override fun showToast(msg: String) {
        runOnUiThread {
            toast(msg)
        }
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
}
