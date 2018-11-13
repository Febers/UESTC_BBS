package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.febers.uestc_bbs.R
import kotlin.properties.Delegates

/**
 * 帖子详情界面，用于回复的Dialog
 * BottomSheet容易造成recyclerView的异常位移
 * 初始化后进行相应控件的绑定
 * 点击发送Button之后，通过接口回调将数据传给主视图
 * TODO 尚不知道有无必要
 */
class PostReplyDialog : AlertDialog{

    private lateinit var topicName: String
    private var topicId: Int by Delegates.notNull<Int>()

    private lateinit var btnReply: Button
    private lateinit var dialog: AlertDialog
    private lateinit var dialogView: View


    constructor(context: Context, topicName: String, topicId: Int):super(context) {
        this.topicName = topicName
        this.topicId = topicId
        dialog = AlertDialog.Builder(context).create()
        /*
            使用inflate的时候使用两个参数的方法，当第二参数为null，即rootView为null，编译器总是警告
            然后，dialog在构建的时候会擦除所有的根视图，所以其实是无影响的，当然，对于recyclerView或者listView来说不一样
            细节可百度
         */
        dialogView = layoutInflater.inflate(R.layout.dialog_post_reply, null)
    }

    fun show(toUserName: String? = null, toUserId: Int?) {

    }
}