package com.febers.uestc_bbs.view.adapter

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.ProjectItemBean
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class OpenProjectAdapter(val context: Context, data: List<ProjectItemBean>):
        CommonBaseAdapter<ProjectItemBean>(context, data, false) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_open_source_projects
    }

    override fun convert(p0: ViewHolder?, p1: ProjectItemBean?, p2: Int) {
        p0?.setText(R.id.text_view_item_open_source_name, p1?.name)
        p0?.setText(R.id.text_view_item_open_source_author, p1?.author)
        p0?.setText(R.id.text_view_item_open_source_des, p1?.des)
    }
}