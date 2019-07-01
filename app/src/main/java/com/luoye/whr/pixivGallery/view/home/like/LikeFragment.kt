package com.luoye.whr.pixivGallery.view.home.like

import android.os.Bundle
import android.view.View
import com.luoye.whr.kotlinlibrary.util.toast
import com.luoye.whr.pixivGallery.adapter.LikeListAdapter
import com.luoye.whr.pixivGallery.common.CommonIllustBean
import com.luoye.whr.pixivGallery.common.IllustsBean
import com.luoye.whr.pixivGallery.presenter.IllustCallback
import com.luoye.whr.pixivGallery.presenter.PixivImagePresenter
import com.luoye.whr.pixivGallery.view.base.BaseTabListFragment
import kotlinx.android.synthetic.main.fragment_img_list.*

class LikeFragment : BaseTabListFragment() {
    var style = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
    }

    override fun getListAdapter() =
            LikeListAdapter(requireContext(), this)

    private fun initList() {
        setupRecyclerView()
        ll_frg_list.refresh()
    }

    override fun getData(dataOperation: (MutableList<IllustsBean>) -> Unit) {
        this.dataOperation = dataOperation
        PixivImagePresenter.getLikeIllust(dataCallback)
    }

    override fun getNext(dataOperation: (MutableList<IllustsBean>) -> Unit) {
        if (!nextUrl.isNullOrEmpty()) {
            this.dataOperation = dataOperation
            PixivImagePresenter.getNext(nextUrl!!, dataCallback)
        } else {
            ll_frg_list.onError("没有更多")
        }
    }

    /**
     * 网络请求回调
     */
    private val dataCallback = object : IllustCallback<CommonIllustBean> {
        override fun onSuccess(t: CommonIllustBean) {
            super.onSuccess(t)
            dataOperation?.invoke(t.illusts)
            nextUrl = t.next_url
        }

        override fun onStart() {
        }

        override fun onFailed(msg: String) {
            toast("加载失败：$msg")
            ll_frg_list.onError("走丢了")
        }
    }
}