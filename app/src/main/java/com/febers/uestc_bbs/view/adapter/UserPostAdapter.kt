package com.febers.uestc_bbs.view.adapter

import android.content.Context
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.UserPListBean
import com.febers.uestc_bbs.utils.ImageLoader
import com.febers.uestc_bbs.utils.TimeUtils
import com.febers.uestc_bbs.view.utils.GlideCircleTransform
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class UserPostAdapter(val context: Context, data: List<UserPListBean.ListBean>, isLoadMore: Boolean):
        CommonBaseAdapter<UserPListBean.ListBean>(context, data, isLoadMore) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_user_post
    }

    override fun convert(p0: ViewHolder?, p1: UserPListBean.ListBean?, p2: Int) {
        p0?.setText(R.id.text_view_item_user_post_title, p1?.title)
        p0?.setText(R.id.text_view_item_user_post_content, p1?.subject)
        p0?.setText(R.id.text_view_item_user_post_reply, ""+p1?.replies!!)
        p0?.setText(R.id.text_view_item_user_post_hits, ""+p1?.hits!!)
        p0?.setText(R.id.text_view_item_user_post_time, TimeUtils.stampChange(p1?.last_reply_date))
        ImageLoader.load(context, p1?.userAvatar, p0?.getView(R.id.image_view_item_user_post_avatar))
    }
}