/*
 * Created by Febers at 18-6-9 下午6:09.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-7 下午5:36.
 */

package com.febers.uestc_bbs.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.GridView

/**scrollview中嵌套gridView时，只显示一行
 * 重写onMeasure方法
 */

class CustomGridView : GridView {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, expandSpec)
    }
}
