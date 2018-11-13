package com.febers.uestc_bbs.utils

import android.view.ViewGroup
import android.webkit.WebView



object WebViewUtils {

    /**
     * 安全销毁webView
     *
     * @param webView
     */
    fun destroyWebView(webView: WebView?): Boolean {
        if (webView != null) {
            val parent = webView.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(webView)
            }
            webView.stopLoading()
            webView.settings.javaScriptEnabled = false
            webView.clearHistory()
            webView.removeAllViews()
            webView.destroy()
        }
        return true
    }
}

