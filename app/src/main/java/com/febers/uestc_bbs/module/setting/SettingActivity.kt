package com.febers.uestc_bbs.module.setting

import android.content.Intent
import android.widget.CheckBox
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.*
import com.febers.uestc_bbs.io.UserManager
import com.febers.uestc_bbs.entity.SettingItemBean
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.module.login.view.LoginActivity
import com.febers.uestc_bbs.io.CacheHelper
import com.febers.uestc_bbs.module.service.HeartMsgService
import com.febers.uestc_bbs.utils.*
import com.febers.uestc_bbs.view.adapter.SettingAdapter
import com.febers.uestc_bbs.view.adapter.SimpleUserAdapter
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.layout_toolbar_common.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.collections.ArrayList

class SettingActivity : BaseActivity() {

    private var options: MutableList<SettingItemBean> = ArrayList()

    private lateinit var settingAdapter: SettingAdapter
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
        options.addAll(initSettingData())
        settingAdapter = SettingAdapter(mContext, options).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                when(i) {
                    0 -> { onHomeLayoutChange() }
                    1 -> {
                        val checkBox = viewHolder.getView<CheckBox>(R.id.check_box_item_setting)
                        checkBox.isChecked = !checkBox.isChecked
                        enableSplash = !enableSplash
                    }
                    2 -> { onHintMethodChange() }
                    3 -> {
                        if (iconChooseView == null) {
                            iconChooseView = IconFragment()
                        }
                        iconChooseView?.show(supportFragmentManager, "icon")
                    }
                    4 -> {
                        val checkBox = viewHolder.getView<CheckBox>(R.id.check_box_item_setting)
                        checkBox.isChecked = !checkBox.isChecked
                        onReceiveMsgChange(!checkBox.isChecked)
                    }
                    5 -> { clearCache() }
                }
            }
        }
        recyclerview_setting_option.adapter = settingAdapter
        getCache()
        btn_restart_app.setOnClickListener {
            RestartUtils.restartApp2()
        }
        btn_restart_app.setTextColor(colorAccent())
    }

    private fun initSettingData(): List<SettingItemBean> {
        layoutItem = SettingItemBean(getString(R.string.home_layout), getString(R.string.choose_home_layout) + layoutDescription)
        val itemSplash = SettingItemBean("闪屏开关", "开启应用时打开闪屏", showCheck = true, checked = enableSplash)
        val itemHint = SettingItemBean(getString(R.string.hint), getString(R.string.set_hint_style))
        val itemIcon = SettingItemBean(getString(R.string.icon), getString(R.string.icon_style_in_launcher))
        val itemSilence = SettingItemBean(getString(R.string.no_disturbing), getString(R.string.no_disturbing_explain), showCheck = true, checked = !loopReceiveMsg)
        cacheItem = SettingItemBean(getString(R.string.clear_cache), "...")
        return arrayListOf(layoutItem, itemSplash, itemHint, itemIcon, itemSilence, cacheItem)
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
        settingAdapter.notifyItemChanged(0)
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
        ThreadPoolMgr.execute(Runnable {
            cacheItem.tip = CacheHelper.CacheSize
            runOnUiThread {
                settingAdapter.notifyItemChanged(4)
            }})
    }

    private fun clearCache() {
        showHint(getString(R.string.cleaning))
        CacheHelper.clearCache()
        getCache()
    }

}