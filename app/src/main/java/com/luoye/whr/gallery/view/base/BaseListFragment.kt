package com.luoye.whr.gallery.view.base

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.request.target.Target
import com.luoye.whr.gallery.R
import com.luoye.whr.gallery.adapter.ListAdapter
import com.luoye.whr.gallery.adapter.MyItemDecoration
import com.luoye.whr.gallery.common.IllustsBean
import com.luoye.whr.gallery.common.loadPixvImg
import com.luoye.whr.gallery.view.preview.PreviewActivity
import com.luoye.whr.kotlinlibrary.util.clearImg
import com.luoye.whr.kotlinlibrary.view.PullLayout
import kotlinx.android.synthetic.main.fragment_img_list.*

/**
 * 通用图片列表fragment
 * 封装了主要的图片加载框架和逻辑
 * 封装了共享元素回调逻辑
 */
abstract class BaseListFragment : BaseControlFragment(), (MutableList<String>, MutableMap<String, View>) -> Unit, View.OnClickListener {

    /***************共享元素**************/
    //状态过滤，每次动画执行会调用两次
    private var sharedElementStat = false

    //共享动画回调
    override fun invoke(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
        sharedElementStat = if (sharedElementStat) {
            sharedElements.clear()
            names.clear()
            ll_frg_list.recyclerView.findViewHolderForAdapterPosition(position)?.let {
                sharedElements[getString(R.string.transName)] = it.itemView
            }
            false
        } else {
            true
        }
    }

    //定位共享元素位置
    private var position = 0
        set(value) {
            field = value
            ll_frg_list.recyclerView.scrollToPosition(value)
        }

    private fun setupPreviewSelectListener() {
        PreviewActivity.addSelectedListener {
            position = it
        }
    }

    /***************共享元素**************/


    private val listAdapter by lazy { ListAdapter(requireContext(), this) }
    private var style = 0
    private val gridItemDecoration = MyItemDecoration()
    protected var nextUrl: String? = null
    protected var dataOperation: ((MutableList<IllustsBean>) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_img_list, container, false)
    }

    override fun changeStyle() {
        setupRecyclerView()
    }

    /**
     * 设置列表风格参数
     */
    protected fun setupRecyclerView() {
        listAdapter.style = style
        when (style % 3) {
            0 -> {
                ll_frg_list.setup(GridLayoutManager(context, 2), listAdapter.adapter, dataChangeListener)
                ll_frg_list.recyclerView.addItemDecoration(gridItemDecoration)
            }
            1 -> {
                ll_frg_list.setup(LinearLayoutManager(context), listAdapter.adapter, dataChangeListener)
                ll_frg_list.recyclerView.removeItemDecoration(gridItemDecoration)
            }
            2 -> {
                ll_frg_list.setup(StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL), listAdapter.adapter, dataChangeListener)
                ll_frg_list.recyclerView.removeItemDecoration(gridItemDecoration)
            }
        }
        style++
    }

    /**
     * glide回收图片内存
     */
    protected fun clearRecyclerImg() {
        listAdapter.adapter.currentHolderList.forEach {
            try {
                context?.clearImg(it.itemView.tag as Target<Any>)
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }

    /**
     * 重新加载图片到内存
     */
    protected fun loadRecyclerImg() {
        val data = listAdapter.adapter.data
        listAdapter.adapter.currentHolderList.forEach {
            val holder = it as ListAdapter.ViewHolder
            val bean = data[holder.layoutPosition]
            if (holder.mIvItem.drawable == null) {
                context?.loadPixvImg(bean.imageUrls.medium, bean.id, holder.mIvItem)
            }
        }
    }

    /**
     * 滑动事件调用回调
     */
    private val dataChangeListener = object : PullLayout.OnDataChangeListener<IllustsBean> {
        override fun onRefresh(dataChanged: (MutableList<IllustsBean>) -> Unit) {
            getData(dataChanged)
        }

        override fun onLoad(currentPage: Int, operation: (MutableList<IllustsBean>) -> Unit) {
            getNext(operation)
        }
    }

    /**
     * item点击事件
     */
    override fun onClick(v: View) {
        val viewHolder = v.getTag(R.id.tag_holder) as ListAdapter.ViewHolder
        val pair1 = Pair<View, String>(viewHolder.itemView, getString(R.string.transName))
        val bundle = ActivityOptions.makeSceneTransitionAnimation(activity, pair1).toBundle()
        startActivity(Intent().apply {
            setupPreviewSelectListener()
            PreviewActivity.data = this@BaseListFragment.listAdapter.adapter.data
            setClass(requireContext(), PreviewActivity::class.java)
            putExtra("position", viewHolder.layoutPosition)
        }, bundle)
    }

    /**
     * 加载数据
     */
    abstract fun getData(dataOperation: (MutableList<IllustsBean>) -> Unit)

    /**
     * 加载更多
     */
    abstract fun getNext(dataOperation: (MutableList<IllustsBean>) -> Unit)

}