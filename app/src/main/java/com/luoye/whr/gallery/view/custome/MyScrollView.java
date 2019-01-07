package com.luoye.whr.gallery.view.custome;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.ScrollView;

/**
 * Created by whr on 16-12-29.
 * 解决ScrollView 与 RecyclerView冲突
 */

public class MyScrollView extends ScrollView {
    public interface ScrollViewListener {
        void onScrollChanged(ScrollView scrollView, int x, int y, int oldx, int oldy);
    }
    public interface ScrollViewTouchListener{
        void onDown(MotionEvent event);

        void onMove(MotionEvent event);

        void onUp(MotionEvent event);
    }
    private int downX;
    private int downY;
    private int mTouchSlop;
    private ScrollViewListener scrollViewListener;
    private ScrollViewTouchListener scrollViewTouchListener;

    public MyScrollView(Context context) {
        this(context,null);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (scrollViewTouchListener!=null)
                scrollViewTouchListener.onDown(e);
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (scrollViewTouchListener!=null)
                scrollViewTouchListener.onMove(e);
//                int moveY = (int) e.getRawY();
//                if (moveY - downY > mTouchSlop) {
//
//                }
//                if (Math.abs(moveY - downY) > mTouchSlop) {
////                    return true;
////                }
                return canScrollVertically(0);
//                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onInterceptTouchEvent(e);
    }

    public void setOnScrollChangedListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public void setOnScrollTouchListener(ScrollViewTouchListener scrollViewTouchListener) {
        this.scrollViewTouchListener = scrollViewTouchListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (scrollViewListener!=null)
        scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
        super.onScrollChanged(l, t, oldl, oldt);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                if (scrollViewTouchListener!=null)
                scrollViewTouchListener.onUp(ev);
                break;
        }
        return super.onTouchEvent(ev);
    }
}
