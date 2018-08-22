/*
 * Created by Febers at 18-8-20 上午12:37.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-20 上午12:37.
 */

package com.febers.uestc_bbs.module.post.view

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.view.WindowManager

class MyBottomSheetDialog(context: Context, style: Int) : BottomSheetDialog(context, style) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }
}