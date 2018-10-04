/*
 * Created by Febers at 18-8-15 下午11:32.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-15 下午11:32.
 */

package com.febers.uestc_bbs.view.adapter

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.SimplePListBean
import com.febers.uestc_bbs.utils.ImageLoader
import com.febers.uestc_bbs.utils.TimeUtils
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class PostSimpleAdapter(val context: Context, data: List<SimplePListBean>, isLoadMore: Boolean):
        CommonBaseAdapter<SimplePListBean>(context, data, isLoadMore) {

    private var isScrolling = false

    override fun convert(p0: ViewHolder?, p1: SimplePListBean?, p2: Int) {
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
        p0?.setText(R.id.text_view_item_post_time, TimeUtils.stampChange(p1?.last_reply_date))
        if(!isScrolling) {
            ImageLoader.load(context, p1?.userAvatar, p0?.getView(R.id.image_view_item_post_avatar))
        }
    }

    fun setScrolling(scrolling: Boolean) {
        isScrolling = scrolling
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_post
    }
}