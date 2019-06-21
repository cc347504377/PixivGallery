package com.luoye.whr.pixivGallery.view.home.setting

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.luoye.whr.pixivGallery.R
import com.luoye.whr.pixivGallery.common.ModeEvent
import com.luoye.whr.pixivGallery.common.SpUtil
import kotlinx.android.synthetic.main.activity_setting.*
import org.greenrobot.eventbus.EventBus

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initListener()
    }

    private fun initListener() {
        st_setting_adult.apply {
            isChecked = SpUtil.adultMode
            setOnCheckedChangeListener { buttonView, isChecked ->
                SpUtil.adultMode = isChecked
                EventBus.getDefault().post(ModeEvent())
            }
        }
    }
}
