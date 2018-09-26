package com.febers.uestc_bbs.view.adapter

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.MsgReplyBean
import com.febers.uestc_bbs.utils.GlideLoadUtils
import com.febers.uestc_bbs.utils.TimeUtils
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class MsgReplyAdapter(val context: Context, data: List<MsgReplyBean.ListBean>, isLoadMore: Boolean):
        CommonBaseAdapter<MsgReplyBean.ListBean>(context, data, isLoadMore) {
    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_msg_reply
    }

    override fun convert(p0: ViewHolder?, p1: MsgReplyBean.ListBean?, p2: Int) {
        p0?.setText(R.id.text_view_msg_reply_author, p1?.board_name)
        p0?.setText(R.id.text_view_msg_reply_quota, p1?.topic_content)
        p0?.setText(R.id.text_view_msg_reply_content, p1?.reply_content)
        p0?.setText(R.id.text_view_msg_reply_date, TimeUtils.stampChange(p1?.replied_date))
        GlideLoadUtils.load(context, p1?.icon, p0?.getView(R.id.image_view_msg_reply_author_avatar))
    }
}