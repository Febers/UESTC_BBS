package com.febers.uestc_bbs.home


import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Handler
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.base.HOME_VIEW_STYLE_BOTTOM
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    private lateinit var permissionUtils: PermissionUtils

    override fun enableThemeHelper(): Boolean = false

    override fun setView(): Int {
        return R.layout.activity_splash
    }

    override fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image_view_splash.drawable.setTint(ThemeHelper.getColorPrimaryBySp())
            text_view_splash.setTextColor(ThemeHelper.getColorPrimaryBySp())
        }
        permissionUtils = PermissionUtils(this)
        permissionUtils
                .requestPermissions("请授予应用正常运行所需要的存储权限", object : PermissionUtils.PermissionListener {
                    override fun doAfterGrand(vararg permission: String?) {
                        start()
                    }
                    override fun doAfterDenied(vararg permission: String?) {
                        showHint("你拒绝的相应的权限,将无法正常使用应用")
                        start()
                    }
                }, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    }

    private fun start() {
        Handler().postDelayed({ startActivity(Intent(this@SplashActivity,
                if (MyApp.homeStyle() == HOME_VIEW_STYLE_BOTTOM) HomeActivity::class.java
                else HomeActivity2::class.java))
            overridePendingTransition(0, 0)
            finish()
        }, 800)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        permissionUtils.handleRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}

/////////////////////////////////////////////////////////////////////////////

//	                               _(\_/)
//	                             ,((((^`\
//	                             ((((  (6 \
//	                          ,((((( ,    \
//	       ,,,_              ,(((((  /"._  ,`,
//	      ((((\\ ,...       ,((((   /    `-.-'
//	      )))  ;'    `"'"'""((((   (我是代马
//	    (((  /            (((      \
//	      )) |                      |
//	     ((  |        .       '     |
//	     ))  \     _ '      `t   ,.')
//	     (   |   y;- -,-""'"-.\   \/
//	    )   / ./  ) /         `\  \
//	        |./   ( (           / /'
//	        ||     \\          //'|
//	        ||      \\       _//'||
//	        ||       ))     |_/  ||
//	        \_\     |_/          ||
//	        `'"                  \_\
//	                             `'"

/////////////////////////////////////////////////////////////////////////////

