package com.febers.uestc_bbs.module.setting

import android.content.Intent
import android.widget.CheckBox
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.entity.SettingItemBean
import com.febers.uestc_bbs.io.CacheManager
import com.febers.uestc_bbs.module.service.HeartMsgService
import com.febers.uestc_bbs.utils.*
import com.febers.uestc_bbs.view.adapter.SettingAdapter
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*
import kotlin.collections.ArrayList

class SettingActivity : BaseActivity() {
    private lateinit var settingAdapter1: SettingAdapter
    private lateinit var settingAdapter2: SettingAdapter
    private lateinit var settingAdapter3: SettingAdapter

    private lateinit var cacheItem: SettingItemBean
    private var iconChooseView: IconFragment? = null

    private var homeLayout by PreferenceUtils(mContext, HOME_VIEW_STYLE, HOME_VIEW_STYLE_BOTTOM)
    private lateinit var layoutItem: SettingItemBean
    private lateinit var layoutDescription: String

    private var hintWay by PreferenceUtils(mContext, HINT_WAY, HINT_BY_TOAST)

    private var loopReceiveMsg by PreferenceUtils(mContext, LOOP_RECEIVE_MSG, true)

    private var enableSplash by PreferenceUtils(mContext, SP_SPLASH_ENABLE, true)

    override fun setView(): Int = R.layout.activity_setting

    override fun setToolbar(): Toolbar? = toolbar_common

    override fun setTitle(): String? = getString(R.string.setting)

    override fun initView() {
    }

    override fun afterCreated() {
        layoutDescription = if (homeLayout == HOME_VIEW_STYLE_BOTTOM) getString(R.string.home_layout_bottom)
        else getString(R.string.home_layout_drawer)
        text_view_setting_1.setTextColor(colorAccent())
        text_view_setting_2.setTextColor(colorAccent())
        text_view_setting_3.setTextColor(colorAccent())

        settingAdapter1 = SettingAdapter(mContext, initSettingData1()).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                when(i) {
                    0 -> {
                        onHomeLayoutChange()
                    }
                    1 -> {
                        val checkBox = viewHolder.getView<CheckBox>(R.id.check_box_item_setting)
                        checkBox.isChecked = !checkBox.isChecked
                        enableSplash = !enableSplash
                    }
                    2 -> {
                        //更改滑动范围
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

        settingAdapter2 = SettingAdapter(mContext, initSettingData2()).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                when(i) {
                    0 -> {

                    }
                    1 -> {

                    }
                    2 -> {
                        onHintMethodChange()
                    }
                }
            }
        }
        recyclerview_setting_option_2.adapter = settingAdapter2

        settingAdapter3 = SettingAdapter(mContext, initSettingData3()).apply {
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
        layoutItem = SettingItemBean(getString(R.string.home_layout), getString(R.string.choose_home_layout) + layoutDescription)
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

    private fun onHomeLayoutChange() {
        homeLayout = if (homeLayout == HOME_VIEW_STYLE_BOTTOM) {
            layoutDescription = getString(R.string.home_layout_drawer)
            HOME_VIEW_STYLE_DRAWER
        } else {
            layoutDescription = getString(R.string.home_layout_bottom)
            HOME_VIEW_STYLE_BOTTOM
        }
        showHint("重启应用生效")
        layoutItem.tip = getString(R.string.choose_home_layout) + layoutDescription
        settingAdapter1.notifyItemChanged(0)
    }

    private fun onHintMethodChange() {
        if (hintWay == HINT_BY_TOAST) {
            hintWay = HINT_BY_SNACK_BAR
            HintUtils.show(mContext, getString(R.string.hint_way_changed))
        } else if (hintWay == HINT_BY_SNACK_BAR) {
            hintWay = HINT_BY_TOAST
            HintUtils.show(getString(R.string.hint_way_changed))
        }
    }

    private fun onReceiveMsgChange(isLoop: Boolean) {
        loopReceiveMsg = isLoop
        val intent = Intent(mContext, HeartMsgService::class.java)
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
            ThreadMgr.ui(mContext) {
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