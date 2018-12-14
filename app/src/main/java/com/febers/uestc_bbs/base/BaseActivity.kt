package com.febers.uestc_bbs.base

import android.os.Bundle
import android.view.MenuItem
import org.greenrobot.eventbus.EventBus
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.utils.HintUtils
import com.febers.uestc_bbs.utils.log
import com.febers.uestc_bbs.view.custom.SupportActivity
import com.febers.uestc_bbs.view.helper.hideStatusBar


/**
 * 抽象Activity
 *
 */
abstract class BaseActivity : SupportActivity(), BaseView {

    protected val contentView: Int
        get() = setView()

    protected val context = this@BaseActivity

    protected open fun setMenu(): Int? = null

    protected open fun setToolbar(): Toolbar? = null

    protected open fun enableHideStatusBar(): Boolean = true

    protected open fun registerEventBus() = false

    protected abstract fun setView(): Int

    protected abstract fun initView()

    protected open fun afterCreated(){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
        if (!enableThemeHelper() && enableHideStatusBar()) {
            hideStatusBar()
        }
        setSupportActionBar(setToolbar())
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
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

    private var isInitAllView = false

    /**
     * 如果在Activity的启动过程中处理太多事务，将造成
     * 启动的迟滞，该方法在Activity启动、后台隐藏之后回调
     *
     */
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!isInitAllView && hasFocus) {
            afterCreated()
            isInitAllView = true
        }
    }

    override fun showHint(msg: String) {
        runOnUiThread {
            HintUtils.show(this@BaseActivity, msg)
        }
    }

    override fun showError(msg: String) {
        showHint(msg)
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

/*
 *
 * _ooOoo_
 * o8888888o
 * 88" . "88
 * (| -_- |)
 *  O\ = /O
 * ___/`---'\____
 * .   ' \\| |// `.
 * / \\||| : |||// \
 * / _||||| -:- |||||- \
 * | | \\\ - /// | |
 * | \_| ''\---/'' | |
 * \ .-\__ `-` ___/-. /
 * ___`. .' /--.--\ `. . __
 * ."" '< `.___\_<|>_/___.' >'"".
 * | | : `- \`.;`\ _ /`;.`/ - ` : | |
 * \ \ `-. \_ __\ /__ _/ .-` / /
 * ======`-.____`-.___\_____/___.-`____.-'======
 * `=---='
 *          .............................................
 *           佛曰：bug泛滥，我已瘫痪！
 */
