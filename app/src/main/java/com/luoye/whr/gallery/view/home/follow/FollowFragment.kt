package com.luoye.whr.gallery.view.home.follow

import android.os.Bundle
import android.view.View
import com.luoye.whr.gallery.common.CommonIllustBean
import com.luoye.whr.gallery.common.IllustsBean
import com.luoye.whr.gallery.presenter.PixivImagePresenter
import com.luoye.whr.gallery.view.base.BaseTabListFragment
import com.luoye.whr.kotlinlibrary.net.PublicCallback
import com.luoye.whr.kotlinlibrary.util.toast
import kotlinx.android.synthetic.main.fragment_img_list.*

class FollowFragment : BaseTabListFragment() {

    var style = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
    }

    private fun initList() {
        setupRecyclerView()
        ll_frg_list.refresh()
    }

    override fun getData(dataOperation: (MutableList<IllustsBean>) -> Unit) {
        this.dataOperation = dataOperation
        PixivImagePresenter.getFollowIllusts(dataCallback)
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
    private val dataCallback = object : PublicCallback.DataCallBack<CommonIllustBean> {
        override fun onSuccess(t: CommonIllustBean) {
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