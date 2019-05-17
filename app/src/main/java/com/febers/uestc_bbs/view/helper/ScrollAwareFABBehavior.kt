package com.febers.uestc_bbs.view.helper

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ScrollAwareFABBehavior(context: Context, attrs: AttributeSet): FloatingActionButton.Behavior() {

    var scaleHideX: ObjectAnimator? = null
    var scaleHideY: ObjectAnimator? = null
    var scaleShowX: ObjectAnimator? = null
    var scaleShowY: ObjectAnimator? = null

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, directTargetChild: View, target: View, axes: Int): Boolean {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
                || super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes)
        //return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes)
    }

    @SuppressLint("RestrictedApi")
    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        if (dyConsumed > 1 && child.visibility == View.VISIBLE) {
            scaleHide(child)
        } else if (dyConsumed < -1 && child.visibility == View.INVISIBLE) {
            scaleShow(child)
        }
    }

    @SuppressLint("RestrictedApi")
    private fun scaleHide(child: FloatingActionButton) {
        if (scaleHideX == null || scaleHideY == null) {
            scaleHideX = ObjectAnimator.ofFloat(child, "scaleX", 1f, 0f)
            scaleHideY = ObjectAnimator.ofFloat(child, "scaleY", 1f, 0f)
            scaleHideX!!.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) { }
                override fun onAnimationCancel(animation: Animator?) { }
                override fun onAnimationStart(animation: Animator?) { }
                override fun onAnimationEnd(animation: Animator?) {
                    child.visibility = View.INVISIBLE
                }
            })
        }
        AnimatorSet().apply {
            duration = 100
            play(scaleHideX)
            play(scaleHideY)
            start()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun scaleShow(child: FloatingActionButton) {
        if (scaleShowX == null || scaleShowY == null) {
            scaleShowX = ObjectAnimator.ofFloat(child, "scaleX", 1f)
            scaleShowY = ObjectAnimator.ofFloat(child, "scaleY", 1f)
        }
        child.visibility = View.VISIBLE
        AnimatorSet().apply {
            duration = 100
            play(scaleShowX)
            play(scaleShowY)
            start()
        }
    }
}