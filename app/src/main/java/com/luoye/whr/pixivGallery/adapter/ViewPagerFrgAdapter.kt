package com.luoye.whr.pixivGallery.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class ViewPagerFrgAdapter(fm: FragmentManager, private val frgList: List<Fragment>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment = frgList.get(p0)

    override fun getCount(): Int = frgList.size
}