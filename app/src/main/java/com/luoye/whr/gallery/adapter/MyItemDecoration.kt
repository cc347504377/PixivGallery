package com.luoye.whr.gallery.adapter

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.luoye.whr.gallery.MyApplication
import com.luoye.whr.kotlinlibrary.util.dpToPx

class MyItemDecoration : RecyclerView.ItemDecoration() {

    private val margin by lazy { dpToPx(2f, MyApplication.context.get()!!).toInt() }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.set(margin, margin, margin, margin)
    }
}