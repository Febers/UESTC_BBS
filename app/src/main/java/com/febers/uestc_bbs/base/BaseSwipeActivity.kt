package com.febers.uestc_bbs.base

import me.yokeyword.fragmentation_swipeback.core.ISwipeBackActivity
import me.yokeyword.fragmentation.SwipeBackLayout
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import me.yokeyword.fragmentation_swipeback.core.SwipeBackActivityDelegate



abstract class BaseSwipeActivity : BaseActivity(), ISwipeBackActivity {

    val mSwipeDelegate = SwipeBackActivityDelegate(this)

    protected open fun setToolbar(): Toolbar? = null

    protected open fun setMenu(): Int? {
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSwipeDelegate.onCreate(savedInstanceState)
        swipeBackLayout.setParallaxOffset(0.0f)
        swipeBackLayout.setEdgeLevel(SwipeBackLayout.EdgeLevel.MED)
        setSupportActionBar(setToolbar())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (setMenu()!=null) {
            setToolbar()!!.inflateMenu(setMenu()!!)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (setMenu()!=null) {
            menuInflater.inflate(setMenu()!!, menu)
        }
        return true
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}