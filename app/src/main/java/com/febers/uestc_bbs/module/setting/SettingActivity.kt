package com.febers.uestc_bbs.module.setting

import android.content.Intent
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.SettingItemBean
import com.febers.uestc_bbs.io.CacheManager
import com.febers.uestc_bbs.module.dialog.Dialog
import com.febers.uestc_bbs.module.service.HeartMsgService
import com.febers.uestc_bbs.utils.*
import com.febers.uestc_bbs.view.adapter.SettingAdapter
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*
import me.yokeyword.fragmentation.SwipeBackLayout

class SettingActivity : BaseActivity() {

    private lateinit var settingAdapter1: SettingAdapter
    private lateinit var settingAdapter2: SettingAdapter
    private lateinit var settingAdapter3: SettingAdapter

    private lateinit var cacheItem: SettingItemBean

    private var iconChooseView: IconFragment? = null

    private var homeLayoutValue by PreferenceUtils(ctx, HOME_VIEW_STYLE, HOME_VIEW_STYLE_BOTTOM)
    private var swipeBackRange by PreferenceUtils(ctx, SP_SWIPE_BACK_RANGE_VALUE, SWIPE_BACK_MED)
    private var hintWay by PreferenceUtils(ctx, HINT_WAY, HINT_BY_TOAST)
    private var loopReceiveMsg by PreferenceUtils(ctx, LOOP_RECEIVE_MSG, true)
    private var enableSplash by PreferenceUtils(ctx, SP_SPLASH_ENABLE, true)
    private var renderContentValue by PreferenceUtils(ctx, SP_RENDER_CONTENT_MODE, RENDER_MODE_TRANSFER)

    private var homeLayoutDialog: AlertDialog? = null
    private var swipeBackDialog: AlertDialog? = null
    private var renderContentDialog: AlertDialog? = null

    override fun setView(): Int = R.layout.activity_setting
    override fun setToolbar(): Toolbar? = toolbar_common
    override fun setTitle(): String? = getString(R.string.setting)
    override fun initView() {
    }

