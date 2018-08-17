/*
 * Created by Febers at 18-8-15 下午11:32.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午11:32.
 */

package com.febers.uestc_bbs.adaper

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.SimplePostBean
import com.febers.uestc_bbs.utils.TimeUtils
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class PostItemAdapter(context: Context, data: List<SimplePostBean>, isLoadMore: Boolean):
        CommonBaseAdapter<SimplePostBean>(context, data, isLoadMore) {

    override fun convert(p0: ViewHolder?, p1: SimplePostBean?, p2: Int) {
        p0?.setText(R.id.text_view_item_poster, p1?.user_nick_name)
        p0?.setText(R.id.text_view_item_post_block, p1?.board_name)
        p0?.setText(R.id.text_view_item_post_title, p1?.title)
        p0?.setText(R.id.text_view_item_post_content, p1?.subject)
        p0?.setText(R.id.text_view_item_post_reply, p1?.replies)
        p0?.setText(R.id.text_view_item_post_hits, p1?.hits)
        p0?.setText(R.id.text_view_item_post_time, TimeUtils.stampToDate(p1?.last_reply_date))
        if(p0?.convertView?.visibility == View.VISIBLE) {
            Glide.with(p0!!.convertView).load(p1?.userAvatar).apply(RequestOptions().circleCrop())
                    .into(p0!!.getView(R.id.image_view_item_post_avatar))
        }
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_post
    }
}