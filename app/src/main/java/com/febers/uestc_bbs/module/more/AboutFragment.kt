package com.febers.uestc_bbs.module.more

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseSwipeFragment
import com.febers.uestc_bbs.base.SHOW_BOTTOM_BAR_ON_DESTROY
import com.febers.uestc_bbs.entity.SettingItemBean
import com.febers.uestc_bbs.module.setting.SettingFragment
import com.febers.uestc_bbs.view.adapter.SettingAdapter
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment: BaseSwipeFragment() {

    private var items1: MutableList<SettingItemBean> = ArrayList()
    private var items2: MutableList<SettingItemBean> = ArrayList()
    private lateinit var settingAdapter2: SettingAdapter
    private lateinit var settingAdapter3: SettingAdapter

    override fun setToolbar(): Toolbar? {
        return toolbar_about
    }

    override fun setContentView(): Int {
        return R.layout.fragment_about
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

        settingAdapter2 = SettingAdapter(context!!, items1)
        recyclerview_about_1.adapter = settingAdapter2

        settingAdapter3 = SettingAdapter(context!!, items2)
        recyclerview_about_2.adapter = settingAdapter3

        items1.addAll(initSettingData1())
        settingAdapter2.notifyDataSetChanged()
        items2.addAll(initSettingData2())
        settingAdapter3.notifyDataSetChanged()
    }

    private fun initSettingData1(): List<SettingItemBean> {
        val item1 = SettingItemBean(getString(R.string.version), getString(R.string.version_value))
        val item2 = SettingItemBean(getString(R.string.feedback_and_other), getString(R.string.developer_email))
        val item3 = SettingItemBean(getString(R.string.check_update), getString(R.string.check_update))
        return arrayListOf(item1, item3, item2)
    }

    private fun initSettingData2(): List<SettingItemBean> {
        val item1 = SettingItemBean(getString(R.string.source_code), "")
        val item2 = SettingItemBean(getString(R.string.open_source_project), "")
        val item3 = SettingItemBean(getString(R.string.open_source_license), "Apache License")
        return arrayListOf(item1, item2, item3)
    }

    companion object {
        @JvmStatic
        fun newInstance(showBottomBarOnDestroy: Boolean): AboutFragment = AboutFragment().apply {
            arguments = Bundle().apply {
                putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
            }
        }
    }
}