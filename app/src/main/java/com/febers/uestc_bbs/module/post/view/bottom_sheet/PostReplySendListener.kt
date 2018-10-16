package com.febers.uestc_bbs.module.post.view.bottom_sheet

interface PostReplySendListener {
    fun onReplySend(toUid: Int, isQuote: Int, replyId: Int, vararg contents: Pair<Int, String>)
}