package com.febers.uestc_bbs.module.more

import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import kotlinx.android.synthetic.main.activity_debug.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*

class DebugActivity: BaseActivity() {

    override fun setView(): Int = R.layout.activity_debug

    override fun setToolbar(): Toolbar? = toolbar_common

    override fun setTitle(): String? = "Debug"

    override fun initView() {
        val content = intent.getStringExtra("debug")
        tv_debug_0.text = content
    }
}