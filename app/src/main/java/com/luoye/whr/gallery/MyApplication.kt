package com.luoye.whr.gallery

import android.app.Application
import com.luoye.whr.gallery.common.PixivAuthBean
import com.luoye.whr.gallery.common.SpUtil
import com.luoye.whr.gallery.presenter.PixivUserPresenter
import com.luoye.whr.kotlinlibrary.net.PublicCallback
import com.luoye.whr.kotlinlibrary.util.toast
import java.lang.ref.WeakReference

class MyApplication : Application() {
    companion object {
        lateinit var context: WeakReference<MyApplication>
    }

    override fun onCreate() {
        super.onCreate()
        context = WeakReference(this)
        refreshToken()
    }

    /**
     * P站授权token在短时间就会失效 需要重新授权
     */
    fun refreshToken() {
        PixivUserPresenter.refreshToken(object : PublicCallback.DataCallBack<PixivAuthBean> {
            override fun onStart() {
                toast("更新用户授权信息")
            }

            override fun onFailed(msg: String) {

            }

            override fun onSuccess(t: PixivAuthBean) {
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

        })
    }

}