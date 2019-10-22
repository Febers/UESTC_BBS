package com.febers.uestc_bbs

import android.content.ComponentCallbacks2
import android.content.Context
import android.os.Process
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.io.UserHelper
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.utils.PreferenceUtils
import kotlin.properties.Delegates
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.BallPulseFooter
import androidx.multidex.MultiDexApplication
import com.febers.uestc_bbs.base.HOME_VIEW_STYLE
import com.febers.uestc_bbs.base.HOME_VIEW_STYLE_BOTTOM
import com.febers.uestc_bbs.home.HomeActivity
import com.febers.uestc_bbs.home.HomeActivity2
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.utils.ApiUtils
import com.febers.uestc_bbs.lib.emotion.EmotionManager
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/////////////////////////////////////////////////////////////////////////////

//		　　　　
//		　　　　　　 \\ - - //
//		　　　　　　 (-●  ●- )
//		　　　　　　　\  _  /
//		　　　　　　　 \  /
//		┏oOOo-━━━━━━━━━━━┓
//		┃　　　　　　　　　 ┃
//		┃　　UESTC_BBS   ┃
//		┃   @Febers     ┃
//		┃　　　　　　　　  ┃
//		┗━━━━━━━━━━-oOOo┛

/////////////////////////////////////////////////////////////////////////////
class MyApp: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return
//        }
//        LeakCanary.install(this)

        context = applicationContext

        initBugly()
        initEmotionView()
        UEHandler.getInstance().init()
    }

    /**
     * 初始化Bugly
     */
    private fun initBugly() {
        val packageName = applicationContext.packageName
        val processName = getProcessName(Process.myPid())
        // 设置是否为上报进程
        val strategy = CrashReport.UserStrategy(MyApp.context())
        strategy.isUploadProcess = processName == null || processName == packageName

//        Beta.autoInit = true
        //初步去除 Bugly 更新，改用其他方式
//        Beta.autoCheckUpgrade = true
//        Beta.upgradeListener = UpgradeListener { ret, strategy, isManual, isSilence ->
//            if (strategy != null) {
//                context.startActivity(
//                        Intent().apply {
//                            setClass(context, UpdateActivity::class.java)
//                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                        }
//                )
//            }
//        }
        Bugly.init(context, ApiUtils.BUGLY_APP_ID, false)
    }

    /**
     * 初始化表情包界面
     */
    private fun initEmotionView() {
        EmotionManager.Builder()
                .with(context) // 传递 Context
                .configFileName("first.xml")// 配置文件名称
                .emoticonDir("face") // asset 下存放表情的目录路径（asset——> configFileName 之间的路径,结尾不带斜杠）
                .sourceDir("first") // 存放 emoji 表情资源文件夹路径（emoticonDir 图片资源之间的路径,结尾不带斜杠）
                .defaultBounds(30)//emoji 表情显示出来的宽高
                .cacheSize(1024)//加载资源到内存时 LruCache 缓存大小
                .defaultTabIcon(R.drawable.xic_emot_blue_24dp)//emoji表情Tab栏图标
                .emojiColumn(4)//单页显示表情的列数
                .emojiRow(3)//单页显示表情的行数
                .imageLoader { path, imageView ->
                    ImageLoader.load(context = context,
                            url = path,
                            imageView = imageView,
                            isCircle = false)
                }
                .build()
    }

    companion object {
        private var context: Context by Delegates.notNull()
        var uiHidden: Boolean = false
        /*
            为了处理表情键盘的返回键逻辑而设置的标志变量
            在#KeyBoardManager的showEmotionLayout中设置为true，
            在调用表情键盘的Activity拦截返回键事件的方法中设置为false
         */
        var emotionViewVisible = false

        var msgCount = 0

        fun context() = context
        fun user(): UserSimpleBean = UserHelper.getNowUser()

        fun homeLayout(): Int {
            val homeLayout by PreferenceUtils(context(), HOME_VIEW_STYLE, HOME_VIEW_STYLE_BOTTOM)
            return homeLayout
        }

        fun getHomeActivity() = if (homeLayout() == HOME_VIEW_STYLE_BOTTOM) HomeActivity::class.java
        else HomeActivity2::class.java

        init {
            /**
             * 初始化刷新控件
             */
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                ClassicsHeader(context)
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                layout.setFooterHeight(38f)
                BallPulseFooter(context)
            }
        }
    }

    /**
     * 系统根据不同的内存状态回调该线程
     *
     * @param level 不同的内存状态
     * TRIM_MEMORY_COMPLETE：内存不足，并且该进程在后台进程列表最后一个，马上就要被清理
     * TRIM_MEMORY_MODERATE：内存不足，并且该进程在后台进程列表的中部。
     * TRIM_MEMORY_BACKGROUND：内存不足，并且该进程是后台进程。
     * TRIM_MEMORY_UI_HIDDEN：内存不足，并且该进程的UI已经不可见了。
     */
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            uiHidden = true
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }

    /**
     * 在系统内存不足，所有后台程序都被杀死时，
     * 系统会调用OnLowMemory
     */
    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName = reader.readLine()
            if (processName.isNotEmpty()) {
                processName = processName.trim()
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return null
    }
}
