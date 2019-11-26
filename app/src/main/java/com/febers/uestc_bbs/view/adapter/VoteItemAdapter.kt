package com.febers.uestc_bbs.view.adapter

import android.content.Context
import android.widget.CheckBox
import android.widget.ProgressBar
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.lib.baseAdapter.ViewHolder
import com.febers.uestc_bbs.lib.baseAdapter.base.CommonBaseAdapter

class VoteItemAdapter(val context: Context, data: List<PostDetailBean.TopicBean.PollInfoBean.PollItemListBean>, private val canVote: Boolean):
        CommonBaseAdapter<PostDetailBean.TopicBean.PollInfoBean.PollItemListBean>(context, data, false) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_vote
    }

    override fun convert(p0: ViewHolder?, p1: PostDetailBean.TopicBean.PollInfoBean.PollItemListBean?, p2: Int) {
        p0?.setText(R.id.text_view_vote_item_des, p1?.name)
        p0?.setText(R.id.text_view_vote_item_result, p1?.percent + "(${p1?.total_num})")
        p0?.getView<CheckBox>(R.id.check_box_vote_item)?.apply {
            isEnabled = canVote
            if (canVote) {
                this.isChecked = p1?.isSelectd == 1
            }
        }
        p0?.getView<ProgressBar>(R.id.progress_bar_vote_item)?.progress = p1?.percent!!.percentStringToIntValue()
    }

    private fun String.percentStringToIntValue():Int {
        return this.substring(0, this.length - 1).toFloat().toInt()
    }
}