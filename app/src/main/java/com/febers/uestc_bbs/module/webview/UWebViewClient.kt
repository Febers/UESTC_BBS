package com.febers.uestc_bbs.module.webview

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.http.SslError
import android.webkit.*
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.utils.log
import com.febers.uestc_bbs.utils.web
import org.jetbrains.anko.browse
import org.jetbrains.anko.email

class UWebViewClient(private val context: Context,
                     private var acceptAllRequest: Boolean = true,
                     private var openUrlOut: Boolean = false,
                     private var processImageClick: Boolean = false): WebViewClient() {

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
        if (url.contains("mailto")) {
            val indexMailTo = url.indexOf("mailto:") + "mailto:".length
            val mail = url.substring(indexMailTo, url.length)
            log { "mail is $mail" }
            context.email(mail)
            return true
        }
        if (openUrlOut) {
            context.browse(url)
            return true
        } else {
            if (!url.startsWith("http")) {
                context.browse(url)
                return true
            }
            context.web(url)
            return true
        }
    }

    @TargetApi(21)
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        if (request.url.toString().contains("mailto")) {
            val indexMailTo = request.url.toString().indexOf("mailto:") + "mailto:".length
            val mail = request.url.toString().substring(indexMailTo, request.url.toString().length)
            log { "mail is $mail" }
            context.email(mail)
            return true
        }
        if (openUrlOut) {
            context.browse(request.url.toString())
            return true
        } else {
            if (!request.url.toString().startsWith("http")) {
                log { "非http开头链接，交给系统: ${request.url}" }
                context.browse(request.url.toString(), true)
                return true
            }
            context.web(request.url.toString())
            return true
        }
    }

    override fun onPageFinished(view: WebView, url: String) {
        view.settings.javaScriptEnabled = true
        super.onPageFinished(view, url)
        if (processImageClick) {
            log { "pageFinish, 注入js方法" }
            view.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "    objs[i].onclick=function()  " +
                    "    {  "
                    + "        window.imagelistener.openImage(this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                    "    }  " +
                    "}" +
                    "})()")
        }
    }
}