package com.febers.uestc_bbs.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.ThemeItemBean
import com.febers.uestc_bbs.view.custom.CircleColorView

class ThemeGridViewAdapter(private val context: Context,
                           private val themeItems: List<ThemeItemBean>,
                           private val onItemClickListener: (position: Int) -> Unit): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        var view = convertView
        if (view == null || view.tag == null) {
            viewHolder = ViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.item_layout_theme_choose, null)
            viewHolder.circleColorView = view.findViewById(R.id.color_view_theme_choose)
            viewHolder.linearLayout = view.findViewById(R.id.linear_layout_theme_choose_item)
        } else {
            viewHolder = view.tag as ViewHolder
        }
        viewHolder.linearLayout?.setOnClickListener { onItemClickListener(position) }
        viewHolder.circleColorView?.setColor(themeItems[position].color)
        return view!!
    }

    override fun getItem(position: Int): Any {
        return themeItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return themeItems.size
    }

    class ViewHolder {
        var circleColorView: CircleColorView? = null
        var linearLayout: LinearLayout? = null
    }
}