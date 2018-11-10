package com.febers.uestc_bbs.view.helper

import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.core.widget.NestedScrollView

object FABBehaviorHelper {

    fun fabBehaviorWithScrollView(scrollView: NestedScrollView, fab: FloatingActionButton) {
        scrollView.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            //Log.i("PD", "scrollY: $scrollY, oldScrollY: $oldScrollY diff ${ -oldScrollY + scrollY}")
            if (scrollY - oldScrollY < -3) {
                //Log.i("PD", "fab show")
                fab.show()
            }
            if (scrollY - oldScrollY > 3) {
                //Log.i("PD", "fab hide")
                fab.hide()
            }
        }

    }
}