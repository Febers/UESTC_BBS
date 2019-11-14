package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView

/**
 * 重写 isFocus 方法，让其永远返回 true，达到当界面有多个焦点时，
 * TextView 都认为没有失去焦点的作用，从而开启跑马灯效果
 */
class MarqueeTextView : TextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun isFocused(): Boolean = true
}
