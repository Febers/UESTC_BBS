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

    private var lastX = 0f
    private var lastY = 0f

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    /**
     * 使用内部拦截法，拦截滑动事件，不向上传递
     *
     * @param ev 滑动事件
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val x = ev!!.x
        val y = ev.y

        when(ev.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true) //处理此类事件
            }
            MotionEvent.ACTION_MOVE -> {
                val deltaX = x - lastX
                val deltaY = y - lastY
//                log("delta y $deltaY")
//                //deltaY > 0 为下拉，交给父布局
//                if (deltaY > 0) {
//                    parent.requestDisallowInterceptTouchEvent(false)    //父布局处理
//                } else {
//                    parent.requestDisallowInterceptTouchEvent(true) //处理此类事件
//                }
                parent.requestDisallowInterceptTouchEvent(true) //处理此类事件
            }
        }
        lastX = x
        lastY = y
          //非常重要
        return super.dispatchTouchEvent(ev)
    }

}