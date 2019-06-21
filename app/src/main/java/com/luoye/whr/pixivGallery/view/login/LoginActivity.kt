package com.luoye.whr.pixivGallery.view.login

import android.app.ProgressDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.luoye.whr.pixivGallery.R
import com.luoye.whr.pixivGallery.common.PixivAuthBean
import com.luoye.whr.pixivGallery.common.SpUtil
import com.luoye.whr.pixivGallery.presenter.PixivUserPresenter
import com.luoye.whr.pixivGallery.view.home.HomeActivity
import com.luoye.whr.kotlinlibrary.util.startActivity
import com.luoye.whr.kotlinlibrary.util.toast
import com.luoye.whr.pixivGallery.presenter.IllustCallback
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private val progressDialog by lazy {
        ProgressDialog(this).apply {
            setCancelable(true)
            setMessage("登录中，不要慌~")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initListener()
    }

    private fun initListener() {
        bt_login_log.setOnClickListener {
            postAuthToken()
        }
    }

    /**
     * 获得授权信息
     */
    private fun postAuthToken() {
        val username = et_login_username.text.toString()
        val password = et_login_password.text.toString()
        when {
            username.isBlank() -> {
                toast("用户名不能为空")
                return
            }
            password.isBlank() -> {
                toast("密码不能为空")
                return
            }
        }
        PixivUserPresenter.postAuthToken(username, password, object : IllustCallback<PixivAuthBean> {
            override fun onStart() {
                progressDialog.show()
            }

            override fun onFailed(msg: String) {
                toast("登录失败：$msg")
                progressDialog.dismiss()
            }

            override fun onSuccess(t: PixivAuthBean) {
                super.onSuccess(t)
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
                startActivity<HomeActivity>()
                finish()
                progressDialog.dismiss()
            }
        })
    }
}
