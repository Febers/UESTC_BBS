/*
 * Created by Febers at 18-8-16 下午6:31.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-16 下午6:28.
 */

package com.febers.uestc_bbs.module.user.view

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.ARG_PARAM1
import com.febers.uestc_bbs.base.BasePopFragment
import kotlinx.android.synthetic.main.fragment_my_reply.*


class RepliesFragment : BasePopFragment() {

    override fun setToolbar(): Toolbar {
        return toolbar_my_reply
    }

    override fun setContentView(): Int {
        return R.layout.fragment_my_reply
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                RepliesFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        return attachToSwipeBack(view!!)
    }
}
