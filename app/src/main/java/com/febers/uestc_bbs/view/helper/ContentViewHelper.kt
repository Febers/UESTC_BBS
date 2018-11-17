package com.febers.uestc_bbs.view.helper

import android.util.Log.i
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.febers.uestc_bbs.entity.PostDetailBean
import com.febers.uestc_bbs.module.theme.ThemeHelper
import com.febers.uestc_bbs.module.image.ImageLoader
import com.febers.uestc_bbs.utils.ViewClickUtils
import com.febers.uestc_bbs.utils.encodeSpaces


/**
 * 动态向视图中添加帖子内容
 * type对应的视图类型如下:
 * 0：文本（解析链接）；1：图片；3：音频;4:链接;5：附件
 *
"content": [
{
"infor": "http://bbs.uestc.edu.cn/data/attachment/forum/201805/28/221826jnm65xdo95zxsb2v.png",
"type": 1,
"originalInfo": "http://bbs.uestc.edu.cn/data/attachment/forum/201805/28/221826jnm65xdo95zxsb2v.png",
"aid": 1880982
},
{
"infor": "利用课余时间，开发了一个Android端应用“i成电”，应用主页为：",
"type": 0,
"originalInfo": "http://bbs.uestc.edu.cn/data/attachment/forum/201805/28/221826jnm65xdo95zxsb2v.png",
"aid": 1880982
},
{
"infor": "http://app.febers.tech/",
"type": 4,
"originalInfo": "http://bbs.uestc.edu.cn/data/attachment/forum/201805/28/221826jnm65xdo95zxsb2v.png",
"aid": 1880982,
"url": "http://app.febers.tech/"
},

{
"infor": "iuestc_beta2.9.apk",
"type": 5,
"originalInfo": "http://bbs.uestc.edu.cn/data/attachment/forum/201805/28/222551mkmyz00hkd2bksms.png",
"aid": 1880984,
"url": "http://bbs.uestc.edu.cn/forum.php?mod=attachment&aid=MTg4MDk4NXxlNWY3OGE5YnwxNTM0NTM2NTg0fDE5NjQ4NnwxNzE4MTM0",
"desc": "2.94 MB, 下载次数: 44"
}
],
 */
const val CONTENT_TYPE_TEXT = 0
const val CONTENT_TYPE_IMG = 1
const val CONTENT_TYPE_AUDIO = 3
const val CONTENT_TYPE_URL = 4
const val CONTENT_TYPE_FILE = 5
const val DIVIDE_HEIGHT = 20

class ContentViewHelper(private val linearLayout: LinearLayout, private val mContents: List<PostDetailBean.ContentBean>) {

    private var imageUrlList: MutableList<String> = ArrayList()
    private var imageViewList: MutableList<ImageView> = ArrayList()
    private var imageMapList: MutableList<Map<String, ImageView>>? = ArrayList()

    private var mStringBuilder: StringBuilder = StringBuilder()
    private val IMAGE_VIEW_MARGIN = 10
    val IMAGE_VIEW_WIDTH = 1000
    val IMAGE_VIEW_HEIGHT = 725
    private val context = linearLayout.context

    fun getImageMapList() = imageMapList

    fun create() {
        imageUrlList.clear()
        imageViewList.clear()
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
            ImageTextHelper.setImageText(textView, stringBuilder.toString())
            linearLayout.addView(textView)
        }
        fun drawImageView() {
            drawTextView()
            val imageView = getImageView(mContents[position].originalInfo.toString())
            linearLayout.addView(imageView)
            linearLayout.gravity = Gravity.CENTER
            imageMapList?.add(mapOf(mContents[position].originalInfo.toString() to imageView))
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
                    stringBuilder
                            .append(" "+urlTransform(raw = mContents[position].url, title = mContents[position].infor)+" ")
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
        setLineSpacing(1.0f, 1.3f)
        textSize = 16f
        setTextColor(ThemeHelper.getTextColorPrimary())
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
            setMargins(IMAGE_VIEW_MARGIN, IMAGE_VIEW_MARGIN, IMAGE_VIEW_MARGIN, IMAGE_VIEW_MARGIN) }
        return imageView.apply {
            layoutParams = marginLayoutParams
        }
    }

    /**
     * 将content.infor中的表情gif图片变成超链接，
     * 然后交给{@link #ImageTextHelper.setImageText(TextView tv, String html)} 处理
     * raw比如:
     * ...你好啊[mobcent_phiz=http://bbs.uestc.edu.cn/static/image/smiley/too/1.gif]...
     * 转换成:
     * <img src="http://bbs.uestc.edu.cn/static/image/smiley/too/1.gif">
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
            i("Utils", "$e")
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