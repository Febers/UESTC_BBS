package com.febers.uestc_bbs.view.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.MoreItemBean
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class MoreItemAdapter(context: Context, data: List<MoreItemBean>, isLoadMore: Boolean):
        CommonBaseAdapter<MoreItemBean>(context, data, isLoadMore) {

    override fun convert(p0: ViewHolder?, p1: MoreItemBean?, p2: Int) {
        p0?.setText(R.id.item_more_text_view, p1!!.itemName)
        p0?.setBgResource(R.id.item_more_image_view, p1!!.itemIcon)
        if (p1!!.showSwitch) {
            p0?.getView<SwitchCompat>(R.id.switch_more_item)?.visibility = View.VISIBLE
            p0?.getView<SwitchCompat>(R.id.switch_more_item)?.isChecked = p1.isCheck
            p0?.getView<TextView>(R.id.text_view_more_item_night)?.visibility = View.VISIBLE
        }
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_more
    }
}