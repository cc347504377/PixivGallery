package com.luoye.whr.gallery.model

import android.graphics.Bitmap
import android.widget.ImageView
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.luoye.whr.gallery.MyApplication
import com.luoye.whr.kotlinlibrary.download.HttpConnection
import com.luoye.whr.kotlinlibrary.download.RequestCallback
import com.luoye.whr.kotlinlibrary.util.*
import java.util.concurrent.Executors
import com.luoye.whr.gallery.common.folder


object ImgModel {

    private val context = MyApplication.context.get()!!

    private val threadPool = Executors.newFixedThreadPool(3)

    fun downloadOriginImg(originUrl: String, pId: String, callback: RequestCallback) {
        threadPool.execute {
            val file = createFile(folder, originUrl.fileName)
            if (file.length() > 0) {
                callback.onSuccess(file.absolutePath)
            } else {
                val header = mapOf("Referer" to "https://www.pixiv.net/member_illust.php?mode=medium&illustId=$pId")
                HttpConnection(originUrl)
                        .setHeaders(header)
                        .downloadFile(file, 0, callback)
            }
        }
    }

    fun loadThumbnailImg(url: String, pId: String, imageView: ImageView, loadFinishCall: (() -> Unit)? = null) {
        val header = {
            mutableMapOf("Referer" to "https://www.pixiv.net/member_illust.php?mode=medium&illustId=$pId")
        }
        GlideApp.with(context)
                .asBitmap()
                .load(GlideUrl(url, header))
                .into(object : BitmapImageViewTarget(imageView) {
                    override fun setResource(resource: Bitmap?) {
                        resource?.let {
                            val ratio = it.height / it.width.toFloat()
                            imageView.apply {
                                layoutParams = layoutParams.apply {
                                    height = (imageView.measuredWidth * ratio).toInt()
                                }
                            }
                        }
                        super.setResource(resource)
                        loadFinishCall?.invoke()
                    }
                })
    }
}