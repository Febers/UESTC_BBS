/*
 * Created by Febers at 18-8-18 下午2:07.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-18 下午2:07.
 */

package com.febers.uestc_bbs.adaper

import android.content.Context
import android.view.View
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.PostReplyBean
import com.febers.uestc_bbs.module.post.utils.PostContentViewUtils
import com.febers.uestc_bbs.view.utils.GlideCircleTransform
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter


const val REPLY_IS_QUOTA = "1"
class PostReplyItemAdapter(context: Context, data: List<PostReplyBean>, isLoadMore: Boolean):
        CommonBaseAdapter<PostReplyBean>(context, data, isLoadMore) {

    override fun convert(p0: ViewHolder?, p1: PostReplyBean?, p2: Int) {
        p0?.setText(R.id.text_view_post_reply_author, p1?.reply_name)
        p0?.setText(R.id.text_view_post_reply_date, p1?.posts_data)
        p0?.setText(R.id.text_view_post_reply_title, p1?.userTitle)
        p0?.setText(R.id.text_view_post_reply_floor, "#"+p1?.position)
//        p0?.setText(R.id.text_view_post_reply_content, p1?.reply_content?.get(0)?.infor)
        //支持表情
//        ImageTextUtil.setImageText(p0?.getView(R.id.text_view_post_reply_content),
//                PostContentViewUtils.emotionTransform(p1?.reply_content?.get(0)?.infor).encodeSpaces())
        PostContentViewUtils.creat(p0?.convertView?.context, p0?.convertView?.findViewById(R.id.linear_layout_post_reply), p1?.reply_content)
        if (p1?.is_quote == REPLY_IS_QUOTA) {
            p0?.setVisibility(R.id.linear_layout_post_reply_quota, View.VISIBLE)
            p0?.setText(R.id.text_view_post_reply_quota, p1?.quote_content?.multiLineSpaces())
        }
        Glide.with(p0?.convertView?.context).load(p1?.icon)
                .transform(GlideCircleTransform(p0?.convertView?.context))
                .into(p0?.getView(R.id.image_view_post_reply_author_avatar))
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_post_reply
    }

    //将引用的回复中的前缀跟内容分得更开
    private fun String.multiLineSpaces(): String = replace("\n", "\n\n")
}