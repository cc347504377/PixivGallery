package com.luoye.whr.pixivGallery.view.home.rank

import android.os.Bundle
import com.luoye.whr.pixivGallery.common.CommonIllustBean
import com.luoye.whr.pixivGallery.common.IllustsBean
import com.luoye.whr.pixivGallery.presenter.PixivImagePresenter
import com.luoye.whr.pixivGallery.view.base.BasePageListFragment
import com.luoye.whr.kotlinlibrary.net.PublicCallback
import com.luoye.whr.kotlinlibrary.util.*
import kotlinx.android.synthetic.main.fragment_img_list.*

/**
 * 图片列表Fragment
 * 通用图片展示
 */
open class RankListFragment : BasePageListFragment() {

    private var mode = 0
    private var date = ""

    /**
     * 接收数据需要请求的数据类型
     */
    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        args?.let {
            mode = it.getInt("mode", 0)
            date = it.getString("date", "")
        }
    }

    override fun init() {
        initList()
    }

    private fun initList() {
        setupRecyclerView()
        ll_frg_list.refresh()
    }

    override fun getData(dataOperation: (MutableList<IllustsBean>) -> Unit) {
        this.dataOperation = dataOperation
        PixivImagePresenter.getRankList(mode, date, dataCallback)
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