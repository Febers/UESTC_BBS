/*
 * Created by Febers at 18-8-14 上午1:28.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-13 下午10:42.
 */

package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ListView

class CustomListView : ListView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        val expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }
}
