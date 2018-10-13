package com.febers.uestc_bbs.view.utils

import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.NestedScrollView

object FABBehaviorHelper {

    fun fabBehaviorWithScrollView(scrollView: NestedScrollView, fab: FloatingActionButton) {
        scrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY - oldScrollY < -3) {
                fab.show()
            }
            if (scrollY - oldScrollY > 3) {
                fab.hide()
            }
        }
    }
}