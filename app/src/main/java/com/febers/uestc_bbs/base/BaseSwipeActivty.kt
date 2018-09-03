package com.febers.uestc_bbs.base

import me.yokeyword.fragmentation_swipeback.core.ISwipeBackActivity
import me.yokeyword.fragmentation.SwipeBackLayout
import android.os.Bundle
import me.yokeyword.fragmentation_swipeback.core.SwipeBackActivityDelegate



abstract class BaseSwipeActivty : BaseActivity(), ISwipeBackActivity {

    val mSwipeDelegate = SwipeBackActivityDelegate(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSwipeDelegate.onCreate(savedInstanceState)
        swipeBackLayout.setParallaxOffset(0.0f)
        swipeBackLayout.setEdgeLevel(SwipeBackLayout.EdgeLevel.MED)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mSwipeDelegate.onPostCreate(savedInstanceState)
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mSwipeDelegate.swipeBackLayout
    }

    /**
     * 是否可滑动
     * @param enable
     */
    override fun setSwipeBackEnable(enable: Boolean) {
        mSwipeDelegate.setSwipeBackEnable(enable)
    }

    override fun setEdgeLevel(edgeLevel: SwipeBackLayout.EdgeLevel) {
        mSwipeDelegate.setEdgeLevel(edgeLevel)
    }

    override fun setEdgeLevel(widthPixel: Int) {
        mSwipeDelegate.setEdgeLevel(widthPixel)
    }

    /**
     * 限制SwipeBack的条件,默认栈内Fragment数 <= 1时 , 优先滑动退出Activity , 而不是Fragment
     *
     * @return true: Activity优先滑动退出;  false: Fragment优先滑动退出
     */
    override fun swipeBackPriority(): Boolean {
        return mSwipeDelegate.swipeBackPriority()
    }
}