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
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
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
abstract class BasePopFragment: BaseFragment(), ISwipeBackFragment {

    internal val mSwipeDelegate = SwipeBackFragmentDelegate(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.findViewById<Toolbar>(R.id.toolbar_home)?.visibility = View.GONE
        activity?.findViewById<AHBottomNavigation>(R.id.bottom_navigation_home)?.visibility = View.GONE
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected abstract fun setToolbar(): Toolbar?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSwipeDelegate.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSwipeDelegate.onViewCreated(view, savedInstanceState)
        getSwipeBackLayout().setParallaxOffset(0.0f) // 滑动退出视觉差，默认0.3
        //添加toolbar点击返回
        val activity: AppCompatActivity = getActivity() as AppCompatActivity
        activity.setSupportActionBar(setToolbar())
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setToolbar()?.setNavigationOnClickListener { pop() }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
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
        activity?.findViewById<Toolbar>(R.id.toolbar_home)?.visibility = View.VISIBLE
        activity?.findViewById<AHBottomNavigation>(R.id.bottom_navigation_home)?.visibility = View.VISIBLE
        super.onDestroyView()
    }

}