    override fun afterCreated() {
        text_view_setting_1.setTextColor(colorAccent())
        text_view_setting_2.setTextColor(colorAccent())
        text_view_setting_3.setTextColor(colorAccent())

        settingAdapter1 = SettingAdapter(ctx, initSettingData1()).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                when(i) {
                    0 -> {
                        if (homeLayoutDialog == null) {
                            var checked = homeLayoutValue
                            homeLayoutDialog = Dialog.build(ctx) {
                                singleChoiceItems(getString(R.string.home_layout),
                                        listOf(getString(R.string.home_layout_bottom), getString(R.string.home_layout_drawer)),
                                        descriptions = emptyList(),
                                        checked = homeLayoutValue,
                                        onChecked = { item: String, position: Int ->
                                            checked = position
                                        })
                                positiveButton("确定", action = { dialog: AlertDialog? ->
                                    dialog?.dismiss()
                                    homeLayoutValue = checked
                                    showHint("重启应用生效")
                                })
                            }
                        }
                        homeLayoutDialog!!.show()
                    }
                    1 -> {
                        val checkBox = viewHolder.getView<CheckBox>(R.id.check_box_item_setting)
                        checkBox.isChecked = !checkBox.isChecked
                        enableSplash = !enableSplash
                    }
                    2 -> {
                        //更改滑动范围
                        if (swipeBackDialog == null) {
                            var checked = swipeBackRange
                            swipeBackDialog = Dialog.build(ctx) {
                                singleChoiceItems("设置滑动范围",
                                        listOf("禁止滑动", "屏幕边缘", "屏幕中间", "全屏幕"),
                                        descriptions = emptyList(),
                                        checked = swipeBackRange,
                                        onChecked = { item: String, position: Int ->
                                            checked = position
                                        })
                                positiveButton("确定", action = { dialog: AlertDialog? ->
                                    dialog?.dismiss()
                                    when(checked) {
                                        0 -> {
                                            setSwipeBackEnable(false)
                                            swipeBackRange = SWIPE_BACK_FORBIDDEN
                                        }
                                        1 -> {
                                            setSwipeBackEnable(true)
                                            swipeBackRange = SWIPE_BACK_MIN
                                            setEdgeLevel(SwipeBackLayout.EdgeLevel.MIN)
                                        }
                                        2 -> {
                                            setSwipeBackEnable(true)
                                            swipeBackRange = SWIPE_BACK_MED
                                            setEdgeLevel(SwipeBackLayout.EdgeLevel.MED)
                                        }
                                        3 -> {
                                            setSwipeBackEnable(true)
                                            swipeBackRange = SWIPE_BACK_MAX
                                            setEdgeLevel(SwipeBackLayout.EdgeLevel.MAX)
                                        }
                                    }
                                })
                            }
                        }
                        swipeBackDialog!!.show()
                    }
                    3 -> {
                        if (iconChooseView == null) {
                            iconChooseView = IconFragment()
                        }
                        iconChooseView?.show(supportFragmentManager, "icon")
                    }
                }
            }
        }
        recyclerview_setting_option_1.adapter = settingAdapter1

        settingAdapter2 = SettingAdapter(ctx, initSettingData2()).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                when(i) {
                    0 -> {
                        if (renderContentDialog == null) {
                            var checked = renderContentValue
                            renderContentDialog = Dialog.build(ctx) {
                                singleChoiceItems("渲染机制",
                                        listOf("WebView加载模式", "Layout动态添加模式"),
                                        listOf("加载图片和音频资源更自然，在低版本机型上可能会出现问题", "加载长图、大图效果一般，可能会造成卡顿"),
                                        checked = renderContentValue,
                                        onChecked = { item: String, position: Int ->
                                            checked = position
                                        })
                                positiveButton("确定", action = { dialog: AlertDialog? ->
                                    dialog?.dismiss()
                                    renderContentValue = checked
                                })
                            }
                        }
                        renderContentDialog!!.show()
                    }
                    1 -> {
                        startActivity(Intent(ctx, PostItemSettingActivity::class.java))
                    }
                    2 -> {
                        onHintMethodChange()
                    }
                }
            }
        }
        recyclerview_setting_option_2.adapter = settingAdapter2

        settingAdapter3 = SettingAdapter(ctx, initSettingData3()).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                when(i) {
                    0 -> {
                        val checkBox = viewHolder.getView<CheckBox>(R.id.check_box_item_setting)
                        checkBox.isChecked = !checkBox.isChecked
                        onReceiveMsgChange(!checkBox.isChecked)
                    }
                    1 -> { clearCache() }
                }
            }
        }
        recyclerview_setting_option_3.adapter = settingAdapter3

        getCache()
        btn_restart_app.setOnClickListener {
            RestartUtils.restartApp2()
        }
        btn_restart_app.setTextColor(colorAccent())
    }

    private fun initSettingData1(): List<SettingItemBean> {
        val layoutItem = SettingItemBean(getString(R.string.home_layout), getString(R.string.choose_home_layout))
        val itemSplash = SettingItemBean("闪屏开关", "开启应用时打开闪屏", showCheck = true, checked = enableSplash)
        val itemSwipe = SettingItemBean("滑动范围", "选择滑动返回的触发范围")
        val itemIcon = SettingItemBean(getString(R.string.icon), getString(R.string.icon_style_in_launcher))
        return arrayListOf(layoutItem, itemSplash, itemSwipe, itemIcon)
    }
    private fun initSettingData2(): List<SettingItemBean> {
        val itemContentMode = SettingItemBean("渲染机制", "选择帖子详情界面的渲染机制")
        val itemPostItem = SettingItemBean("列表样式", "自定义帖子列表的显示样式")
        val itemHint = SettingItemBean(getString(R.string.hint), getString(R.string.set_hint_style))
        return arrayListOf(itemContentMode, itemPostItem, itemHint)
    }

    private fun initSettingData3(): List<SettingItemBean> {
        val itemSilence = SettingItemBean(getString(R.string.no_disturbing), getString(R.string.no_disturbing_explain), showCheck = true, checked = !loopReceiveMsg)
        cacheItem = SettingItemBean(getString(R.string.clear_cache), "...")
        return arrayListOf(itemSilence, cacheItem)
    }

    private fun onHintMethodChange() {
        if (hintWay == HINT_BY_TOAST) {
            hintWay = HINT_BY_SNACK_BAR
            HintUtils.show(ctx, getString(R.string.hint_way_changed))
        } else if (hintWay == HINT_BY_SNACK_BAR) {
            hintWay = HINT_BY_TOAST
            HintUtils.show(getString(R.string.hint_way_changed))
        }
    }

    private fun onReceiveMsgChange(isLoop: Boolean) {
        loopReceiveMsg = isLoop
        val intent = Intent(ctx, HeartMsgService::class.java)
        if (loopReceiveMsg) {
            startService(intent)
        } else {
            stopService(intent)
        }
    }

    private fun getCache() {
        logd { "即将获取缓存" }
        ThreadMgr.io {
            cacheItem.tip = CacheManager.CacheSize
            logd { "获取缓存大小成功：${cacheItem.tip}" }
            ThreadMgr.ui(ctx) {
                settingAdapter3.notifyItemChanged(settingAdapter3.dataCount-1)
            }
        }
    }

    private fun clearCache() {
        showHint(getString(R.string.cleaning))
        CacheManager.clearCache()
        getCache()
    }

}