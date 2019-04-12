package com.luoye.whr.pixivGallery.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.ViewTarget
import com.luoye.whr.pixivGallery.MyApplication
import com.luoye.whr.kotlinlibrary.util.GlideApp
import com.luoye.whr.kotlinlibrary.util.getAppName

val folder = with(MyApplication.context.get()!!) {
    cacheDir.absolutePath + "/" + getAppName() + "/"
}

fun Context.loadPixvImg(
    url: String,
    illustId: String,
    imageView: ImageView,
    crop: Boolean = true,
    requestListener: RequestListener<Drawable>? = null
): ViewTarget<ImageView, Drawable> {
    val header = {
        mutableMapOf("Referer" to "https://www.pixiv.net/member_illust.php?mode=medium&illustId=$illustId")
    }
    return GlideApp
            .with(this)
            .load(GlideUrl(url, header))
            .transition(withCrossFade())
            .apply {
                if (crop) {
                    centerCrop()
                }
                requestListener?.let {
                    listener(it)
                }
            }
            .into(imageView)
}

fun Context.loadPixvRawImg(url: String, illustId: String, imageView: ImageView): BitmapImageViewTarget {
    val header = {
        mutableMapOf("Referer" to "https://www.pixiv.net/member_illust.php?mode=medium&illustId=$illustId")
    }
    return GlideApp.with(this)
            .asBitmap()
            .load(GlideUrl(url, header))

            .into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    resource?.let {
                        val ratio = it.height / it.width.toFloat()
                        imageView.apply {
                            layoutParams = layoutParams.apply {
                                height = (measuredWidth * ratio).toInt()
                            }
                        }
                    }
                    super.setResource(resource)
                }
            })
}