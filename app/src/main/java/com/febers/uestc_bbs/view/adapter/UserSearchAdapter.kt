package com.febers.uestc_bbs.view.adapter

import android.content.Context
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.SearchUserBeanList
import com.febers.uestc_bbs.module.image.ImageLoader
import com.othershe.baseadapter.ViewHolder
import com.othershe.baseadapter.base.CommonBaseAdapter

class UserSearchAdapter(val context: Context, data: List<SearchUserBeanList>):
        CommonBaseAdapter<SearchUserBeanList>(context, data, false) {

    override fun getItemLayoutId(): Int {
        return R.layout.item_layout_user_search
    }

    override fun convert(holder: ViewHolder?, data: SearchUserBeanList?, position: Int) {
        holder?.setText(R.id.tv_user_name_user_search, data?.name)
        ImageLoader.load(context, data?.icon, holder?.getView(R.id.iv_user_avatar_user_search))
    }
}