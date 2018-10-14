package com.febers.uestc_bbs.module.post.view.bottom_sheet

interface PostReplySendListener {
    fun onReplySend(vararg contents: Pair<String, String>, toUid: Int)
}