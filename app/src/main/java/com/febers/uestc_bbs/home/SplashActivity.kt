package com.febers.uestc_bbs.home


import android.Manifest
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
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
        if (intent.getBooleanExtra(RESTART_APP, false)) {
            ActivityMgr.removeAllActivitiesExceptOne(mContext)
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
                }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)

    }

    private fun start() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            image_view_splash.drawable.setTint(ThemeHelper.getColorPrimaryBySp())
            text_view_splash.setTextColor(ThemeHelper.getColorPrimaryBySp())
        }

        val scaleX = ObjectAnimator.ofFloat(image_view_splash, "scaleX", 0.8f, 1.1f)
        val scaleY = ObjectAnimator.ofFloat(image_view_splash, "scaleY", 0.8f, 1.1f)
        scaleX.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) { }
            override fun onAnimationCancel(animation: Animator?) { }
            override fun onAnimationStart(animation: Animator?) { }
            override fun onAnimationEnd(animation: Animator?) {
                startActivity()
            }
        })
        AnimatorSet().apply {
            duration = 900
            play(scaleX)
            play(scaleY)
            start()
        }
    }

    private fun startActivity() {
        startActivity(Intent(mContext,
                if (MyApp.homeLayout() == HOME_VIEW_STYLE_BOTTOM) HomeActivity::class.java
                else HomeActivity2::class.java))
        overridePendingTransition(0, 0)
        finish()
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

