/*
 * Created by Febers at 18-6-9 上午9:22.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-9 上午9:21.
 */

package com.febers.uestc_bbs.home

import android.support.v4.app.Fragment

/**
 * Created by Febers on img_2018/2/3.
 */

object HomeFragmentManager {

    private var sPostFragment: PostFragment? = null
    private var sBlockListFragment: BlockListFragment? = null
    private var sNoticeFragment: NoticeFragment? = null
    private var sMessageFragment: MessageFragment? = null
    private var sMoreFragment: MoreFragment? = null

    fun getInstance(position: Int): Fragment {

        when (position) {
            0 -> {
                if (sPostFragment == null) {
                    sPostFragment = PostFragment()
                }
                return sPostFragment as Fragment
            }
            1 -> {
                if (sBlockListFragment == null) {
                    sBlockListFragment = BlockListFragment()
                }
                return sBlockListFragment as Fragment
            }
            2 -> {
                if (sNoticeFragment == null) {
                    sNoticeFragment = NoticeFragment()
                }
                return sNoticeFragment as Fragment
            }
            3 -> {
                if (sMessageFragment == null) {
                    sMessageFragment = MessageFragment()
                }
                return sMessageFragment as Fragment
            }
            4 -> {
                if (sMoreFragment == null) {
                    sMoreFragment = MoreFragment()
                }
                return sMoreFragment as Fragment
            }
        }
        return Fragment()
    }
}
