package com.febers.uestc_bbs.module.post.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.ITEM_AUTHOR_ONLY
import com.febers.uestc_bbs.base.ITEM_ORDER_POSITIVE
import com.febers.uestc_bbs.entity.OptionItemBean
import com.febers.uestc_bbs.view.adapter.PostOptionAdapter
import kotlinx.android.synthetic.main.layout_bottom_sheet_option.*

class PostOptionBottomSheet(context: Context, style: Int, val itemClickListener: OptionClickListener):
        BottomSheetDialog(context, style) {

    private lateinit var optionItemAdapter: PostOptionAdapter
    private var optionList: MutableList<OptionItemBean> = ArrayList()
    private var orderPositive: Boolean = true
    private var authorOnly: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        optionList.addAll(getOptionList())
        optionItemAdapter = PostOptionAdapter(context, optionList)
        optionItemAdapter.setOnItemClickListener { viewHolder, optionItemBean, i ->
            if (i == ITEM_AUTHOR_ONLY) {
                authorOnly = !authorOnly
                optionList.clear()
                optionList.addAll(getOptionList())
                optionItemAdapter.notifyDataSetChanged()
                itemClickListener.onOptionItemSelect(ITEM_AUTHOR_ONLY)
            }
            if (i == ITEM_ORDER_POSITIVE) {
                orderPositive = !orderPositive
                optionList.clear()
                optionList.addAll(getOptionList())
                optionItemAdapter.notifyDataSetChanged()
                itemClickListener.onOptionItemSelect(ITEM_ORDER_POSITIVE)
            }
        }
        recyclerview_post_option.adapter = optionItemAdapter
    }

    private fun getOptionList(): List<OptionItemBean> {
        return arrayListOf(
                OptionItemBean(if (authorOnly)"查看所有" else "只看楼主",
                        if (authorOnly)R.drawable.ic_people_blue_24dp else R.drawable.ic_person_blue_24dp),
                OptionItemBean(if (orderPositive)"倒序查看" else "正序查看",
                        if (orderPositive)R.drawable.ic_order_navi_green_24dp else R.drawable.ic_order_posi_green_24dp),
                OptionItemBean("Web版", R.drawable.ic_web_purple_24dp),
                OptionItemBean("复制链接", R.drawable.ic_copy_red_24dp))
    }
}