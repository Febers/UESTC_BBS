package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.LinearLayout
import com.febers.uestc_bbs.utils.log

/**
 * 该布局拦截了所有触控事件，交由自己和子布局处理
 * 用在设置刷新界面样式的BottomSheetDialogFragment中
 * 因为该界面需要拦截滑动事件，否则BottomSheet会影响用户操作
 * 参考：一文解决Android View滑动冲突 - 简书
 * https://www.jianshu.com/p/982a83271327
 */
class InterceptTouchLayout: LinearLayout {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 使用内部拦截法，拦截滑动事件，不向上传递
     *
     * @param ev 滑动事件
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)  //非常重要
        return super.dispatchTouchEvent(ev)
    }

}