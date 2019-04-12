package com.luoye.whr.pixivGallery.view.search

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.*
import com.luoye.whr.pixivGallery.R
import com.luoye.whr.pixivGallery.common.IllustsBean
import com.luoye.whr.pixivGallery.common.SearchIllustBean
import com.luoye.whr.pixivGallery.common.SpUtil
import com.luoye.whr.pixivGallery.presenter.PixivImagePresenter
import com.luoye.whr.pixivGallery.view.base.BaseActivityListFragment
import com.luoye.whr.kotlinlibrary.net.PublicCallback
import com.luoye.whr.kotlinlibrary.util.toast
import kotlinx.android.synthetic.main.fragment_img_list.*

class SearchFragment : BaseActivityListFragment() {

    var style = 0
    private var keyWord = ""
    private var filterType = 0
    private var sortType = 0
    private val sortPopMenu by lazy {
        val array = arrayOf("时间", "热度")
        AlertDialog.Builder(requireContext())
                .setSingleChoiceItems(array, 0) { dialog, which ->
                    if (which == 1 && !SpUtil.isPremium) {
                        toast("只有会员才能使用热度排行")
                    } else if (sortType != which) {
                        sortType = which
                        ll_frg_list.refresh()
                    }
                    dialog.dismiss()
                }
                .create()
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        args?.let {
            keyWord = it.getString("keyWord", "")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search_filter_0 -> {
                updateFilter(0)
            }
            R.id.menu_search_filter_1 -> {
                updateFilter(1)
            }
            R.id.menu_search_filter_2 -> {
                updateFilter(2)
            }
            R.id.menu_search_filter_3 -> {
                updateFilter(3)
            }
            R.id.menu_search_filter_4 -> {
                updateFilter(4)
            }
            R.id.menu_search_sort -> {
                sortPopMenu.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateFilter(filter: Int) {
        filterType = filter
        ll_frg_list.refresh()
    }

    private val dataCallback = object : PublicCallback.DataCallBack<SearchIllustBean> {
        override fun onSuccess(t: SearchIllustBean) {
            dataOperation?.invoke(t.illusts.toMutableList())
            nextUrl = t.next_url
        }

        override fun onStart() {
        }

        override fun onFailed(msg: String) {
            toast("加载失败：$msg")
            ll_frg_list.onError("走丢了")
        }
    }

    /**
     * 加载更多
     */
    override fun getNext(dataOperation: (MutableList<IllustsBean>) -> Unit) {
        if (!nextUrl.isNullOrEmpty()) {
            this.dataOperation = dataOperation
            PixivImagePresenter.getNext(nextUrl!!, dataCallback)
        } else {
            ll_frg_list.onError("没有更多")
        }
    }

    /**
     * 刷新数据
     */
    override fun getData(dataOperation: (MutableList<IllustsBean>) -> Unit) {
        this.dataOperation = dataOperation
        PixivImagePresenter.getSearch(keyWord, filterType, sortType, dataCallback)
    }
}