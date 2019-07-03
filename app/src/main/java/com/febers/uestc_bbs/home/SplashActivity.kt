package com.febers.uestc_bbs.home


import android.Manifest
import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.module.search.view.SearchActivity
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.module.update.UpdateHelper
import com.febers.uestc_bbs.utils.PermissionUtils
import com.febers.uestc_bbs.utils.PreferenceUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : BaseActivity() {

    private lateinit var permissionUtils: PermissionUtils

    override fun enableThemeHelper(): Boolean = false

    override fun setView(): Int = R.layout.activity_splash

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
            val colorPrimary = ThemeHelper.getColorPrimaryBySp()
            image_view_splash.drawable.setTint(colorPrimary)
            text_view_splash.setTextColor(colorPrimary)
            image_view_drawable_list.drawable.setTint(colorPrimary)
            image_view_topic_user.drawable.setTint(colorPrimary)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            var shortCutInit by PreferenceUtils(mContext, SHORTCUT_INIT, false)
//            if (!shortCutInit) {
                initShortCuts()
                shortCutInit = true
//            }
        }
    }

    private fun startActivity() {
        startActivity(Intent(mContext,
                if (MyApp.homeLayout() == HOME_VIEW_STYLE_BOTTOM) HomeActivity::class.java
                else HomeActivity2::class.java))
        overridePendingTransition(0, 0)
        UpdateHelper.check()
        finish()
    }

    @RequiresApi(Build.VERSION_CODES.N_MR1)
    private fun initShortCuts() {
        val shortcutManager = getSystemService(ShortcutManager::class.java)
        val hotShortcut = ShortcutInfo.Builder(mContext, "hot")
                .setShortLabel("热门")
                .setLongLabel("热门帖子")
                .setIcon(Icon.createWithResource(mContext, R.drawable.xic_hot_white))
                .setIntent(Intent(mContext, MyApp.getHomeActivity()).apply {
                    action = Intent.ACTION_VIEW
                    putExtra(SHORTCUT_HOT, true)
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                })
                .build()
        val msgShortcut = ShortcutInfo.Builder(mContext, "msg")
                .setShortLabel("消息")
                .setLongLabel("我的消息")
                .setIcon(Icon.createWithResource(mContext, R.drawable.xic_notice_white))
                .setIntent(Intent(mContext, MyApp.getHomeActivity()).apply {
                    action = Intent.ACTION_VIEW
                    putExtra(SHORTCUT_MSG, true)
                    flags = Intent.FLAG_ACTIVITY_SINGLE_TOP })
                .build()
        val searchShortcut = ShortcutInfo.Builder(mContext, "search")
                .setShortLabel("搜索")
                .setLongLabel("帖子搜索")
                .setIcon(Icon.createWithResource(mContext, R.drawable.xic_search_white_24dp))
                .setIntent(Intent(mContext, SearchActivity::class.java).apply {
                    action = Intent.ACTION_VIEW })
                .build()
        shortcutManager.dynamicShortcuts = arrayListOf(hotShortcut, msgShortcut,searchShortcut)
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

