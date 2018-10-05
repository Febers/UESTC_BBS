package com.febers.uestc_bbs.utils

import android.content.Context
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.utils.BlurTransformation
import com.febers.uestc_bbs.view.utils.GlideCircleTransform
import com.febers.uestc_bbs.view.utils.GlideImageGetter

object ImageLoader {

    private val TAG = "ImageLoader"

    /**
     * Glide 加载 拦截异步加载数据时Glide 抛出的异常
     *
     * @param context
     * @param url           加载图片的url地址  String
     * @param imageView     加载图片的ImageView 控件
     * @param placeImage 图片展示错误的本地图片 id
     */
    fun load(context: Context?, url: String?, imageView: ImageView?,
             placeImage: Int = R.mipmap.ic_default_avatar,
             isCircle: Boolean = true,
             isBlur: Boolean = false,
             noCache: Boolean = false) {
        try {
            Glide.with(context).load(url)
                    .apply {
                        if (isCircle) {
                            this.transform(GlideCircleTransform(context))
                        }
                        if (isBlur) {
                            this.transform(BlurTransformation(context))
                        }
                        if (noCache) {
                            this.diskCacheStrategy(DiskCacheStrategy.NONE)
                        }
                        this.placeholder(placeImage)
                        this.error(R.mipmap.ic_default_avatar)
                        this.crossFade()
                    }
                    .into(imageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        imageView?.setOnClickListener {
            ViewClickUtils.clickToImgBig(url, context)
        }
    }

    fun loadViewTarget(context: Context?, url: String?, viewTarget: GlideImageGetter.ImageGetterViewTarget) {
        try {
            Glide.with(context).load(url)
                    .placeholder(R.mipmap.ic_place_holder_grey)
                    .error(R.mipmap.ic_place_holder_grey)
                    .crossFade().into(viewTarget)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        viewTarget.view.setOnClickListener {
            ViewClickUtils.clickToImgBig(url, context)
        }
    }
}
