/*
 * Created by Febers at 18-8-13 下午11:27.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-13 下午11:23.
 */

package com.febers.uestc_bbs.adaper

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.MoreItemBean
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class MoreItemAdapter(context: Context, data: List<MoreItemBean>, isLoadMore: Boolean):
        CommonBaseAdapter<MoreItemBean>(context, data, isLoadMore) {

    override fun convert(p0: ViewHolder?, p1: MoreItemBean?, p2: Int) {
        p0?.setText(R.id.item_more_text_view, p1!!.itemName)
        p0?.setBgResource(R.id.item_more_image_view, p1!!.itemIcon)
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_more
    }
}