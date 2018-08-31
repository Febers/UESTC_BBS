/*
 * Created by Febers at 18-8-20 上午12:37.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-20 上午12:37.
 */

package com.febers.uestc_bbs.module.post.view

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.view.View
import android.view.WindowManager
import com.febers.uestc_bbs.utils.KeyBoardUtils
import com.lqr.emoji.EmotionKeyboard
import com.lqr.emoji.IEmotionSelectedListener
import kotlinx.android.synthetic.main.layout_bottom_sheet_reply.*

class PostBottomSheet(context: Context, style: Int) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        emotion_layout.visibility = View.GONE
        emotion_layout.attachEditText(edit_view)
        emotion_layout.setEmotionSelectedListener(object : IEmotionSelectedListener {
            override fun onEmojiSelected(key: String?) {

            }

            override fun onStickerSelected(categoryName: String?, stickerName: String?, stickerBitmapPath: String?) {

            }
        })
        image_view_emoji.setOnClickListener { openEmoKeyBoard() }
    }

    fun openEmoKeyBoard() {
        if (emotion_layout.visibility == View.VISIBLE){
            emotion_layout.visibility = View.GONE
            KeyBoardUtils.openKeyboard(edit_view, context)
        } else {
            KeyBoardUtils.closeKeyboard(edit_view, context)
            emotion_layout.visibility = View.VISIBLE
        }
    }
}