package com.febers.uestc_bbs.view.adapter

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.DetailItemBean
import com.febers.uestc_bbs.lib.baseAdapter.ViewHolder
import com.febers.uestc_bbs.lib.baseAdapter.base.CommonBaseAdapter

class UserDetailAdapter(context: Context, data: List<DetailItemBean>):
        CommonBaseAdapter<DetailItemBean>(context, data, false){

    override fun convert(p0: ViewHolder?, p1: DetailItemBean?, p2: Int) {
        p0?.setText(R.id.text_view_item_detail_param, p1?.itemParam)
        p0?.setText(R.id.text_view_item_detail_value, p1?.itemValue)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_user_detail
    }
}