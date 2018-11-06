package com.febers.uestc_bbs.utils

import android.content.Context
import android.widget.ImageView

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.febers.uestc_bbs.GlideApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.view.helper.GlideImageGetter
import jp.wasabeef.glide.transformations.BlurTransformation

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
             noCache: Boolean = false,
             clickToViewer: Boolean = true) {
        try {
            GlideApp.with(context!!).load(url)
                    .apply {
                        if (isCircle) {
                            this.apply(RequestOptions.bitmapTransform(CircleCrop()))
                        }
                        if (isBlur) {
                            this.apply(RequestOptions.bitmapTransform(BlurTransformation()))
                        }
                        if (noCache) {
                            this.diskCacheStrategy(DiskCacheStrategy.NONE)
                        }
                        if (placeImage != null) {
                            this.placeholder(placeImage)
                        }
                        this.error(R.mipmap.image_error_200200)
                    }
                    .transition(withCrossFade())
                    .into(imageView!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (clickToViewer) {
            imageView?.setOnClickListener {
                ViewClickUtils.clickToImageViewer(url, context)
            }
        }
    }

    fun loadResource(context: Context?, resourceId: Int, imageView: ImageView?, isCircle: Boolean) {
        GlideApp.with(context!!).load(resourceId)
                .apply {
                    if (isCircle) {
                        this.apply(RequestOptions.bitmapTransform(CircleCrop()))
                    }
                }
                .into(imageView!!)
    }
    /**
     * 预加载图片，防止边绘制ImageView边加载图片，边滑动时的卡顿问题
     * @param context Context
     * @param url     加载图片的url地址  String
     */
    fun preload(context: Context?, url: String?) {
        try {
            GlideApp.with(context!!).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .preload()
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
        try {
            GlideApp.with(context!!).load(url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(R.mipmap.image_placeholder_400200)
                    .error(R.mipmap.image_error_400200)
                    .transition(withCrossFade())
                    .into(imageView!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadViewTarget(context: Context?,
                       url: String?,
                       viewTarget: GlideImageGetter.ImageGetterViewTarget,
                       noCache: Boolean) {
        try {
            GlideApp.with(context!!).load(url)
                    .apply {
                        if (noCache) {
                            diskCacheStrategy(DiskCacheStrategy.NONE)
                        }
                    }
                    .error(R.mipmap.ic_place_holder_grey)
                    .into(viewTarget)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


