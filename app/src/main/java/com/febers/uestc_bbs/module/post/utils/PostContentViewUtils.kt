/*
 * Created by Febers at 18-8-18 下午3:08.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-18 下午2:32.
 */

package com.febers.uestc_bbs.module.post.utils

import android.content.Context
import android.graphics.Color
import android.util.Log.i
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.SimpleContentBean
import com.febers.uestc_bbs.utils.encodeSpaces


import com.febers.uestc_bbs.view.utils.ImageTextUtils


/**
 * 动态向视图中添加帖子内容
 * type对应的视图类型如下:
 * 0：文本（解析链接）；1：图片；3：音频;4:链接;5：附件
 *
"content": [
{
"infor": " 本帖最后由 四条眉毛 于 2018-6-8 14:16 编辑 ",
"type": 0
},
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
const val CONTENT_TYPE_TEXT = "0"
const val CONTENT_TYPE_IMG = "1"
const val CONTENT_TYPE_AUDIO = "3"
const val CONTENT_TYPE_URL = "4"
const val CONTENT_TYPE_FILE = "5"   //下一个content的描述信息
const val DIVIDE_HEIGHT = 20

object PostContentViewUtils {

    private lateinit var mContents: List<SimpleContentBean>
    private lateinit var mStringBuilder: StringBuilder
    private final val IMAGE_VIEW_MARGIN = 20

    fun create(linearLayout: LinearLayout?, contents: List<SimpleContentBean>?) {
        if (contents == null || linearLayout == null) {
            return
        }
        mContents = contents
        mStringBuilder = StringBuilder()
        linearLayout.removeAllViews()
        cycleDrawView(linearLayout, mStringBuilder, position = 0)
    }

    private fun cycleDrawView(linearLayout: LinearLayout, stringBuilder: StringBuilder, position: Int) {
        if (position >= mContents.size) {
            val textView = getTextView(linearLayout.context)
            ImageTextUtils.setImageText(textView, stringBuilder.toString())
            linearLayout.addView(textView)
            return
        }
        when(mContents[position].type) {
            CONTENT_TYPE_TEXT -> {
                i("Utils", "TEXT:")
                mStringBuilder.append(emotionTransform(mContents[position].infor).encodeSpaces())
                cycleDrawView(linearLayout, stringBuilder, position+1)
            }
            CONTENT_TYPE_URL -> {
                i("Utils", "URL:")
                stringBuilder.append("  ")
                    .append(urlTransform(mContents[position].url, mContents[position].infor))
                    .append("  ")
                cycleDrawView(linearLayout, stringBuilder, position+1)
            }
            CONTENT_TYPE_IMG -> {
                i("Utils", "IMG:")
                val textView = getTextView(linearLayout.context)
                ImageTextUtils.setImageText(textView, mStringBuilder.toString())
                linearLayout.addView(textView)

                val imageView = getImageView(linearLayout.context)
                Glide.with(linearLayout.context).load(mContents[position].originalInfo)
                        .placeholder(R.mipmap.ic_default_avatar).into(imageView)
                linearLayout.addView(imageView)
                cycleDrawView(linearLayout, StringBuilder(), position+1)
            }
            CONTENT_TYPE_FILE -> {
                i("Utils", "FILE:")
                stringBuilder.append(" ")
                        .append(urlTransform(mContents[position].url, mContents[position].infor))
                        .append(" ")
                cycleDrawView(linearLayout, stringBuilder, position+1)
            }
            else -> {
                i("Utils", "OTHER:${mContents[position]}")
                mStringBuilder.append(mContents[position].infor)
                cycleDrawView(linearLayout, stringBuilder, position+1)
            }
        }
    }

    private fun getTextView(context: Context): TextView = TextView(context).apply {
        setLineSpacing(1.0f, 1.3f)
        setTextColor(Color.parseColor("#DD000000"))
        textSize = 16f
        setTextIsSelectable(true)
        layoutParams = ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun getImageView(context: Context): ImageView {
        val imageView = ImageView(context).apply {
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            layoutParams = ViewGroup
                    .LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        //val layoutParams = imageView.layoutParams
        val marginLayoutParams = ViewGroup.MarginLayoutParams(imageView.layoutParams).apply {
            setMargins(IMAGE_VIEW_MARGIN, IMAGE_VIEW_MARGIN, IMAGE_VIEW_MARGIN, IMAGE_VIEW_MARGIN) }
        imageView.layoutParams = marginLayoutParams
        return imageView
    }


    //绘制间隔视图
    fun divideView(context: Context?, height: Int = DIVIDE_HEIGHT): View {
        val view = View(context)
        view.layoutParams = ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
        return view
    }

    /**
     * 将content.infor中的表情gif图片变成超链接，
     * 然后交给{@link #ImageTextUtils.setImageText(TextView tv, String html)} 处理
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
}