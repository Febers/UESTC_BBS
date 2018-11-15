/*
 * Created by Febers at 18-8-20 上午12:37.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-20 上午12:37.
 */

package com.febers.uestc_bbs.module.post.view.bottom_sheet

import android.content.Context
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.view.View
import android.view.WindowManager
import com.febers.uestc_bbs.base.REPLY_NO_QUOTA
import com.febers.uestc_bbs.base.REPLY_QUOTA
import kotlinx.android.synthetic.main.layout_bottom_sheet_reply.*

/**
 * 帖子详情界面，回复的底部弹出框
 * 发送消息按钮之后之后显示progress
 *
 */
class PostReplyBottomSheet(context: Context, style: Int, private val listener: PostReplySendListener):
        BottomSheetDialog(context, style) {

    private var isQuote: Int = REPLY_NO_QUOTA
    private var toUName: String = ""
    private var replyId: Int = 0
    private var topicId: Int = 0
    private var toUid: Int = 0
    private var lastToUid = toUid
    private var needSendImages: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        image_view_post_reply_picture.visibility = View.INVISIBLE
        image_view_post_reply_emoji.visibility = View.INVISIBLE
        image_view_post_reply_at.visibility = View.INVISIBLE

        image_view_post_reply_emoji.setOnClickListener {
            //TODO 回帖添加图片
        }
        /*
            点击发送按钮之后发送消息
            将此后的逻辑交给调用的界面
         */
        btn_post_reply.setOnClickListener {
            val stContent: String = edit_view_post_reply.text.toString()
            if (stContent.isEmpty()) return@setOnClickListener
            isQuote = if (topicId == toUid) {
                REPLY_NO_QUOTA
            } else {
                REPLY_QUOTA
            }
            progress_bar_post_reply.visibility = View.VISIBLE
            listener.onReplySend(toUid = toUid, isQuote = isQuote, replyId = replyId, contents = *arrayOf(0 to stContent))
        }
        setOnCancelListener {
            edit_view_post_reply.clearFocus()
        }
    }

    /**
     * 由主界面调用，传入必要的参数
     *
     * @param topicId 作者的id
     * @param toUid 回复给谁
     * @param replyId 此为引用回复时引用的回复的id
     * @param toUName 所回复用户的名称
     */
    fun showWithData(topicId: Int, toUid: Int, replyId: Int, toUName: String) {
        this.topicId = topicId
        this.toUid = toUid
        this.replyId = replyId
        this.toUName = toUName
        if (toUid == topicId) this.toUName += "(楼主)"
        text_view_post_reply_to_name.text = this.toUName
        if (lastToUid != toUid) {
            edit_view_post_reply.text.clear()
            lastToUid = this.toUid
        }
        show()
    }

    /**
     * 显示底部框
     * 编辑视图获取焦点
     */
    override fun show() {
        super.show()
        edit_view_post_reply.requestFocus()
    }

    fun setImagePaths(imgPaths: List<String>) {
        needSendImages.addAll(imgPaths)
    }

    /**
     * 当回复成功发送之后，主界面将调用这个方法
     * 隐藏底部框和进度条
     */
    fun finish() {
        super.hide()
        progress_bar_post_reply.visibility = View.GONE
    }
}