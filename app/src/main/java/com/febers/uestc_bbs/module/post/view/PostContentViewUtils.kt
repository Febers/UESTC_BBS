/*
 * Created by Febers at 18-8-18 上午4:42.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-18 上午4:38.
 */

package com.febers.uestc_bbs.module.post.view

import android.content.Context
import android.graphics.Color
import android.text.util.Linkify
import android.util.Log.i
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.entity.SimpleContentBean

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
const val CONTENT_TYPE_AUDIO = "2"
const val CONTENT_TYPE_URL = "3"
const val CONTENT_TYPE_FILE = "4"
const val DIVIDE_HEIGHT = 20

object PostContentViewUtils {
    fun creat(context: Context?, linearLayout: LinearLayout?, contents: List<SimpleContentBean>?) {
        if (contents == null) {
            return
        }
        for (content in contents) {
            if (context == null) {
                return
            }
            if (content.type == CONTENT_TYPE_TEXT) {
                //添加文本
                var textView = TextView(context)
                textView.setLineSpacing(1.0f, 1.3f)
                textView.setTextColor(Color.parseColor("#DD000000"))
                textView.textSize = 16f
                textView.autoLinkMask = Linkify.ALL
                textView.setText(content.infor)
                //宽高
                textView.layoutParams = ViewGroup
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                linearLayout?.addView(textView)
                linearLayout?.addView(divideView(context, 40))
                continue
            }
            if (content.type == CONTENT_TYPE_IMG) {
                //添加图片
                val imageView = ImageView(context)
                imageView.layoutParams = ViewGroup
                        .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800)
                linearLayout?.addView(imageView)
                linearLayout?.addView(divideView(context))
                Glide.with(context).load(content.originalInfo).into(imageView)
                continue
            }
            if (content.type == CONTENT_TYPE_AUDIO) {
                //添加音频
                continue
            }
            if (content.type == CONTENT_TYPE_URL) {
                //添加超链接
                continue
            }
            if (content.type == CONTENT_TYPE_FILE) {
                //添加附件
                continue
            }
        }
    }

    fun divideView(context: Context?, height: Int = DIVIDE_HEIGHT): View {
        val view = View(context)
        view.layoutParams = ViewGroup
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height)
        return view
    }
}