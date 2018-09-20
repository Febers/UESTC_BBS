/*
 * Created by Febers at 18-8-16 下午10:32.
 * Copyright (c). All rights reserved.
 * Last modified 18-8-16 下午10:32.
 */

package com.febers.uestc_bbs.base

import android.os.Bundle
import android.support.annotation.FloatRange
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
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
 * 销毁时又恢复
 */
const val SHOW_BOTTOM_BAR_ON_DESTROY = "show_bottom_bar"

abstract class BaseSwipeFragment: BaseFragment(), ISwipeBackFragment {

    internal val mSwipeDelegate = SwipeBackFragmentDelegate(this)
    protected var showBottomBarOnDestroy = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.findViewById<AHBottomNavigation>(R.id.bottom_navigation_home)?.visibility = View.GONE
        val view = super.onCreateView(inflater, container, savedInstanceState)
        return attachToSwipeBack(view!!)
    }

    protected open fun setMenu(): Int? {
        return null
    }

    protected abstract fun setToolbar(): Toolbar?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSwipeDelegate.onCreate(savedInstanceState)
        arguments?.let {
            fid = it.getString(FID)
            showBottomBarOnDestroy = it.getBoolean(SHOW_BOTTOM_BAR_ON_DESTROY)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSwipeDelegate.onViewCreated(view, savedInstanceState)
        swipeBackLayout.setParallaxOffset(0.0f) // 滑动退出视觉差，默认0.3
        swipeBackLayout.setEdgeLevel(SwipeBackLayout.EdgeLevel.MED)
        //添加toolbar点击返回
        val activity: AppCompatActivity = getActivity() as AppCompatActivity
        activity.setSupportActionBar(setToolbar())
        activity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
        setToolbar()?.setNavigationOnClickListener { pop() }
        if (setMenu()!=null) {
            setHasOptionsMenu(true)
            setToolbar()?.inflateMenu(setMenu()!!)
            setToolbar()?.title = ""
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            hideSoftInput()
            pop()
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
}