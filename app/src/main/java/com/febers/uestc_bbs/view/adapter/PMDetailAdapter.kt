package com.febers.uestc_bbs.view.adapter

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.PMDetailBean
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.utils.PMTimeUtils
import com.febers.uestc_bbs.utils.TimeUtils
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class PMDetailAdapter(val context: Context, data: List<PMDetailBean.BodyBean.PmListBean.MsgListBean>,
                      private val userId: Int, private val timeUtils: PMTimeUtils):
        CommonBaseAdapter<PMDetailBean.BodyBean.PmListBean.MsgListBean>(context, data, false) {
    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_pm_detail
    }

    override fun convert(p0: ViewHolder?, p1: PMDetailBean.BodyBean.PmListBean.MsgListBean?, p2: Int) {
        val leftLayout: LinearLayout = p0?.getView(R.id.linear_layout_pm_left)!!
        val rightLayout: LinearLayout = p0.getView(R.id.linear_layout_pm_right)!!
        if (p1?.type == "text") {
            val textView = TextView(context)
            textView.text = p1.content
            if (userId == p1.sender) {
                textView.setTextColor(Color.WHITE)
                rightLayout.addView(textView)
                leftLayout.visibility = View.GONE
            } else {
                textView.setTextColor(Color.DKGRAY)
                leftLayout.addView(textView)
                rightLayout.visibility = View.GONE
            }
        }
        if (p1?.type == "image") {
            val imageView = ImageView(context)
            if (userId == p1.sender) {
                rightLayout.addView(imageView)
            } else {
                leftLayout.addView(imageView)
            }
            ImageLoader.load(context, p1.content, imageView, isCircle = false)
        }
        if (timeUtils.isShowTime(p1?.time)) {
            (p0.getView(R.id.text_view_pm_time) as TextView).visibility = View.VISIBLE
            p0.setText(R.id.text_view_pm_time, TimeUtils.stampChange(p1?.time))
        } else {
            (p0.getView(R.id.text_view_pm_time) as TextView).visibility = View.GONE
        }
    }
}