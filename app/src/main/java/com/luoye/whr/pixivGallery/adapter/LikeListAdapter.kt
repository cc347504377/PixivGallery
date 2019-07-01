package com.luoye.whr.pixivGallery.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.View
import com.luoye.whr.kotlinlibrary.net.PublicCallback
import com.luoye.whr.kotlinlibrary.util.toast
import com.luoye.whr.pixivGallery.MyApplication
import com.luoye.whr.pixivGallery.common.IllustsBean
import com.luoye.whr.pixivGallery.presenter.PixivImagePresenter

/**
 * 隐藏默认收藏按钮
 * 提供长按取消收藏功能
 */
class LikeListAdapter(context: Context, itemClickListener: View.OnClickListener)
    : CommonListAdapter(context, itemClickListener) {

    init {
        showLikeStat = false
    }

    private val unLikeDWarnDialog by lazy {
        AlertDialog.Builder(context)
                .setTitle("警告")
                .setMessage("是否移除该收藏项？")
                .setNegativeButton("取消") { dialog, which ->
                    dialog.dismiss()
                }
                .setPositiveButton("确定") { dialog, which ->
                    PixivImagePresenter.postUnlikeIllust(illustId, object : PublicCallback.StatCallBack {
                        override fun onStart() {
                        }

                        override fun onSuccess() {
                            adapter.apply {
                                data.removeAt(illustPosition)
                                notifyItemRemoved(illustPosition)
                            }
                        }

                        override fun onFailed(msg: String) {
                            MyApplication.context.get()?.toast("移除失败")
                        }
                    })
                    dialog.dismiss()
                }
    }

    private var illustPosition = 0
    private var illustId = 0L

    override fun bindView(holder: ViewHolder, bean: IllustsBean) {
        super.bindView(holder, bean)
        holder.itemView.setOnLongClickListener {
            illustId = bean.id.toLong()
            illustPosition = holder.layoutPosition
            unLikeDWarnDialog.show()
            false
        }
    }
}