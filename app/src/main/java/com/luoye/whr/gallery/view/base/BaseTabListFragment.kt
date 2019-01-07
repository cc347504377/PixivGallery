package com.luoye.whr.gallery.view.base

import android.os.Bundle
import android.view.View

abstract class BaseTabListFragment : BaseSharedFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSharedCallback()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            clearRecyclerImg()
            removeSharedCallback()
        } else {
            loadRecyclerImg()
            setupSharedCallback()
        }
    }
}