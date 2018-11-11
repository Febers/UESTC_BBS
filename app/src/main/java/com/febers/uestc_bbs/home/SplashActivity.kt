package com.febers.uestc_bbs.home


import android.Manifest
import android.content.Intent
import android.os.Handler
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.module.setting.BuglyHelper
import com.febers.uestc_bbs.utils.PermissionUtils

class SplashActivity : BaseActivity() {

    override fun enableThemeHelper(): Boolean = false

    override fun setView(): Int {
        return R.layout.activity_splash
    }

    override fun initView() {
        PermissionUtils(this)
                .requestPermissions("请授予应用正常运行所需要的存储权限", object : PermissionUtils.PermissionListener {
                    override fun doAfterGrand(vararg permission: String?) {

                    }
                    override fun doAfterDenied(vararg permission: String?) {
                        showToast("你拒绝的相应的权限,将无法正常使用应用")
                    }
                }, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

        Handler().postDelayed({
            startActivity(Intent(this, HomeActivity::class.java))
            overridePendingTransition(0, 0)
            finish()
        }, 800)
        Thread{
            BuglyHelper().init()
        }.start()
    }
}

