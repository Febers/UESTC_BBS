/*
 * Created by Febers at 18-6-12 下午6:41.
 * Copyright (c). All rights reserved.
 * Last modified 18-6-12 下午6:41.
 */

package com.febers.uestc_bbs.view.custom

import android.content.Context
import android.support.design.widget.FloatingActionButton
import android.util.AttributeSet
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.view.View


class ScrollAwareFABBehavior(context: Context, attributesSet: AttributeSet):
        FloatingActionButton.Behavior(context, attributesSet) {

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton,
                                     directTargetChild: View, target: View, axes: Int, type: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        if (dyConsumed > 0 && child.visibility == View.VISIBLE) {
            child.hide()
        } else if (dyConsumed < 0 && child.visibility != View.VISIBLE) {
            child.show()
        }
    }
}