package com.febers.uestc_bbs.module.more

import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.utils.RestartUtils
import kotlinx.android.synthetic.main.activity_exception.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*

class ExceptionActivity: BaseActivity() {

    override fun setView(): Int = R.layout.activity_exception
    override fun setToolbar(): Toolbar? = toolbar_common
    override fun setTitle(): String? = "哎呀，应用出错啦！"

    override fun initView() {
        val thread: String = intent.getStringExtra("thread")
        val throwable: String = intent.getStringExtra("throwable")
        val errorInfo = """
            异常线程：$thread
            异常信息: $throwable
        """
        tv_exception_detail.text = errorInfo
        btn_restart_app.setOnClickListener {
            RestartUtils.restartApp2()
        }
    }
}