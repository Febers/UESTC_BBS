package com.febers.uestc_bbs.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout

object ImageViewUtils {

    fun match(context: Context, imageView: ImageView, intrinsicWidth: Int? = null, intrinsicHeight: Int? = null) {
        var width: Int
        var height: Int

        //获取屏幕宽高
        val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val metrics = DisplayMetrics()
        manager.defaultDisplay.getMetrics(metrics)
        val sWidth = metrics.widthPixels
        val sHeight = metrics.heightPixels

        //获取图片宽高
        val dWidth: Int
        val dHeight: Int
        if (intrinsicHeight == null || intrinsicWidth == null) {
            val drawable = imageView.drawable
            dWidth = drawable.intrinsicWidth
            dHeight = drawable.intrinsicHeight
        } else {
            dWidth = intrinsicWidth
            dHeight = intrinsicHeight
        }
        val scale: Float
        //图片高度小于屏幕高度，比如图片270，540，屏幕1080，2030
        if (dHeight < sHeight) {
            if (dWidth < sWidth) {
                //图片宽度小于屏幕宽度，需要增大宽度和高度
                scale = sWidth.toFloat() / dWidth / 1.1f //不能增大太多
                width = sWidth
                height = (dHeight*scale).toInt()
            } else {
                //图片宽度大于屏幕宽度，需要压缩宽度
                scale = dWidth.toFloat() / sWidth
                width = sWidth
                height = (dHeight/scale).toInt()
            }
        } else {
            //图片高度大于屏幕宽度，比如图片2070，5040，屏幕1080，2030
            if (dWidth < sWidth) {
                //图片宽度小于屏幕宽度，需要增大高度
                height = (dHeight / 1.1).toInt()
                width = (dWidth / 1.1).toInt()
            } else {
                //图片宽度大于屏幕宽度，需要压缩高度和宽度
                scale = dHeight.toFloat() / sHeight
                width = (sWidth/scale).toInt()
                height = (sHeight/scale).toInt()
            }
        }
        //logi { "屏幕：$sWidth,$sHeight, 图片：$dWidth,$dHeight, 最终 width: $width, height: $height" }
        //最后再统一减大小
        width = (width/1.2).toInt()
        height = (height/1.2).toInt()
        val params = LinearLayout.LayoutParams(width, height)
        imageView.layoutParams = params
    }
}
