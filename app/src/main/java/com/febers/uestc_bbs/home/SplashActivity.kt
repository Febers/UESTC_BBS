package com.febers.uestc_bbs.home


import android.content.Intent
import android.os.Handler
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity

class SplashActivity : BaseActivity() {

    override fun enableThemeHelper(): Boolean = false

    override fun setView(): Int {
        return R.layout.activity_splash
    }

    override fun initView() {
        Handler().postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }, 800)
    }
}

