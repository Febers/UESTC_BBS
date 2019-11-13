package com.febers.uestc_bbs.module.webview.javascript_interface

import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import com.febers.uestc_bbs.base.IMAGE_URL
import com.febers.uestc_bbs.base.IMAGE_URLS
import com.febers.uestc_bbs.module.image.ImageActivity
import com.febers.uestc_bbs.utils.log

class ImageClickInterface(private val context: Context, private val imageUrls: Array<String>) {

    @JavascriptInterface
    fun openImage(imageUrl: String) {
        log { "js接口方法被调用" }
        context.startActivity(Intent(context, ImageActivity::class.java).apply {
            putExtra(IMAGE_URLS, imageUrls)
            putExtra(IMAGE_URL, imageUrl)
        })
    }
}