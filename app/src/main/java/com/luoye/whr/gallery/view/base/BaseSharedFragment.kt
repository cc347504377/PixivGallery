package com.luoye.whr.gallery.view.base

import com.luoye.whr.gallery.view.home.HomeActivity

abstract class BaseSharedFragment : BaseListFragment() {

    protected fun setupSharedCallback() {
        (activity as HomeActivity).addSharedCallback(this)
    }

    protected fun removeSharedCallback() {
        (activity as HomeActivity).removeSharedCallback(this)
    }

    override fun onDestroyView() {
        removeSharedCallback()
        super.onDestroyView()
    }
}