package com.luoye.whr.gallery.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class HomeFrgAdapter(fm: FragmentManager,
                     private val frgList: List<Fragment>,
                     private val titleList: List<CharSequence>) : FragmentStatePagerAdapter(fm) {

    override fun getItem(p0: Int): Fragment = frgList.get(p0)

    override fun getCount(): Int = frgList.size

    override fun getPageTitle(position: Int) = titleList[position]

}