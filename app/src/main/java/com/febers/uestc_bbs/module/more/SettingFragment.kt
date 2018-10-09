package com.febers.uestc_bbs.module.more

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseSwipeFragment
import com.febers.uestc_bbs.base.SHOW_BOTTOM_BAR_ON_DESTROY
import com.febers.uestc_bbs.entity.SettingItemBean
import com.febers.uestc_bbs.utils.ImageCacheUtils
import com.febers.uestc_bbs.view.adapter.SettingAdapter
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BaseSwipeFragment() {

    private val settingData: MutableList<SettingItemBean> = ArrayList()
    private lateinit var settingAdapter1: SettingAdapter
    private lateinit var settingAdapter2: SettingAdapter
    private lateinit var settingAdapter3: SettingAdapter

    override fun setToolbar(): Toolbar? {
        return toolbar_setting
    }

    override fun setContentView(): Int {
        return R.layout.fragment_setting
    }

    override fun initView() {
        settingAdapter1 = SettingAdapter(context!!, initSettingData1())
        recyclerview_setting_1.adapter = settingAdapter1

        settingAdapter2 = SettingAdapter(context!!, initSettingData2())
        recyclerview_setting_2.adapter = settingAdapter2

        settingAdapter3 = SettingAdapter(context!!, initSettingData3())
        recyclerview_setting_3.adapter = settingAdapter3
    }

    private fun initSettingData1(): List<SettingItemBean> {
        val item1 = SettingItemBean(getString(R.string.clear_image_cache), ImageCacheUtils.cacheSize)
        val item2 = SettingItemBean("无图模式", "不显示帖子图片", showCheck = true, checked = false)
        return arrayListOf(item1, item2)
    }

    private fun initSettingData2(): List<SettingItemBean> {
        val item1 = SettingItemBean(getString(R.string.version), getString(R.string.version_value))
        val item2 = SettingItemBean(getString(R.string.feedback_and_other), getString(R.string.developer_email))
        val item3 = SettingItemBean(getString(R.string.check_update), "")
        return arrayListOf(item1, item2, item3)
    }

    private fun initSettingData3(): List<SettingItemBean> {
        val item1 = SettingItemBean(getString(R.string.source_code), "")
        val item2 = SettingItemBean(getString(R.string.open_source_project), "")
        val item3 = SettingItemBean(getString(R.string.open_source_license), "Apache License")
        return arrayListOf(item1, item2, item3)
    }

    companion object {
        @JvmStatic
        fun newInstance(showBottomBarOnDestroy: Boolean) =
                SettingFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
                    }
                }
    }
}