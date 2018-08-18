/*
 * Created by Febers at 18-8-18 下午2:07.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-18 下午2:07.
 */

package com.febers.uestc_bbs.adaper

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.PostReplyBean
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class PostReplyItemAdapter(context: Context, data: List<PostReplyBean>, isLoadMore: Boolean):
        CommonBaseAdapter<PostReplyBean>(context, data, isLoadMore) {

    override fun convert(p0: ViewHolder?, p1: PostReplyBean?, p2: Int) {

    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_post_reply
    }
}