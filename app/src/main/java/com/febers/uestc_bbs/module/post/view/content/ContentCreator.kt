package com.febers.uestc_bbs.module.post.view.content

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.febers.uestc_bbs.GlideApp
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.module.post.view.content.image_text.ImageTextHelper
import com.febers.uestc_bbs.module.theme.ThemeManager
import com.febers.uestc_bbs.utils.*
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

class ContentCreator(
        private var mLinearLayout: LinearLayout?,
        private var mContents: List<PostDetailBean.ContentBean>,
        private val mTextColor: Int? = null,
        private val mTextLinkColor: Int? = null) {

    private var imageMapList: MutableList<Map<String, ImageView>> = ArrayList()
    private var imageUrlList: MutableList<String> = ArrayList()

    private var mStringBuilder: StringBuilder = StringBuilder()
    private val IMAGE_VIEW_MARGIN = 8
    private val IMAGE_VIEW_WIDTH = getWindowWidth()/2
    private val IMAGE_VIEW_HEIGHT = getWindowWidth()/2
    private var context = mLinearLayout?.context
    private var belowTextView = true    //图片是否在文字下面，如果是，间距拉大

    fun getImageMapList() = imageMapList

    fun getImageUrlList() = imageUrlList

    fun create() {
        mLinearLayout ?: return
        mLinearLayout!!.removeAllViews()
        cycleDrawView2()
    }

    fun reset(linearLayout: LinearLayout, contents: List<PostDetailBean.ContentBean>) {
        mLinearLayout = linearLayout
        mContents = contents
        mStringBuilder.clear()
        imageMapList.clear()
        imageUrlList.clear()
        context = mLinearLayout?.context
    }

    /**
     * 不使用递归而是循环的方式绘制帖子内容
     */
    private fun cycleDrawView2() {
        val stringBuilder = StringBuilder()

        fun drawTextView() {
            val textView = getTextView()
            ImageTextHelper.setImageText(textView, stringBuilder.toString(), mTextLinkColor)
            mLinearLayout?.addView(textView)
            belowTextView = true
        }
        fun drawImageView(content: PostDetailBean.ContentBean) {
            drawTextView()
            val imageView = getImageView(content.originalInfo.toString())
            mLinearLayout?.addView(imageView)
            mLinearLayout?.gravity = Gravity.CENTER
            imageMapList.add(mapOf(content.originalInfo.toString() to imageView))
            imageUrlList.add(content.originalInfo.toString())
            belowTextView = false
        }
        fun drawFileView(url: String, title: String) {
            val button = getFileButton(url, title)
            mLinearLayout?.addView(button)
        }

        loop@ for ( (position, content) in mContents.withIndex()) {
            when(content.type) {
                CONTENT_TYPE_TEXT -> {
                    stringBuilder.append(emotionTrans2Url(content.infor).encodeSpaces())
                }
                CONTENT_TYPE_URL -> {
                    stringBuilder
                            .append(" "+urlTransform(raw = content.url, title = content.infor)+" ")
                }
                CONTENT_TYPE_IMG -> {
                    drawImageView(content)
                    stringBuilder.clear()
                }
                CONTENT_TYPE_FILE -> {
                    if (content.infor?.unMatchImageUrl()!!) {
                        drawFileView(url = ""+content.url, title = ""+content.infor)
                    }
                }
                else -> {
                    stringBuilder.append(content.infor)
                }
            }
            //当遍历结束之后之后，绘制stringBuilder的内容
            if (position == mContents.size-1) {
                val textView = getTextView()
                ImageTextHelper.setImageText(textView, stringBuilder.toString())
                mLinearLayout?.addView(textView)
            }
        }
    }

    //创建TextView
    private fun getTextView(): TextView = TextView(context).apply {
        setLineSpacing(1.0f, 1.2f)
        textSize = 16f
        setTextColor(ThemeManager.colorTextFirst())
        mTextColor?.let {
            setTextColor(it)
        }
        mTextLinkColor?.let {
            setLinkTextColor(it)
        }
        linksClickable = true
        setLinkTextColor(colorAccent())
        setTextIsSelectable(true)
        layoutParams = ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    //创建ImageView
    private fun getImageView(url: String): ImageView {
        val imageView = ImageView(context).apply {
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            layoutParams = ViewGroup
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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
            || endsWith(".JPG") || endsWith(".PNG") ||
            endsWith(".JPEG") || endsWith(".BMP") ||
            endsWith(".GIF") || endsWith(".gif")


    private fun String.unMatchImageUrl(): Boolean = !matchImageUrl()
}