package com.luoye.whr.pixivGallery.view.base

import android.app.SharedElementCallback
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_img_list.*

abstract class BaseActivityListFragment : BaseListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initShared()
        initList()
    }

    private fun initShared() {
        activity?.setExitSharedElementCallback(object : SharedElementCallback() {
            override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
                this@BaseActivityListFragment.invoke(names, sharedElements)
            }
        })
    }

    private fun initList() {
        setupRecyclerView()
        ll_frg_list.refresh()
    }
}