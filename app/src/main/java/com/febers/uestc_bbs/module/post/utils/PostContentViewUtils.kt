/*
 * Created by Febers at 18-8-18 下午3:08.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-18 下午2:32.
 */

package com.febers.uestc_bbs.module.post.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.util.Linkify
import android.util.Log.i
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.febers.uestc_bbs.base.BaseFragment
import com.febers.uestc_bbs.entity.SimpleContentBean
import com.febers.uestc_bbs.utils.encodeSpaces
import com.febers.uestc_bbs.view.utils.GlideImageGetter


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
const val CONTENT_TYPE_FILE = "5"
const val DIVIDE_HEIGHT = 20

object PostContentViewUtils {
    fun create(context: Context?, linearLayout: LinearLayout?, contents: List<SimpleContentBean>?) {
        if (contents == null) {
            return
        }
        linearLayout?.removeAllViews()
        val stringBuilder = StringBuilder()
        for (content in contents) {
            if (context == null) {
                return
            }
            if (content.type == CONTENT_TYPE_TEXT) {
                stringBuilder.append(emotionTransform(content.infor).encodeSpaces())
                continue
            }
            if (content.type == CONTENT_TYPE_IMG) {
                stringBuilder.append(imgTransform(content.originalInfo))
                continue
            }
            if (content.type == CONTENT_TYPE_AUDIO) {
                //添加音频
                continue
            }
            if (content.type == CONTENT_TYPE_URL) {
                stringBuilder.append(" ")
                stringBuilder.append(urlTransform(content.url, content.infor))
                stringBuilder.append(" ")
                continue
            }
            if (content.type == CONTENT_TYPE_FILE) {
                //添加附件
                continue
            }
        }
        val textView = TextView(context)
        textView.setLineSpacing(1.0f, 1.3f)
        textView.setTextColor(Color.parseColor("#DD000000"))
        textView.textSize = 16f
        textView.setTextIsSelectable(true)

        ImageTextUtils.setImageText(textView, stringBuilder.toString())
        //宽高
        textView.layoutParams = ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        linearLayout?.addView(textView)
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
        var begin = -1
        var newContent: String = raw
        try {
            begin = raw.indexOf("""[mobcent_phiz=""", lastBegin)
            if (begin == -1) {
                return newContent
            }
            var end = raw.indexOf("""]""", begin)
            var rawFormatString = raw.substring(begin, end+1) //需要包括]
            var rawUrlString = raw.substring(begin+14, end) //不需要包括]
            var imgString = """<img src="${rawUrlString}">"""
            newContent = raw.replace(rawFormatString, imgString)
        } catch (e:Exception) {
            i("Utils", "${e.toString()}")
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
        return """<img src=${raw}>"""
    }

    private fun urlTransform(raw: String?, title: String?): String {
        if (raw == null || title == null) {
            return ""
        }
        return """<a href="${raw}">${title}</a>"""
    }
}