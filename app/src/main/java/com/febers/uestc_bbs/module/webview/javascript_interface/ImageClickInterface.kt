package com.febers.uestc_bbs.module.webview.javascript_interface

import android.content.Context
import android.webkit.JavascriptInterface
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.utils.logi

class ImageClickInterface(private val context: Context, private val imageUrls: Array<String>) {

    @JavascriptInterface
    fun openImage(imageUrl: String) {
        logi { "js接口方法被调用" }
        if (imageUrls.contains(imageUrl)) {
            ClickContext.clickToImageViewer(imageUrl, imageUrls, context)
        }
    }
}