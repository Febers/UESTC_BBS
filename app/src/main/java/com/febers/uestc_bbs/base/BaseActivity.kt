package com.febers.uestc_bbs.base

import android.os.Bundle
import android.view.MenuItem
import org.jetbrains.anko.toast
import org.greenrobot.eventbus.EventBus
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.utils.KeyboardUtils
import com.febers.uestc_bbs.utils.ToastUtils
import com.febers.uestc_bbs.view.custom.SupportActivity
import com.febers.uestc_bbs.view.helper.hideStatusBar


/**
 * 抽象Activity
 */
abstract class BaseActivity : SupportActivity(), BaseView {

    protected val contentView: Int
        get() = setView()

    protected open fun setMenu(): Int? = null

    protected open fun setToolbar(): Toolbar? = null

    protected open fun enableHideStatusBar(): Boolean = true

    protected open fun registerEventBus() = false

    protected abstract fun setView(): Int

    protected abstract fun initView()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
        if (!enableThemeHelper() && enableHideStatusBar()) {
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

    override fun showToast(msg: String) {
        runOnUiThread {
            ToastUtils.show(msg)
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
