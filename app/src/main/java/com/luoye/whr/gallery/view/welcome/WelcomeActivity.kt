package com.luoye.whr.gallery.view.welcome

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.luoye.whr.gallery.R
import com.luoye.whr.gallery.common.SpUtil
import com.luoye.whr.gallery.view.home.HomeActivity
import com.luoye.whr.gallery.view.login.LoginActivity
import com.luoye.whr.kotlinlibrary.util.startActivity
import com.luoye.whr.kotlinlibrary.util.timer

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
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
