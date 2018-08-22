package com.febers.uestc_bbs.adaper

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.ThemeItemBean
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class ThemeAdapter(context: Context, data: List<ThemeItemBean>, isLoadMore: Boolean) : CommonBaseAdapter<ThemeItemBean>(context, data, isLoadMore) {
    override fun convert(p0: ViewHolder?, p1: ThemeItemBean?, p2: Int) {
        p0?.setBgResource(R.id.image_view_theme_color, p1!!.itemColor)
        p0?.setText(R.id.text_view_theme_name, p1!!.itemName)
        if (p1!!.itemUsing) {
            p0?.setText(R.id.text_view_theme_using, "使用中")
        }
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_theme
    }
}