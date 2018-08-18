/*
 * Created by Febers at 18-8-15 下午11:32.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午11:32.
 */

package com.febers.uestc_bbs.adaper

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.SimpleTopicBean
import com.febers.uestc_bbs.module.post.view.GlideCircleTransform
import com.febers.uestc_bbs.utils.TimeUtils
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class PostItemAdapter(context: Context, data: List<SimpleTopicBean>, isLoadMore: Boolean):
        CommonBaseAdapter<SimpleTopicBean>(context, data, isLoadMore) {

    override fun convert(p0: ViewHolder?, p1: SimpleTopicBean?, p2: Int) {
        var summary = p1?.subject   //普通帖子
        if (summary == null) {
            summary = p1?.summary   //热门帖子
        }
        p0?.setText(R.id.text_view_item_poster, p1?.user_nick_name)
        p0?.setText(R.id.text_view_item_post_block, p1?.board_name)
        p0?.setText(R.id.text_view_item_post_title, p1?.title)

        p0?.setText(R.id.text_view_item_post_content, summary)
        p0?.setText(R.id.text_view_item_post_reply, p1?.replies)
        p0?.setText(R.id.text_view_item_post_hits, p1?.hits)
        p0?.setText(R.id.text_view_item_post_time, TimeUtils.stampToDate(p1?.last_reply_date))
        if(p0?.convertView?.visibility == View.VISIBLE) {
            Glide.with(p0.convertView.context).load(p1?.userAvatar)
                    .transform(GlideCircleTransform(p0.convertView.context))
                    .into(p0.getView(R.id.image_view_item_post_avatar))
        }
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_post
    }

    override fun setLoadEndView(loadEndView: View?) {
        super.setLoadEndView(loadEndView)
    }

    override fun setLoadEndView(loadEndId: Int) {
        super.setLoadEndView(loadEndId)
    }
}