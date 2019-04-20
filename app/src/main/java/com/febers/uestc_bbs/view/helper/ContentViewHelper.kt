package com.febers.uestc_bbs.view.helper

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.module.context.ClickContext
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.utils.encodeSpaces
import com.febers.uestc_bbs.utils.getWindowWidth
import com.febers.uestc_bbs.utils.log
import org.jetbrains.anko.browse


/**
 * 动态向视图中添加帖子内容
 * type对应的视图类型如下:
 * 0：文本（解析链接）；1：图片；3：音频;4:链接;5：附件
 *
 */
const val CONTENT_TYPE_TEXT = 0
const val CONTENT_TYPE_IMG = 1
const val CONTENT_TYPE_AUDIO = 3
const val CONTENT_TYPE_URL = 4
const val CONTENT_TYPE_FILE = 5
const val DIVIDE_HEIGHT = 20

class ContentViewHelper(
        private val linearLayout: LinearLayout,
        private val mContents: List<PostDetailBean.ContentBean>,
        private val mTextColor: Int? = null,
        private val mTextLinkColor: Int? = null) {

    private var imageMapList: MutableList<Map<String, ImageView>>? = ArrayList()

    private var mStringBuilder: StringBuilder = StringBuilder()
    private val IMAGE_VIEW_MARGIN = 8
    private val IMAGE_VIEW_WIDTH = getWindowWidth()
    private val IMAGE_VIEW_HEIGHT = getWindowWidth()
    private val context = linearLayout.context
    private var belowTextView = true    //图片是否在文字下面，如果是，间距拉大
    fun getImageMapList() = imageMapList

    fun create() {
        linearLayout.removeAllViews()
        cycleDrawView(mStringBuilder, position = 0)
    }

    /**
     * 根据Content的内容，递归地绘制内容
     * 当遇到text内容，添加进stringBuilder
     * 遇到image，结束一次绘制，继续开始下一次绘制
     */
    private fun cycleDrawView(stringBuilder: StringBuilder, position: Int) {
        fun drawTextView() {
            val textView = getTextView()
            ImageTextHelper.setImageText(textView, stringBuilder.toString(), mTextLinkColor)
            linearLayout.addView(textView)
            belowTextView = true
        }
        fun drawImageView() {
            drawTextView()
            val imageView = getImageView(mContents[position].originalInfo.toString())
            linearLayout.addView(imageView)
            linearLayout.gravity = Gravity.CENTER
            imageMapList?.add(mapOf(mContents[position].originalInfo.toString() to imageView))
            belowTextView = false
        }
        fun drawFileView(url: String, title: String) {
            val button = getFileButton(url, title)
            linearLayout.addView(button)
        }
        //当遍历结束之后之后，绘制stringBuilder的内容
        if (position >= mContents.size) {
            val textView = getTextView()
            ImageTextHelper.setImageText(textView, stringBuilder.toString())
            linearLayout.addView(textView)
            return
        }
        //根据type选择不同的策略
        when(mContents[position].type) {
            CONTENT_TYPE_TEXT -> {
                stringBuilder.append(emotionTransform(mContents[position].infor).encodeSpaces())
                cycleDrawView(stringBuilder, position + 1)
            }
            CONTENT_TYPE_URL -> {
                stringBuilder
                    .append(" "+urlTransform(raw = mContents[position].url, title = mContents[position].infor)+" ")
                cycleDrawView(stringBuilder, position + 1)
            }
            CONTENT_TYPE_IMG -> {
                drawImageView()
                cycleDrawView(StringBuilder(), position + 1)
            }
            CONTENT_TYPE_FILE -> {
                if (mContents[position].infor?.unMatchImageUrl()!!) {
//                    stringBuilder
//                            .append(" "+urlTransform(raw = mContents[position].url, title = mContents[position].infor)+" ")
                    drawFileView(url = ""+mContents[position].url, title = ""+mContents[position].infor)
                }
                cycleDrawView(stringBuilder, position + 1)
            }
            else -> {
                stringBuilder.append(mContents[position].infor)
                cycleDrawView(stringBuilder, position + 1)
            }
        }
    }

    //创建TextView
    private fun getTextView(): TextView = TextView(context).apply {
        setLineSpacing(1.0f, 1.2f)
        textSize = 16f
        setTextColor(ThemeHelper.getTextColorPrimary())
        mTextColor?.let {
            setTextColor(it)
        }
        mTextLinkColor?.let {
            setLinkTextColor(it)
        }
        linksClickable = true
        setLinkTextColor(ThemeHelper.getColorAccent())
        setTextIsSelectable(true)
        layoutParams = ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    //创建ImageView
    private fun getImageView(url: String): ImageView {
        val imageView = ImageView(context).apply {
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            layoutParams = ViewGroup
                    .LayoutParams(IMAGE_VIEW_WIDTH, IMAGE_VIEW_HEIGHT)
        }
        val marginLayoutParams = ViewGroup.MarginLayoutParams(imageView.layoutParams).apply {
            setMargins(IMAGE_VIEW_MARGIN,
                    if (belowTextView)  3*IMAGE_VIEW_MARGIN else 0,
                    IMAGE_VIEW_MARGIN,
                    0) }
        return imageView.apply {
            layoutParams = marginLayoutParams
        }
    }

    //创建附件下载的 button
    private fun getFileButton(url: String, title: String): Button = Button(context).apply {
        text = "附件：$title"
        setOnClickListener {
            context.browse(url, true)
        }
    }

    /**
     * 将content.infor中的表情gif图片变成超链接，
     * 然后交给{@link #ImageTextHelper.setImageText(TextView tv, String html)} 处理
     * raw比如:
     * ...[mobcent_phiz=http://bbs.uestc.edu.cn/static/image/smiley/first/1.gif]...
     * 转换成:
     * <img src="http://bbs.uestc.edu.cn/static/image/smiley/first/1.gif">
     */
    private fun emotionTransform(raw: String?, lastBegin: Int = 0): String{
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
        return emotionTransform(newContent, begin)
    }

    /**
     * 将图片的content转换成html的图片标签，
     * 然后交给imageTextView统一加载
     */
    private fun imgTransform(raw: String?): String {
        if (raw == null) {
            return ""
        }
        return """<img src=$raw>"""
    }

    //将 url 和对应的 title 转换成html格式
    private fun urlTransform(raw: String?, title: String?): String {
        if (raw == null || title == null) {
            return ""
        }
        return """<a href="$raw">$title</a>"""
    }

    private fun String.matchImageUrl(): Boolean = endsWith(".jpg") || endsWith(".png") ||
                endsWith(".jpeg") || endsWith(".bmp")


    private fun String.unMatchImageUrl(): Boolean = !matchImageUrl()
}