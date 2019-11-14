package com.febers.uestc_bbs.module.webview

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.view.View
import android.webkit.*
import android.widget.ProgressBar

import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.webview.listener.OnReceivedTitleListener
import com.febers.uestc_bbs.utils.log
import org.jetbrains.anko.browse
import org.jetbrains.anko.email

object WebViewConfiguration {

    class Configuration(private val context: Context, private val webView: WebView) {

        private val webSettings: WebSettings = webView.settings
        private lateinit var webViewClient: WebViewClient
        private lateinit var webChromeClient: WebChromeClient

        private var progressBar: ProgressBar? = null
        private var supportProgressBar = false

        private var openUrlOut = false
        private var acceptAllRequest = true
        private val enableJavaScript = true
        private var withoutImage = false
        private var processSourceCode = false
        private var processImageClick = false

        private var onReceivedTitleListener: OnReceivedTitleListener? = null

        @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
        fun setJavaScriptEnabled(enable: Boolean): Configuration {
            webSettings.javaScriptEnabled = enable
            return this
        }

        @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
        fun addJavaScriptInterface(`object`: Any, name: String): Configuration {
            webSettings.javaScriptEnabled = true
            webSettings.javaScriptCanOpenWindowsAutomatically = true
            webView.addJavascriptInterface(`object`, name)
            return this
        }

        fun setCacheMode(mode: Int): Configuration {
            webSettings.cacheMode = mode
            return this
        }

        fun setAppCacheEnabled(b: Boolean?): Configuration {
            webSettings.setAppCacheEnabled(b!!)
            return this
        }

        fun setAppCachePath(path: String): Configuration {
            webSettings.setAppCachePath(path)
            return this
        }

        fun setUseWideViewPort(useWideViewPort: Boolean?): Configuration {
            webSettings.useWideViewPort = useWideViewPort!!
            return this
        }

        fun setLoadWithOverviewMode(loadWithOverviewMode: Boolean?): Configuration {
            webSettings.loadWithOverviewMode = loadWithOverviewMode!!
            return this
        }

        fun setSupportZoom(supportZoom: Boolean?): Configuration {
            webSettings.setSupportZoom(supportZoom!!)
            return this
        }

        fun setBuiltInZoomControls(builtInZoomControls: Boolean?): Configuration {
            webSettings.builtInZoomControls = builtInZoomControls!!
            return this
        }

        fun setDisplayZoomControls(displayZoomControls: Boolean?): Configuration {
            webSettings.displayZoomControls = displayZoomControls!!
            return this
        }

        fun setSupportWindow(supportWindow: Boolean?): Configuration {
            webSettings.javaScriptCanOpenWindowsAutomatically = supportWindow!!
            return this
        }

        fun setDomEnabled(domEnabled: Boolean?): Configuration {
            webSettings.databaseEnabled = true
            webSettings.domStorageEnabled = domEnabled!!
            return this
        }

        fun setClientWithoutImage(): Configuration {
            withoutImage = true
            return this
        }

        fun acceptAnyRequest(accepter: Boolean?): Configuration {
            acceptAllRequest = accepter!!
            return this
        }

        fun setBlockNetworkImage(block: Boolean?): Configuration {
            webSettings.blockNetworkImage = block!!
            return this
        }

        fun setSupportProgressBar(support: Boolean?, progressBar: ProgressBar): Configuration {
            supportProgressBar = support!!
            this.progressBar = progressBar
            return this
        }

        fun setOpenUrlOut(openUrlOut: Boolean?): Configuration {
            this.openUrlOut = openUrlOut!!
            return this
        }

        fun setProcessSourceCode(processHtml: Boolean?, `object`: Any, name: String): Configuration {
            this.processSourceCode = processHtml!!
            addJavaScriptInterface(`object`, name)
            return this
        }

        fun setProcessImageClick(processImageClick: Boolean, `object`: Any, name: String): Configuration {
            this.processImageClick = processImageClick
            addJavaScriptInterface(`object`, name)
            return this
        }

        fun addOnReceivedTitleListener(onReceivedTitleListener: OnReceivedTitleListener): Configuration {
            this.onReceivedTitleListener = onReceivedTitleListener
            return this
        }

        fun configure() {
            webViewClient = object : WebViewClient() {
                override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                    if (acceptAllRequest) {
                        handler.proceed()
                    }
                }

                /**
                 * webveiw只能打开以http/https开头的网页
                 * 遇到其他开头的url，比如一些scheme，比如打开其他应用
                 * 以openapp开头的url时，就会报错，需要手动处理
                 */
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    if (openUrlOut) {
                        context.browse(url, true)
                        return true
                    } else {
                        if (!url.startsWith("http")) {
                            context.browse(url, true)
                            return true
                        }
                        view.loadUrl(url)
                        return true
                    }
                }

                @TargetApi(21)
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    log { "url: ${request.url}" }
                    if (openUrlOut) {
                        context.browse(request.url.toString())
                        return true
                    } else {
                        if (!request.url.toString().startsWith("http")) {
                            log { "非http开头链接，交给系统: ${request.url}" }
                            context.browse(request.url.toString(), true)
                            return true
                        }
                        view.loadUrl(request.url.toString())
                        return true
                    }
                }
            }

            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView, newProgress: Int) {
                    if (supportProgressBar && progressBar != null) {
                        if (newProgress == 100) {
                            progressBar!!.visibility = View.GONE
                        } else {
                            progressBar!!.visibility = View.VISIBLE
                            progressBar!!.progress = newProgress
                        }
                    }
                }

                override fun onReceivedTitle(view: WebView?, title: String?) {
                    super.onReceivedTitle(view, title)
                    if (onReceivedTitleListener != null) {
                        onReceivedTitleListener!!.onReceived(title ?: webView.url)
                    }
                }
            }
            if (Build.VERSION.SDK_INT > 21) {
                CookieManager.getInstance().flush()
            } else {
                CookieSyncManager.createInstance(webView.context)
                CookieSyncManager.getInstance().sync()
            }

            //延迟加载图片,对于4.4直接加载
            //            if (Build.VERSION.SDK_INT >= 19) {
            //                webSettings.setLoadsImagesAutomatically(true);
            //            } else {
            //                webSettings.setLoadsImagesAutomatically(false);
            //            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //防止不加载https资源情况出现
                webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
            webView.webViewClient = webViewClient
            webView.webChromeClient = webChromeClient
        }
    }
}
