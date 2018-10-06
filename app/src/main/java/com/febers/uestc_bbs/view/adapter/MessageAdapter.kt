package com.febers.uestc_bbs.view.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.MsgAtBean
import com.febers.uestc_bbs.entity.MsgPrivateBean
import com.febers.uestc_bbs.entity.MsgReplyBean
import com.febers.uestc_bbs.entity.MsgSystemBean
import com.febers.uestc_bbs.utils.ImageLoader
import com.febers.uestc_bbs.utils.TimeUtils
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

interface MsgBaseAdapter

class MsgPrivateAdapter(val context: Context, data: List<MsgPrivateBean.BodyBean.ListBean>, isLoadMore: Boolean):
        CommonBaseAdapter<MsgPrivateBean.BodyBean.ListBean>(context, data, isLoadMore), MsgBaseAdapter {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_msg_private
    }

    override fun convert(p0: ViewHolder?, p1: MsgPrivateBean.BodyBean.ListBean?, p2: Int) {
        if(p1?.isNew == 1) {  //未读
            p0?.getView<ImageView>(R.id.image_view_msg_private_unread)?.visibility = View.VISIBLE
        }
        p0?.setText(R.id.text_view_msg_private_author, p1?.toUserName)
        p0?.setText(R.id.text_view_msg_private_date, TimeUtils.stampChange(p1?.lastDateline))
        p0?.setText(R.id.text_view_msg_private_content, p1?.lastSummary)
        ImageLoader.load(context, p1?.toUserAvatar, p0?.getView(R.id.image_view_msg_private_author_avatar))
    }
}

class MsgReplyAdapter(val context: Context, data: List<MsgReplyBean.ListBean>, isLoadMore: Boolean):
        CommonBaseAdapter<MsgReplyBean.ListBean>(context, data, isLoadMore), MsgBaseAdapter {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_msg_reply
    }

    override fun convert(p0: ViewHolder?, p1: MsgReplyBean.ListBean?, p2: Int) {
        p0?.setText(R.id.text_view_msg_reply_author, p1?.reply_nick_name)
        p0?.setText(R.id.text_view_msg_reply_quota, p1?.topic_content)
        p0?.setText(R.id.text_view_msg_reply_content, p1?.reply_content)
        p0?.setText(R.id.text_view_msg_reply_date, TimeUtils.stampChange(p1?.replied_date))
        ImageLoader.load(context, p1?.icon, p0?.getView(R.id.image_view_msg_reply_author_avatar))
    }
}

class MsgSystemAdapter(val context: Context, data: List<MsgSystemBean.BodyBean.DataBean>, isLoadMore: Boolean):
        CommonBaseAdapter<MsgSystemBean.BodyBean.DataBean>(context, data, isLoadMore), MsgBaseAdapter {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_msg_system
    }

    override fun convert(p0: ViewHolder?, p1: MsgSystemBean.BodyBean.DataBean?, p2: Int) {
        p0?.setText(R.id.text_view_msg_system_author, p1?.type)
        p0?.setText(R.id.text_view_msg_system_content, p1?.note)
        p0?.setText(R.id.text_view_msg_system_date, TimeUtils.stampChange(p1?.dateline))
        ImageLoader.load(context, p1?.authorAvatar, p0?.getView(R.id.image_view_msg_system_author_avatar))
    }
}

class MsgAtAdapter(val context: Context, data: List<MsgAtBean.ListBean>, isLoadMore: Boolean):
        CommonBaseAdapter<MsgAtBean.ListBean>(context, data, isLoadMore), MsgBaseAdapter {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_msg_at
    }

    override fun convert(p0: ViewHolder?, p1: MsgAtBean.ListBean?, p2: Int) {
        p0?.setText(R.id.text_view_msg_at_author, p1?.reply_nick_name)
        p0?.setText(R.id.text_view_msg_at_date, TimeUtils.stampChange(p1?.replied_date))
        p0?.setText(R.id.text_view_msg_at_quota, p1?.topic_content)
        p0?.setText(R.id.text_view_msg_at_content, p1?.reply_content)
        ImageLoader.load(context, p1?.icon, p0?.getView(R.id.image_view_msg_at_author_avatar))
    }
}
