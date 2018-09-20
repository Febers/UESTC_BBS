package com.febers.uestc_bbs.utils

import android.content.Context
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.febers.uestc_bbs.R

object GlideLoadUtils {

    private val TAG = "GlideLoadUtils"

    /**
     * Glide 加载 拦截异步加载数据时Glide 抛出的异常
     *
     * @param context
     * @param url           加载图片的url地址  String
     * @param imageView     加载图片的ImageView 控件
     * @param placeImage 图片展示错误的本地图片 id
     */
    fun load(context: Context?, url: String?, imageView: ImageView?, placeImage: Int = R.mipmap.ic_default_avatar) {
        try {
            Glide.with(context).load(url).placeholder(placeImage).crossFade().into(imageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
