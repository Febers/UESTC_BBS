/*
 * Created by Febers at 18-8-18 下午2:07.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-18 下午2:07.
 */

package com.febers.uestc_bbs.view.adapter

import android.content.Context
import android.view.View
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.utils.ImageLoader
import com.febers.uestc_bbs.utils.TimeUtils
import com.febers.uestc_bbs.view.utils.ContentViewHelper
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter


const val REPLY_IS_QUOTA = 1
class PostReplyItemAdapter(val context: Context, data: List<PostDetailBean.ListBean>, isLoadMore: Boolean):
        CommonBaseAdapter<PostDetailBean.ListBean>(context, data, isLoadMore) {

    override fun convert(p0: ViewHolder?, p1: PostDetailBean.ListBean?, p2: Int) {
        p0?.setText(R.id.text_view_post_reply_author, p1?.reply_name)
        p0?.setText(R.id.text_view_post_reply_date, TimeUtils.stampChange(p1?.posts_date))
        p0?.setText(R.id.text_view_post_reply_content, p1?.userTitle)
        p0?.setText(R.id.text_view_post_reply_floor, "#"+(p1?.position?.minus(1)))

        ContentViewHelper.create(p0?.convertView?.findViewById(R.id.linear_layout_post_reply), p1?.reply_content)
        if (p1?.is_quote == REPLY_IS_QUOTA) {
            p0?.setVisibility(R.id.linear_layout_post_reply_quota, View.VISIBLE)
            p0?.setText(R.id.text_view_post_reply_quota, p1.quote_content?.multiLineSpaces())
        }
        ImageLoader.load(context, p1?.icon, p0?.getView(R.id.image_view_post_reply_author_avatar))

        //将Helper中预加载的图片填充到view中
        ContentViewHelper.getImageUrls().forEachIndexed { index, s ->
            ContentViewHelper.getImageViews()[index].apply {
                ImageLoader.usePreload(context = context, url = ContentViewHelper.getImageUrls()[index], imageView = this)
            }
        }
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_post_reply
    }

    //将引用的回复中的前缀跟内容分得更开
    private fun String.multiLineSpaces(): String = replace("\n", "\n\n")
}