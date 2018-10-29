package com.febers.uestc_bbs.module.setting

import android.os.Bundle
import android.util.Log
import android.widget.CheckBox
import androidx.appcompat.widget.Toolbar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseSwipeFragment
import com.febers.uestc_bbs.base.SHOW_BOTTOM_BAR_ON_DESTROY
import com.febers.uestc_bbs.entity.SettingItemBean
import com.febers.uestc_bbs.utils.ImageCacheUtils
import com.febers.uestc_bbs.view.adapter.SettingAdapter
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BaseSwipeFragment() {

    private var items1: MutableList<SettingItemBean> = ArrayList()

    private lateinit var settingAdapter1: SettingAdapter


    override fun setContentView(): Int {
        return R.layout.fragment_setting
    }

    override fun setToolbar(): Toolbar? {
        return toolbar_setting
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        settingAdapter1 = SettingAdapter(context!!, items1).apply {
            setOnItemClickListener { viewHolder, settingItemBean, i ->
                when(i) {
                    0 -> {start(RefreshStyleFragment.newInstance())}
                    1 -> {
                        val checkBox = viewHolder.getView<CheckBox>(R.id.check_box_item_setting)
                        checkBox.isChecked = !checkBox.isChecked
                        Log.i("setting", ":msg")
                    }
                    2 -> clearCache()
                    3 -> {
                        val checkBox = viewHolder.getView<CheckBox>(R.id.check_box_item_setting)
                        checkBox.isChecked = !checkBox.isChecked
                        Log.i("setting", ":save")
                    }
                }
            }
        }
        recyclerview_setting_1.adapter = settingAdapter1

        items1.addAll(initSettingData1())
        settingAdapter1.notifyDataSetChanged()
    }

    private fun initSettingData1(): List<SettingItemBean> {
        val item3 = SettingItemBean("刷新控件", "设置刷新控件样式")
        val item4 = SettingItemBean("后台接收消息", "除非强制退出，否则将一直发送心跳包查询消息", showCheck = true, checked = false)
        val item1 = SettingItemBean(getString(R.string.clear_image_cache), ImageCacheUtils.cacheSize)
        val item2 = SettingItemBean("缓存首页帖子", "保存首页的第一页帖子数据", showCheck = true, checked = true)
        return arrayListOf(item3, item4, item1, item2)
    }

    private fun clearCache() {

    }

    companion object {
        @JvmStatic
        fun newInstance(showBottomBarOnDestroy: Boolean): SettingFragment = SettingFragment().apply {
            arguments = Bundle().apply {
                putBoolean(SHOW_BOTTOM_BAR_ON_DESTROY, showBottomBarOnDestroy)
            }
        }
    }
}