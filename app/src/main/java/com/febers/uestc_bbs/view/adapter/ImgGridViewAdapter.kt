package com.febers.uestc_bbs.view.adapter

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.utils.ImageLoader


class ImgGridViewAdapter(private val context: Context, private val imgPaths: List<String>) : BaseAdapter() {

    private var imageClickListener: OnImageClickListener? = null

    override fun getCount(): Int {
        return imgPaths.size
    }

    override fun getItem(i: Int): Any {
        return imgPaths[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var view = view
        val holder: ViewHolder
        if (view == null || view.tag == null) {
            val inflater = (context as Activity).layoutInflater
            view = inflater.inflate(R.layout.item_layout_post_img, null)
            holder = ViewHolder()
            holder.btnDelete = view!!.findViewById(R.id.btn_img_item_post_edit)
            holder.image = view.findViewById(R.id.image_view_item_post_edit)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        ImageLoader.loadResource(context, imgPaths[i], holder.image, false)
        holder.image?.setOnClickListener { v ->
            imageClickListener?.onImageClick(i)
        }

        if (i == imgPaths.size - 1) {
            holder.btnDelete!!.visibility = View.GONE
        } else {
            holder.btnDelete!!.visibility = View.VISIBLE
        }
        /*
         * 点击图片上的删除按钮之后
         * 回调接口的监听器，通知图片已删除
         */
        holder.btnDelete!!.setOnClickListener { v ->
            imageClickListener?.onImageDeleteClick(i)
        }
        return view
    }

    internal inner class ViewHolder {
        var btnDelete: ImageView? = null
        var image: ImageView? = null
    }

    fun setImageClickListener(imageClickListener: OnImageClickListener) {
        this.imageClickListener = imageClickListener
    }

    interface OnImageClickListener {
        fun onImageClick(position: Int)
        fun onImageDeleteClick(position: Int)
    }
}


