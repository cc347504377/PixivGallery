package com.luoye.whr.gallery.common

import android.animation.ValueAnimator
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator

class FabScrollBehavior
// 因为需要在布局xml中引用，所以必须实现该构造方法
(context: Context, attrs: AttributeSet) : FloatingActionButton.Behavior(context, attrs) {

    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout,
                                     child: FloatingActionButton,
                                     directTargetChild: View,
                                     target: View, axes: Int, type: Int): Boolean {
        // 确保滚动方向为垂直方向
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    private var isScrollDown = false
    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: FloatingActionButton, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        if (dyConsumed > 0 && !isScrollDown) { // 向下滑动
            isScrollDown = true
            animateOut(child)
        } else if (dyConsumed < 0 && isScrollDown) { // 向上滑动
            isScrollDown = false
            animateIn(child)
        }
    }

    // FAB移出屏幕动画（隐藏动画）
    private fun animateOut(fab: FloatingActionButton) {

        ValueAnimator().apply {
            setFloatValues(1f, 0f)
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Float
                fab.scaleX = value
                fab.scaleY = value
                fab.alpha = value
            }
            start()
        }
    }

    // FAB移入屏幕动画（显示动画）
    private fun animateIn(fab: FloatingActionButton) {
        ValueAnimator().apply {
            setFloatValues(0f, 1f)
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Float
                fab.scaleX = value
                fab.scaleY = value
                fab.alpha = value
            }
            start()
        }
    }
}