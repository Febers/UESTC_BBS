package com.febers.uestc_bbs.view.adapter

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.OptionItemBean
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class PostOptionAdapter(context: Context, data: List<OptionItemBean>):
        CommonBaseAdapter<OptionItemBean>(context, data, false) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_post_option
    }

    override fun convert(p0: ViewHolder?, p1: OptionItemBean?, p2: Int) {
        p0?.setText(R.id.text_view_item_post_option, p1?.itemName)
        p0?.setBgResource(R.id.image_view_item_post_option, p1?.itemIcon!!)
    }
}