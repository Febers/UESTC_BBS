/*
 * Created by Febers at 18-8-20 上午12:37.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-20 上午12:37.
 */

package com.febers.uestc_bbs.module.post.view.bottom_sheet

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.view.View
import android.view.WindowManager
import com.febers.uestc_bbs.base.REPLY_NO_QUOTA
import com.febers.uestc_bbs.base.REPLY_QUOTA
import kotlinx.android.synthetic.main.layout_bottom_sheet_reply.*

class PostReplyBottomSheet(context: Context, style: Int, private val listener: PostReplySendListener):
        BottomSheetDialog(context, style) {

    private var topicId: Int = 0
    private var toUid: Int = 0
    private var toUName: String = ""
    private var isQuote: Int = REPLY_NO_QUOTA
    private var replyId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        image_view_post_reply_picture.visibility = View.INVISIBLE
        image_view_post_reply_emoji.visibility = View.INVISIBLE
        image_view_post_reply_at.visibility = View.INVISIBLE

        image_view_post_reply_emoji.setOnClickListener {  }
        btn_post_reply.setOnClickListener {
            val stContent: String = edit_view_post_reply.text.toString()
            if (stContent.isEmpty()) return@setOnClickListener
            isQuote = if (topicId == toUid) {
                REPLY_NO_QUOTA
            } else {
                REPLY_QUOTA
            }
            listener.onReplySend(toUid = toUid, isQuote = isQuote, replyId = replyId, contents = *arrayOf(0 to stContent))
        }
    }

    fun showWithData(topicId: Int, toUId: Int, replyId: Int, toUName: String) {
        this.topicId = topicId
        this.toUid = toUId
        this.replyId = replyId
        this.toUName = toUName
        text_view_post_reply_to_name.text = this.toUName
        super.show()
    }
}