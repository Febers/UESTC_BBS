package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class VoteView: ViewGroup {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun <T> setVoteData(data: List<T>) {

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //测量子控件
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        //获取宽高的模式，大小
        val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

        //获取子View的宽，高
        val childAt = getChildAt(0)
        val childMeasuredWidth = childAt.measuredWidth
        val childMeasuredHeight = childAt.measuredHeight

        //分四种情况讨论
        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            //宽度设置为3个子view宽度相加，高度设置为一个子View高度
            setMeasuredDimension(childMeasuredWidth * 3, childMeasuredHeight)
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            //宽度设置为3个子View宽度相加；
            //高度为exactly模式，直接取测量高度大小即可(分析ViewGroup$getChildMeasureSpec源码可知)
            setMeasuredDimension(childMeasuredWidth * 3, heightSpecSize)
        } else if (heightSpecMode == MeasureSpec.AT_MOST){
            //宽度为exactly模式，直接取测量的宽度值；高度为一个子View高度
            setMeasuredDimension(widthSpecSize, childMeasuredHeight)
        } else {
            //宽高都是exactly模式，则直接使用父view给的建议值大小
            setMeasuredDimension(widthSpecSize, heightSpecSize)
        }
    }


}