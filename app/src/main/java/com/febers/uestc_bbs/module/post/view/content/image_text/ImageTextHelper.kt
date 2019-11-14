package com.febers.uestc_bbs.module.post.view.content.image_text

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.util.Linkify
import android.view.View
import android.widget.TextView

import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.utils.colorAccent

/**
 * 构建可显示图片的textview
 * 参考：https://github.com/CentMeng/RichTextView
 */
object ImageTextHelper {

    private fun getUrlDrawable(source: String?, textView: TextView): Drawable {
        val imageGetter = GlideImageGetter(textView.context, textView)
        return imageGetter.getDrawable(source)
    }

    fun setImageText(tv: TextView, html: String, linkTextColor: Int? = null) {
        val context = tv.context
        val htmlStr = Html.fromHtml(html)

        tv.isClickable = true
        tv.setTextIsSelectable(true)
        tv.text = htmlStr

        //tv.setMovementMethod(LinkMovementMethod.newInstance());
        //换成下面的方法，否则超链接设置失效
        tv.autoLinkMask = Linkify.WEB_URLS or Linkify.EMAIL_ADDRESSES
        linkTextColor?.let {
            tv.setLinkTextColor(it)
        }
        val text = tv.text
        if (text is Spannable) {
            val end = text.length
            val sp = tv.text as Spannable
            val urls = sp.getSpans(0, end, URLSpan::class.java)
            val images = sp.getSpans(0, end, ImageSpan::class.java)
            val styleSpans = sp.getSpans(0, end, StyleSpan::class.java)
            val colorSpans = sp.getSpans(0, end, ForegroundColorSpan::class.java)
            val style = SpannableStringBuilder(text)
            style.clearSpans()
            for (url in urls) {
                val myUrlSpan = MyUrlSpan(url.url, context)
                style.setSpan(myUrlSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                val colorSpan = ForegroundColorSpan(colorAccent())
                style.setSpan(colorSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            for (url in images) {
                val span = ImageSpan(getUrlDrawable(url.source, tv), url.source!!)
                style.setSpan(span, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                val myUrlSpan = MyUrlSpan(url.source!!, context)
                style.setSpan(myUrlSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            for (styleSpan in styleSpans) {
                style.setSpan(styleSpan, sp.getSpanStart(styleSpan), sp.getSpanEnd(styleSpan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            for (colorSpan in colorSpans) {
                style.setSpan(colorSpan, sp.getSpanStart(colorSpan), sp.getSpanEnd(colorSpan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            tv.text = style
        }
    }

    private class MyUrlSpan internal constructor(private val mUrl: String, private val mContext: Context) : ClickableSpan() {

        override fun onClick(widget: View) {
            ClickContext.linkClick(mUrl, mContext)
        }
    }
}
