package com.luoye.whr.pixivGallery.common

import android.content.Context
import com.luoye.whr.pixivGallery.MyApplication

object SpUtil {

    private val sp = MyApplication.context.get()
            ?.getSharedPreferences("default.sp", Context.MODE_PRIVATE)

    var auth: String
        get() = commonGet("Authorization")
        set(value) {
            commonSet("Authorization", value)
        }

    var refreshToken: String
        get() = commonGet("refresh_token")
        set(value) {
            commonSet("refresh_token", value)
        }

    var deviceToken: String
        get() = commonGet("device_token")
        set(value) {
            commonSet("device_token", value)
        }

    var userId: String
        get() = commonGet("userId")
        set(value) {
            commonSet("userId", value)
        }

    var isPremium: Boolean
        get() = sp?.getBoolean("isPremium", false) ?: false
        set(value) {
            sp?.let {
                val edit = it.edit()
                edit.putBoolean("isPremium", value)
                edit.apply()
            }
        }

    var userAccount: String
        get() = commonGet("userAccount")
        set(value) {
            commonSet("userAccount", value)
        }

    var userName: String
        get() = commonGet("userName")
        set(value) {
            commonSet("userName", value)
        }

    var userEmail: String
        get() = commonGet("userEmail")
        set(value) {
            commonSet("userEmail", value)
        }

    var userHead: String
        get() = commonGet("userHead")
        set(value) {
            commonSet("userHead", value)
        }

    var searchBean: String
        get() = commonGet("searchBean")
        set(value) {
            commonSet("searchBean", value)
            System.currentTimeMillis()
        }

    var searchBeanTime: Long
        get() = sp?.getLong("searchBeanTime", 0L) ?: 0L
        set(value) {
            sp?.edit()?.let {
                it.putLong("searchBeanTime", value)
                it.apply()
            }
        }

    var adultMode: Boolean
        get() = sp?.getBoolean("adultMode", false) ?: false
        set(value) {
            sp?.let {
                val edit = it.edit()
                edit.putBoolean("adultMode", value)
                edit.apply()
            }
        }

    private fun commonGet(key: String) = if (sp == null) {
        " "
    } else {
        sp.getString(key, " ")!!
    }

    private fun commonSet(key: String, value: String) {
        sp?.let {
            val edit = it.edit()
            edit.putString(key, value)
            edit.apply()
        }
    }
}