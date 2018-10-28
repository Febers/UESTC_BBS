package com.febers.uestc_bbs.module.more

import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseActivity
import com.febers.uestc_bbs.entity.SettingItemBean
import com.febers.uestc_bbs.utils.ImageCacheUtils
import com.febers.uestc_bbs.view.adapter.SettingAdapter
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity() {

    private var items1: MutableList<SettingItemBean> = ArrayList()
    private var items2: MutableList<SettingItemBean> = ArrayList()
    private var items3: MutableList<SettingItemBean> = ArrayList()
    private lateinit var settingAdapter1: SettingAdapter
    private lateinit var settingAdapter2: SettingAdapter
    private lateinit var settingAdapter3: SettingAdapter

    override fun setToolbar(): Toolbar? {
        return toolbar_setting
    }

    override fun setView(): Int {
        return R.layout.activity_setting
    }

    override fun initView() {
        settingAdapter1 = SettingAdapter(this, items1)
        recyclerview_setting_1.adapter = settingAdapter1

        settingAdapter2 = SettingAdapter(this, items2)
        recyclerview_setting_2.adapter = settingAdapter2

        settingAdapter3 = SettingAdapter(this, items3)
        recyclerview_setting_3.adapter = settingAdapter3

        items1.addAll(initSettingData1())
        settingAdapter1.notifyDataSetChanged()
        items2.addAll(initSettingData2())
        settingAdapter2.notifyDataSetChanged()
        items3.addAll(initSettingData3())
        settingAdapter3.notifyDataSetChanged()
    }

    private fun initSettingData1(): List<SettingItemBean> {
        val item3 = SettingItemBean("刷新控件", "设置刷新控件样式")
        val item4 = SettingItemBean("后台接收消息", "除非强制退出，否则将一直发送心跳包查询消息", showCheck = true, checked = false)
        val item1 = SettingItemBean(getString(R.string.clear_image_cache), ImageCacheUtils.cacheSize)
        val item2 = SettingItemBean("无图模式", "不显示帖子图片", showCheck = true, checked = false)
        return arrayListOf(item3, item4, item1, item2)
    }

    private fun initSettingData2(): List<SettingItemBean> {
        val item1 = SettingItemBean(getString(R.string.version), getString(R.string.version_value))
        val item2 = SettingItemBean(getString(R.string.feedback_and_other), getString(R.string.developer_email))
        val item3 = SettingItemBean(getString(R.string.check_update), getString(R.string.check_update))
        return arrayListOf(item1, item3, item2)
    }

    private fun initSettingData3(): List<SettingItemBean> {
        val item1 = SettingItemBean(getString(R.string.source_code), "")
        val item2 = SettingItemBean(getString(R.string.open_source_project), "")
        val item3 = SettingItemBean(getString(R.string.open_source_license), "Apache License")
        return arrayListOf(item1, item2, item3)
    }
}