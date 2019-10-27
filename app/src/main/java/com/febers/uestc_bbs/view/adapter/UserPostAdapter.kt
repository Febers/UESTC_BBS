package com.febers.uestc_bbs.view.adapter

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.UserPostBean
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.utils.TimeUtils
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class UserPostAdapter(val context: Context, data: List<UserPostBean.ListBean>):
        CommonBaseAdapter<UserPostBean.ListBean>(context, data, false) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_user_post
    }

    override fun convert(p0: ViewHolder?, p1: UserPostBean.ListBean?, p2: Int) {
        p0?.setText(R.id.text_view_item_user_post_title, p1?.title)
        p0?.setText(R.id.text_view_item_user_post_content, p1?.subject)
        p0?.setText(R.id.text_view_item_user_post_reply, ""+p1?.replies)
        p0?.setText(R.id.text_view_item_user_post_time, TimeUtils.stampChange(p1?.last_reply_date))
        ImageLoader.load(context, p1?.userAvatar, p0?.getView(R.id.image_view_item_user_post_avatar))
    }
}

class SimplePostAdapter(context: Context, data: List<UserPostBean.ListBean>): CommonBaseAdapter<UserPostBean.ListBean>(context, data, false) {

    override fun getItemLayoutId(): Int = R.layout.item_layout_user_post_simple

    override fun convert(holder: ViewHolder?, data: UserPostBean.ListBean?, position: Int) {
        holder?.setText(R.id.tv_item_user_post_title, data?.title)
        holder?.setText(R.id.tv_item_user_post_time, TimeUtils.stampChange(data?.last_reply_date))
        holder?.setText(R.id.tv_item_user_post_reply, ""+data?.replies)
    }
}