package com.luoye.whr.pixivGallery

import android.app.Application
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.luoye.whr.kotlinlibrary.util.fromJson
import com.luoye.whr.kotlinlibrary.util.log
import com.luoye.whr.pixivGallery.common.PixivAuthBean
import com.luoye.whr.pixivGallery.common.SpUtil
import com.luoye.whr.pixivGallery.presenter.PixivUserPresenter
import com.luoye.whr.kotlinlibrary.util.toast
import com.luoye.whr.pixivGallery.model.PixivUserModel
import com.luoye.whr.pixivGallery.presenter.IllustCallback
import java.lang.Exception
import java.lang.ref.WeakReference

class MyApplication : Application() {
    companion object {
        lateinit var context: WeakReference<MyApplication>
    }

    override fun onCreate() {
        super.onCreate()
        context = WeakReference(this)
    }

    /**
     * P站授权token在短时间就会失效 需要重新授权
     */
    fun refreshToken() {
        PixivUserPresenter.refreshToken(object : IllustCallback<PixivAuthBean> {
            override fun onStart() {
                toast("更新用户授权信息")
            }

            override fun onFailed(msg: String) {
            }

            override fun onSuccess(t: PixivAuthBean) {
                super.onSuccess(t)
                saveAuth(t)
            }
        })
    }

    fun refreshTokenSync() {
        try {
            PixivUserModel.refreshToken().execute().apply {
                body()?.string()?.let {
                    try {
                        saveAuth(it.fromJson())
                    } catch (e: JsonSyntaxException) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveAuth(t: PixivAuthBean) {
        SpUtil.apply {
            val user = t.response.user
            auth = "Bearer ${t.response.accessToken}"
            refreshToken = t.response.refreshToken
            deviceToken = t.response.deviceToken
            userId = user.id
            isPremium = user.isPremium
            userAccount = user.account
            userId = user.id
            userEmail = user.mailAddress
            userHead = user.profileImageUrls.px170x170
            userName = user.name
        }
    }
}