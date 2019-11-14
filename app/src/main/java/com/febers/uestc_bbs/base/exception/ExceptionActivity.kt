package com.febers.uestc_bbs.base.exception

import android.os.Build
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.utils.RestartUtils
import kotlinx.android.synthetic.main.activity_exception.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*
import org.jetbrains.anko.email

class ExceptionActivity: BaseActivity() {

    override fun setView(): Int = R.layout.activity_exception
    override fun setToolbar(): Toolbar? = toolbar_common
    override fun setTitle(): String? = "哎呀，应用出错啦！"

    override fun initView() {
        setSwipeBackEnable(false)
        val thread: String = intent.getStringExtra("thread")
        val throwable: String = intent.getStringExtra("throwable")
        val errorInfo  = StringBuilder()
        errorInfo.append("设备: ${Build.MANUFACTURER} ${Build.MODEL} \n")
        errorInfo.append("系统: Android ${Build.VERSION.RELEASE} \n")
        errorInfo.append("线程: $thread \n")
        errorInfo.append("堆栈: $throwable \n")
        tv_exception_detail.text = errorInfo.toString()
        btn_restart_app.setOnClickListener {
            RestartUtils.restartApp2()
        }
        btn_send_exception.setOnClickListener {
            email(getString(R.string.developer_email), "i河畔异常信息反馈", errorInfo.toString())
        }
    }
}