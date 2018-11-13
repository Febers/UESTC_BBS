package com.febers.uestc_bbs.view.adapter

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.utils.TimeUtils
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class PostListAdapter(val context: Context, data: List<PostListBean.ListBean>):
        CommonBaseAdapter<PostListBean.ListBean>(context, data, false) {

    override fun convert(p0: ViewHolder?, p1: PostListBean.ListBean?, p2: Int) {
        var summary = p1?.subject   //普通帖子
        if (summary == null) {
            summary = p1?.summary   //热门帖子
        }
        p0?.setText(R.id.text_view_item_poster, p1?.user_nick_name)
        p0?.setText(R.id.text_view_item_post_block, p1?.board_name)
        p0?.setText(R.id.text_view_item_post_title, p1?.title)

        p0?.setText(R.id.text_view_item_post_content, summary)
        p0?.setText(R.id.text_view_item_post_reply, p1?.replies.toString())
        p0?.setText(R.id.text_view_item_post_hits, p1?.hits.toString())
        p0?.setText(R.id.text_view_item_post_time, TimeUtils.stampChange(p1?.last_reply_date))

        ImageLoader.load(context, p1?.userAvatar, p0?.getView(R.id.image_view_item_post_avatar))
    }

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_post
    }
}

class StickyPostAdapter(val context: Context, data: List<PostListBean.TopTopicListBean>):
        CommonBaseAdapter<PostListBean.TopTopicListBean>(context, data, false) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_sticky_post
    }

    override fun convert(p0: ViewHolder?, p1: PostListBean.TopTopicListBean?, p2: Int) {
        p0?.setText(R.id.text_view_item_sticky_post, p1?.title)
    }
}