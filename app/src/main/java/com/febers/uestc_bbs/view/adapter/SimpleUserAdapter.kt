package com.febers.uestc_bbs.view.adapter

import android.content.Context
import android.view.View
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.io.UserManager
import com.febers.uestc_bbs.entity.UserSimpleBean
import com.febers.uestc_bbs.lib.baseAdapter.ViewHolder
import com.febers.uestc_bbs.lib.baseAdapter.base.CommonBaseAdapter
import com.febers.uestc_bbs.view.custom.CircleColorView

class SimpleUserAdapter(val context: Context, val data: MutableList<UserSimpleBean>):
        CommonBaseAdapter<UserSimpleBean>(context, data, false) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_user_simple
    }

    override fun convert(p0: ViewHolder?, p1: UserSimpleBean?, p2: Int) {
        p0?.setText(R.id.text_view_user_item_name, p1?.name)
        if (p1?.uid == UserManager.getNowUid()) {
            p0?.getView<CircleColorView>(R.id.color_view_user_now)?.visibility = View.VISIBLE
        } else {
            p0?.getView<CircleColorView>(R.id.color_view_user_now)?.visibility = View.INVISIBLE
        }
    }
}