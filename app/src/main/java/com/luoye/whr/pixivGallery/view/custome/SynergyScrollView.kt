package com.luoye.whr.pixivGallery.view.custome

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.ScrollView

/**
 * Created by whr on 16-12-29.
 * 解决ScrollView 与 RecyclerView冲突
 */

class SynergyScrollView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = -1) : ScrollView(context, attrs, defStyleAttr) {

    private var downX: Int = 0
    private var downY: Int = 0
    private val mTouchSlop: Int by lazy { ViewConfiguration.get(context).scaledTouchSlop }
    private var scrollViewListener: ScrollViewListener? = null
    private var scrollViewTouchListener: ScrollViewTouchListener? = null

    interface ScrollViewListener {
        fun onScrollChanged(scrollView: ScrollView, x: Int, y: Int, oldx: Int, oldy: Int)
    }

    interface ScrollViewTouchListener {
        fun onDown(event: MotionEvent)

        fun onMove(event: MotionEvent)

        fun onUp(event: MotionEvent)
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                scrollViewTouchListener?.onDown(e)
                downX = e.rawX.toInt()
                downY = e.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                scrollViewTouchListener?.onMove(e)
                if (Math.abs(e.rawY - downY) > mTouchSlop) {
                    return canScrollVertically(0)
                }
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return super.onInterceptTouchEvent(e)
    }

    fun setOnScrollChangedListener(scrollViewListener: ScrollViewListener) {
        this.scrollViewListener = scrollViewListener
    }

    fun setOnScrollTouchListener(scrollViewTouchListener: ScrollViewTouchListener) {
        this.scrollViewTouchListener = scrollViewTouchListener
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        scrollViewListener?.onScrollChanged(this, l, t, oldl, oldt)
        super.onScrollChanged(l, t, oldl, oldt)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_UP -> {
                scrollViewTouchListener?.onUp(ev)
            }
        }
        return super.onTouchEvent(ev)
    }
}
