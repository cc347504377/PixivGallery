package com.luoye.whr.gallery.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.luoye.whr.gallery.R
import com.luoye.whr.gallery.common.RecommendTagBean
import com.luoye.whr.gallery.common.loadPixvImg
import com.luoye.whr.gallery.view.search.SearchListActivity
import com.luoye.whr.kotlinlibrary.util.bindView

class SearchTagAdapter(context: Context, val data: MutableList<RecommendTagBean.TrendTag>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_search_recommend_tag, p0, false))
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        val holder = p0 as ViewHolder
        val bean = data[p1]
        holder.mTvSearchItem.text = bean.tag
        holder.itemView.apply {
            context.loadPixvImg(bean.illust.imageUrls.medium, bean.illust.id.toString(), holder.mIvSearchItem)
            setOnClickListener {
                context.startActivity(Intent(context, SearchListActivity::class.java).apply {
                    putExtras(Bundle().apply {
                        putString("keyWord", bean.tag)
                    })
                })
            }
        }
    }

    private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mIvSearchItem: ImageView by bindView(R.id.iv_search_item)
        val mTvSearchItem: TextView by bindView(R.id.tv_search_item)
    }

}