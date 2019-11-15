package com.febers.uestc_bbs.module.post.view.content

import android.graphics.Color
import android.os.Build
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.module.theme.ThemeManager
import com.febers.uestc_bbs.module.webview.UWebViewClient
import com.febers.uestc_bbs.module.webview.javascript_interface.ImageClickInterface
import com.febers.uestc_bbs.utils.ColorUtils
import com.febers.uestc_bbs.utils.colorAccent
import com.febers.uestc_bbs.utils.encodeSpaces
import com.febers.uestc_bbs.utils.log
import org.jetbrains.anko.collections.forEachWithIndex

/**
 * const val CONTENT_TYPE_TEXT = 0
 * const val CONTENT_TYPE_IMG = 1
 * const val CONTENT_TYPE_AUDIO = 3
 * const val CONTENT_TYPE_URL = 4
 * const val CONTENT_TYPE_FILE = 5
 */
object ContentTransfer {

    private val imageUrls: MutableList<String> = ArrayList()

    fun transfer(webView: WebView, contents: List<PostDetailBean.ContentBean>) {

        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.setBackgroundColor(Color.parseColor(if (ThemeManager.isNightTheme()) "#00363636" else "#00ffffff"))

        webView.settings.javaScriptEnabled = true
        webView.settings.setAppCacheEnabled(true)
        webView.settings.databaseEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.setSupportZoom(false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //防止不加载http资源情况出现
            webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        //主贴图文视图的绘制
        webView.loadDataWithBaseURL(null, json2Html(contents), "text/html; charset=UTF-8", "UTF-8", null)

        //插入图片点击接口
        webView.addJavascriptInterface(ImageClickInterface(webView.context, imageUrls.toTypedArray()), "imagelistener")
        webView.webViewClient = UWebViewClient(webView.context, processImageClick = true)
    }

    private fun json2Html(contents: List<PostDetailBean.ContentBean>): String {
        imageUrls.clear()
        val sb = StringBuilder()
        val textColor = if (ThemeManager.isNightTheme()) "white" else "black"
        val linkColor = ColorUtils.int2String(colorAccent())
        contents.forEachWithIndex { index, content ->
//            log { content.toString() }
            when(content.type) {
                CONTENT_TYPE_TEXT -> {
                    sb.append(""" <font color="$textColor" style="word-break:break-all">${emotionTrans2Url(content.infor).encodeSpaces()} </font>""")
                }
                CONTENT_TYPE_IMG -> {
                    sb.append("""<p style="text-align:center;"><img style="max-width:90%;height:auto" src="${content.infor}"/></p>""")
                    imageUrls.add(content.infor+"")
                }
                CONTENT_TYPE_URL -> {
                    if (!content.infor!!.matchImageUrl()) {
                        sb.append("""<a href="${content.url}" style="word-break:break-all;color:$linkColor">${content.infor}</a>""")
                    }
                }
                CONTENT_TYPE_AUDIO -> {
//                    sb.append("""<br><embed height="100" width="100" src="${content.url}" /><br>""")
                    sb.append("""<br><audio src="${content.infor}" controls="controls" style="clear:both;display:block;margin:auto"/><br>""")
                }
                CONTENT_TYPE_FILE -> {
                    if (!content.infor!!.matchImageUrl()) {
                        sb.append("""<br><a href="${content.url}" download="${content.url}" style="word-break:break-all;color:$linkColor">${content.infor}</a><br>""")
                    }
                }
            }
        }
//        log { "content html: ${sb.toString()}" }
        return sb.toString()
    }

    private fun String?.simplifyDownloadUrl(): String {
        return this+""
    }

    /**
     * 将content.infor中的表情gif图片变成超链接，
     * 然后交给{@link #ImageTextHelper.setImageText(TextView tv, String html)} 处理
     * raw比如:
     * ...[mobcent_phiz=http://bbs.uestc.edu.cn/static/image/smiley/first/1.gif]...
     * 转换成:
     * <img src="http://bbs.uestc.edu.cn/static/image/smiley/first/1.gif">
     */
    private fun emotionTrans2Url(raw: String?, lastBegin: Int = 0): String{
        if (raw == null) {
            return ""
        }
        val begin: Int
        var newContent: String = raw
        try {
            begin = raw.indexOf("""[mobcent_phiz=""", lastBegin)
            if (begin == -1) {
                return newContent
            }
            val end = raw.indexOf("""]""", begin)
            val rawFormatString = raw.substring(begin, end+1) //需要包括]
            val rawUrlString = raw.substring(begin+14, end) //不需要包括]
            val imgString = """<img src="$rawUrlString">"""
            newContent = raw.replace(rawFormatString, imgString)
        } catch (e:Exception) {
            return newContent
        }
        return emotionTrans2Url(newContent, begin)
    }

    private fun String.matchImageUrl(): Boolean = endsWith(".jpg") || endsWith(".png") ||
            endsWith(".jpeg") || endsWith(".bmp")
            || endsWith(".JPG") || endsWith(".PNG") ||
            endsWith(".JPEG") || endsWith(".BMP") ||
            endsWith(".GIF") || endsWith(".gif")
}