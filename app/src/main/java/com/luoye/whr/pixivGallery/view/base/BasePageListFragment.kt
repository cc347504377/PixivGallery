package com.luoye.whr.pixivGallery.view.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.luoye.whr.kotlinlibrary.util.log

abstract class BasePageListFragment : BaseSharedFragment() {

    private var isFirst = true
    private var isUserVisible = false
    private var isViewCreated = false
    private var needInit = false
    private var isStyleChanged = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (needInit) {
            start()
        }
        isViewCreated = true
    }

    /**
     * 实现类初始化
     */
    abstract fun init()

    /**
     * 开始执行
     */
    private fun start() {
        setupSharedCallback()
        init()
    }

    /**
     * 切换显示风格
     */
    override fun changeStyle() {
        if (isUserVisible) {
            setupRecyclerView()
        } else {
            isStyleChanged = true
        }
    }

    /**
     * 判断当前页面是否可见
     * 如果不可见回收图片内存
     *
     * 注意：仅针对viewpager嵌套有效
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isUserVisible = isVisibleToUser
        if (isFirst && isVisibleToUser) {
            if (isViewCreated) {
                start()
            } else {
                needInit = true
            }
            isFirst = false
        } else if (!isVisibleToUser && !isFirst) {
            clearRecyclerImg()
            removeSharedCallback()
        } else if (isVisibleToUser && !isFirst) {
            if (isStyleChanged) {
                setupRecyclerView()
                isStyleChanged = false
            }
            loadRecyclerImg()
            setupSharedCallback()
        }
    }
}