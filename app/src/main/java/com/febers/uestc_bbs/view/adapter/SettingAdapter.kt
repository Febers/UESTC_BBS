package com.febers.uestc_bbs.view.adapter

import android.content.Context
import android.view.View
import android.widget.CheckBox
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.SettingItemBean
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class SettingAdapter(val context: Context, data: List<SettingItemBean>):
        CommonBaseAdapter<SettingItemBean>(context, data, false) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_setting
    }

    override fun convert(p0: ViewHolder?, p1: SettingItemBean?, p2: Int) {
        p1 ?: return
        p0?.setText(R.id.text_view_item_setting_first, p1.title)
        p0?.setText(R.id.text_view_item_setting_second, p1.tip)
        if (p1.showCheck) {
            (p0?.getView(R.id.check_box_item_setting) as CheckBox).apply {
                visibility = View.VISIBLE
                if (p1.checked) {
                    isChecked = true
                }
            }
        }
    }
}