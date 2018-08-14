/*
 * Created by Febers at 18-8-14 上午1:45.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-14 上午1:45.
 */

package com.febers.uestc_bbs.module.message.view

import android.util.Log
import com.febers.uestc_bbs.R
import com.febers.uestc_bbs.base.BaseFragment

/**
 * 私信通知
 */
class PMFragment : BaseFragment() {
    override fun setContentView(): Int {
        return R.layout.fragment_sub_message
    }

    override fun initView() {

    }

    override fun lazyLoad() {

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.i("PMFragment", "$hidden")
    }
}