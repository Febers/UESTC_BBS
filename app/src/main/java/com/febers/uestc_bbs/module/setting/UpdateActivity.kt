package com.febers.uestc_bbs.module.setting

import android.view.KeyEvent
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity



class UpdateActivity: BaseActivity() {

    override fun enableThemeHelper(): Boolean = false

    override fun setView(): Int = R.layout.activity_update

    override fun initView() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}