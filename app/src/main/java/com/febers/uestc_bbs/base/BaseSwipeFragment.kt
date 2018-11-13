package com.febers.uestc_bbs.base

import android.os.Bundle
import androidx.annotation.FloatRange
import android.view.*
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.febers.uestc_bbs.R
import me.yokeyword.fragmentation.SwipeBackLayout
import me.yokeyword.fragmentation_swipeback.core.ISwipeBackFragment
import me.yokeyword.fragmentation_swipeback.core.SwipeBackFragmentDelegate

/**
 * 由主Activity创建的一类Fragment
 * 弹出时，隐藏主Activity的bottomBar和toolbar
 * 默认支持滑动返回
 * 为了应用风格更和谐，暂时取消滑动返回
 * 销毁时又恢复
 */
const val SHOW_BOTTOM_BAR_ON_DESTROY = "show_bottom_bar"

abstract class BaseSwipeFragment: BaseFragment(), ISwipeBackFragment {

    internal val mSwipeDelegate = SwipeBackFragmentDelegate(this)
    protected var showBottomBarOnDestroy = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        try {
            activity?.findViewById<AHBottomNavigation>(R.id.bottom_navigation_home)?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val view = super.onCreateView(inflater, container, savedInstanceState)
        return attachToSwipeBack(view!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSwipeDelegate.onCreate(savedInstanceState)
        mSwipeDelegate.setSwipeBackEnable(false)
        arguments?.let {
            showBottomBarOnDestroy = it.getBoolean(SHOW_BOTTOM_BAR_ON_DESTROY)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSwipeDelegate.onViewCreated(view, savedInstanceState)
        swipeBackLayout.setParallaxOffset(0.0f) // 滑动退出视觉差，默认0.3
        swipeBackLayout.setEdgeLevel(SwipeBackLayout.EdgeLevel.MED)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (setMenu() == null) return
        inflater?.inflate(setMenu()!!, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            pop()
            hideSoftInput()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun attachToSwipeBack(view: View): View {
        return mSwipeDelegate.attachToSwipeBack(view)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        mSwipeDelegate.onHiddenChanged(hidden)
    }

    override fun getSwipeBackLayout(): SwipeBackLayout {
        return mSwipeDelegate.swipeBackLayout
    }

    /**
     * 是否可滑动
     *
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
     * Set the offset of the parallax slip.
     */
    override fun setParallaxOffset(@FloatRange(from = 0.0, to = 1.0) offset: Float) {
        mSwipeDelegate.setParallaxOffset(offset)
    }

    override fun onDestroyView() {
        mSwipeDelegate.onDestroyView()
        if (showBottomBarOnDestroy) {
            activity?.findViewById<AHBottomNavigation>(R.id.bottom_navigation_home)?.visibility = View.VISIBLE
        }
        super.onDestroyView()
    }

    override fun onBackPressedSupport(): Boolean {
        pop()
        return true
    }

    protected fun enableSwipeBack(enable: Boolean) {
        mSwipeDelegate.setSwipeBackEnable(enable)
    }
}