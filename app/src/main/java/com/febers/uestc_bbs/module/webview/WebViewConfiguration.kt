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

object WebViewConfiguration {

    private val TAG = "WebViewConfiguration"

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

        @SuppressLint("JavascriptInterface", "SetJavaScriptEnabled")
        fun setJavaScriptEnabled(enable: Boolean?): Configuration {
            webSettings.javaScriptEnabled = true
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
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        }
                        return true
                    } else {
                        if (url.startsWith("openapp")) {
                            return true
                        }
                        //view.loadUrl(url);
                        ClickContext.linkClick(url, view.context)
                        return true
                    }
                }

                @TargetApi(21)
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    if (openUrlOut) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(request.url.toString()))
                        if (intent.resolveActivity(context.packageManager) != null) {
                            context.startActivity(intent)
                        }
                        return true
                    } else {
                        if (request.url.toString().startsWith("openapp")) {
                            return true
                        }
                        //view.loadUrl(request.getUrl().toString());
                        ClickContext.linkClick(request.url.toString(), view.context)
                        return true
                    }
                }

                override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
                    if (withoutImage) {
                        if (url.contains("image") ||
                                url.contains("png") ||
                                url.contains("jpg")) {
                            return WebResourceResponse(null, null, null)
                        }
                    }
                    return super.shouldInterceptRequest(view, url)
                }

                @TargetApi(21)
                override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest): WebResourceResponse? {
                    if (withoutImage) {
                        if (request.url.toString().contains("image") ||
                                request.url.toString().contains("png") ||
                                request.url.toString().contains("jpg")) {
                            return WebResourceResponse(null, null, null)
                        }
                    }
                    return super.shouldInterceptRequest(view, request)
                }

                override fun onPageFinished(view: WebView, url: String) {
                    if (!webSettings.loadsImagesAutomatically) {
                        webSettings.loadsImagesAutomatically = true
                    }
                    if (processSourceCode) {
                        //解析网页源码
                        view.loadUrl("javascript:HTMLOUT.processHTML(document.documentElement.outerHTML);")
                    }
                    if (processImageClick) {
                        view.loadUrl("""
                            javascript:(function(){
                                var objs = document.getElementsByTagName("img"); 
                                for(var i=0;i<objs.length;i++) {
                                    objs[i].οnclick=function() {  
                                        window.imagelistener.openImage(this.src);   //通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                                    }  
                                }
                            })()
                        """.trimIndent())
                    }
                    super.onPageFinished(view, url)
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
            webView.webViewClient = webViewClient
            webView.webChromeClient = webChromeClient
        }
    }
}
