package com.febers.uestc_bbs.view.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.PostListBean
import com.febers.uestc_bbs.lib.baseAdapter.ViewHolder
import com.febers.uestc_bbs.lib.baseAdapter.base.CommonBaseAdapter
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.utils.TimeUtils

class PostListAdapter(val context: Context, data: List<PostListBean.ListBean>, val showBoardName: Boolean = true):
        CommonBaseAdapter<PostListBean.ListBean>(context, data, true) {

    override fun convert(holder: ViewHolder?, bean: PostListBean.ListBean?, p2: Int) {
        holder ?: return
        bean ?: return
        var summary = bean.subject   //普通帖子
        if (summary == null) {
            summary = bean.summary   //热门帖子
        }
        holder.setText(R.id.text_view_item_post_title, bean.title)
        holder.getView<ImageView>(R.id.image_view_item_post_avatar).let {
            if (MyApp.postItemVisibleSetting.contains(0)) {
                it.visibility = View.VISIBLE
                ImageLoader.load(context, bean.userAvatar, holder.getView(R.id.image_view_item_post_avatar))
            } else {
                it.visibility = View.GONE
            }
        }
        holder.getView<TextView>(R.id.text_view_item_poster).let {
            if (MyApp.postItemVisibleSetting.contains(1)) {
                it.visibility = View.VISIBLE
                it.text = bean.user_nick_name
            } else {
                it.visibility = View.GONE
            }
        }
        holder.getView<TextView>(R.id.text_view_item_post_time).let {
            if (MyApp.postItemVisibleSetting.contains(1)) {
                it.visibility = View.VISIBLE
                it.text = TimeUtils.stampChange(bean.last_reply_date)
            } else {
                it.visibility = View.GONE
            }
        }
        holder.getView<TextView>(R.id.text_view_item_post_content).let {
            if (MyApp.postItemVisibleSetting.contains(2)) {
                it.visibility = View.VISIBLE
                it.text = summary
            } else {
                it.visibility = View.GONE
            }
        }
        holder.getView<TextView>(R.id.text_view_item_post_block).let {
            if (MyApp.postItemVisibleSetting.contains(3) && showBoardName) {
                it.visibility = View.VISIBLE
                it.text = bean.board_name
            } else {
                it.visibility = View.GONE
            }
        }
        holder.getView<TextView>(R.id.text_view_item_post_hits).let {
            if (MyApp.postItemVisibleSetting.contains(4)) {
                it.visibility = View.VISIBLE
                it.text = bean.hits.toString()
            } else {
                it.visibility = View.GONE
                holder.getView<ImageView>(R.id.image_view_item_post_hits).visibility = View.GONE
            }
        }
        holder.getView<TextView>(R.id.text_view_item_post_reply).let {
            if (MyApp.postItemVisibleSetting.contains(5)) {
                it.visibility = View.VISIBLE
                it.text = bean.replies.toString()
            } else {
                it.visibility = View.GONE
                holder.getView<ImageView>(R.id.image_view_reply_list).visibility = View.GONE
            }
        }
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