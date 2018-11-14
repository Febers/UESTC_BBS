package com.febers.uestc_bbs.view.helper

import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.widget.NestedScrollView

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