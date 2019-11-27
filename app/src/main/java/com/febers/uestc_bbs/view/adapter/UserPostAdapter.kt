package com.febers.uestc_bbs.view.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.febers.uestc_bbs.MyApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.UserPostBean
import com.febers.uestc_bbs.lib.baseAdapter.ViewHolder
import com.febers.uestc_bbs.lib.baseAdapter.base.CommonBaseAdapter
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.utils.TimeUtils

class UserPostAdapter(val context: Context, data: List<UserPostBean.ListBean>):
        CommonBaseAdapter<UserPostBean.ListBean>(context, data, false) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_user_post
    }

    override fun convert(holder: ViewHolder?, bean: UserPostBean.ListBean?, p2: Int) {
        holder ?: return
        bean ?: return
        holder.setText(R.id.text_view_item_user_post_title, bean.title)
        holder.getView<ImageView>(R.id.image_view_item_user_post_avatar).let {
            if (MyApp.postItemVisibleSetting.contains(0)) {
                ImageLoader.load(context, bean.userAvatar, it)
            } else {
                it.visibility = View.GONE
            }
        }
        holder.getView<TextView>(R.id.text_view_item_user_post_time).let {
            if (MyApp.postItemVisibleSetting.contains(1)) {
                it.text = TimeUtils.stampChange(bean.last_reply_date)
            } else {
                it.visibility = View.GONE
            }
        }
        holder.getView<TextView>(R.id.text_view_item_user_post_content).let {
            if (MyApp.postItemVisibleSetting.contains(2)) {
                it.text = bean.subject
            } else {
                it.visibility = View.GONE
            }
        }
        holder.getView<TextView>(R.id.text_view_item_user_post_reply).let {
            if (MyApp.postItemVisibleSetting.contains(5)) {
                it.text = bean.replies.toString()
            } else {
                it.visibility = View.GONE
            }
        }
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