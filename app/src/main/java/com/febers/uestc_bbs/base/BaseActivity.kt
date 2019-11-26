package com.febers.uestc_bbs.base

import android.os.Build
import android.os.Bundle
import android.view.*
import org.greenrobot.eventbus.EventBus
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.exception.ExceptionHandler
import com.febers.uestc_bbs.base.mvp.BaseView
import com.febers.uestc_bbs.utils.HintUtils
import com.febers.uestc_bbs.lib.fragmentation.SupportActivity
import com.febers.uestc_bbs.module.theme.ThemeManager
import com.febers.uestc_bbs.utils.PreferenceUtils
import com.febers.uestc_bbs.utils.hideStatusBar
import me.yokeyword.fragmentation.SwipeBackLayout


/**
 * 抽象Activity
 *
 */
abstract class BaseActivity : SupportActivity(), BaseView {

    protected val contentView: Int
        get() = setView()

    protected val ctx: BaseActivity
        get() = this@BaseActivity

    protected open fun setMenu(): Int? = null

    protected open fun setToolbar(): Toolbar? = null

    protected open fun setTitle(): String? = null

    protected open fun enableHideStatusBar(): Boolean = false

    protected open fun registerEventBus() = false

    protected abstract fun setView(): Int

    protected abstract fun initView()

    protected open fun afterCreated(){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentView)
        ActivityMgr.putActivity(ctx)
        Thread.currentThread().uncaughtExceptionHandler = ExceptionHandler.getInstance()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ThemeManager.isNightTheme()) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            } else {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        if (enableHideStatusBar()) {
            hideStatusBar()
        }
        initSwipeBack()
        setSupportActionBar(setToolbar())
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
        setTitle()?.apply {
            setToolbar()?.title = this
        }
        if (setMenu() != null) {
            setToolbar()!!.inflateMenu(setMenu()!!)
        }
        initView()
    }

    private fun initSwipeBack() {
        val swipeBackRange by PreferenceUtils(ctx, SP_SWIPE_BACK_RANGE_VALUE, SWIPE_BACK_MED)
        when(swipeBackRange) {
            0 -> {
                setSwipeBackEnable(false)
            }
            1 -> {
                setSwipeBackEnable(true)
                setEdgeLevel(SwipeBackLayout.EdgeLevel.MIN)
            }
            2 -> {
                setSwipeBackEnable(true)
                setEdgeLevel(SwipeBackLayout.EdgeLevel.MED)
            }
            3 -> {
                setSwipeBackEnable(true)
                setEdgeLevel(SwipeBackLayout.EdgeLevel.MAX)
            }
        }
    }

    override fun onStart() {
        if (registerEventBus()) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
        }
        super.onStart()
    }

    override fun onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onStop()
    }

    protected open fun getEmptyViewForRecyclerView(recyclerView: RecyclerView): View =
            LayoutInflater
                    .from(ctx)
                    .inflate(R.layout.layout_server_null, recyclerView.parent as ViewGroup, false)

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
            HintUtils.show(ctx, msg)
        }
    }

    override fun showError(msg: String) {
        showHint(msg)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (setMenu() != null) {
            menuInflater.inflate(setMenu()!!, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
            else -> { }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRestart() {
        super.onRestart()
        MyApp.uiHidden = false
    }

    override fun onDestroy() {
        super.onDestroy()
        ActivityMgr.removeActivity(ctx)
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
