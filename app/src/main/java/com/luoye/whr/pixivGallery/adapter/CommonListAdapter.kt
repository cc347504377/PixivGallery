package com.luoye.whr.pixivGallery.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.luoye.whr.pixivGallery.R
import com.luoye.whr.pixivGallery.common.IllustsBean
import com.luoye.whr.pixivGallery.common.loadPixvImg
import com.luoye.whr.kotlinlibrary.base.BaseAdapter
import com.luoye.whr.kotlinlibrary.util.bindView

/**
 * 图片列表适配器工厂类
 * 包含三种样式：列表、表格、自适应列表
 * 三种适配器数据为共享数据
 */
class CommonListAdapter(private val context: Context, private val itemClickListener: View.OnClickListener) {

    var style = 0
    private var isFirst = true

    val adapter: BaseAdapter<IllustsBean>
        get() {
            // 数据同步
            if (isFirst) {
                listAdapter.data = gridAdapter.data
                staggeredAdapter.data = listAdapter.data
                isFirst = false
            }
            // 适配器匹配
            return when (style % 3) {
                0 -> gridAdapter
                1 -> listAdapter
                2 -> staggeredAdapter
                else -> gridAdapter
            }
        }

    private val gridAdapter = BaseAdapter<IllustsBean>(
            { parent, _ ->
                ViewHolder(BaseAdapter.getView(parent, context, R.layout.item_img_grid))
            },
            { data, holder, position ->
                val viewHolder = holder as ViewHolder
                val bean = data[position]
                val mUrl = bean.imageUrls.medium
                val illusstId = bean.id
                bindView(viewHolder, bean)
                viewHolder.itemView.apply {
                    if (tag == null) {
                        // 动态计算item尺寸
                        post {
                            val lp = layoutParams
                            lp.height = measuredWidth
                            layoutParams = lp
                        }
                    }
                    // 加载
                    tag = context?.loadPixvImg(mUrl, illusstId, holder.mIvItem, false)
                }
            },
            {
                recyclerView(it)
            })

    private val listAdapter = BaseAdapter<IllustsBean>(
            { parent, _ ->
                ViewHolder(BaseAdapter.getView(parent, context, R.layout.item_img_linear))
            },
            { data, holder, position ->
                val viewHolder = holder as ViewHolder
                val bean = data[position]
                val mUrl = bean.imageUrls.medium
                val illusstId = bean.id
                bindView(viewHolder, bean)
                viewHolder.itemView.apply {
                    if (tag == null) {
                        post {
                            val lp = layoutParams
                            lp.height = measuredWidth / 4 * 3
                            layoutParams = lp
                        }
                    }
                    // 加载
                    tag = context?.loadPixvImg(mUrl, illusstId, holder.mIvItem, false)
                }
            },
            {
                recyclerView(it)
            })

    private val staggeredAdapter = BaseAdapter<IllustsBean>(
            { parent, _ ->
                ViewHolder(BaseAdapter.getView(parent, context, R.layout.item_img_staggered))
            },
            { data, holder, position ->
                val viewHolder = holder as ViewHolder
                val bean = data[position]
                val mUrl = bean.imageUrls.medium
                val illusstId = bean.id
                bindView(viewHolder, bean)
                viewHolder.itemView.apply {
                    post {
                        layoutParams = layoutParams.apply {
                            val ratio = bean.height / bean.width.toFloat()
                            height = (measuredWidth * ratio).toInt()
                        }
                    }
                    // 加载
                    tag = context.loadPixvImg(mUrl, illusstId, viewHolder.mIvItem, false)
                }
            },
            {
                recyclerView(it)
            })

    /**
     * 显示角标
     * 可能存在多张图片的图片集
     */
    private fun showImgSize(holder: ViewHolder, bean: IllustsBean) {
        holder.mTvItemSize.apply {
            if (bean.metaPages.isNotEmpty()) {
                visibility = View.VISIBLE
                text = "${bean.metaPages.size}p"
            } else {
                visibility = View.INVISIBLE
            }
        }
    }

    /**
     * 绑定view复用部分
     */
    private fun bindView(holder: ViewHolder, bean: IllustsBean) {
        holder.itemView.apply {
            setTag(R.id.tag_holder, holder)
            setOnClickListener(itemClickListener)
        }
        showImgSize(holder, bean)
    }

    /**
     * 回收bitmap
     */
    private fun recyclerView(holder: RecyclerView.ViewHolder) {
//        try {
//            context.clearImg(holder.itemView.tag as Target<Any>)
//        } catch (t: Throwable) {
//            t.printStackTrace()
//        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIvItem: ImageView by bindView(R.id.iv_item)
        val mTvItemSize: TextView by bindView(R.id.tv_item_size)
    }
}