package com.febers.uestc_bbs.module.image

import android.app.Activity
import android.content.Context
import android.widget.ImageView

import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.febers.uestc_bbs.GlideApp
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.view.helper.GlideImageGetter
import jp.wasabeef.glide.transformations.BlurTransformation

object ImageLoader {

    /**
     * Glide 加载 拦截异步加载数据时Glide 抛出的异常
     *
     * @param context 所有加载图片的方法都拦截了异常，因为Context销毁之后Glide会报错，非常频繁
     * @param url           加载图片的url地址  String
     * @param imageView     加载图片的ImageView 控件
     * @param placeImage 图片加载过程中的本地图片 id
     */
    fun load(context: Context?, url: String?, imageView: ImageView?,
             placeImage: Int? = R.drawable.ic_default_avatar_circle,
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
                        this.error(R.drawable.image_error_200200)
                    }
                    .transition(withCrossFade())
                    .into(imageView!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (clickToViewer) {
            imageView?.setOnClickListener {
                ClickContext.clickToImageViewer(url, context)
            }
        }
    }

    /**
     * 通常用于加载帖子里的图片
     *
     * @param context 所有加载图片的方法都拦截了异常，因为Context销毁之后Glide会报错，非常频繁
     * @param url           加载图片的url地址  String
     * @param imageView     加载图片的ImageView 控件
     */
    fun loadForContent(context: Context?, url: String?, imageView: ImageView?,
                       placeImage: Int? = R.drawable.xic_placeholder_empty,
                       clickToViewer: Boolean = true) {
        val sizeOptions = RequestOptions().override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
        try {
            GlideApp.with(context!!).load(url)
                    .apply {
                        if (placeImage != null) {
                            this.placeholder(placeImage)
                        }
                    }
                    .error(R.drawable.image_error_400200)
                    .apply(sizeOptions)
                    .centerInside()
                    .into(imageView!!)

        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (clickToViewer) {
            imageView?.setOnClickListener {
                ClickContext.clickToImageViewer(url, context)
            }
        }
    }

    /**
     * 加载本地资源文件
     *
     * @param context
     * @param resourceId
     * @param imageView
     * @param isCircle 是否加载圆形图片，默认false
     */
    fun loadResource(context: Context?, resourceId: Int, imageView: ImageView?, isCircle: Boolean = false) {
        try {
            GlideApp.with(context!!).load(resourceId)
                    .apply {
                        if (isCircle) {
                            this.apply(RequestOptions.bitmapTransform(CircleCrop()))
                        }
                    }
                    .into(imageView!!)
        } catch (e: Exception) {}
    }

    /**
     * 加载本地资源文件
     *
     * @param context
     * @param path
     * @param imageView
     * @param isCircle 是否加载圆形图片，默认false
     */
    fun loadResource(context: Context?, path: String, imageView: ImageView?, isCircle: Boolean = false) {
        try {
            GlideApp.with(context!!).load(path)
                    .apply {
                        if (isCircle) {
                            this.apply(RequestOptions.bitmapTransform(CircleCrop()))
                        }
                    }
                    .fitCenter()
                    .into(imageView!!)
        } catch (e: Exception) {}
    }

    /**
     * 此方法在图文视图中被调用，作用是为Target加载网络图片
     *
     * @param context
     * @param url
     * @param viewTarget
     * @param noCache
     */
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
                    .error(R.drawable.ic_place_holder_grey)
                    .into(viewTarget)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 不加载图片获取网络图片的大小
     *
     * @param url
     */
//    fun getImageWidthAndHeight(mContext: Context,
//                               url: String): Array<Int> {
//        var width = 0
//        var height= 0
//        GlideApp.with(mContext).asBitmap().load(url).into(object : SimpleTarget<Bitmap>() {
//            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                width = resource.width
//                height = resource.height
//            }
//        })
//        return arrayOf(width, height)
//    }
}


