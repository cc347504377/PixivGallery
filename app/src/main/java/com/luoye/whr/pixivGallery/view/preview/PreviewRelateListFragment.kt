package com.luoye.whr.pixivGallery.view.preview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luoye.whr.pixivGallery.R
import com.luoye.whr.pixivGallery.adapter.ListAdapter
import com.luoye.whr.pixivGallery.common.CommonIllustBean
import com.luoye.whr.pixivGallery.common.IllustsBean
import com.luoye.whr.pixivGallery.presenter.PixivImagePresenter
import com.luoye.whr.kotlinlibrary.net.PublicCallback
import com.luoye.whr.kotlinlibrary.util.toast
import kotlinx.android.synthetic.main.fragment_precview_relate.*

class PreviewRelateListFragment : Fragment(), View.OnClickListener {

    override fun onClick(v: View) {
        val viewHolder = v.getTag(R.id.tag_holder) as ListAdapter.ViewHolder
        PreviewActivity.startActivity(this, listAdapter.adapter.data,
                viewHolder.layoutPosition)
    }

    private var illustsId = ""
    private var nextUrl: String? = null
    private val listAdapter by lazy { ListAdapter(requireContext(), this) }
    private var dataOperation: ((MutableList<IllustsBean>) -> Unit)? = null

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        args?.let {
            illustsId = it.getString("illustsId", "")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_precview_relate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
    }

    private fun initList() {
        rv_preview_relate.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = listAdapter.adapter
            loadListener = {
                getNext()
            }
        }
        refresh()
    }

    private var loadMode = 0
    private val load_mode_refresh = 0
    private val load_mode_load = 1

    private fun refresh() {
        loadMode = load_mode_refresh
        PixivImagePresenter.getRelatedIllust(illustsId, dataCallback)
    }

    private fun getNext() {
        if (!nextUrl.isNullOrEmpty()) {
            loadMode = load_mode_load
            PixivImagePresenter.getNext(nextUrl!!, dataCallback)
        } else {
            toast("没有更多")
        }
    }

    private val dataCallback = object : PublicCallback.DataCallBack<CommonIllustBean> {
        override fun onSuccess(t: CommonIllustBean) {
            listAdapter.adapter.apply {
                if (loadMode == load_mode_refresh) {
                    data.clear()
                }
                val size = data.size
                data.addAll(t.illusts)
                notifyItemRangeChanged(size, t.illusts.size)
            }
            nextUrl = t.next_url
        }

        override fun onStart() {
        }

        override fun onFailed(msg: String) {
            toast("加载失败：$msg")
        }
    }
}