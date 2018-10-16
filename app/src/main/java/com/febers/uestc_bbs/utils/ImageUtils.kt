package com.febers.uestc_bbs.utils

import android.content.Context
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.helper.BlurTransformation
import com.febers.uestc_bbs.view.helper.GlideCircleTransform
import com.febers.uestc_bbs.view.helper.GlideImageGetter

object ImageLoader {

    private val TAG = "ImageLoader"

    /**
     * Glide 加载 拦截异步加载数据时Glide 抛出的异常
     * 通常用于加载头像
     * @param context
     * @param url           加载图片的url地址  String
     * @param imageView     加载图片的ImageView 控件
     * @param placeImage 图片加载过程中的本地图片 id
     */
    fun load(context: Context?, url: String?, imageView: ImageView?,
             placeImage: Int? = R.mipmap.ic_default_avatar,
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
                        if (placeImage != null) {
                            this.placeholder(placeImage)
                        }
                        this.error(R.mipmap.image_error_200200)
                        this.crossFade()
                    }
                    .into(imageView)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        imageView?.setOnClickListener {
            ViewClickUtils.clickToImageViewer(url, context)
        }
    }

    /**
     * 预加载图片，防止边绘制ImageView边加载图片，边滑动时的卡顿问题
     * @param context Context
     * @param url     加载图片的url地址  String
     */
    fun preload(context: Context?, url: String?) {
        try {
            Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.SOURCE).preload()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 使用在后台预加载的图片，由于此时视图已经绘制完毕
     * 只需要将Glide加载的图片填充进ImageView即可
     * 通常用于加载帖子中的内容
     * @param context
     * @param url        加载图片的url地址  String
     * @param imageView  加载图片的ImageView 控件
     */
    fun usePreload(context: Context?, url: String?, imageView: ImageView?) {
        Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.mipmap.image_placeholder_400200)
                .error(R.mipmap.image_error_400200)
                .into(imageView)
    }

    fun loadViewTarget(context: Context?, url: String?, viewTarget: GlideImageGetter.ImageGetterViewTarget,
                       noCache: Boolean) {
        try {
            Glide.with(context).load(url)
                    .apply {
                        if (noCache) {
                            diskCacheStrategy(DiskCacheStrategy.NONE)
                        }
                    }
                    .error(R.mipmap.ic_place_holder_grey)
                    .crossFade().into(viewTarget)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        viewTarget.view.setOnClickListener {
            //ViewClickUtils.clickToImageViewer(url, context)
        }
    }
}
