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
import kotlinx.android.synthetic.main.layout_bottom_sheet_reply.*

class PostReplyBottomSheet(context: Context, style: Int, val listener: PostReplySendListener):
        BottomSheetDialog(context, style) {

    private var toUserId: Int = 0
    private var toUserName: String = ""

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
            listener.onReplySend(contents = *arrayOf("text" to stContent), toUid = toUserId)
        }
    }

    fun showWithData(toUId: Int, toUName: String) {
        toUserId = toUId
        toUserName = toUName
        text_view_post_reply_to_name.text = toUserName
        super.show()
    }
}