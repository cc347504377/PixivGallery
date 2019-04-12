package com.luoye.whr.pixivGallery.view.welcome

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AlphaAnimation
import com.luoye.whr.pixivGallery.R
import com.luoye.whr.pixivGallery.common.SpUtil
import com.luoye.whr.pixivGallery.view.home.HomeActivity
import com.luoye.whr.pixivGallery.view.login.LoginActivity
import com.luoye.whr.kotlinlibrary.util.startActivity
import com.luoye.whr.kotlinlibrary.util.timer
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        AlphaAnimation(0.1f, 1f).apply {
            duration = 800
            iv_wel_logo.animation = this
            start()
        }
        timer(1000L) {
            if (SpUtil.auth.isBlank()) {
                startActivity<LoginActivity>()
            } else {
                startActivity<HomeActivity>()
            }
            finish()
        }
    }
}
