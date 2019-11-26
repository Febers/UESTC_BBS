package com.febers.uestc_bbs.module.dialog

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.widget.CompoundButtonCompat
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.lib.baseAdapter.ViewHolder
import com.febers.uestc_bbs.lib.baseAdapter.base.CommonBaseAdapter
import com.febers.uestc_bbs.utils.colorAccent

class ListAdapter(context: Context, data: List<String>): CommonBaseAdapter<String>(context, data, false) {

    override fun convert(holder: ViewHolder?, data: String?, position: Int) {
        holder?.setText(R.id.tv_item_list_dialog, data)
    }

    override fun getItemLayoutId(): Int = R.layout.item_layout_list_dialog
}

class ChoiceAdapter(context: Context,
                    titles: List<String>,
                    private val messages: List<String>,
                    var checked: List<Int>):
        CommonBaseAdapter<String>(context, titles, false) {

    override fun convert(holder: ViewHolder?, data: String?, position: Int) {
        holder?.setText(R.id.tv_title_item_choice_dialog, data)
        if (messages.isNotEmpty() && position < messages.size) {
            holder?.getView<TextView>(R.id.tv_des_item_choice_dialog)?.apply {
                text = messages[position]
                visibility = View.VISIBLE
            }
        }
        holder?.getView<CheckBox>(R.id.check_item_choice_dialog)?.apply {
            CompoundButtonCompat.setButtonTintList(this,
                    ColorStateList(arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()), intArrayOf(colorAccent(), Color.GRAY)))
            isChecked = checked.contains(position)
        }
    }

    override fun getItemLayoutId(): Int = R.layout.item_layout_choice_list_dialog
}