package com.febers.uestc_bbs.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.IconItemBean

class IconGridViewAdapter(private val context: Context,
                          private val icons: List<IconItemBean>,
                          private val clickListener: (position: Int) -> Unit): BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: IconGridViewAdapter.ViewHolder
        var view = convertView
        if (view == null || view.tag == null) {
            viewHolder = ViewHolder()
            view = LayoutInflater.from(context).inflate(R.layout.item_layout_choose_icon, null)
            viewHolder.iconImage = view.findViewById(R.id.image_view_icon_item)
            (viewHolder.iconImage as ImageView).setImageResource(icons[position].image)
            viewHolder.iconIsChoose = view.findViewById(R.id.image_view_icon_chosen_item)
            viewHolder.itemLayout = view.findViewById(R.id.linear_layout_icon_item)
        } else {
            viewHolder = view.tag as ViewHolder
        }
        if (icons[position].isChoose) {
            (viewHolder.iconIsChoose as ImageView).visibility = View.VISIBLE
        } else {
            (viewHolder.iconIsChoose as ImageView).visibility = View.INVISIBLE
        }
        viewHolder.itemLayout?.setOnClickListener { clickListener(position) }
        return view!!
    }

    override fun getItem(position: Int): Any {
        return icons[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return icons.count()
    }

    class ViewHolder {
        var iconImage: ImageView? = null
        var iconIsChoose: ImageView? = null
        var itemLayout: LinearLayout? = null
    }
}